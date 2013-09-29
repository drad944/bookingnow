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
	 * 鐎靛棝鎸滅粻妤佺《
	*/
	private static final String KEY_ALGORITHM = "AES";
	
	private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
	
	/**
	 * 閸掓繂顬婇崠鏍х槕闁斤拷
	 * 
	 * @return byte[] 鐎靛棝鎸�
	 * @throws Exception
	 */
	public static byte[] initSecretKey() {
		//鏉╂柨娲栭悽鐔稿灇閹稿洤鐣剧粻妤佺《閻ㄥ嫮顬楃�鍡楃槕闁姐儳娈�KeyGenerator 鐎电钖�
		KeyGenerator kg = null;
		try {
			kg = KeyGenerator.getInstance(KEY_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return new byte[0];
		}
		//閸掓繂顬婇崠鏍劃鐎靛棝鎸滈悽鐔稿灇閸ｎ煉绱濇担鍨従閸忛攱婀佺涵顔肩暰閻ㄥ嫬鐦戦柦銉ャ亣鐏忥拷
		//AES 鐟曚焦鐪扮�鍡涙寽闂�灝瀹虫稉锟�28
		kg.init(128);
		//閻㈢喐鍨氭稉锟介嚋鐎靛棝鎸�
		SecretKey  secretKey = kg.generateKey();
		return secretKey.getEncoded();
	}
	
	/**
	 * 鏉烆剚宕茬�鍡涙寽
	 * 
	 * @param key	娴滃矁绻橀崚璺虹槕闁斤拷
	 * @return 鐎靛棝鎸�
	 */
	public static Key toKey(byte[] key){
		//閻㈢喐鍨氱�鍡涙寽
		return new SecretKeySpec(key, KEY_ALGORITHM);
	}
	
	/**
	 * 閸旂姴鐦�
	 * 
	 * @param data	瀵板懎濮炵�鍡樻殶閹癸拷
	 * @param key	鐎靛棝鎸�
	 * @return byte[]	閸旂姴鐦戦弫鐗堝祦
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data,Key key) throws Exception{
		return encrypt(data, key,DEFAULT_CIPHER_ALGORITHM);
	}
	
	/**
	 * 閸旂姴鐦�
	 * 
	 * @param data	瀵板懎濮炵�鍡樻殶閹癸拷
	 * @param key	娴滃矁绻橀崚璺虹槕闁斤拷
	 * @return byte[]	閸旂姴鐦戦弫鐗堝祦
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data,byte[] key) throws Exception{
		return encrypt(data, key,DEFAULT_CIPHER_ALGORITHM);
	}
	
	
	/**
	 * 閸旂姴鐦�
	 * 
	 * @param data	瀵板懎濮炵�鍡樻殶閹癸拷
	 * @param key	娴滃矁绻橀崚璺虹槕闁斤拷
	 * @param cipherAlgorithm	閸旂姴鐦戠粻妤佺《/瀹搞儰缍斿Ο鈥崇础/婵夘偄鍘栭弬鐟扮础
	 * @return byte[]	閸旂姴鐦戦弫鐗堝祦
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data,byte[] key,String cipherAlgorithm) throws Exception{
		//鏉╂ê甯�鍡涙寽
		Key k = toKey(key);
		return encrypt(data, k, cipherAlgorithm);
	}
	
	/**
	 * 閸旂姴鐦�
	 * 
	 * @param data	瀵板懎濮炵�鍡樻殶閹癸拷
	 * @param key	鐎靛棝鎸�
	 * @param cipherAlgorithm	閸旂姴鐦戠粻妤佺《/瀹搞儰缍斿Ο鈥崇础/婵夘偄鍘栭弬鐟扮础
	 * @return byte[]	閸旂姴鐦戦弫鐗堝祦
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data,Key key,String cipherAlgorithm) throws Exception{
		//鐎圭偘绶ラ崠锟�		
		Cipher cipher = Cipher.getInstance(cipherAlgorithm);
		//娴ｈ法鏁ょ�鍡涙寽閸掓繂顬婇崠鏍电礉鐠佸墽鐤嗘稉鍝勫鐎靛棙膩瀵拷
		cipher.init(Cipher.ENCRYPT_MODE, key);
		//閹笛嗩攽閹垮秳缍�
		return cipher.doFinal(data);
	}
	
	
	
	/**
	 * 鐟欙絽鐦�
	 * 
	 * @param data	瀵板懓袙鐎靛棙鏆熼幑锟�	 * @param key	娴滃矁绻橀崚璺虹槕闁斤拷
	 * @return byte[]	鐟欙絽鐦戦弫鐗堝祦
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data,byte[] key) throws Exception{
		return decrypt(data, key,DEFAULT_CIPHER_ALGORITHM);
	}
	
	/**
	 * 鐟欙絽鐦�
	 * 
	 * @param data	瀵板懓袙鐎靛棙鏆熼幑锟�	 * @param key	鐎靛棝鎸�
	 * @return byte[]	鐟欙絽鐦戦弫鐗堝祦
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data,Key key) throws Exception{
		return decrypt(data, key,DEFAULT_CIPHER_ALGORITHM);
	}
	
	/**
	 * 鐟欙絽鐦�
	 * 
	 * @param data	瀵板懓袙鐎靛棙鏆熼幑锟�	 * @param key	娴滃矁绻橀崚璺虹槕闁斤拷
	 * @param cipherAlgorithm	閸旂姴鐦戠粻妤佺《/瀹搞儰缍斿Ο鈥崇础/婵夘偄鍘栭弬鐟扮础
	 * @return byte[]	鐟欙絽鐦戦弫鐗堝祦
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data,byte[] key,String cipherAlgorithm) throws Exception{
		//鏉╂ê甯�鍡涙寽
		Key k = toKey(key);
		return decrypt(data, k, cipherAlgorithm);
	}

	/**
	 * 鐟欙絽鐦�
	 * 
	 * @param data	瀵板懓袙鐎靛棙鏆熼幑锟�	 * @param key	鐎靛棝鎸�
	 * @param cipherAlgorithm	閸旂姴鐦戠粻妤佺《/瀹搞儰缍斿Ο鈥崇础/婵夘偄鍘栭弬鐟扮础
	 * @return byte[]	鐟欙絽鐦戦弫鐗堝祦
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data,Key key,String cipherAlgorithm) throws Exception{
		//鐎圭偘绶ラ崠锟�		
		Cipher cipher = Cipher.getInstance(cipherAlgorithm);
		//娴ｈ法鏁ょ�鍡涙寽閸掓繂顬婇崠鏍电礉鐠佸墽鐤嗘稉楦啃掔�鍡樐佸锟�		cipher.init(Cipher.DECRYPT_MODE, key);
		//閹笛嗩攽閹垮秳缍�
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
		
		String data ="AES閺佺増宓";
		System.out.println("閸旂姴鐦戦崜宥嗘殶閹癸拷 string:"+data);
		System.out.println("閸旂姴鐦戦崜宥嗘殶閹癸拷 byte[]:"+showByteArray(data.getBytes()));
		System.out.println();
		byte[] encryptData = encrypt(data.getBytes(), k);
		System.out.println("閸旂姴鐦戦崥搴㈡殶閹癸拷 byte[]:"+showByteArray(encryptData));
		System.out.println("閸旂姴鐦戦崥搴㈡殶閹癸拷 hexStr:"+parseByte2HexStr(encryptData));
		System.out.println();
		byte[] decryptData = decrypt(encryptData, k);
		System.out.println("鐟欙絽鐦戦崥搴㈡殶閹癸拷 byte[]:"+showByteArray(decryptData));
		System.out.println("鐟欙絽鐦戦崥搴㈡殶閹癸拷 string:"+new String(decryptData));
		
	}
}



