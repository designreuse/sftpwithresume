package com.designre.ftp.download;

import java.util.Properties;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.SftpProgressMonitor;


public class SftpTask extends SwingWorker<Void, Void> {

	 
	private String host;
	private int port;
	private String username;
	private String password;
	
	private String downloadPath;
	private String saveDir;

	private JSch jsch = null;
    private Session session = null;
    private Channel channel = null;
    private ChannelSftp channelSftp = null;
	private SftpGUI gui;
	
	public SftpTask(String host, int port, String username, String password, String downloadPath, String saveDir, SftpGUI gui) {
		
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
		this.downloadPath = downloadPath;
		this.saveDir = saveDir;
		this.gui = gui;
	}
	
	public void connect() {
        try {
           
            jsch = new JSch();
            session = jsch.getSession(username, host, port);

            session.setPassword(password);
            
            Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            session.connect();
            
            channel = session.openChannel("sftp");
            channel.connect();
            channelSftp = (ChannelSftp) channel;

        } catch (JSchException e) {
        	JOptionPane.showMessageDialog(null, "Error downloading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


	/**
	 * Executed in background thread
	 */	
	@Override
	protected Void doInBackground() throws Exception {
		
		try {
			connect();
			
			long fileSize = getFileSize(downloadPath);
			gui.setFileSize(SizeUtil.conevertToHumanReadableSize(fileSize));
			channelSftp.get(downloadPath, saveDir, new TransferProgress(), ChannelSftp.RESUME);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Error downloading file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);			
			ex.printStackTrace();
			setProgress(0);
			cancel(true);			
		} 
		return null;
	}
	
	public static class MyProgressMonitor implements SftpProgressMonitor
	  {
	   
	    long count=0;
	    long max=0;

	     public void init(int op, String src, String dest, long max)
	     {
	          this.max=max;
	          count=0;
	    }

	     public boolean count(long _count)
	     {
	          this.count+=_count;
	          SftpGUI.progressBar.setValue((int)this.count);
	          return true;
	    }
	    public void end()
	    {
	        
	    }
	  }
	
	@Override
	protected void done() {
		if (!isCancelled()) {
			JOptionPane.showMessageDialog(null, "File has been downloaded successfully!", "Message", JOptionPane.INFORMATION_MESSAGE);
		}
	}	
	
	 public long getFileSize (String srcSftpFilePath) throws SftpException {  
	        long filesize = 0;
	        try {  
	            SftpATTRS sftpATTRS = channelSftp.lstat(srcSftpFilePath);  
	            filesize = sftpATTRS.getSize();  
	        } catch (Exception e) {  
	            filesize = -1;
	            if (e.getMessage().toLowerCase().equals("no such file")) {  
	                filesize = -2;
	                JOptionPane.showMessageDialog(null, "No Such File: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	            }  
	        }  
	        return filesize;  
	    }  
}