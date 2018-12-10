package west.unikoblenz.beseppi.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

//Creates view of results of benchmark
public class ResultViewer {
	private JTabbedPane tabbedPane = new JTabbedPane();
	private JFrame mainFrame = new JFrame();
	private JMenuBar menuBar = new JMenuBar();
	private JMenu fileMenu;
	private JMenuItem openMenuItem;
	private JMenuItem addMenuItem;
	private JPanel addFilePanel;
	private JPanel mainPanel;
	private JLabel compTable;
	private JLabel execTable;
    private HtmlTable htmlTableCompl;
    private HtmlTable htmlTableExec;
    
	public ResultViewer() {

	}

	public ResultViewer(boolean isEmpty) {
		prepareViewer();

	}

	//Creates the table for benchmark results
	public void createTable(File resultPath, String storeName) {

		htmlTableCompl = new HtmlTable(resultPath, "completeness");
		htmlTableExec = new HtmlTable(resultPath, "executionTime");
		compTable = new JLabel(htmlTableCompl.returnCompTableString());
		execTable = new JLabel(htmlTableExec.returnExecTableString());
		JScrollPane compScroll = new JScrollPane(compTable);
		JScrollPane execScroll = new JScrollPane(execTable);
		tabbedPane.add("Completeness and Soundness", compScroll);
		tabbedPane.add("ExecutionTime", execScroll);
	}

	//updates table when a new htmlTable is added
	public void updateTable(File resultPath) {
         htmlTableCompl.addCompletenessTable(resultPath);
         htmlTableExec.addExecTable(resultPath);
         compTable.setText(htmlTableCompl.returnCompTableString());
 		 execTable.setText(htmlTableExec.returnExecTableString());
 		 mainFrame.repaint();
	}

	//Prepares viewer for display html tables
	public void prepareViewer(){
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		fileMenu = new JMenu("File");
	    openMenuItem = new JMenuItem("Open");
	    openMenuItem.addActionListener(new OpenFileListener());
	    addMenuItem = new JMenuItem("Add");
	    addMenuItem.addActionListener(new AddFileListener());
	    
	    fileMenu.add(openMenuItem);
	    fileMenu.add(addMenuItem);
	    menuBar.add(fileMenu);
	    mainFrame.setJMenuBar(menuBar);
	    
	   // mainFrame.setLayout(new BoxLayout(mainFrame, BoxLayout.Y_AXIS));
	    
	    addFilePanel = new JPanel();
	    JButton addFileButton = new JButton("Add File");
	    addFileButton.setAlignmentX(Component.LEFT_ALIGNMENT);
	    addFileButton.addActionListener(new OpenFileListener());
	    addFilePanel.add(addFileButton);
	    //mainPanel.add(addFilePanel);
	  
		
		mainPanel.add(tabbedPane);
		mainFrame.add(mainPanel);
		mainFrame.setPreferredSize(new Dimension(800, 600));
		mainFrame.setVisible(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.pack();
	}

	
	//Allows for adding csv file to result viewer
	public class AddFileListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			JFileChooser fileChooser = new JFileChooser();

			fileChooser.setCurrentDirectory(new File("/home/adrian/Documents/myBenchmakFinal")); //CHANGE ME

			int returnVal = fileChooser.showOpenDialog(new JFrame());

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				if (file.getAbsolutePath().endsWith(".csv")) {
					updateTable(file);
				} else {
					JOptionPane.showMessageDialog(mainFrame,
							"File is not a CSV File", "Wrong Input",
							JOptionPane.WARNING_MESSAGE);
				}
			}

		}

	}

	//Allows for adding several csvs in the same directory
	public class OpenFileListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			JFileChooser fileChooser = new JFileChooser();

			fileChooser.setCurrentDirectory(new File("/home/adrian/Documents/myBenchmakFinal"));

			int returnVal = fileChooser.showOpenDialog(new JFrame());

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				if (file.getAbsolutePath().endsWith(".csv")) {
					createTable(file, file.getName());
				} else {
					JOptionPane.showMessageDialog(mainFrame,
							"File is not a CSV File", "Wrong Input",
							JOptionPane.WARNING_MESSAGE);
				}

			}
		}

	}

}
