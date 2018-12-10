package west.unikoblenz.beseppi.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HtmlTable {
	private String[] headers;
	private List<String> stores;
	private List<ArrayList<String>> compRows;
	private List<ArrayList<String>> execRows;

	public HtmlTable(File csvFile, String type) {
		stores = new ArrayList<String>();
		stores.add(csvFile.getName());
		compRows = new ArrayList<ArrayList<String>>();
		execRows = new ArrayList<ArrayList<String>>();
		String[][] tableData = readCsv(csvFile);
		if (type.equals("completeness")) {
			createCompletenessTable(tableData);
		} else {
			createExecutionTimeTable(tableData);
		}

	}

	public void createExecutionTimeTable(String[][] tableData) {
		headers = new String[] { "Execution Time" };
		for (String[] csvRow : tableData) {
			ArrayList<String> currentList = new ArrayList<String>();
			currentList.add(csvRow[0]);
			currentList.add(csvRow[5]);
			execRows.add(currentList);
		}
	}

	public void createCompletenessTable(String[][] tableData) {
		headers = new String[] { "Completeness", "Soundness", "Error" };
		for (String[] csvRow : tableData) {
			ArrayList<String> currentList = new ArrayList<String>();
			currentList.add(csvRow[0]);
			currentList.add(csvRow[2]);
			currentList.add(csvRow[3]);
			currentList.add(csvRow[4]);
			compRows.add(currentList);
		}
	}

	public void addCompletenessTable(File csvFile) {
		String[][] tableData = readCsv(csvFile);
		stores.add(csvFile.getName());

		int index = 0;
		for (ArrayList<String> row : compRows) {
			String[] currentCsvRow = tableData[index];
			row.add(currentCsvRow[2]);
			row.add(currentCsvRow[3]);
			row.add(currentCsvRow[4]);
			index++;
		}
	}
	
	public void addExecTable(File csvFile){
		String[][] tableData = readCsv(csvFile);
		stores.add(csvFile.getName());
		
		int index = 0;
		for (ArrayList<String> row : execRows) {
			String[] currentCsvRow = tableData[index];
			row.add(currentCsvRow[5]);
			index++;
		}
	}

	public String returnExecTableString() {
		String html = "<html> <head> <style> table, td, th {border: 1px solid black;} </style> </head>";
		html += "<table style = \"background-color:white;\">";
		html += "<tr> <th> Query Name </th>";
		int storesIndex = 0;
		for (String store : stores) {
			storesIndex++;
			html += "<th>" + store + "</th>";
		}
		html += "</tr>";
		html += "<tr> <th> </th>";
		for (int i = 0; i < storesIndex; i++) {
			html += "<th> Average Execution time (ms)";// TODO change to better name;
		}
		html += "</tr>";
		for (ArrayList<String> row : execRows) {
			html += "<tr>";
			for (String value : row) {
				html += "<td>" + value + "</td>";
			}
			html += "</tr>";
		}
		html += "</table> </body> </html>";
		return html;
	}

	public String returnCompTableString() {
		String html = "<html> <head> <style> table, td, th {border: 1px solid black;} </style> </head>";
		html += "<table style = \"background-color:white;\">";
		html += "<tr> <th> Query Name </th>";
		int storesIndex = 0;
		for (String store : stores) {
			storesIndex++;
			html += "<th colspan = \"3\">" + store + "</th>";
		}
		html += "</tr>";
		html += "<tr> <th> </th>";
		for (int i = 0; i < storesIndex; i++) {
			for (String head : headers) {
				html += "<th> " + head + "</th>";
			}
		}
		html += "</tr>";

		for (ArrayList<String> row : compRows) {
			html += "<tr>";
			int index = 0;
			for (String value : row) {
				if (index == 0) {
					html += "<td>" + value + "</td>";

				}

				if (index == 1) {
					if (isNumber(value)) {
						double val = Double.parseDouble(value);
						if (val == 1) {
							html += "<td bgcolor = \"#6cf055\">" + value
									+ "</td>";
						} else {
							html += "<td bgcolor = \"yellow\">" + value
									+ "</td>";
						}
					} else {
						html += "<td>" + value + "</td>";
					}
				}

				if (index == 2) {
					if (isNumber(value)) {
						double val = Double.parseDouble(value);
						if (val == 1) {
							html += "<td bgcolor = \"#6cf055\">" + value
									+ "</td>";
						} else {
							html += "<td bgcolor = \"#e75656\">" + value
									+ "</td>";
						}
					} else {
						html += "<td>" + value + "</td>";
					}
				}

				if (index == 3) {
					System.out.println(value.length());
					if (value.length()>2) {
						html += "<td bgcolor = \"#e75656\">" + value + "</td>";
						System.out.println("Error");
					} else {
						html += "<td bgcolor = \"#6cf055\">" + value + "</td>";
					}
				}
				index++;
				index = index % 4;
				if (index == 0) {
					index++;
				}

			}
			html += "</tr>";
		}
		html += "</table> </body> </html>";

		return html;
	}

	public String[][] readCsv(File csvFile) {
		BufferedReader br = null;
		String line = "";
		String csvSplitBy = ",";
		try {
			int i = 0;
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
				i++;
			}
			br.close();
			// rows = i - 1;
			String[][] tableData = new String[i - 1][7];
			br = new BufferedReader(new FileReader(csvFile));
			br.readLine();
			int index = 0;
			while ((line = br.readLine()) != null) {
				String[] row = line.split(csvSplitBy);
				row[1] = row[1].split(" ", 2)[0];
				tableData[index] = row;
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

	public boolean isNumber(String input) {
		try {
			Double.parseDouble(input);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

}
