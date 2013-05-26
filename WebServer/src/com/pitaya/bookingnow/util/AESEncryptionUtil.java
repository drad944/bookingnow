package com.pitaya.bookingnow.util;

import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES Encryption<br/>
 * secret key length:	128bit, default:	128 bit<br/>
 * mode:	ECB/CBC/PCBC/CTR/CTS/CFB/CFB8 to CFB128/OFB/OBF8 to OFB128<br/>
 * 
 */
public class AESEncryptionUtil {
	
	/**
	 * ��Կ�㷨
	*/
	private static final String KEY_ALGORITHM = "AES";
	
	private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
	
	/**
	 * ��ʼ����Կ
	 * 
	 * @return byte[] ��Կ 
	 * @throws Exception
	 */
	public static byte[] initSecretKey() {
		//��������ָ���㷨��������Կ�� KeyGenerator ����
		KeyGenerator kg = null;
		try {
			kg = KeyGenerator.getInstance(KEY_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return new byte[0];
		}
		//��ʼ������Կ��������ʹ�����ȷ������Կ��С
		//AES Ҫ����Կ����Ϊ 128
		kg.init(128);
		//����һ����Կ
		SecretKey  secretKey = kg.generateKey();
		return secretKey.getEncoded();
	}
	
	/**
	 * ת����Կ
	 * 
	 * @param key	��������Կ
	 * @return ��Կ
	 */
	private static Key toKey(byte[] key){
		//������Կ
		return new SecretKeySpec(key, KEY_ALGORITHM);
	}
	
	/**
	 * ����
	 * 
	 * @param data	����������
	 * @param key	��Կ
	 * @return byte[]	��������
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data,Key key) throws Exception{
		return encrypt(data, key,DEFAULT_CIPHER_ALGORITHM);
	}
	
	/**
	 * ����
	 * 
	 * @param data	����������
	 * @param key	��������Կ
	 * @return byte[]	��������
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data,byte[] key) throws Exception{
		return encrypt(data, key,DEFAULT_CIPHER_ALGORITHM);
	}
	
	
	/**
	 * ����
	 * 
	 * @param data	����������
	 * @param key	��������Կ
	 * @param cipherAlgorithm	�����㷨/����ģʽ/��䷽ʽ
	 * @return byte[]	��������
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data,byte[] key,String cipherAlgorithm) throws Exception{
		//��ԭ��Կ
		Key k = toKey(key);
		return encrypt(data, k, cipherAlgorithm);
	}
	
	/**
	 * ����
	 * 
	 * @param data	����������
	 * @param key	��Կ
	 * @param cipherAlgorithm	�����㷨/����ģʽ/��䷽ʽ
	 * @return byte[]	��������
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data,Key key,String cipherAlgorithm) throws Exception{
		//ʵ����
		Cipher cipher = Cipher.getInstance(cipherAlgorithm);
		//ʹ����Կ��ʼ��������Ϊ����ģʽ
		cipher.init(Cipher.ENCRYPT_MODE, key);
		//ִ�в���
		return cipher.doFinal(data);
	}
	
	
	
	/**
	 * ����
	 * 
	 * @param data	����������
	 * @param key	��������Կ
	 * @return byte[]	��������
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data,byte[] key) throws Exception{
		return decrypt(data, key,DEFAULT_CIPHER_ALGORITHM);
	}
	
	/**
	 * ����
	 * 
	 * @param data	����������
	 * @param key	��Կ
	 * @return byte[]	��������
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data,Key key) throws Exception{
		return decrypt(data, key,DEFAULT_CIPHER_ALGORITHM);
	}
	
	/**
	 * ����
	 * 
	 * @param data	����������
	 * @param key	��������Կ
	 * @param cipherAlgorithm	�����㷨/����ģʽ/��䷽ʽ
	 * @return byte[]	��������
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data,byte[] key,String cipherAlgorithm) throws Exception{
		//��ԭ��Կ
		Key k = toKey(key);
		return decrypt(data, k, cipherAlgorithm);
	}

	/**
	 * ����
	 * 
	 * @param data	����������
	 * @param key	��Կ
	 * @param cipherAlgorithm	�����㷨/����ģʽ/��䷽ʽ
	 * @return byte[]	��������
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data,Key key,String cipherAlgorithm) throws Exception{
		//ʵ����
		Cipher cipher = Cipher.getInstance(cipherAlgorithm);
		//ʹ����Կ��ʼ��������Ϊ����ģʽ
		cipher.init(Cipher.DECRYPT_MODE, key);
		//ִ�в���
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
	
	/**��������ת����16���� 
	 * @param buf 
	 * @return 
	 */  
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
		System.out.println("key��"+showByteArray(key));
		
		Key k = toKey(key);
		
		String data ="AES����";
		System.out.println("����ǰ����: string:"+data);
		System.out.println("����ǰ����: byte[]:"+showByteArray(data.getBytes()));
		System.out.println();
		byte[] encryptData = encrypt(data.getBytes(), k);
		System.out.println("���ܺ�����: byte[]:"+showByteArray(encryptData));
		System.out.println("���ܺ�����: hexStr:"+parseByte2HexStr(encryptData));
		System.out.println();
		byte[] decryptData = decrypt(encryptData, k);
		System.out.println("���ܺ�����: byte[]:"+showByteArray(decryptData));
		System.out.println("���ܺ�����: string:"+new String(decryptData));
	}
}

