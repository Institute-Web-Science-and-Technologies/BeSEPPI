package west.unikoblenz.beseppi.gui;

import java.io.File;




public class PathValidator {

	public static boolean validateBenchmark(File fpath) {
		boolean queries = false;
		boolean dataset = false;
		if (!fpath.isDirectory()) {
			return false;
		}
		File[] pathContent = fpath.listFiles();
		
		for (File e : pathContent){
			String fileName = e.getName();

			
			if (fileName.equals("dataset") && e.isDirectory() ){
				dataset = true;
			}
			else{
				if (fileName.equals("queries") && e.isDirectory() ){
					queries = true;
				}
			}
		}
		return (dataset && queries);
	}

	
public static void main(String[] args) {
	
	
	
}	
	
}
