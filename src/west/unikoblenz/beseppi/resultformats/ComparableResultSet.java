package west.unikoblenz.beseppi.resultformats;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Arrays;

import west.unikoblenz.beseppi.comparators.StringArrayComparator;

//Comparabe result sets holds variables, variable bindings and execution time
public class ComparableResultSet implements Iterable<String[]> {
	private StringArrayComparator comp = new StringArrayComparator();
	private SortedSet<String[]> bindingSet = new TreeSet<String[]>(comp);
	private String[] variableArray; // Holds all variables in Result Set
	private long executionTime;

	public ComparableResultSet(String[] head) {
		this.variableArray = head;
	}

	public String[] getVariables() {
		return this.variableArray;
	}
	
	public int size(){
		return bindingSet.size();
	}

	// Prints the result set to the console
	public void print() {
		System.out.println("{");
		Iterator<String[]> bindingsIterator = bindingSet.iterator();
		while (bindingsIterator.hasNext()) {
			System.out.print("{");
			String[] currentValueArray = bindingsIterator.next();
			for (int i = 0; i < currentValueArray.length; i++) {
				System.out.print("(" + this.variableArray[i] + ", "
						+ currentValueArray[i] + ")");
				if (currentValueArray.length - i > 1) {
					System.out.print(", ");
				}
			}
			System.out.println("}");
		}
		System.out.println("}");
	}

	public void add(String[] bindings) {
		bindingSet.add(bindings);
	}

	public Iterator<String[]> iterator() {
		return bindingSet.iterator();
	}

	//sorts variable array lexogrphically and result arrays respectively
	public void sortVariables() {
		String[] unorderedVariables = this.variableArray.clone();
		Arrays.sort(this.variableArray);
		SortedSet<String[]> sortedBindingSet = new TreeSet<String[]>(comp);
		for (String[] oldBinding : this.bindingSet) {
			String[] newBinding = new String[oldBinding.length];
			for (int newIndex = 0; newIndex < this.variableArray.length; newIndex++) {
				int oldIndex = Arrays.asList(unorderedVariables).indexOf(
						this.variableArray[newIndex]);
				newBinding[newIndex] = oldBinding[oldIndex];
			}
		sortedBindingSet.add(newBinding);
		}
		this.bindingSet = sortedBindingSet;
	}

	public long getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(long exeuctionTime) {
		this.executionTime = exeuctionTime;
	}
	
}
