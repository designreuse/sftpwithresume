package com.designre.http;

import java.util.Hashtable;

public class PackageInfo {
	
	
	public static final String PACKAGE_NAME      = "package_name_";
	public static final String PACKAGE_URL       = "package_url_";
	public static final String PACKAGE_CHECKSUM  = "package_checksum_";
	
	private String packageName;
	private String packageURL;
	private String packageCheckSum;
	
	String[] args;
	
	Hashtable<String, PackageInfo> packagesInfo = new Hashtable<String, PackageInfo>();
	
	public PackageInfo(String packageName, String packageURL, String packageChecksum){
		
		this.packageName = packageName;
		this.packageURL = packageURL;
		this.packageCheckSum = packageChecksum;
	}
	
    public PackageInfo(String[] args){
    	this.args = args;
	}
    
    public PackageInfo(){
	}
	
	public String getPackageName() {
		return packageName;
	}


	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}


	public String getPackageURL() {
		return packageURL;
	}


	public void setPackageURL(String packageURL) {
		this.packageURL = packageURL;
	}


	public String getPackageCheckSum() {
		return packageCheckSum;
	}


	public void setPackageCheckSum(String packageCheckSum) {
		this.packageCheckSum = packageCheckSum;
	}
	public Hashtable<String, PackageInfo> getPackagesInfo() {
		return packagesInfo;
	}

	public void setPackagesInfo(Hashtable<String, PackageInfo> packagesInfo) {
		this.packagesInfo = packagesInfo;
	}
	

	public Hashtable<String, PackageInfo> getPackagesInfo(String[] args) {
		
		args = new String[3];
		args[0] = "package_name_0 = Full IP";
		args[1] = "package_url_0=http://www.drgateway.com/ip.zip";
		args[2] = "package_checksum_0=55f2bb99aa17e405d5b39f99aaaa9172";
		
		
		try {

			for (int i = 0; i < args.length; i++)
			{
				String arg = args[i];

			 if (arg.startsWith(PACKAGE_NAME))
				{
					//extract index of the package
				    String sIndex = arg.substring(arg.indexOf(PACKAGE_NAME) + PACKAGE_NAME.length(), arg.indexOf("="));
				    String packageName = arg.substring(arg.indexOf("=") + 1);
				    sIndex = sIndex.trim();
				    PackageInfo jnlpArguments = (PackageInfo)packagesInfo.get(sIndex);
				    if(jnlpArguments==null){
				    	jnlpArguments = new PackageInfo();
				    	jnlpArguments.setPackageName(packageName);
				    }
				    
				    else
				    	jnlpArguments.setPackageName(packageName);
				    
				    packagesInfo.put(sIndex, jnlpArguments);
				
				}
			 else if (arg.startsWith(PACKAGE_URL))
				{
					//extract index of the package
				    String sIndex = arg.substring(arg.indexOf(PACKAGE_URL) + PACKAGE_URL.length(), arg.indexOf("="));
				    sIndex = sIndex.trim();
				    String packageURL = arg.substring(arg.indexOf("=") + 1);
				    PackageInfo jnlpArguments = (PackageInfo)packagesInfo.get(sIndex);
				    if(jnlpArguments==null){
				    	jnlpArguments = new PackageInfo();
				    	jnlpArguments.setPackageURL(packageURL);
				    }
				    
				    else
				    	jnlpArguments.setPackageURL(packageURL);
				    
				    packagesInfo.put(sIndex, jnlpArguments);
				} 
			 
			 else if (arg.startsWith(PACKAGE_CHECKSUM))
				{
					//extract index of the package
				    String sIndex = arg.substring(arg.indexOf(PACKAGE_CHECKSUM) + PACKAGE_CHECKSUM.length(), arg.indexOf("="));
				    sIndex = sIndex.trim();
				    String packageCheckSum = arg.substring(arg.indexOf("=") + 1);
				    PackageInfo jnlpArguments = (PackageInfo)packagesInfo.get(sIndex);
				    if(jnlpArguments==null){
				    	jnlpArguments = new PackageInfo();
				    	jnlpArguments.setPackageCheckSum(packageCheckSum);
				    }
				    
				    else
				    	jnlpArguments.setPackageCheckSum(packageCheckSum);
				    
				    packagesInfo.put(sIndex, jnlpArguments);
				} 
			 
			
			}
	
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return packagesInfo;
	}
	
	public static void main(String[] args){

		System.out.println(new PackageInfo().getPackagesInfo(args));
	
		
	}

	@Override
	public String toString() {
		return "JnlpArguments [packageName=" + packageName + ", packageURL=" + packageURL + ", packageCheckSum="
				+ packageCheckSum + "]";
	}


}
