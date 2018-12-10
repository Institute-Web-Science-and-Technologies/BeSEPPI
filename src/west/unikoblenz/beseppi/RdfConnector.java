package west.unikoblenz.beseppi;

import java.util.Iterator;
import java.util.List;


import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.apache.jena.sparql.engine.http.QueryExceptionHTTP;

import west.unikoblenz.beseppi.resultformats.AskQueryResult;
import west.unikoblenz.beseppi.resultformats.ComparableResultSet;

//This class is a general RDF connector. It can connect to any store with respective endpoints
public class RdfConnector implements DatabaseAccessor{
	private RDFConnection conn ;

	//Constructor needs query and update Service endpoint graph store protocol is optional 
	public RdfConnector(String queryServiceEndpoint,
			String updateServiceEndpoint, String graphStoreProtocolEndpoint) {
		this.conn = RDFConnectionFactory.connect(queryServiceEndpoint,
				updateServiceEndpoint, graphStoreProtocolEndpoint);
	}

	public void loadDataset(String file) {
		this.conn.load(file);
	}

	//Executes ASK query and returns AskQueryResult
	public AskQueryResult askQuery(String query) throws QueryExceptionHTTP {
		long startTime = System.currentTimeMillis();
		QueryExecution qExec = this.conn.query(query);
		boolean result = qExec.execAsk();
		long endTime = System.currentTimeMillis();
		long executionTime = endTime - startTime;
		qExec.close();
		return new AskQueryResult(result, executionTime);
	}

	
	//Executes SELECT query and returns comparable result set
	public ComparableResultSet selectQuery(String query) throws QueryExceptionHTTP {
		long startTime = System.currentTimeMillis();
		QueryExecution qExec = this.conn.query(query);
		ResultSet rs = qExec.execSelect();
		long endTime = System.currentTimeMillis();
		long executionTime = endTime - startTime;

		//Checks for vars
		List<String> jenaVars = rs.getResultVars();
		String[] vars = new String[jenaVars.size()];
		int i = 0;
		Iterator<String> varIt = jenaVars.iterator();
		while (varIt.hasNext()) {
			vars[i] = varIt.next();
			i++;
		}

		//create Result Set
		ComparableResultSet benchResultSet = new ComparableResultSet(vars);
		benchResultSet.setExecutionTime(executionTime);
		while (rs.hasNext()) {
			String[] newBindings = new String[vars.length];
			QuerySolution currentSolution = rs.next();
			for (int j = 0; j < vars.length; j++) {
				RDFNode currentNode = currentSolution.get(vars[j]);
				newBindings[j] = currentNode.toString();
			}
			benchResultSet.add(newBindings);
		}
		qExec.close();
		return benchResultSet;
	}

	//close connection
	public void close() {
		this.conn.close();
	}


}
