package west.unikoblenz.beseppi.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


//Allows to chose between result viewer or benchmark executer
public class ToolChooser {
private JFrame mainFrame = new JFrame("Tool Chooser");
private JButton viewerButton = new JButton("Open Result Viewer"); 
private JButton benchButton = new JButton("Open Benchmark Suite");

	
public ToolChooser(){
	JPanel mainPanel = new JPanel();
	JPanel completePanel = new JPanel();
	completePanel.setLayout(new BoxLayout(completePanel, BoxLayout.Y_AXIS));
	mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
	mainPanel.add(new JLabel("      "));
	mainPanel.add(benchButton);
	mainPanel.add(new JLabel("      "));
	mainPanel.add(viewerButton);
	mainPanel.add(new JLabel("      "));
	completePanel.add(new JLabel("\n"));
	completePanel.add(mainPanel);
	completePanel.add(new JLabel("\n"));
	
	viewerButton.addActionListener(new ViewerListener());
	benchButton.addActionListener(new SuiteListener());
	
	mainFrame.add(completePanel);
	mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	mainFrame.pack();
	mainFrame.setVisible(true);
}

	public static void main(String[] args) {
		new ToolChooser();

	}

	public class ViewerListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			mainFrame.dispose();
			new ResultViewer(true);
			
		}
		
	}
	
	public class SuiteListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			mainFrame.dispose();
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
				  new StartGui();
				}
			});;
		}
		
	}
}
