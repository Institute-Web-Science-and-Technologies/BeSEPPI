package west.unikoblenz.beseppi.databaseaccessors;

import java.util.Iterator;
import java.util.List;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;

import west.unikoblenz.beseppi.DatabaseAccessor;
import west.unikoblenz.beseppi.resultformats.AskQueryResult;
import west.unikoblenz.beseppi.resultformats.ComparableResultSet;

public class JenaAccessor implements DatabaseAccessor {

	private String service = "http://localhost:3030/ds/"; // Where the query is
															// sent

	public ComparableResultSet selectQuery(String query) {
		long startTime= System.currentTimeMillis();
		QueryExecution myExecution = QueryExecutionFactory.sparqlService(
				service, query);
		ResultSet jenaResults = myExecution.execSelect();
		long endTime = System.currentTimeMillis();
		long executionTime = endTime-startTime;
		System.out.println("Execution Time in ms:" + executionTime );
		List<String> jenaVars = jenaResults.getResultVars();
		String[] vars = new String[jenaVars.size()];
		int i = 0;
		Iterator<String> varIt = jenaVars.iterator();
		while (varIt.hasNext()) {
			vars[i] = varIt.next();
			i++;
		}

		ComparableResultSet benchResultSet = new ComparableResultSet(vars);
		benchResultSet.setExecutionTime(executionTime);
		while (jenaResults.hasNext()) {
			String[] newBindings = new String[vars.length];
			QuerySolution currentSolution = jenaResults.next();
			for (int j = 0; j < vars.length; j++) {
				RDFNode currentNode = currentSolution.get(vars[j]);
				newBindings[j] = currentNode.toString();
			}
			benchResultSet.add(newBindings);
		}
		myExecution.close();
		return benchResultSet;
	}

	public AskQueryResult askQuery(String query) {
		long startTime= System.currentTimeMillis();
		QueryExecution myExecution = QueryExecutionFactory.sparqlService(
				service, query);
		long endTime = System.currentTimeMillis();
		long executionTime = endTime-startTime;
		System.out.println("Execution Time in ms:" + executionTime );
		AskQueryResult queryResult = new AskQueryResult(myExecution.execAsk(), executionTime);
		myExecution.close();
		return queryResult;
	}

	public void loadDataset(String filePath) {
		// TODO Auto-generated method stub
		
	}

	
	

}
