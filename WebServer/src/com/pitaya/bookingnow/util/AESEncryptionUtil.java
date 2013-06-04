package com.pitaya.bookingnow.util;

import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES Coder<br/>
 * secret key length:	128bit, default:	128 bit<br/>
 * mode:	ECB/CBC/PCBC/CTR/CTS/CFB/CFB8 to CFB128/OFB/OBF8 to OFB128<br/>
 * padding:	Nopadding/PKCS5Padding/ISO10126Padding/
 * @author Aub
 * 
 */
public class AESEncryptionUtil {
	
	/**
	 * 瀵嗛挜绠楁硶
	*/
	private static final String KEY_ALGORITHM = "AES";
	
	private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
	
	/**
	 * 鍒濆鍖栧瘑閽�
	 * 
	 * @return byte[] 瀵嗛挜 
	 * @throws Exception
	 */
	public static byte[] initSecretKey() {
		//杩斿洖鐢熸垚鎸囧畾绠楁硶鐨勭瀵嗗瘑閽ョ殑 KeyGenerator 瀵硅薄
		KeyGenerator kg = null;
		try {
			kg = KeyGenerator.getInstance(KEY_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return new byte[0];
		}
		//鍒濆鍖栨瀵嗛挜鐢熸垚鍣紝浣垮叾鍏锋湁纭畾鐨勫瘑閽ュぇ灏�
		//AES 瑕佹眰瀵嗛挜闀垮害涓�128
		kg.init(128);
		//鐢熸垚涓�釜瀵嗛挜
		SecretKey  secretKey = kg.generateKey();
		return secretKey.getEncoded();
	}
	
	/**
	 * 杞崲瀵嗛挜
	 * 
	 * @param key	浜岃繘鍒跺瘑閽�
	 * @return 瀵嗛挜
	 */
	public static Key toKey(byte[] key){
		//鐢熸垚瀵嗛挜
		return new SecretKeySpec(key, KEY_ALGORITHM);
	}
	
	/**
	 * 鍔犲瘑
	 * 
	 * @param data	寰呭姞瀵嗘暟鎹�
	 * @param key	瀵嗛挜
	 * @return byte[]	鍔犲瘑鏁版嵁
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data,Key key) throws Exception{
		return encrypt(data, key,DEFAULT_CIPHER_ALGORITHM);
	}
	
	/**
	 * 鍔犲瘑
	 * 
	 * @param data	寰呭姞瀵嗘暟鎹�
	 * @param key	浜岃繘鍒跺瘑閽�
	 * @return byte[]	鍔犲瘑鏁版嵁
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data,byte[] key) throws Exception{
		return encrypt(data, key,DEFAULT_CIPHER_ALGORITHM);
	}
	
	
	/**
	 * 鍔犲瘑
	 * 
	 * @param data	寰呭姞瀵嗘暟鎹�
	 * @param key	浜岃繘鍒跺瘑閽�
	 * @param cipherAlgorithm	鍔犲瘑绠楁硶/宸ヤ綔妯″紡/濉厖鏂瑰紡
	 * @return byte[]	鍔犲瘑鏁版嵁
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data,byte[] key,String cipherAlgorithm) throws Exception{
		//杩樺師瀵嗛挜
		Key k = toKey(key);
		return encrypt(data, k, cipherAlgorithm);
	}
	
	/**
	 * 鍔犲瘑
	 * 
	 * @param data	寰呭姞瀵嗘暟鎹�
	 * @param key	瀵嗛挜
	 * @param cipherAlgorithm	鍔犲瘑绠楁硶/宸ヤ綔妯″紡/濉厖鏂瑰紡
	 * @return byte[]	鍔犲瘑鏁版嵁
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data,Key key,String cipherAlgorithm) throws Exception{
		//瀹炰緥鍖�
		Cipher cipher = Cipher.getInstance(cipherAlgorithm);
		//浣跨敤瀵嗛挜鍒濆鍖栵紝璁剧疆涓哄姞瀵嗘ā寮�
		cipher.init(Cipher.ENCRYPT_MODE, key);
		//鎵ц鎿嶄綔
		return cipher.doFinal(data);
	}
	
	
	
	/**
	 * 瑙ｅ瘑
	 * 
	 * @param data	寰呰В瀵嗘暟鎹�
	 * @param key	浜岃繘鍒跺瘑閽�
	 * @return byte[]	瑙ｅ瘑鏁版嵁
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data,byte[] key) throws Exception{
		return decrypt(data, key,DEFAULT_CIPHER_ALGORITHM);
	}
	
	/**
	 * 瑙ｅ瘑
	 * 
	 * @param data	寰呰В瀵嗘暟鎹�
	 * @param key	瀵嗛挜
	 * @return byte[]	瑙ｅ瘑鏁版嵁
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data,Key key) throws Exception{
		return decrypt(data, key,DEFAULT_CIPHER_ALGORITHM);
	}
	
	/**
	 * 瑙ｅ瘑
	 * 
	 * @param data	寰呰В瀵嗘暟鎹�
	 * @param key	浜岃繘鍒跺瘑閽�
	 * @param cipherAlgorithm	鍔犲瘑绠楁硶/宸ヤ綔妯″紡/濉厖鏂瑰紡
	 * @return byte[]	瑙ｅ瘑鏁版嵁
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data,byte[] key,String cipherAlgorithm) throws Exception{
		//杩樺師瀵嗛挜
		Key k = toKey(key);
		return decrypt(data, k, cipherAlgorithm);
	}

	/**
	 * 瑙ｅ瘑
	 * 
	 * @param data	寰呰В瀵嗘暟鎹�
	 * @param key	瀵嗛挜
	 * @param cipherAlgorithm	鍔犲瘑绠楁硶/宸ヤ綔妯″紡/濉厖鏂瑰紡
	 * @return byte[]	瑙ｅ瘑鏁版嵁
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data,Key key,String cipherAlgorithm) throws Exception{
		//瀹炰緥鍖�
		Cipher cipher = Cipher.getInstance(cipherAlgorithm);
		//浣跨敤瀵嗛挜鍒濆鍖栵紝璁剧疆涓鸿В瀵嗘ā寮�
		cipher.init(Cipher.DECRYPT_MODE, key);
		//鎵ц鎿嶄綔
		return cipher.doFinal(data);
	}
	
	public static String  showByteArray(byte[] data){
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
	
	public static String parseByte2HexStr(byte buf[]) {  
	    StringBuffer sb = new StringBuffer();  
	    for (int i = 0; i < buf.length; i++) {  
	            String hex = Integer.toHexString(buf[i] & 0xFF);  
	            if (hex.length() == 1) {  
	                    hex = '0' + hex;  
	            }  
	            sb.append(hex.toUpperCase());  
	    }  
	    return sb.toString();  
	}  
	
	public static void main(String[] args) throws Exception {
		byte[] key = initSecretKey();
		System.out.println("key"+showByteArray(key));
		
		Key k = toKey(key);
		
		String data ="AES鏁版嵁";
		System.out.println("鍔犲瘑鍓嶆暟鎹� string:"+data);
		System.out.println("鍔犲瘑鍓嶆暟鎹� byte[]:"+showByteArray(data.getBytes()));
		System.out.println();
		byte[] encryptData = encrypt(data.getBytes(), k);
		System.out.println("鍔犲瘑鍚庢暟鎹� byte[]:"+showByteArray(encryptData));
		System.out.println("鍔犲瘑鍚庢暟鎹� hexStr:"+parseByte2HexStr(encryptData));
		System.out.println();
		byte[] decryptData = decrypt(encryptData, k);
		System.out.println("瑙ｅ瘑鍚庢暟鎹� byte[]:"+showByteArray(decryptData));
		System.out.println("瑙ｅ瘑鍚庢暟鎹� string:"+new String(decryptData));
		
	}
}



