package com.designre.http;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CheckSum {


    /**
     * Private constructor for a utility class.
     */
    private CheckSum() {
    }

    /**
     * <p>
     * Creates the cryptographic checksum of a given file using the specified algorithm.</p>
     *
     * @param algorithm the algorithm to use to calculate the checksum
     * @param file the file to calculate the checksum for
     * @return the checksum
     * @throws IOException when the file does not exist
     * @throws NoSuchAlgorithmException when an algorithm is specified that does not exist
     */
    public static byte[] getChecksum(String algorithm, File file) throws NoSuchAlgorithmException, IOException {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            FileChannel ch = fis.getChannel();
            long remainingToRead = file.length();
            long start = 0;
            while (remainingToRead > 0) {
                long amountToRead;
                if (remainingToRead > Integer.MAX_VALUE) {
                    remainingToRead -= Integer.MAX_VALUE;
                    amountToRead = Integer.MAX_VALUE;
                } else {
                    amountToRead = remainingToRead;
                    remainingToRead = 0;
                }
                MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, start, amountToRead);
                digest.update(byteBuffer);
                start += amountToRead;
            }
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex) {
                   System.out.println(ex.getMessage());
                }
            }
        }
        return digest.digest();
    }

    /**
     * Calculates the MD5 checksum of a specified file.
     *
     * @param file the file to generate the MD5 checksum
     * @return the hex representation of the MD5 hash
     * @throws IOException when the file passed in does not exist
     * @throws NoSuchAlgorithmException when the MD5 algorithm is not available
     */
    public static String getMD5Checksum(File file) throws IOException, NoSuchAlgorithmException {
        byte[] b = getChecksum("MD5", file);
        return getHex(b);
    }

    /**
     * Calculates the SHA1 checksum of a specified file.
     *
     * @param file the file to generate the MD5 checksum
     * @return the hex representation of the SHA1 hash
     * @throws IOException when the file passed in does not exist
     * @throws NoSuchAlgorithmException when the SHA1 algorithm is not available
     */
    public static String getSHA1Checksum(File file) throws IOException, NoSuchAlgorithmException {
        byte[] b = getChecksum("SHA1", file);
        return getHex(b);
    }
    /**
     * Hex code characters used in getHex.
     */
    private static final String HEXES = "0123456789abcdef";

    /**
     * <p>
     * Converts a byte array into a hex string.</p>
     *
     * <p>
     * This method was copied from <a
     * href="http://www.rgagnon.com/javadetails/java-0596.html">http://www.rgagnon.com/javadetails/java-0596.html</a></p>
     *
     * @param raw a byte array
     * @return the hex representation of the byte array
     */
    public static String getHex(byte[] raw) {
        if (raw == null) {
            return null;
        }
        final StringBuilder hex = new StringBuilder(2 * raw.length);
        for (final byte b : raw) {
            hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(HEXES.charAt(b & 0x0F));
        }
        return hex.toString();
    }
    
    public static void main(String[] args){
    	
    	try {
			String checkSum = CheckSum.getMD5Checksum(new File("c:\\users\\missaoui\\invecas\\ip.zip"));
			System.out.println(checkSum);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
}