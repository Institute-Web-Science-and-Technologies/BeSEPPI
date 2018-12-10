package west.unikoblenz.beseppi.gui;

public class NewBenchmark {
private String name;
private String path;
	
	public NewBenchmark(String name, String path){
		this.setName(name);
		this.setPath(path);	
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
    
	public NewBenchmark getBenchmark(){
		return this;
	}

}
