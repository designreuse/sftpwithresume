package com.designre.ftp.download;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.designre.ftp.JFilePicker;

public class SftpGUI extends JApplet implements PropertyChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel labelHost = new JLabel("Host:");
	private JLabel labelPort = new JLabel("Port:");
	private JLabel labelUsername = new JLabel("Username:");
	private JLabel labelPassword = new JLabel("Password:");
	private JLabel labelDownloadPath = new JLabel("Download path:");

	private JTextField fieldHost = new JTextField(40);
	private JTextField fieldPort = new JTextField(5);
	private JTextField fieldUsername = new JTextField(30);
	private JPasswordField fieldPassword = new JPasswordField(30);
	private JTextField fieldDownloadPath = new JTextField(30);

	private JFilePicker filePicker = new JFilePicker("Save file to: ", "Browse...");

	private JButton buttonDownload = new JButton("Download");

	private JLabel labelFileSize = new JLabel("File Size: ");
	private JTextField fieldFileSize = new JTextField(15);
	
	private JLabel labelProgress = new JLabel("Progress:");
	public static JProgressBar progressBar = new JProgressBar(0, 100);

	private SftpTask task;
	
	public SftpGUI() {
		
		// set up layout
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(5, 5, 5, 5);

		// set up components
		filePicker.setMode(JFilePicker.MODE_SAVE);
		filePicker.getFileChooser().setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fieldFileSize.setEditable(false);
		
		buttonDownload.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				buttonDownloadActionPerformed(event);
			}
		});

		progressBar = new JProgressBar(0, 100);
		progressBar.setStringPainted(true);

		// add components to the frame
		constraints.gridx = 0;
		constraints.gridy = 0;
		add(labelHost, constraints);

		constraints.gridx = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 1.0;
		add(fieldHost, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		add(labelPort, constraints);

		constraints.gridx = 1;
		add(fieldPort, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		add(labelUsername, constraints);

		constraints.gridx = 1;
		add(fieldUsername, constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		add(labelPassword, constraints);

		constraints.gridx = 1;
		add(fieldPassword, constraints);

		constraints.gridx = 0;
		constraints.gridy = 4;
		add(labelDownloadPath, constraints);

		constraints.gridx = 1;
		add(fieldDownloadPath, constraints);

		constraints.gridx = 0;
		constraints.gridwidth = 2;
		constraints.gridy = 5;
		constraints.anchor = GridBagConstraints.WEST;

		add(filePicker, constraints);

		constraints.gridx = 0;
		constraints.gridy = 6;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		add(buttonDownload, constraints);

		constraints.gridx = 0;
		constraints.gridy = 7;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;

		add(labelFileSize, constraints);

		constraints.gridx = 1;
		add(fieldFileSize, constraints);

		constraints.gridx = 0;
		constraints.gridy = 8;
		add(labelProgress, constraints);

		constraints.gridx = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		progressBar.setVisible(true);
		add(progressBar, constraints);
	
	}

	/**
	 * handle click event of the Download button
	 */
	private void buttonDownloadActionPerformed(ActionEvent event) {
		String host = fieldHost.getText();
		int port = Integer.parseInt(fieldPort.getText());
		String username = fieldUsername.getText();
		String password = new String(fieldPassword.getPassword());
		String downloadPath = fieldDownloadPath.getText();
		String saveDir = filePicker.getSelectedFilePath();

		progressBar.setValue(0);
		task = new SftpTask(host, port, username, password, downloadPath, saveDir, this);
		task.addPropertyChangeListener(this);
		task.execute();
	
	}

	/**
	 * Update the progress bar's state whenever the progress of download
	 * changes.
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
			int progress = (Integer) evt.getNewValue();
			progressBar.setValue(progress);
		}
	}

	void setFileSize(String fileSize) {
		fieldFileSize.setText(String.valueOf(fileSize));
	}


	/**
	 * Launch the application
	 */
	public void init() {
		try {
			// set look and feel to system dependent
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}	
}