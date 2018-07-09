/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webservices;

import java.math.BigInteger;
import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

/**
 * PBE Coder<br/> Algorithm/secret key length/default secret key length/<br/> 1.
 * PBEWithMD5AndTripleDES/112,168/168
 *
 * @author Jianwei Chris Leng, July 30, 2012
 */
public class PBECoder {

    public static final String encryptorAlgorithm = "PBEWithMD5AndTripleDES";
    public static  String encryptorPassword = "passw0rd";
    public static  int ITERATION_COUNT = 100;
    public static byte[] salt = "00000000".getBytes(); 
    

    /**
     * Initial salt.<br/> It's length must be 8.
     *
     * @return byte[] the salt for encrypt.
     * @throws Exception
     */
    public static byte[] initSalt() throws Exception {

        // Get a random number   
        SecureRandom random = new SecureRandom();

        // generate the SALT
        return random.generateSeed(8);
    }

    private static String showByteArray(byte[] data) {
        if (null == data) {
            return null;
        }
        StringBuilder sb = new StringBuilder("");
        for (byte b : data) {
            String s = Integer.toHexString(0xFF & b);
            sb.append(s);
        }
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    /**
     * transfer password key.
     *
     * @param password password before
     * @return Key key
     */
    private static Key toKey(String password) throws Exception {
        // get key algorithm   
        PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
        // get key factory
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(encryptorAlgorithm);
        // generate the ky
        return keyFactory.generateSecret(keySpec);
    }

    /**
     * encryption.
     *
     * @param data the data that waiting for being encrypted.
     * @param key security key
     * @param salt security salt
     * @return byte[] encrypted data.
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, String password, byte[] salt) throws Exception {

        // get key
        Key key = toKey(password);

        // instance the PBE generate
        PBEParameterSpec paramSpec = new PBEParameterSpec(salt, ITERATION_COUNT);
        // instanced the     
        Cipher cipher = Cipher.getInstance(encryptorAlgorithm);
        // init   
        cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
        // return the key
        return cipher.doFinal(data);
    }
    
    
    public static String encrypt(String data) throws Exception{
        if( data == null )
            throw new Exception("we can't encrypt null string");
        
        byte[] encryptData = encrypt(data.getBytes(), encryptorPassword, salt);
        
        String encoded = new BigInteger(encryptData).toString(16);
        return encoded;
    }
    
    
    public static String decrypt(byte[] data) throws Exception{
        if( data == null )
            throw new Exception("we can't decode null string");
        
        byte[] decryptData = decrypt(data, encryptorPassword, salt);
        
        String decoded = new String(decryptData);
        return decoded;
    }
    

    /**
     * test function
     */
    public static void main(String[] args) throws Exception {
        
        System.out.println("salt：" + showByteArray(salt));
        // This password must be ASCII, o.w. it throws errors.
        
        System.out.println("口令：" + encryptorPassword);

        String data = "passw0rd";
        System.out.println("before encrypted: string:" + data);
        System.out.println("before encrypted: byte[]:" + showByteArray(data.getBytes()));
        System.out.println("before encrypted: byte[]:" + bytesToHexString(data.getBytes()));
        System.out.println();
        byte[] encryptData = encrypt(data.getBytes(), encryptorPassword, salt);
        System.out.println("encrypted: byte[]:" + bytesToHexString(encryptData));
        System.out.println("encrypted: byte[]:" + encryptData);
        System.out.println("encrypted: byte[]:" + new BigInteger(encryptData).toString(16));
        //System.out.println("加密后数据: hexStr:"+Hex.encodeHexStr(encryptData));   
        System.out.println();
        
        
       


        
        byte[] decryptData = decrypt(encryptData, encryptorPassword ,salt);   
        System.out.println("decoded: byte[]:"+showByteArray(decryptData));   
        System.out.println("decoded: string:"+new String(decryptData));   
        
        PBECoder.prepareForCONFIG();
        System.out.println("1 --- "   );
        
        //StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();

//encryptor.setPassword("jasypt"); // 设定密码 

//encryptor.setAlgorithm("PBEWithMD5AndTripleDES"); // 选择一种算法 



//String encryptedText = encryptor.encrypt("passw0rd");
//System.out.println(encryptedText);

    }

    /**
     * decode
     *
     * @param data encoded data.
     * @param key   encryption key.
     * @param salt  encryption salt.
     * @return byte[] decoded data.
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, String password, byte[] salt) throws Exception {
        // convert key   
        Key key = toKey(password);
        // preare the parameters for the    
        PBEParameterSpec paramSpec = new PBEParameterSpec(salt, ITERATION_COUNT);
        
        // create the instance of the PBE   
        Cipher cipher = Cipher.getInstance(encryptorAlgorithm);
        // init
        cipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
        
        // return the decoded string   
        return cipher.doFinal(data);
    }

    public static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    public static void prepareForCONFIG() {
        PBECoder.salt = env.Parameters.umls_encryptorSalt;
        PBECoder.ITERATION_COUNT = env.Parameters.umls_encryptorIterationCount;
        PBECoder.encryptorPassword = env.Parameters.umls_encryptorPassword;
    }
}
