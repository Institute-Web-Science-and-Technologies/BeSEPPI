package west.unikoblenz.beseppi.gui;

import java.awt.BorderLayout;
import java.awt.Component;


import javax.swing.*;

public class CheckboxTextFieldEntry extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel contentPanel = new JPanel();
	JRadioButton radioButton;

	public CheckboxTextFieldEntry(String name, String defaultText) {
		BoxLayout boxlayout = new BoxLayout(contentPanel, BoxLayout.X_AXIS);
		contentPanel.setLayout(boxlayout);
		radioButton = new JRadioButton(name);
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new BorderLayout());
		JTextField textField = new JTextField(defaultText);
		//textField.setPreferredSize(new Dimension(1,2));
		
		textPanel.add(textField);
		
		radioButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		//textField.setAlignmentX(Component.RIGHT_ALIGNMENT);
		radioButton.setSelected(true); 
		contentPanel.add(radioButton);
//		contentPanel.add(textPanel);
		//contentPanel.add(textField);

	}
	
	public JRadioButton getRadioButton(){
		return radioButton;
	}
	
	public JPanel getContent(){
		return this.contentPanel;
	}
}
