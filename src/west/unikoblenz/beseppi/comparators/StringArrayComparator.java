package west.unikoblenz.beseppi.comparators;

import java.util.Comparator;

public class StringArrayComparator implements Comparator<String[]> {

	public int compare(String[] array1, String[] array2){
		for (int i = 0; i < array1.length; i++){
			if (array1[i].compareTo(array2[i]) < 0){
				return -1;
			}
			if (array1[i].compareTo(array2[i]) > 0){
				return 1;
			}
		}
		return 0;
	}
}
