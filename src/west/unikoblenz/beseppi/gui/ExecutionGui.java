package west.unikoblenz.beseppi.gui;

import java.awt.Component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;

import west.unikoblenz.beseppi.Main;

public class ExecutionGui {
	private StartGui startGui;
	private JFrame mainFrame = new JFrame("Benchmark Execution");
	private JProgressBar progressBar;
	private JPanel consolePanel = new JPanel();
	private JPanel mainPanel = new JPanel();
	private JTextArea console = new JTextArea(20, 40);
	private JScrollPane scroll;
	private int queryNumber;
	private JButton continueButton;
	private JButton cancelButton;
	private ExecutionGui executionGui = this;
	private JScrollBar vertical;

	public ExecutionGui(StartGui gui) throws InterruptedException {

		this.startGui = gui;

		mainFrame.setResizable(false);
		progressBar = new JProgressBar(0, 100);
		progressBar.setStringPainted(true);
		progressBar.setValue(0);
		createMainPanel();

		mainFrame.add(mainPanel);
		mainFrame.setVisible(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		mainFrame.pack();
		console.append("Benchmark Run starts\n");

		Thread executionThread = new Thread() {
			int i = 0;
			int j = 0;

			public void run() {

				// Get which benchmark should be executed
				for (i = 0; i < startGui.benchList.size(); i++) {
					JRadioButton currentButton = (startGui.benchList.get(i));
					if (currentButton.isSelected()) {
						break;
					}
				}

				NewBenchmark benchmark = startGui.benchmarkInstances.get(i);

				// Get which connection should be used
				for (j = 0; j < startGui.connList.size(); j++) {
					JRadioButton currentButton = (startGui.benchList.get(j));
					if (currentButton.isSelected()){
						break;
					}
				}

				RestfulConnection conn = startGui.connectionInstances.get(j);
				
				String[] arguments = { benchmark.getPath(),
						startGui.getResultPath().getAbsolutePath(),
                        conn.getQuery(), "", conn.getData()
						
				};

				Main.executeFromGui(arguments, executionGui);
			}
		};
		executionThread.start();

	}

	public void createLogConsole() {
		console.setLineWrap(true);
		console.setEditable(false);
		console.setVisible(true);

		scroll = new JScrollPane(console);

		scroll.setSize(20, 40);

		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		consolePanel.add(scroll);

	}

	public void createMainPanel() {
		createLogConsole();
		BoxLayout boxlayout = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);

		mainPanel.setLayout(boxlayout);

		mainPanel.add(consolePanel);
		mainPanel.add(progressBar);

		mainPanel.add(new JLabel("\n"));

		JPanel buttonPanel = new JPanel();
		BoxLayout buttonLayout = new BoxLayout(buttonPanel, BoxLayout.X_AXIS);
		buttonPanel.setLayout(buttonLayout);

		cancelButton = new JButton("  Cancel ");
		CancelListener cancel = new CancelListener();
		cancelButton.addActionListener(cancel);

		continueButton = new JButton("Show results");
		ContinueListener showResult = new ContinueListener();
		continueButton.addActionListener(showResult);
		continueButton.setEnabled(false);

		buttonPanel.add(cancelButton);
		buttonPanel.add(new JLabel("  "));
		buttonPanel.add(continueButton);
		buttonPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		mainPanel.add(buttonPanel);

		mainPanel.add(new JLabel("\n"));
		vertical = scroll.getVerticalScrollBar();
	}

	// dummy method for showing execution gui
	public void goOn() throws InterruptedException {

		queryNumber = 186;

		JScrollBar vertical = scroll.getVerticalScrollBar();

		for (int progress = 0; progress <= queryNumber; progress++) {

			vertical.setValue(vertical.getMaximum());
			updateGui(progress);

			Thread.sleep(40);

		}
		cancelButton.setEnabled(false);
		continueButton.setEnabled(true);

		console.append("Benchmark run finished succesfully");
		vertical.setValue(vertical.getMaximum());

	}

	public void finishExecution() {
		cancelButton.setEnabled(false);
		continueButton.setEnabled(true);
	}

	public void consoleAppend(String message) {
		console.append(message);
		vertical.setValue(vertical.getMaximum());
	}

	public void updateGui(int progress) {
		console.append("Processing query " + progress + "\n");
		progressBar.setValue(progress);
		progressBar.setString(Integer.toString(progress) + "/" + queryNumber);

	}

	public void setQueryNumber(int i) {
		queryNumber = i * 12;
		progressBar.setMaximum(i * 12);
	}

	public class CancelListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			mainFrame.dispose();
			startGui.setVisible();

		}

	}

	public class ContinueListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {

			ResultViewer viewer = new ResultViewer();
			File[] listOfFiles = startGui.getResultPath().listFiles();
			for (File file : listOfFiles) {
				if (file.getName().endsWith(".csv")) {
					viewer.createTable(file.getAbsoluteFile(), file.getName());
				}
			}

			viewer.prepareViewer();
			mainFrame.dispose();

		}

	}

}
