package west.unikoblenz.beseppi.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.util.LinkedList;

import javax.swing.border.*;

import javax.swing.*;


//This class is the main gui for BeSEPPI
public class StartGui {
	private StartGui startGui = this;
	private JFrame mainFrame = new JFrame("Property Path Benchmark Framework");
	private JPanel upperPart = new JPanel();
	private JPanel lowerPart = new JPanel();
	private JPanel benchmarkPanel = new JPanel();
	private JPanel storePanel = new JPanel();
	private JPanel resultPanel = new JPanel();
	private JPanel mainPanel = new JPanel();
	private JPanel completePanel = new JPanel();
	private JTextField choosenResultPath = new JTextField(10);
	private JPanel benchmarks = new JPanel();
	private JPanel stores = new JPanel();
	private JLabel deleteMeBenchmarks = new JLabel("\n");
	private JLabel deleteMeStores = new JLabel("\n");
	private JScrollPane scrollBenchmarks = new JScrollPane(benchmarks);
	private JScrollPane scrollStores = new JScrollPane(stores);
	private File resultPath;
	private ButtonGroup radioGroup = new ButtonGroup();
	public LinkedList<JRadioButton> benchList = new LinkedList<JRadioButton>();
	public LinkedList<JRadioButton> connList = new LinkedList<JRadioButton>();
	public LinkedList<NewBenchmark> benchmarkInstances = new LinkedList<NewBenchmark>();
	public LinkedList<RestfulConnection> connectionInstances = new LinkedList<RestfulConnection>();


	public StartGui() {
		prepareCompletePanel(); //prepare panel to display all components

		mainFrame.add(completePanel);
		mainFrame.pack(); //pack everything to be scaled correctly
		mainFrame.setVisible(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}


	//Creates panel which allows to add benchmarks
	public void createBenchmarkPanel() {
		benchmarks.setLayout(new BoxLayout(benchmarks, BoxLayout.Y_AXIS));
		benchmarks.add(deleteMeBenchmarks);

		BoxLayout boxlayout = new BoxLayout(benchmarkPanel, BoxLayout.Y_AXIS);
		benchmarkPanel.setLayout(boxlayout);

		benchmarkPanel.add(scrollBenchmarks);

		JButton addNewBenchmarkButton = new JButton("Add new Benchmark");
		AddBenchmarkListener addBenchmark = new AddBenchmarkListener();
		addNewBenchmarkButton.addActionListener(addBenchmark);
		addNewBenchmarkButton.setAlignmentX(Component.CENTER_ALIGNMENT);

		benchmarkPanel.add(new JLabel("\n"));
		benchmarkPanel.add(addNewBenchmarkButton);

		Border border = BorderFactory.createLineBorder(Color.black);
		TitledBorder title = new TitledBorder(border, "Benchmarks");
		benchmarkPanel.setBorder(title);
	}

	public JPanel getBenchmarks(){
		return this.benchmarks;
		
	}
	
	public LinkedList<JRadioButton> getBenchList(){
		return this.benchList;
	}
	
	public LinkedList<NewBenchmark> getNewBenchmarks(){
		return this.benchmarkInstances;
	}
	
	
	
	//Creates panel which allows to add stores
	public void createStorePanel() {
		stores.setLayout(new BoxLayout(stores, BoxLayout.Y_AXIS));
		stores.add(deleteMeStores);

		BoxLayout boxlayout = new BoxLayout(storePanel, BoxLayout.Y_AXIS);
		storePanel.setLayout(boxlayout);

		storePanel.add(scrollStores);

		JPanel buttons = new JPanel();
		BoxLayout buttonlayout = new BoxLayout(buttons, BoxLayout.X_AXIS);
		buttons.setLayout(buttonlayout);

		JButton addRestful = new JButton("New RESTful Connection");
		RestfulListener restList = new RestfulListener();
		addRestful.addActionListener(restList);
		buttons.add(addRestful);


		storePanel.add(new JLabel("\n"));
		storePanel.add(buttons);

		Border border = BorderFactory.createLineBorder(Color.black);
		TitledBorder title = new TitledBorder(border, "Stores");
		storePanel.setBorder(title);
	}

	//Create Result panel where can be chosen where result will be stored
	public void createResultPanel() {
		JPanel labelAndButton = new JPanel();
		BoxLayout smallLayout = new BoxLayout(labelAndButton, BoxLayout.X_AXIS);
		labelAndButton.setLayout(smallLayout);

		BoxLayout layout = new BoxLayout(resultPanel, BoxLayout.Y_AXIS);
		resultPanel.setLayout(layout);

		labelAndButton.add(new JLabel());

		JButton resultPathButton = new JButton("Choose Path");
		ResultPathListener pathListener = new ResultPathListener();
		resultPathButton.addActionListener(pathListener);

		labelAndButton.add(resultPathButton);
		labelAndButton.setAlignmentX(Component.CENTER_ALIGNMENT);

		resultPanel.add(new JLabel("\n"));

		resultPanel.add(new JLabel("\n"));
		JPanel textPanel = new JPanel();
		BoxLayout textLayout = new BoxLayout(textPanel, BoxLayout.X_AXIS);
		textPanel.setLayout(textLayout);
		textPanel.add(new JLabel(
				"                                  Result Path:  "));
		textPanel.add(choosenResultPath);
		textPanel.add(new JLabel(
				"                                                "));

		resultPanel.add(textPanel);
		resultPanel.add(new JLabel("\n"));
		resultPanel.add(labelAndButton);
		resultPanel.add(new JLabel("\n"));

		Border border = BorderFactory.createLineBorder(Color.black);
		TitledBorder title = new TitledBorder(border, "Result");
		resultPanel.setBorder(title);
	}

	//Wrapper for preparing stores and benchmarks
	public void prepareUpperPart() {
		createBenchmarkPanel();
		createStorePanel();

		GridLayout layout = new GridLayout(1, 2, 1, 1);
		upperPart.setLayout(layout);

		upperPart.add(benchmarkPanel);
		upperPart.add(storePanel);

	}

	//Wrapper for preparing result panel
	public void prepareLowerPart() {
		createResultPanel();

		GridLayout layout = new GridLayout(1, 1, 1, 1);
		lowerPart.setLayout(layout);

		lowerPart.add(resultPanel);

	}

	public void prepareMainPanel() {
		prepareUpperPart();
		prepareLowerPart();

		GridLayout layout = new GridLayout(2, 1, 1, 1);
		mainPanel.setLayout(layout);

		mainPanel.add(upperPart);
		mainPanel.add(lowerPart);
	}

	public void prepareCompletePanel() {
		prepareMainPanel();

		BoxLayout boxlayout = new BoxLayout(completePanel, BoxLayout.Y_AXIS);
		completePanel.setLayout(boxlayout);

		completePanel.add(mainPanel);
		StartListener listenerStart = new StartListener();
		JButton button = new JButton("Start");
		button.addActionListener(listenerStart);
		button.setAlignmentX(Component.CENTER_ALIGNMENT);

		completePanel.add(button);
	}

	//For debuging only
	public void debugView() {
		prepareCompletePanel();

		mainFrame.add(completePanel);

		mainFrame.pack();
		mainFrame.setVisible(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	//prepares compelte gui
	public void prepareGui() {
		mainFrame.setSize(600, 380);
		mainFrame.setLayout(new BorderLayout());
		mainFrame.add(upperPart, BorderLayout.CENTER);
		mainFrame.add(resultPanel, BorderLayout.PAGE_END);

		// mainFrame.setResizable(false);
		mainFrame.setVisible(true);

		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void setVisible() {
		mainFrame.setVisible(true);
	}

	public void addBenchmark(String benchName) {
		CheckboxTextFieldEntry newBenchmark = new CheckboxTextFieldEntry(
				benchName, "Query Timeout in ms");
		benchList.add(newBenchmark.getRadioButton());
		//benchmarkInstances.add(benchmarkAdder.getBenchmark());
		benchmarks.add(newBenchmark.getContent());
		benchmarks.remove(deleteMeBenchmarks);
		benchmarks.revalidate();
		mainFrame.repaint();

	}

	//Allows for adding a new store client 
	public void addStoreClient(String name) {
		JPanel contentPanel = new JPanel();
		JRadioButton radio = new JRadioButton(name);
		radio.setSelected(true);
		connList.add(radio);
		radioGroup.add(radio);
		radio.setAlignmentX(Component.LEFT_ALIGNMENT);
		contentPanel.add(radio);
		stores.remove(deleteMeStores);
		stores.add(contentPanel);
		stores.revalidate();
		stores.repaint();
	}
	
	public void addToRadioGroup(CheckboxTextFieldEntry entry){
		radioGroup.add(entry.getRadioButton());
	}

	public File getResultPath() {
		return resultPath;
	}

	public void addBenchmarkInstance(NewBenchmark bench){
		this.benchmarkInstances.add(bench);
	}
	//Listeners for adding new benchmark
	private class AddBenchmarkListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			@SuppressWarnings("unused")
			BenchmarkAdder adder = new BenchmarkAdder(startGui);
            
		}

	}

	//Listener to start benchmark execution
	public class StartListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			if (!(resultPath == null)) {
				mainFrame.setVisible(false);

				Thread executionThread = new Thread() {
					public void run() {
						try {
							 new ExecutionGui(startGui);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				};
				executionThread.start();
			} else {
				JOptionPane.showMessageDialog(mainFrame,
						"Please chose a result directory.",
						"No Result Directory", JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	//Listener to add result path
	public class ResultPathListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File("/home"));
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = fileChooser.showOpenDialog(new JFrame());

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				choosenResultPath.setText(file.getAbsolutePath());
				resultPath = file;
			}
		}

	}

	
	//Listener to add restful client
	public class RestfulListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			new RestfulAdder(startGui);
		}

	}

	public class ClientListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			new ClientAdder(startGui);

		}

	}

}
