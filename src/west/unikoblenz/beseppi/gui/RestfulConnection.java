package west.unikoblenz.beseppi.gui;

public class RestfulConnection {
    private String query;
    private String data;
    private String name;
    
    public RestfulConnection(String query, String data, String name){
    	this.query  = query;
    	this.data   = data;
    	this.name   = name;
    }

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
