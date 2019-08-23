package com.flypay.utils;



import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

/**
 * MD5加密算法
 */
public class Md5Digest {
	/**
	 * Used building output as Hex
	 */
	private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	
	private static final String ENCODE = "UTF-8";
	
	private static final char[] bufferChars = {'0','1','2','3','4','5','6','7','8','9',
		'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T',
		'U','V','W','X','Y','Z',
		'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t',
		'u','v','w','x','y','z'};
	
	/**
	 * 对字符串进行MD5加密
	 * 
	 * @param text
	 *            明文
	 * 
	 * @return 密文
	 */
	public static String md5(String str) {
		MessageDigest messageDigest = null;  		  
		        try {  
		            messageDigest = MessageDigest.getInstance("MD5");  
		            messageDigest.reset();  
		  
		            messageDigest.update(str.getBytes("UTF-8"));  
		        } catch (NoSuchAlgorithmException e) {
		            System.out.println("NoSuchAlgorithmException caught!");
		            return null;
		        } catch (UnsupportedEncodingException e) {  
		            e.printStackTrace();  
		            return null;
		        }  
		        byte[] byteArray = messageDigest.digest();  
		  
		        StringBuffer md5StrBuff = new StringBuffer();  
		  
		        for (int i = 0; i < byteArray.length; i++) {              
		            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)  
		                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));  
		            else  
		                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));  
		        }  		  
		        return md5StrBuff.toString();
	}

	public static char[] encodeHex(byte[] data) {

		int l = data.length;

		char[] out = new char[l << 1];

		// two characters form the hex value.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
			out[j++] = DIGITS[0x0F & data[i]];
		}

		return out;
	}
	/**
	 * 带有key值得md5摘要函数
	 * @param aValue  要获取摘要的字符串
	 * @param aKey  获取摘要用的key值
	 * @return
	 */
	public static String md5WithKey(String aValue, String aKey) {
		if(null == aKey || "".equals(aKey)){
			return md5(aValue);
		}
		byte k_ipad[] = new byte[64];
		byte k_opad[] = new byte[64];
		byte keyb[];
		byte value[];
		try {
			keyb = aKey.getBytes(ENCODE);
			value = aValue.getBytes(ENCODE);
		} catch (UnsupportedEncodingException e) {
			keyb = aKey.getBytes();
			value = aValue.getBytes();
		}
		Arrays.fill(k_ipad, keyb.length, 64, (byte) 54);
		Arrays.fill(k_opad, keyb.length, 64, (byte) 92);
		for (int i = 0; i < keyb.length; i++) {
			k_ipad[i] = (byte) (keyb[i] ^ 0x36);
			k_opad[i] = (byte) (keyb[i] ^ 0x5c);
		}
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		md.update(k_ipad);
		md.update(value);
		byte dg[] = md.digest();
		md.reset();
		md.update(k_opad);
		md.update(dg, 0, 16);
		dg = md.digest();
		return toHex(dg);
	}
	

	/**
	 * 将byte[] 用16进制表示
	 * @param input
	 * @return
	 */
	public static String toHex(byte input[]) {
		if (input == null)
			return null;
		StringBuffer output = new StringBuffer(input.length * 2);
		for (int i = 0; i < input.length; i++) {
			int current = input[i] & 0xff;
			if (current < 16)
				output.append("0");
			output.append(Integer.toString(current, 16));
		}

		return output.toString();
	}
	/**
	 * 从0-9,A-Z,a-z 中随机取值返回固定长度的随机字符串
	 * @param length 返回的随机字符的长度
	 * @return
	 */
	public static String getRandomChars(int length){
		char[] chars = new char[length];
		Random random = new Random();
		int m = 0;
		for(int i=0;i<length;i++){
			m= random.nextInt(bufferChars.length);
			chars[i] = bufferChars[m];
		}
		return new String(chars);
	}
}