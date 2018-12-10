package west.unikoblenz.beseppi;



import west.unikoblenz.beseppi.resultformats.AskQueryResult;
import west.unikoblenz.beseppi.resultformats.ComparableResultSet;

public interface DatabaseAccessor {

	public ComparableResultSet selectQuery(String query) ;

	public AskQueryResult askQuery(String query) ;
	
	public void loadDataset(String filePath);

}
