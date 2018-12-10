package west.unikoblenz.beseppi;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;

import west.unikoblenz.beseppi.resultformats.ComparableResultSet;
import west.unikoblenz.beseppi.resultformats.ResultSetLoader;

public class QueryManager {

	//reads a single query
	public String readQuery(String queryPath) throws IOException {
		FileInputStream inputStream = new FileInputStream(queryPath);
		String query = IOUtils.toString(inputStream, Charset.defaultCharset());
		return query;
	}
	
	//reads ASK query Reference Result
	public boolean readAskReference(String queryPath)throws IOException{
		FileInputStream inputStream = new FileInputStream(queryPath);
		String reference = IOUtils.toString(inputStream, Charset.defaultCharset());
		if (reference.startsWith("true")){
			return true;
		}
		if (reference.startsWith("false")){
			return false;
		}
		return false; 
	}

	//Reads reference result set
	public ComparableResultSet readReferenceResult(String resultPath) throws IOException {
		ResultSetLoader loader= new ResultSetLoader();
		ComparableResultSet referenceResult = loader.readCsvResult(resultPath);
		return referenceResult;
	}
}
