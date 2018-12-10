package west.unikoblenz.beseppi.resultformats;

// Ask query results consist of bool and execution time
public class AskQueryResult {
	private boolean result;
	private long executionTime;
	
	public AskQueryResult(boolean result, long executionTime){
		this.setResult(result);
		this.setExecutionTime(executionTime);
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public long getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(long executionTime) {
		this.executionTime = executionTime;
	}
}
