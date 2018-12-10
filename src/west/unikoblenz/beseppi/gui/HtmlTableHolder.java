package west.unikoblenz.beseppi.gui;


public class HtmlTableHolder {
	private String htmlTable;
	
	
	
	public HtmlTableHolder(){
		createTable(new String[1][1]);
	}
	
	
	public String getTable(){
		return htmlTable;
	}
	
	
	public void createTable(String[][] tableData){
		//initial html blabla
		String html = "<html> <head> <style> table, td {border: 1px solid black;} </style> </head>";
		html += "<table style = \"background-color:white;\"> <tr> <th> Name </th> <th> Alter </th> </tr>";
		html += "<tr> <td> Adrian </td> <td> 25 </td> </tr>";
		html += "<tr> <td> Anso </td> <td> 27 </td> </tr>";
		html += "</table> </body> </html>";
		htmlTable = html;
		
		
	}
	
	

	
}
