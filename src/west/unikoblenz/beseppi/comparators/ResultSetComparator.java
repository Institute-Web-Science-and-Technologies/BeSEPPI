package west.unikoblenz.beseppi.comparators;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import west.unikoblenz.beseppi.resultformats.ComparableResultSet;

//This class is a comparator to compute completeness and correctness of Results 
public class ResultSetComparator {
	private String outputPath; //where output should be saved

	public ResultSetComparator(String outputPath) {
		this.outputPath = outputPath;
	}

	public Double[] getCompletenessAndSoundness(ComparableResultSet reference,
			ComparableResultSet newResult, String queryName) {
		StringArrayComparator comp = new StringArrayComparator();
		ComparableResultSet missingResults = new ComparableResultSet(
				reference.getVariables());
		ComparableResultSet wrongResults = new ComparableResultSet(
				newResult.getVariables());

		int correctBindings = 0;
		Double[] completenessAndSoundness = new Double[2];
		Iterator<String[]> referenceIterator = reference.iterator();
		Iterator<String[]> newResultIterator = newResult.iterator();

		int compareResult = 0;

		if (referenceIterator.hasNext() && newResultIterator.hasNext()) {
			boolean continueLoop = true;
			String[] currentRef = referenceIterator.next();
			String[] currentNew = newResultIterator.next();

			while (continueLoop) {
				compareResult = comp.compare(currentRef, currentNew);
				// Reference and result are equal
				if (compareResult == 0) {
					correctBindings++;
					if (referenceIterator.hasNext()
							&& newResultIterator.hasNext()) {
						currentRef = referenceIterator.next();
						currentNew = newResultIterator.next();
					} else {
						continueLoop = false;
					}
				}

				// Reference < NewResult
				if (compareResult <= -1) {
					missingResults.add(currentRef);
					if (referenceIterator.hasNext()) {
						currentRef = referenceIterator.next();
					} else {
						continueLoop = false;
					}
				}

				// Reference > NewResult
				if (compareResult >= 1) {
					wrongResults.add(currentNew);
					if (newResultIterator.hasNext()) {
						currentNew = newResultIterator.next();
					} else {
						continueLoop = false;
					}
				}
			}
			if (compareResult <= -1)
				wrongResults.add(currentNew);
			while (referenceIterator.hasNext()) {
				missingResults.add(referenceIterator.next());
			}

			// add all remaining new result bindings to wrong bindings
			if (compareResult >= 1) {
				missingResults.add(currentRef);
				while (newResultIterator.hasNext()) {
					wrongResults.add(newResultIterator.next());
				}
			}
		}
		while (newResultIterator.hasNext()) {
			wrongResults.add(newResultIterator.next());
		}
		while (referenceIterator.hasNext()) {
			missingResults.add(referenceIterator.next());
		}
		// add all remaining reference bindings to missing bindings

		// write wrong and missing results to file if there are any
		if (missingResults.size() + wrongResults.size() > 0) {
			try {
				printMissingAndWrongResults(missingResults, wrongResults, queryName);
			} catch (IOException e) {
				System.out.println("Given output Path not found results are not saved!");
			}
		}

		// calculate metrics
		if (reference.size() == 0) {
			completenessAndSoundness[0] = 1.0;
			completenessAndSoundness[1] = 1.0;
			if (newResult.size() > 0) {
				completenessAndSoundness[1] = 0.0;
			}
			return completenessAndSoundness;
		}
		if (newResult.size() == 0) {
			completenessAndSoundness[0] = 1.0;
			completenessAndSoundness[1] = 1.0;
			if (reference.size() > 0) {
				completenessAndSoundness[0] = 0.0;
			}
			return completenessAndSoundness;
		}
		completenessAndSoundness[0] = (double) correctBindings
				/ reference.size();
		completenessAndSoundness[1] = (double) correctBindings
				/ newResult.size();
		return completenessAndSoundness;
	}

	
	//compares variables and thereby decides if result sets are compatible
	//Return true if result sets are comparable, returns false if not
	public boolean compareVariables(ComparableResultSet result1,
			ComparableResultSet result2) {
		String[] variables1 = result1.getVariables();
		String[] variables2 = result2.getVariables();
		if (variables1.length == variables2.length) {
			for (int i = 0; i < variables1.length; i++) {
				if (variables1[i] != variables2[i]) {
					return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}

	
	//Function that writes missing and wrong results to output file
	public void printMissingAndWrongResults(ComparableResultSet missing,
			ComparableResultSet wrong, String queryName) throws IOException {
		String outputFileName = queryName.replace(".sparql", ".info");
		String output = "";
		
		//If there are any missing results
		if (missing.size() > 0) {
			output += "Missing Results: \n";
			Iterator<String[]> missingIterator = missing.iterator();
			String[] missingVars = missing.getVariables();
			while (missingIterator.hasNext()) {
				output += "{";
				String[] currentValueArray = missingIterator.next();
				for (int i = 0; i < currentValueArray.length; i++) {
					output += "(" + missingVars[i] + ", "
							+ currentValueArray[i] + ")";
					if (currentValueArray.length - i > 1) {
						output += ", ";
					}
				}
				output += "}\n";
			}
		}
		
		//If there are any wrong results
		if (wrong.size() > 0) {
			output += "\nWrong Results:\n";
			Iterator<String[]> wrongIterator = wrong.iterator();
			String[] wrongVars = wrong.getVariables();
			while (wrongIterator.hasNext()) {
				output += "{";
				String[] currentValueArray = wrongIterator.next();
				for (int i = 0; i < currentValueArray.length; i++) {
					output += "(" + wrongVars[i] + ", " + currentValueArray[i]
							+ ")";
					if (currentValueArray.length - i > 1) {
						output += ", ";
					}
				}
				output += "}\n";
			}
		}
		
		//Write missing and wrong results to file
		BufferedWriter outputWriter = new BufferedWriter(new FileWriter(
				outputPath+"/"+ outputFileName));
		outputWriter.write(output);
		outputWriter.close();
	}
}
