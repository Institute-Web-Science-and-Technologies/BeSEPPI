package west.unikoblenz.beseppi.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JTable;

//Creates table for comparing soundness and completeness
public class SoundAndCompGui {
	private File resultPath;
	private JTable table;
	private int rows;

	public SoundAndCompGui(File path) {
		this.resultPath = path;
		String[][] data = readCsv();
		createTable(data);
		iterateTable();
	}

	public JTable getTable() {
		return this.table;
	}	
		
	
	public void iterateTable() {
		for (int row_index = 0; row_index < rows; row_index++) {
			for (int col_index = 0; col_index <= 4; col_index++) {
				table.getColumnModel().getColumn(col_index).setCellRenderer(new TableColorRenderer());
			}
		}

	}

	public void createTable(String[][] tableData) {
		String[] columnNames = { "Query Name", "ASK Result",
				"Completeness", "Soundness", "Error" };

		JTable table = new JTable(tableData, columnNames);
		this.table = table;
	}

	
	//Reads csv and renders it in table
	public String[][] readCsv() {
		BufferedReader br = null;
		String line = "";
		String csvSplitBy = ",";
		try {
			int i = 0;
			br = new BufferedReader(new FileReader(resultPath));
			while ((line = br.readLine()) != null) {
				i++;
			}
			br.close();
			rows = i - 1;
			String[][] tableData = new String[i - 1][5];
			br = new BufferedReader(new FileReader(resultPath));
			br.readLine();
			int index = 0;
			while ((line = br.readLine()) != null) {
				String[] row = line.split(csvSplitBy);
				int rowIndex = 0;
				int inputIndex = 0;
				for (String cell : row){
					if (rowIndex == 0 || rowIndex == 2 || rowIndex == 3 || rowIndex == 4 || rowIndex == 5 ){
						tableData[index][inputIndex] = cell;
						inputIndex++;
					}
					rowIndex++;
				}
				
				
				index++;
			}

			return tableData;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

}
