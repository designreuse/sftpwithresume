package com.designre.ftp.download;

import com.jcraft.jsch.SftpProgressMonitor;

public class TransferProgress implements SftpProgressMonitor
{
    public double count=0;
    private int percentage;
    public double totalSize;
    @Override
    public void init(int op, String src, String dest, long max) 
        {
        this.totalSize=max;
        }
    @Override
    public boolean count(long count) 
        {
        this.count += count;
        this.percentage = (int) ((this.count / this.totalSize) * 100.0);
        SftpGUI.progressBar.setValue((int)this.percentage);     
        return true;
        }

    @Override
    public void end() 
        {
        System.out.println("Total Copied "+this.percentage+" %");
        }
}