package safe;

import java.math.BigInteger;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

/**
 * AES Coder<br/>
 * secret key length:	128bit, default:	128 bit<br/>
 * mode:	ECB/CBC/PCBC/CTR/CTS/CFB/CFB8 to CFB128/OFB/OBF8 to OFB128<br/>
 * padding:	Nopadding/PKCS5Padding/ISO10126Padding/
 * @author Aub
 * 
 */
public class AESCoder {
	
	/**
	 * 密钥算法
	*/
	private static final String KEY_ALGORITHM = "AES";
	
	private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
	
	/**
	 * 初始化密钥
	 * 
	 * @return byte[] 密钥 
	 * @throws Exception
	 */
	public static byte[] initSecretKey() {
		//返回生成指定算法的秘密密钥的 KeyGenerator 对象
		KeyGenerator kg = null;
		try {
			kg = KeyGenerator.getInstance(KEY_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			//e.printStackTrace();
			return new byte[0];
		}
		//初始化此密钥生成器，使其具有确定的密钥大小
		//AES 要求密钥长度为 128
		kg.init(128);
		//生成一个密钥
		SecretKey  secretKey = kg.generateKey();
		key = secretKey.getEncoded();
		showByteArray(key);
		return key; 
	}
	
	/**
	 * 转换密钥
	 * 
	 * @param key	二进制密钥
	 * @return 密钥
	 */
	private static Key toKey(byte[] key){
		//生成密钥
		return new SecretKeySpec(key, KEY_ALGORITHM);
	}
	
	/**
	 * 加密
	 * 
	 * @param data	待加密数据
	 * @param key	密钥
	 * @return byte[]	加密数据
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data,Key key) throws Exception{
		return encrypt(data, key,DEFAULT_CIPHER_ALGORITHM);
	}
	
	/**
	 * 加密
	 * 
	 * @param data	待加密数据
	 * @param key	二进制密钥
	 * @return byte[]	加密数据
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data,byte[] key) throws Exception{
		return encrypt(data, key,DEFAULT_CIPHER_ALGORITHM);
	}
	
	
	/**
	 * 加密
	 * 
	 * @param data	待加密数据
	 * @param key	二进制密钥
	 * @param cipherAlgorithm	加密算法/工作模式/填充方式
	 * @return byte[]	加密数据
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data,byte[] key,String cipherAlgorithm) throws Exception{
		//还原密钥
		Key k = toKey(key);
		return encrypt(data, k, cipherAlgorithm);
	}
	
	/**
	 * 加密
	 * 
	 * @param data	待加密数据
	 * @param key	密钥
	 * @param cipherAlgorithm	加密算法/工作模式/填充方式
	 * @return byte[]	加密数据
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data,Key key,String cipherAlgorithm) throws Exception{
		//实例化
		Cipher cipher = Cipher.getInstance(cipherAlgorithm);
		//使用密钥初始化，设置为加密模式
		cipher.init(Cipher.ENCRYPT_MODE, key);
		//执行操作
		return cipher.doFinal(data);
	}
	
	
	
	/**
	 * 解密
	 * 
	 * @param data	待解密数据
	 * @param key	二进制密钥
	 * @return byte[]	解密数据
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data,byte[] key) throws Exception{
		return decrypt(data, key,DEFAULT_CIPHER_ALGORITHM);
	}
	
	/**
	 * 解密
	 * 
	 * @param data	待解密数据
	 * @param key	密钥
	 * @return byte[]	解密数据
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data,Key ikey) throws Exception{
		return decrypt(data, ikey,DEFAULT_CIPHER_ALGORITHM);
	}
	
	/**
	 * 解密
	 * 
	 * @param data	待解密数据
	 * @param key	二进制密钥
	 * @param cipherAlgorithm	加密算法/工作模式/填充方式
	 * @return byte[]	解密数据
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data,byte[] ikey,String cipherAlgorithm) throws Exception{
		//还原密钥
		Key k = toKey(ikey);
		return decrypt(data, k, cipherAlgorithm);
	}

	/**
	 * 解密
	 * 
	 * @param data	待解密数据
	 * @param key	密钥
	 * @param cipherAlgorithm	加密算法/工作模式/填充方式
	 * @return byte[]	解密数据
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data,Key ikey,String cipherAlgorithm) throws Exception{
		//实例化
		Cipher cipher = Cipher.getInstance(cipherAlgorithm);
		//使用密钥初始化，设置为解密模式
		cipher.init(Cipher.DECRYPT_MODE, ikey);
		//执行操作
		return cipher.doFinal(data);
	}
	
	private static String  showByteArray(byte[] data){
		if(null == data){
			return null;
		}
		StringBuilder sb = new StringBuilder("{");
		for(byte b:data){
			sb.append(b).append(",");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append("}");
		return sb.toString();
	}
	
	public static void main(String[] args) throws Exception {
		//byte[] key = initSecretKey();
		System.out.println("key："+showByteArray(key));
		
		Key k = toKey( getkey() );
		String data ="AES数据";
		System.out.println("加密前数据: string:"+data);
		System.out.println("加密前数据: byte[]:"+showByteArray(data.getBytes()));
		System.out.println();
		byte[] encryptData = encrypt(data.getBytes(), k);
		System.out.println("加密后数据: byte[]:"+showByteArray(encryptData));
		//System.out.println("加密后数据: hexStr:"+Hex.encodeHexStr(encryptData));
		System.out.println();
		byte[] decryptData = decrypt(encryptData, k);
		System.out.println("解密后数据: byte[]:"+showByteArray(decryptData));
		System.out.println("解密后数据: string:"+new String(decryptData));
		
		String s=encrypt("test");
		System.out.println("-加密后数据: " + s );
		
		String ff = decrypt(s);
		System.out.println("-解密后数据: " + ff );
	}
	
	private static byte[] key;
	
	private static byte[] getkey(){
		//if( key == null )
			//key = initSecretKey();
			key = new byte[16];
		key[0] = Byte.valueOf("89");
		key[1] = Byte.valueOf("-128");
		key[2] = Byte.valueOf("68");
		key[3] = Byte.valueOf("-102"); 
		key[4] = Byte.valueOf("90");
		key[5] = Byte.valueOf("61");
		key[6] = Byte.valueOf("4");
		key[7] = Byte.valueOf("5");
		key[8] = Byte.valueOf("-29");
		key[9] = Byte.valueOf("-109");
		key[10] = Byte.valueOf("-30");
		key[11] = Byte.valueOf("66");
		key[12] = Byte.valueOf("-59");
		key[13] = Byte.valueOf("101");
		key[14] = Byte.valueOf("-116");
		key[15] = Byte.valueOf("-9");
		
		return key;
	}
	
	final static Key k3 = toKey(getkey());
	public static String encrypt(String text) throws Exception{
		
		if((text == null)||(text.trim().length()<1))
			throw new Exception("we can't encrypt null string!");
		
		Key k = toKey(getkey());
		//System.out.println("key："+showByteArray(key));		
		//Key k = toKey(key);
		
		byte[] encryptData = encrypt( text.getBytes(), k);
		String encoded //= new BigInteger(encryptData  ).toString(16);
			= bytesToHexString( encryptData ); 
		
		
		
		return encoded;
	}
	
	public static String decrypt(String encrypted) throws Exception{
		
		if((encrypted == null)||(encrypted.trim().length()<1))
			throw new Exception("we can't decrypt null string!");
		
		//byte[] key = initSecretKey();
		//System.out.println("key："+showByteArray(key));		
		//Key k = toKey(key);
		Key k2 = toKey(getkey());
		byte[] bytes= Hex.decodeHex( encrypted.toCharArray()  );
		byte[] decryptData = decrypt( bytes, k2);
		String originaltext = new String( decryptData );
		return originaltext;
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
}
