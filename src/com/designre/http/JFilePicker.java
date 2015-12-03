package com.designre.http;


import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class JFilePicker extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JLabel label;
	public JTextField textField;
	public JButton downloadButton;
	
	public JFileChooser fileChooser;
	
	private int mode;
	private String text = "";
	public static final int MODE_OPEN = 1;
	public static final int MODE_SAVE = 2;
	
	public JFilePicker(String textFieldLabel, String buttonLabel) {
		String userDir = System.getProperty("user.home");
		fileChooser = new JFileChooser(userDir);
		fileChooser.setCurrentDirectory(new File(userDir));
		
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		// creates the GUI
		label = new JLabel(textFieldLabel);
		System.out.println(userDir);
		
		textField = new JTextField(30);
		textField.setText(userDir);
		downloadButton = new JButton(buttonLabel);
	
		add(label);
		add(textField);
		add(downloadButton);
		
	}
	public void addFileTypeFilter(String extension, String description) {
		FileTypeFilter filter = new FileTypeFilter(extension, description);
		fileChooser.addChoosableFileFilter(filter);
	}
	
	public void setMode(int mode) {
		this.mode = mode;
	}
	
	public void setText(String text) {
		this.text = text;
		textField.setText(text);
	}
	
	public String getSelectedFilePath() {
		return textField.getText();
	}
	
	public JFileChooser getFileChooser() {
		return this.fileChooser;
	}
}