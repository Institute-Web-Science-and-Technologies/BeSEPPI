package west.unikoblenz.beseppi.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class RestfulAdder {
	JFrame mainFrame = new JFrame("New RESTful Connection");
	JButton addButton = new JButton("Add Connection");
	JTextField queryEndpoint = new JTextField(2);
	JTextField dataEndpoint = new JTextField(2);
	JTextField nameText = new JTextField(2);
	StartGui startGui;

	public RestfulAdder(StartGui gui) {
		this.startGui = gui;
		JPanel name = new JPanel();
		BoxLayout nameLayout = new BoxLayout(name, BoxLayout.X_AXIS);
		name.setLayout(nameLayout);

		name.add(new JLabel("RDF Store Name: "));
		name.add(nameText);

		JPanel query = new JPanel();
		BoxLayout queryLayout = new BoxLayout(query, BoxLayout.X_AXIS);
		query.setLayout(queryLayout);

		query.add(new JLabel("Query Endpoint:  "));
		query.add(queryEndpoint);

		JPanel data = new JPanel();
		BoxLayout dataLayout = new BoxLayout(data, BoxLayout.X_AXIS);
		data.setLayout(dataLayout);

		data.add(new JLabel("Data Endpoint:    "));
		data.add(dataEndpoint);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		AddListener listener = new AddListener();
		addButton.addActionListener(listener);

		mainPanel.add(name);
		mainPanel.add(query);
		mainPanel.add(data);
		mainPanel.add(new JLabel("\n"));
		mainPanel.add(addButton);
		mainPanel.add(new JLabel("\n"));

		mainFrame.add(mainPanel);
		mainFrame.setSize(400, 120);
		mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		mainFrame.setVisible(true);
	}

	public class AddListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			String storeName = nameText.getText();
			String query = queryEndpoint.getText();
			String data = dataEndpoint.getText();

			if (query == null || query.isEmpty() || data == null
					|| data.isEmpty()) {
				JOptionPane.showMessageDialog(mainFrame,
						"Please add a query and a data endpoint.",
						"Missing Input", JOptionPane.WARNING_MESSAGE);
			} else {
				if (storeName == null || storeName.isEmpty()) {
					storeName = "Unnamed store";
				}

         		RestfulConnection newClient = new RestfulConnection(query, data, storeName);
         		startGui.connectionInstances.add(newClient);
				startGui.addStoreClient(storeName + (" (RESTful Client)"));
				mainFrame.dispose();
			}
		}

	}
}
