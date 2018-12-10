package west.unikoblenz.beseppi.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class ClientAdder {
	StartGui startGui;
	JFrame mainFrame = new JFrame("New Java Client");
	JTextField classIdentifier = new JTextField(4);
	JTextField name = new JTextField();
	JButton addButton = new JButton("Add Client");

	public ClientAdder(StartGui gui) {
		this.startGui = gui;
		JPanel namePanel = new JPanel();
		namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));
		namePanel.add(new JLabel("RDF store name:        "));
		namePanel.add(name);

		JPanel classPanel = new JPanel();
		classPanel.setLayout(new BoxLayout(classPanel, BoxLayout.X_AXIS));
		classPanel.add(new JLabel("Fully qualified java class name:  "));
		classPanel.add(classIdentifier);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		AddListener addListener = new AddListener();
		addButton.addActionListener(addListener);

		mainPanel.add(namePanel);
		mainPanel.add(classPanel);
		mainPanel.add(new JLabel("\n"));
		mainPanel.add(addButton);
		mainPanel.add(new JLabel("\n"));

		mainFrame.add(mainPanel);
		mainFrame.setSize(400, 100);
		mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		mainFrame.setVisible(true);

	}

	public class AddListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			String storeName = name.getText();
			String className = classIdentifier.getText();

			if (className == null || className.isEmpty()) {
				JOptionPane.showMessageDialog(mainFrame,
						"Please add a fully qualified class name.",
						"Missing Input", JOptionPane.WARNING_MESSAGE);

			} else {
				if (storeName == null || storeName.isEmpty()) {
					storeName = "Unnamed store";
				}
				startGui.addStoreClient(storeName + " (Java Client)");
				mainFrame.dispose();
			}
		}

	}
}
