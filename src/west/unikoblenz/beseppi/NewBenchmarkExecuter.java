package west.unikoblenz.beseppi;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.RoundingMode;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.text.DecimalFormat;


import org.apache.jena.sparql.engine.http.QueryExceptionHTTP;



import west.unikoblenz.beseppi.comparators.ResultSetComparator;
import west.unikoblenz.beseppi.gui.ExecutionGui;
import west.unikoblenz.beseppi.resultformats.AskQueryResult;
import west.unikoblenz.beseppi.resultformats.ComparableResultSet;

public class NewBenchmarkExecuter {
	private String benchmarkPath;
	private DatabaseAccessor conn;
	private String resultPath;
	private ExecutionGui gui;

	public NewBenchmarkExecuter(String benchmarkPath, String resultPath,
			String queryServiceEndpoint, String updateServiceEndpoint,
			String graphStoreProtocolEndpoint, ExecutionGui gui) {
		this.benchmarkPath = benchmarkPath;
		this.resultPath = resultPath;
		Authenticator.setDefault(new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("dba", "dba".toCharArray());
			}
		});
		conn = new RdfConnector(queryServiceEndpoint, updateServiceEndpoint,
				graphStoreProtocolEndpoint);
		this.gui = gui;
	}

	public NewBenchmarkExecuter(String benchmarkPath, String resultPath,
			DatabaseAccessor databaseAccessor) {
		this.benchmarkPath = benchmarkPath;
		this.resultPath = resultPath;
		this.conn = databaseAccessor;
	}

	public NewBenchmarkExecuter(String benchmarkPath, String resultPath,
			String queryServiceEndpoint, String updateServiceEndpoint,
			String graphStoreProtocolEndpoint) {
		this.benchmarkPath = benchmarkPath;
		this.resultPath = resultPath;
		conn = new RdfConnector(queryServiceEndpoint, updateServiceEndpoint,
				graphStoreProtocolEndpoint);
	}

	public void executeBenchmark() throws IOException {
		QueryManager manager = new QueryManager();
		ResultSetComparator comparator = new ResultSetComparator(resultPath);
		String[] queryFileNames = getQueryFileNames();
		String[] dataFileNames = getDatasetFiles();

		if (gui != null) {
			gui.consoleAppend("Loading Dataset\n");
		}
		for (int i = 0; i < dataFileNames.length; i++) {
			conn.loadDataset(benchmarkPath + "/dataset/" + dataFileNames[i]);
		}

		String outputFileName = resultPath + "/benchmarkResult.csv";
		BufferedWriter outputWriter = new BufferedWriter(new FileWriter(
				outputFileName));
		outputWriter
				.write("Query Number,Query,Completeness,Soundness,Error,Execution Time in ms\n");

		if (gui != null) {
			gui.setQueryNumber(queryFileNames.length);
		}

		// where metrics are stored
		long[][] executionTimes = new long[queryFileNames.length][10];
		double[][] completeness = new double[queryFileNames.length][10];
		double[][] soundness = new double[queryFileNames.length][10];
		String [] errors = new String[queryFileNames.length];

		// execute queries twice without measuring metrics
		int progress = 0;
		for (int currentRun = -2; currentRun <= 9; currentRun++) {
			

			for (int i = 0; i < queryFileNames.length; i++) {
				String currentQuery = manager.readQuery(
						this.benchmarkPath + "/queries/" + queryFileNames[i])
						.replace("\n", "");

				if (currentQuery.startsWith("ASK")) {
					try{
					AskQueryResult currentAskResult = conn
							.askQuery(currentQuery);
					if (currentRun >= 0) {
						boolean currentQueryResult = currentAskResult
								.isResult();
						boolean reference = manager
								.readAskReference(benchmarkPath
										+ "/queries/"
										+ queryFileNames[i].replace(".sparql",
												".csv"));
						boolean outputValue = currentQueryResult == reference;
						if (outputValue) {
							completeness[i][currentRun] = 1;
							soundness[i][currentRun] = 1;
						} else {
							completeness[i][currentRun] = 0;
							soundness[i][currentRun] = 0;
						}
						executionTimes[i][currentRun] = currentAskResult
								.getExecutionTime();
						errors[i] = "-";
					}}catch(QueryExceptionHTTP e){
						if (currentRun >= 0){
						completeness[i][currentRun] = 0.0;
						soundness[i][currentRun] = 0.0;
						executionTimes[i][currentRun] = 0;
						errors[i] = "ERROR";}
					} 
				}

				if (currentQuery.startsWith("SELECT")) {
					try{
					ComparableResultSet currentQueryResult = conn
							.selectQuery(currentQuery);
					if (currentRun >= 0) {
						ComparableResultSet currentReference = manager
								.readReferenceResult(benchmarkPath
										+ "/queries/"
										+ queryFileNames[i].replace(".sparql",
												".csv"));
						currentQueryResult.sortVariables();
						currentReference.sortVariables();
						Double[] currentMetrics = comparator
								.getCompletenessAndSoundness(currentReference,
										currentQueryResult, queryFileNames[i]);
						completeness[i][currentRun] = currentMetrics[0];
						soundness[i][currentRun] = currentMetrics[1];
						executionTimes[i][currentRun] = currentQueryResult
								.getExecutionTime();
						errors[i] = "-";
					}}
					catch (QueryExceptionHTTP e){
						if (currentRun >= 0){
							completeness[i][currentRun] = 0.0;
							soundness[i][currentRun] = 0.0;
							executionTimes[i][currentRun] = 0;
							errors[i] = "ERROR";
						}
					}
				}
				progress ++;
				if (gui != null) {
					gui.updateGui(progress);
				}
			}
			
			
			
			
		}
		
		double[] executionTimesAverages = evaluateExecutionTimes(executionTimes);
		double[] soundnessLowest = evaluateComSound(soundness);
		double[] completeLowest = evaluateComSound(completeness);
		
		for (int i = 0; i < queryFileNames.length; i++) {
			String currentQuery = manager.readQuery(
					this.benchmarkPath + "/queries/" + queryFileNames[i])
					.replace("\n", "");
			outputWriter.append(queryFileNames[i] + "," + currentQuery
					+ "," + myRound(completeLowest[i]) + ","
					+ myRound(soundnessLowest[i]) + ", " + errors[i] +","
					+ executionTimesAverages[i] + "\n");
		}
		
		if (gui != null) {
			gui.finishExecution();
		}
		outputWriter.close();
	}

	private String[] getDatasetFiles() {
		File[] allFiles = new File(benchmarkPath + "/dataset").listFiles();
		String[] dataFileNames = new String[allFiles.length];
		for (int i = 0; i < allFiles.length; i++) {
			dataFileNames[i] = allFiles[i].getName();
		}
		return dataFileNames;
	}

	// Gets array of query file names ordered by number in query names
	private String[] getQueryFileNames() {
		File[] allFiles = new File(benchmarkPath + "/queries").listFiles();
		String[] queryFileNames = new String[(allFiles.length / 2)];
		int j = 0;
		for (int i = 0; i < allFiles.length; i++) {
			String queryName = allFiles[i].getName();
			if (queryName.endsWith(".sparql")) {
				queryFileNames[j] = queryName;
				j++;
			}
		}
		boolean isSorted = false;
		while (!isSorted) {
			isSorted = true;

			for (int i = 0; i < queryFileNames.length - 1; i++) {
				String currentName = queryFileNames[i];
				String currentNamePlus1 = queryFileNames[i + 1];
				if (isGreater(currentName, currentNamePlus1)) {
					queryFileNames[i] = currentNamePlus1;
					queryFileNames[i + 1] = currentName;
					isSorted = false;
				}
			}
		}
		return queryFileNames;
	}

	// comparator function for sorting queryFileNames
	private boolean isGreater(String s1, String s2) {
		int s1Int = Integer.parseInt(s1.replaceAll("[^0-9]", ""));
		int s2Int = Integer.parseInt(s2.replaceAll("[^0-9]", ""));
		if (s1Int > s2Int) {
			return true;
		}
		return false;
	}

	private String myRound(double value) {
		DecimalFormat df = new DecimalFormat("#.####");
		df.setRoundingMode(RoundingMode.CEILING);
		return df.format(value);
	}

	// deletes highest and lowest execution times and calculates averages
	private double[] evaluateExecutionTimes(long[][] executionTimes) {
		double[] averages = new double[executionTimes.length];
		for (int i = 0; i < executionTimes.length; i++) {
			long[] currentTimes = executionTimes[i];
			java.util.Arrays.sort(currentTimes);
			long sum = 0;
			for (int j = 1; j < currentTimes.length; j++) {
				sum += currentTimes[j];
			}
			averages[i] = sum / 8.0;
		}

		return averages;
	}

	private double[] evaluateComSound(double[][] values) {
		double[] results = new double[values.length];
		for (int i = 0; i < values.length; i++) {
			double[] currentResults = values[i];
			java.util.Arrays.sort(currentResults);
			results[i] = currentResults[0];
		}
		return results;
	}
}
