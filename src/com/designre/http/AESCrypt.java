package com.designre.http;


import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class AESCrypt 
{
    private static final String ALGORITHM = "AES";
    private static final String KEY = "1Hbfh667adfDEJ78";
    
    public static String encrypt(String password) throws Exception
    {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(AESCrypt.ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte [] encryptedByteValue = cipher.doFinal(password.getBytes("utf-8"));
      
        return  new BASE64Encoder().encode(encryptedByteValue);               
    }
    
    public static String decrypt(String encryptedPassword) throws Exception
    {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(AESCrypt.ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte [] decryptedValue64 = new BASE64Decoder().decodeBuffer(encryptedPassword);
        byte [] decryptedByteValue = cipher.doFinal(decryptedValue64);
      
        return new String(decryptedByteValue,"utf-8");
        
    }
    
    private static Key generateKey() throws Exception 
    {
        Key key = new SecretKeySpec(AESCrypt.KEY.getBytes(),AESCrypt.ALGORITHM);
        return key;
    }
   
}