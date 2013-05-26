package com.pitaya.bookingnow.util;
import java.security.Key;

import junit.framework.TestCase;
public class AESEncryptionTest extends TestCase {

	public void testAES() {
		
		byte[] key = AESEncryptionUtil.initSecretKey();
		System.out.println("key："+AESEncryptionUtil.showByteArray(key));
		
		Key k = toKey(key);
		
		String data ="AES数据";
		System.out.println("加密前数据: string:"+data);
		System.out.println("加密前数据: byte[]:"+AESEncryptionUtil.showByteArray(data.getBytes()));
		System.out.println();
		byte[] encryptData = AESEncryptionUtil.encrypt(data.getBytes(), k);
		System.out.println("加密后数据: byte[]:"+AESEncryptionUtil.showByteArray(encryptData));
		System.out.println("加密后数据: hexStr:"+AESEncryptionUtil.parseByte2HexStr(encryptData));
		System.out.println();
		byte[] decryptData = AESEncryptionUtil.decrypt(encryptData, k);
		System.out.println("解密后数据: byte[]:"+AESEncryptionUtil.showByteArray(decryptData));
		System.out.println("解密后数据: string:"+new String(decryptData));
	}
}
