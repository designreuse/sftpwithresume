package com.designre.http;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
 
// This class manages the download table's data.
class DownloadsTableModel extends AbstractTableModel implements Observer {
	
	private static final long serialVersionUID = 8489399426552581643L;
     
    // These are the names for the table's columns.
    private static final String[] columnNames = {"Package Name", "URL", "Size",  "Progress", "Status", "CheckSum"};
     
    // These are the classes for each column's values.
    private static final Class[] columnClasses = {String.class, String.class, String.class, JProgressBar.class, String.class, String.class};
     
    // The table's list of downloads.
    private ArrayList<Download> downloadList = new ArrayList<Download>();
    
     
    // Add a new download to the table.
    public void addDownload(Download download) {
         
        // Register to be notified when the download changes.
        download.addObserver(this);
         
        downloadList.add(download);
         
        // Fire table row insertion notification to table.
        fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
    }
     
    // Get a download for the specified row.
    public Download getDownload(int row) {
        return (Download) downloadList.get(row);
    }
     
    // Remove a download from the list.
    public void clearDownload(int row) {
        downloadList.remove(row);
        // Fire table row deletion notification to table.
        fireTableRowsDeleted(row, row);
    }
     
    // Get table's column count.
    public int getColumnCount() {
        return columnNames.length;
    }
     
    // Get a column's name.
    public String getColumnName(int col) {
        return columnNames[col];
    }
     
    // Get a column's class.
    public Class getColumnClass(int col) {
        return columnClasses[col];
    }
     
    // Get table's row count.
    public int getRowCount() {
        return downloadList.size();
    }
     
    // Get value for a specific row and column combination.
    public Object getValueAt(int row, int col) {
         
        Download download = (Download) downloadList.get(row);
        switch (col) {
        
            case 0: // Package Name
            return download.getPackageName();
            
            case 1: // URL
                return download.getUrl();
            case 2: // Size
                long size = download.getSize();
                return (size == -1) ? "" : Long.toString(size);
            case 3: // Progress
                return new Float(download.getProgress());
            case 4: // Status
                return Download.STATUSES[download.getStatus()];
            case 5: // Status
                return download.getCheckSum();
                
        }
        return "";
    }
     
  /* Update is called when a Download notifies its
     observers of any changes */
    public void update(Observable o, Object arg) {
        int index = downloadList.indexOf(o);
         
        // Fire table row update notification to table.
        fireTableRowsUpdated(index, index);
    }
}