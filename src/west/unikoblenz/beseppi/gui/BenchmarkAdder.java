package west.unikoblenz.beseppi.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;

public class BenchmarkAdder {
	private StartGui startGui;
	private JFrame mainFrame = new JFrame("Add Benchmark");
	private JTextField nameTextField;
	private JTextField directoryTextField;
	private File benchmark;
	private NewBenchmark bench;

	public BenchmarkAdder(StartGui gui) {
		this.startGui = gui;

		JPanel mainPanel = new JPanel();
		BoxLayout boxlayout = new BoxLayout(mainPanel, BoxLayout.X_AXIS);

		JButton addButton = new JButton("Add");
		ListenerAdd addListener = new ListenerAdd();
		addButton.addActionListener(addListener);
		mainPanel.setLayout(boxlayout);
		JLabel description = new JLabel(
				"Benchmark directory needs subdirectory:\n");
		JLabel nameLabel = new JLabel("Benchmark Name:       ");
		nameTextField = new JTextField(10);
		JButton openBenchmark = new JButton("Open Benchmark");
		ListenerOpen openListener = new ListenerOpen();
		openBenchmark.addActionListener(openListener);

		mainPanel.add(nameLabel);
		mainPanel.add(nameTextField);
		mainPanel.add(new JLabel("           "));
		mainPanel.add(addButton);
		mainPanel.add(new JLabel("             "));
		// mainPanel.add(openBenchmark);

		JPanel directoryPanel = new JPanel();
		directoryPanel
				.setLayout(new BoxLayout(directoryPanel, BoxLayout.X_AXIS));
		directoryPanel.add(new JLabel("Benchmark Directory: "));
		directoryTextField = new JTextField(10);
		directoryPanel.add(directoryTextField);
		directoryPanel.add(openBenchmark);
		directoryPanel.add(new JLabel(" "));

		JPanel completePanel = new JPanel();
		BoxLayout layout = new BoxLayout(completePanel, BoxLayout.Y_AXIS);
		completePanel.setLayout(layout);

		completePanel.add(description);
		completePanel.add(new JLabel("  - \"queries\" holding all queries\n"));
		completePanel.add(new JLabel("  - \"datasets\" holding all datasets"));
		completePanel.add(new JLabel("   "));

		completePanel.add(directoryPanel);
		completePanel.add(new JLabel("\n"));


		completePanel.add(mainPanel);
		completePanel.add(new JLabel("\n"));

		mainFrame.add(completePanel);
		mainFrame.pack();
		mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		mainFrame.setVisible(true);

	}

	public NewBenchmark getBenchmark(){
		return bench;
	}
	
	public class ListenerOpen implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File("/home"));
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fileChooser.showOpenDialog(new JFrame());

			File benchPath = fileChooser.getSelectedFile();
			if (!PathValidator.validateBenchmark(benchPath)) {
				JOptionPane.showMessageDialog(mainFrame,
						"Choosen Directory is not a benchmark.", "Wrong Input",
						JOptionPane.WARNING_MESSAGE);
			} else {
				benchmark = benchPath;
				directoryTextField.setText(benchmark.getAbsolutePath());

			}

		}
	}

	public class ListenerAdd implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			String benchName = nameTextField.getText();
			if (!(benchmark == null || benchName == null)) {

				Thread executionThread = new Thread() {
					public void run() {
						String benchName = nameTextField.getText();

						startGui.addBenchmark(benchName);
						bench = new NewBenchmark(benchName, benchmark.getAbsolutePath());
						startGui.addBenchmarkInstance(bench);
					}

				};
				executionThread.start();
				mainFrame.setVisible(false);
				mainFrame.dispose();

			}
			else{
				JOptionPane.showMessageDialog(mainFrame,
						"Please Fill out all both text Fields.", "Wrong Input",
						JOptionPane.WARNING_MESSAGE);
			}
		}

	}

}
