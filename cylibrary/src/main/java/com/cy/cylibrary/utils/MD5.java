package com.cy.cylibrary.utils;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5
{
	private static final char HEX_DIGITS[] =
	{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	private static String toHexString(byte[] b)
	{
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++)
		{
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	/**
	 * MD5加密
	 * @param str
	 * @return
	 */
	public static String md5Encode(String str)
	{
		try
		{
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(str.getBytes());
			return toHexString(algorithm.digest());
		}
		catch (NoSuchAlgorithmException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * MD5加密  16位
	 * @param str
	 * @return
	 */
	public static String md5Encode16(String str)
	{
		return  md5Encode(str).substring(8,24);
	}

}
