package west.unikoblenz.beseppi;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import west.unikoblenz.beseppi.gui.ExecutionGui;

public class Main {
	
    //main from which benchmark can be executed from command line
	public static void main(String[] args) {
		switch (args.length) {
		case 1:
			String benchmarkPath = args[0];
			String resultPath = args[1];
			try {
				Class<?> connection = Class.forName(args[2]);
				Constructor<?> cons = connection.getConstructor();
				try {
					Object dbAccessor = cons.newInstance();
					if (dbAccessor instanceof DatabaseAccessor) {
						DatabaseAccessor dbAc = (DatabaseAccessor) dbAccessor;
						NewBenchmarkExecuter executer = new NewBenchmarkExecuter(
								benchmarkPath, resultPath, dbAc);
						try {
							executer.executeBenchmark();
						} catch (IOException e) {
							System.out.println("Benchmark not found");
						}
					}
				} catch (InstantiationException e) {
					
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					
					e.printStackTrace();
				}
			} catch (ClassNotFoundException e1) {
				System.out.println("Error. The class was not found");

			} catch (NoSuchMethodException e) {
				System.out
						.println("No default constructor for given class was found");
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
			
			break;
		case 5:
			String benchPath = args[0];
			String resPath = args[1];
			String queryEndpoint = args[2];
			String updateEndpoint = args[3];
			String dataEndpoint = args[4];
			NewBenchmarkExecuter executer = new NewBenchmarkExecuter(
					benchPath, resPath, queryEndpoint, updateEndpoint,
					dataEndpoint);
			try {
				executer.executeBenchmark();
			} catch (IOException e) {
				System.out.println("Benchmark not found");
			}
			break;
			
		default:
			System.out.println("Error in used arguments");
			System.out.println("Please use the following arguments:");
			System.out
					.println("BenchmarkPath ResultDirectory QueryEndpoint UpdateEndpoint DataEndpoint");

		}

	}
public static void executeFromGui(String[] args, ExecutionGui gui){

	String benchPath = args[0];
	String resPath = args[1];
	String queryEndpoint = args[2];
	String updateEndpoint = args[3];
	String dataEndpoint = args[4];
	
	NewBenchmarkExecuter executer = new NewBenchmarkExecuter(
			benchPath, resPath, queryEndpoint, updateEndpoint,
			dataEndpoint, gui);
	try {
		executer.executeBenchmark();
	} catch (IOException e) {
		System.out.println("Benchmark not found");
	}
}


}


