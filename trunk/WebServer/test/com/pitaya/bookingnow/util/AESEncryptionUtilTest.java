package com.pitaya.bookingnow.util;
import java.security.Key;

import junit.framework.TestCase;
public class AESEncryptionTest extends TestCase {

	public void testAES() {
		
		byte[] key = AESEncryptionUtil.initSecretKey();
		System.out.println("key��"+AESEncryptionUtil.showByteArray(key));
		
		Key k = toKey(key);
		
		String data ="AES����";
		System.out.println("����ǰ����: string:"+data);
		System.out.println("����ǰ����: byte[]:"+AESEncryptionUtil.showByteArray(data.getBytes()));
		System.out.println();
		byte[] encryptData = AESEncryptionUtil.encrypt(data.getBytes(), k);
		System.out.println("���ܺ�����: byte[]:"+AESEncryptionUtil.showByteArray(encryptData));
		System.out.println("���ܺ�����: hexStr:"+AESEncryptionUtil.parseByte2HexStr(encryptData));
		System.out.println();
		byte[] decryptData = AESEncryptionUtil.decrypt(encryptData, k);
		System.out.println("���ܺ�����: byte[]:"+AESEncryptionUtil.showByteArray(decryptData));
		System.out.println("���ܺ�����: string:"+new String(decryptData));
	}
}
