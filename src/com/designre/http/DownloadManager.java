package com.designre.http;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.event.*;

// The Download Manager.
public class DownloadManager extends JFrame implements Observer {
	
	private static final long serialVersionUID = 8489399426552541643L;
     
    // Download table's data model.
    private DownloadsTableModel tableModel;
    private JFilePicker filePicker = new JFilePicker("Download Directory: ", "Browse...");
     
    // Table listing downloads.
    private JTable table;
     
    // These are the buttons for managing the selected download.
    private JButton pauseButton, resumeButton;
    private JButton cancelButton, clearButton;
     
    // Currently selected download.
    private Download selectedDownload;
     
    // Flag for whether or not table selection is being cleared.
    private boolean clearing;
    private static Hashtable<String, PackageInfo> filesToDownload;
     
    // Constructor for Download Manager.
    public DownloadManager(Hashtable<String, PackageInfo> filesToDownload) {
        
    	DownloadManager.filesToDownload = filesToDownload;
    	// Set application title.
        setTitle("IP Download Manager");
         
        // Set window size.
        setSize(640, 480);
         
        // Handle window closing events.
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                actionExit();
            }
        });
         
        // Set up file menu.
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        JMenuItem fileExitMenuItem = new JMenuItem("Exit", KeyEvent.VK_X);
        fileExitMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionExit();
            }
        });
        fileMenu.add(fileExitMenuItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
         
        // Set up add panel.
        JPanel addPanel = new JPanel();
		filePicker.setMode(JFilePicker.MODE_SAVE);
 		filePicker.getFileChooser().setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
 		
 		
 		filePicker.downloadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	 int retval = filePicker.fileChooser.showOpenDialog(null);
                 if (retval == JFileChooser.APPROVE_OPTION) {
                	 filePicker.textField.setText(filePicker.fileChooser.getSelectedFile().getAbsolutePath());
                	 filePicker.downloadButton.setEnabled(false);
                   actionAddWithDownload();
                 }
            }
        });
 		 addPanel.add(filePicker);

        // Set up Downloads table.
        tableModel = new DownloadsTableModel();
        table = new JTable(tableModel);
        actionAdd();
        table.getSelectionModel().addListSelectionListener(new
                ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                tableSelectionChanged();
            }
        });
        // Allow only one row at a time to be selected.
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
         
        // Set up ProgressBar as renderer for progress column.
        ProgressRenderer renderer = new ProgressRenderer(0, 100);
        renderer.setStringPainted(true); // show progress text
        table.setDefaultRenderer(JProgressBar.class, renderer);
         
        // Set table's row height large enough to fit JProgressBar.
        table.setRowHeight(
                (int) renderer.getPreferredSize().getHeight());
         
        // Set up downloads panel.
        JPanel downloadsPanel = new JPanel();
        downloadsPanel.setBorder(BorderFactory.createTitledBorder("Downloads"));
        downloadsPanel.setLayout(new BorderLayout());
        downloadsPanel.add(new JScrollPane(table), BorderLayout.CENTER);
         
        // Set up buttons panel.
        JPanel buttonsPanel = new JPanel();
        pauseButton = new JButton("Pause");
        pauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionPause();
            }
        });
        pauseButton.setEnabled(false);
        buttonsPanel.add(pauseButton);
        resumeButton = new JButton("Resume");
        resumeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionResume();
            }
        });
        resumeButton.setEnabled(false);
        buttonsPanel.add(resumeButton);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionCancel();
            }
        });
        cancelButton.setEnabled(false);
        buttonsPanel.add(cancelButton);
        clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionClear();
            }
        });
        clearButton.setEnabled(false);
        buttonsPanel.add(clearButton);
         
        // Add panels to display.
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(addPanel, BorderLayout.NORTH);
        getContentPane().add(downloadsPanel, BorderLayout.CENTER);
        getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
    }
     
    // Exit this program.
    private void actionExit() {
        System.exit(0);
    }
     
    // Add a new download.
    private void actionAdd() {
    	String saveDir = filePicker.textField.getText();
    	Enumeration<String> e = filesToDownload.keys();
    	while(e.hasMoreElements()){
    		String id = e.nextElement();
    		PackageInfo packageInfo = filesToDownload.get(id);
    		String url = packageInfo.getPackageURL();
        URL verifiedUrl = verifyUrl(url);
        if (verifiedUrl != null) {
        	
            tableModel.addDownload(new Download(packageInfo, saveDir, verifiedUrl));
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Download URL", "Error", JOptionPane.ERROR_MESSAGE);
        }
    	}
    }
    
    private void actionAddWithDownload() {
    	String saveDir = filePicker.textField.getText();
    	int rows = tableModel.getRowCount();
    	for (int i = rows -1 ; i >= 0; i--){
    		tableModel.clearDownload(i);
    		tableModel.fireTableDataChanged();
    	}
    	Enumeration<String> e = filesToDownload.keys();
    	while(e.hasMoreElements()){
    		String id = e.nextElement();
    		PackageInfo packageInfo = filesToDownload.get(id);
    		String url = packageInfo.getPackageURL();
        URL verifiedUrl = verifyUrl(url);
        if (verifiedUrl != null) {
        	
            tableModel.addDownload(new Download(packageInfo, saveDir, verifiedUrl, true));
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Download URL", "Error", JOptionPane.ERROR_MESSAGE);
        }
    	}
    }
     
    // Verify download URL.
    private URL verifyUrl(String url) {
        // Only allow HTTP URLs.
        if (!url.toLowerCase().startsWith("http://"))
            return null;
         
        // Verify format of URL.
        URL verifiedUrl = null;
        try {
            verifiedUrl = new URL(url);
        } catch (Exception e) {
            return null;
        }
         
        // Make sure URL specifies a file.
        if (verifiedUrl.getFile().length() < 2)
            return null;
         
        return verifiedUrl;
    }
     
    // Called when table row selection changes.
    private void tableSelectionChanged() {
    /* Unregister from receiving notifications
       from the last selected download. */
        if (selectedDownload != null)
            selectedDownload.deleteObserver(DownloadManager.this);
         
    /* If not in the middle of clearing a download,
       set the selected download and register to
       receive notifications from it. */
        if (!clearing) {
            selectedDownload = tableModel.getDownload(table.getSelectedRow());
            selectedDownload.addObserver(DownloadManager.this);
            updateButtons();
        }
    }
     
    // Pause the selected download.
    private void actionPause() {
        selectedDownload.pause();
        updateButtons();
    }
     
    // Resume the selected download.
    private void actionResume() {
        selectedDownload.resume();
        updateButtons();
    }
     
    // Cancel the selected download.
    private void actionCancel() {
        selectedDownload.cancel();
        updateButtons();
    }
     
    // Clear the selected download.
    private void actionClear() {
        clearing = true;
        tableModel.clearDownload(table.getSelectedRow());
        clearing = false;
        selectedDownload = null;
        updateButtons();
    }
     
  /* Update each button's state based off of the
     currently selected download's status. */
    private void updateButtons() {
        if (selectedDownload != null) {
            int status = selectedDownload.getStatus();
            switch (status) {
                case Download.DOWNLOADING:
                    pauseButton.setEnabled(true);
                    resumeButton.setEnabled(false);
                    cancelButton.setEnabled(true);
                    clearButton.setEnabled(false);
                    break;
                case Download.PAUSED:
                    pauseButton.setEnabled(false);
                    resumeButton.setEnabled(true);
                    cancelButton.setEnabled(true);
                    clearButton.setEnabled(false);
                    break;
                case Download.ERROR:
                    pauseButton.setEnabled(false);
                    resumeButton.setEnabled(true);
                    cancelButton.setEnabled(false);
                    clearButton.setEnabled(true);
                    break;
                default: // COMPLETE or CANCELLED
                    pauseButton.setEnabled(false);
                    resumeButton.setEnabled(false);
                    cancelButton.setEnabled(false);
                    clearButton.setEnabled(true);
            }
        } else {
            // No download is selected in table.
            pauseButton.setEnabled(false);
            resumeButton.setEnabled(false);
            cancelButton.setEnabled(false);
            clearButton.setEnabled(false);
        }
    }
     
  /* Update is called when a Download notifies its
     observers of any changes. */
    public void update(Observable o, Object arg) {
        // Update buttons if the selected download has changed.
        if (selectedDownload != null && selectedDownload.equals(o))
            updateButtons();
    }
     
    public static void main(String[] args) {
    	
    	     Hashtable<String, PackageInfo> packagesInfo = new PackageInfo().getPackagesInfo(args);
			 DownloadManager manager = new DownloadManager(packagesInfo);
		        manager.show();
	}
}