package west.unikoblenz.beseppi.gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


//Used to color table according to results of benchmark
public class TableColorRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int col) {

		// Cells are by default rendered as a JLabel.
		JLabel l = (JLabel) super.getTableCellRendererComponent(table, value,
				isSelected, hasFocus, row, col);

		// Get the status for the current row.
		String queryType = (String) table.getValueAt(row, 1);
		if (queryType.equalsIgnoreCase("ASK")) {
			//if correct then table = green
			if (table.getValueAt(row, 2).equals("true")) {
				l.setBackground(Color.GREEN);
			} else {
				if (table.getValueAt(row, 2).equals("false")) {
					l.setBackground(Color.RED);
				} else {
					l.setBackground(Color.WHITE);
				}
			}

		}
		if (queryType.equalsIgnoreCase("SELECT")) {
			
			//if completeness or correctness not 1 mark with color
			if (!table.getValueAt(row, 4).equals("1")) {
				l.setBackground(Color.RED);
			} else {
				if (!table.getValueAt(row, 3).equals("1")){
					l.setBackground(Color.YELLOW);
				} else{
					l.setBackground(Color.GREEN);
				}
			}
		}

		// Return the JLabel which renders the cell.
		return l;

	}
}
