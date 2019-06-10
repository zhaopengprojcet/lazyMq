package com.zhao.lazy.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.crypto.stream.CryptoInputStream;
import org.apache.commons.crypto.stream.CryptoOutputStream;
import org.apache.tomcat.util.codec.binary.Base64;

/**
 * AES工具
 * add by zhao of 2019年5月23日
 *
 * 功能描述：
 */
public class AesUtil {
	private static final String transform = "AES/CBC/PKCS5Padding";
	private static final String ivKey = "1234567890123456";
	private static final String keyType = "AES";
	
	public static String encrypt(String context, String password) {
		try {
			final SecretKeySpec key = new SecretKeySpec(getUTF8Bytes(password), keyType);
			final IvParameterSpec iv = new IvParameterSpec(getUTF8Bytes(ivKey));
			Properties properties = new Properties();
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			try (CryptoOutputStream cos = new CryptoOutputStream(transform, properties, outputStream, key, iv)) {
				cos.write(getUTF8Bytes(context));
				cos.flush();
			}
			return Base64.encodeBase64String(outputStream.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String decrypt(String context, String password) {
		try {
			InputStream inputStream = new ByteArrayInputStream(Base64.decodeBase64(context));
			final SecretKeySpec key = new SecretKeySpec(getUTF8Bytes(password), keyType);
			final IvParameterSpec iv = new IvParameterSpec(getUTF8Bytes(ivKey));
			Properties properties = new Properties();
			try (CryptoInputStream cis = new CryptoInputStream(transform, properties, inputStream, key, iv)) {
				byte[] decryptedData = new byte[1024];
				int decryptedLen = 0;
				int i;
				while ((i = cis.read(decryptedData, decryptedLen, decryptedData.length - decryptedLen)) > -1) {
					decryptedLen += i;
				}
				return new String(decryptedData, 0, decryptedLen, StandardCharsets.UTF_8);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static byte[] getUTF8Bytes(String input) {
		return input.getBytes(StandardCharsets.UTF_8);
	}
}
