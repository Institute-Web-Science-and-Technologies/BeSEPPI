package west.unikoblenz.beseppi;

import west.unikoblenz.beseppi.databaseaccessors.JenaAccessor;

public class QueryExecuterFactory {

	public DatabaseAccessor getExecuter(String databaseName) {
		if (databaseName == null) {
			return null;
		}
		if (databaseName == "Jena") {
			return new JenaAccessor();
		}
		return null;
	}

}
