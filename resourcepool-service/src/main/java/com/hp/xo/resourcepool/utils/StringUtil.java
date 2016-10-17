/*
 * Copyright (c) 2009 Hutchison Global Communications Limited,
 *
 * All Rights Reserved.
 * This document contains proprietary information that shall be
 * distributed or routed only within HGC, and its authorized
 * clients, except with written permission of HGC.
 *
 */
package com.hp.xo.resourcepool.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Base64;


import com.hp.xo.resourcepool.Constants;

/**
 * String utility
 * 
 * @author Zhefang Chen
 * 
 */
public final class StringUtil {
    
    private static final String EMPTY="";
    
    private StringUtil() {
    }

	/**
	 * If input string is null then return a empty string
	 * 
	 * @param inStr
	 * @return
	 */
	public static String getStringValue(final String inStr) {
		return (isNullString(inStr)) ? "" : inStr.trim();
	}

	/**
	 * Converts input object to a string.<p/>
	 * If object is null then return the second parameter default string. <p/>
	 * If object is null and default string is null then return empty string. 
	 * 
	 * @param obj
	 * @param defaultStr
	 * @return
	 */
	public static String getObjectStringValue(final Object obj, final String defaultStr) {
		if (obj == null) {
			if (defaultStr != null) {
				return defaultStr;
			} else {
				return "";
			}
		} else {
			return String.valueOf(obj);
		}
	}

	/**
	 * Check if the input string is null.
	 * 
	 * @param inStr
	 * @return boolean value indicating if the string is null
	 */
	public static boolean isNullString(final String inStr) {
		if (inStr == null || inStr.trim().equals("")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Converts a hexadecimal string to a byte array.
	 * 
	 * @param hexStr
	 *            Input hexadecimal string.
	 * @return Byte array.
	 * @throws Exception
	 */
	public static byte[] hexToBytes(String hexStr) throws Exception {
		if (hexStr == null) {
			return null;
		}
		if (hexStr.length() % 2 != 0) {
			throw new Exception("Length of data is not equal to even number");
		}
		byte[] rtnBytes = new byte[hexStr.length() / 2];

		for (int i = 0; i < hexStr.length() / 2; i++) {
			rtnBytes[i] = (byte) Integer.parseInt(hexStr.substring(i * 2, i * 2 + 2), 16);
		}
		return rtnBytes;
	}

	/**
	 * Converts a byte array to string.
	 * 
	 * @param data
	 *            Input byte array.
	 * @return String
	 */
	public static String hexToString(byte[] data) {
		if (data == null) {
			return "";
		}
		;
		StringBuffer sb = new StringBuffer(data.length * 2);
		for (int i = 0; i < data.length; i++) {
			String hex = Integer.toHexString(0xFF & data[i]);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString().toUpperCase();
	}

	/**
	 * Parse a string into a series of string tokens using the specified
	 * delimiter.
	 * 
	 * @param str
	 * @param splitChar
	 * @return Array of string token
	 */
	public static String[] split(String str, char splitChar) {
		if (str == null) {
			return null;
		}
		if (str.trim().equals("")) {
			return new String[0];
		}
		if (str.indexOf(splitChar) == -1) {
			String[] strArray = new String[1];
			strArray[0] = str;
			return strArray;
		}

		ArrayList<String> list = new ArrayList<String>();
		int prevPos = 0;
		for (int pos = str.indexOf(splitChar); pos >= 0; pos = str.indexOf(splitChar, (prevPos = (pos + 1)))) {
			list.add(str.substring(prevPos, pos));
		}
		list.add(str.substring(prevPos, str.length()));

		return (String[]) list.toArray(new String[list.size()]);
	}

	/**
	 * Parse a string into a series of string tokens using the specified
	 * delimiter.
	 * 
	 * @param str
	 *            Input string
	 * @param delim
	 *            The string delimiter.
	 * @return Array of string tokens.
	 */
	public static String[] tokenize(String str, String delim) {
		String[] strs = null;
		if (str != null) {
			StringTokenizer tokens;
			if (delim == null) {
				tokens = new StringTokenizer(str);
			} else {
				tokens = new StringTokenizer(str, delim);
			}
			strs = new String[tokens.countTokens()];
			for (int i = 0; i < strs.length && tokens.hasMoreTokens(); i++) {
				strs[i] = tokens.nextToken();
			}
		}
		return strs;
	}

	/**
	 * Parse a string into a series of string tokens according to fixed length
	 * and return tokenized string array.
	 * 
	 * @param str
	 *            Input string
	 * @param fixedLength
	 *            The length at which the string is tokenized.
	 * @return Array of string tokens.
	 */
	public static String[] tokenize(String str, int fixedLength) {
		String[] strs = null;
		if (str != null && fixedLength > 0) {
			ArrayList<String> list = new ArrayList<String>();
			for (int i = 0; i < str.length(); i += fixedLength) {
				int next = i + fixedLength;
				if (next > str.length()) next = str.length();
				list.add(str.substring(i, next));
			}
			strs = (String[]) list.toArray(new String[] {});
		}
		return strs;
	}

	/**
	 * Convert the input string to String encoded in UTF16LE format.
	 * 
	 * @param input
	 * @return String encoded in UTF16LE format
	 * @throws IOException
	 */
	public static String toUTF16LEString(String input) throws IOException {
		if (input == null || input.length() == 0) {
			return input;
		}

		byte[] b = input.getBytes("UTF-16LE");
		return hexToString(b);
	}

	/**
	 * Left padding the string with the specified padding character upto the specified length.
	 * @param inStr Input string
	 * @param length Padding length
	 * @param paddingChar Padding character
	 * @return Padding string
	 */
	public static String leftPad(String inStr, int length, char paddingChar) {
		if (inStr.length() == length) return inStr;

		StringBuffer outStr = new StringBuffer();
		for (int i = inStr.length(); i < length; i++) {
			outStr.append(paddingChar);
		}
		outStr.append(inStr);

		return outStr.toString();
	}

	/**
	 * Right padding the string with the specified padding character upto the specified length.
	 * @param inStr Input string
	 * @param length Padding length
	 * @param paddingChar Padding character
	 * @return Padding string
	 */
	public static String rightPad(String inStr, int length, char paddingChar) {
		if (inStr.length() == length) return inStr;

		StringBuffer outStr = new StringBuffer();
		outStr.append(inStr);

		for (int i = inStr.length(); i < length; i++) {
			outStr.append(paddingChar);
		}

		return outStr.toString();
	}

	/**
	 * Adds leading zeros to the given String to the specified length. Nothing
	 * will be done if the length of the given String is equal to or greater
	 * than the specified length.
	 * 
	 * @param s
	 *            The source string.
	 * @param len
	 *            The length of the target string.
	 * @return The String after adding leading zeros.
	 */
	public static String addLeadingZero(String s, int len) {
		return addLeadingCharacter(s, '0', len);
	}

	/**
	 * Adds leading spaces to the given String to the specified length. Nothing
	 * will be done if the length of the given String is equal to or greater
	 * than the specified length.
	 * 
	 * @param s
	 *            The source string.
	 * @param len
	 *            The length of the target string.
	 * @return The String after adding leading spaces.
	 */
	public static String addLeadingSpace(String s, int len) {
		return addLeadingCharacter(s, ' ', len);
	}

	/**
	 * Adds specified leading characters to the specified length. Nothing will
	 * be done if the length of the given String is equal to or greater than the
	 * specified length.
	 * 
	 * @param s
	 *            The source string.
	 * @param c
	 *            The leading character(s) to be added.
	 * @param len
	 *            The length of the target string.
	 * @return The String after adding the specified leading character(s).
	 */
	public static String addLeadingCharacter(String s, char c, int len) {
		if (s != null) {
			StringBuffer sb = new StringBuffer();
			int count = len - s.length();
			for (int i = 0; i < count; i++) {
				sb.append(c);
			}
			sb.append(s);
			return sb.toString();
		} else {
			return null;
		}
	}

	/**
	 * Removes leading zeros from the given String, if any.
	 * 
	 * @param s
	 *            The source string.
	 * @return The String after removing leading zeros.
	 */
	public static String removeLeadingZero(String s) {
		return removeLeadingCharacter(s, '0');
	}

	/**
	 * Removes leading spaces from the given String, if any.
	 * 
	 * @param s
	 *            The source string.
	 * @return The String after removing leading spaces.
	 */
	public static String removeLeadingSpace(String s) {
		return removeLeadingCharacter(s, ' ');
	}

	/**
	 * Removes specified leading characters from the given String, if any.
	 * 
	 * @param s
	 *            The source string.
	 * @param c
	 *            The leading character(s) to be removed.
	 * @return The String after removing the specified leading character(s).
	 */
	public static String removeLeadingCharacter(String s, char c) {
		if (s != null) {
			int len = s.length();
			int i = 0;
			for (i = 0; i < len; i++) {
				if (s.charAt(i) != c) {
					break;
				}
			}
			if (i > 0) {
				return s.substring(i);
			} else {
				return s;
			}
		} else {
			return null;
		}
	}

	/**
	 * Appends zeros to the given String to the specified length. Nothing will
	 * be done if the length of the given String is equal to or greater than the
	 * specified length.
	 * 
	 * @param s
	 *            The source string.
	 * @param len
	 *            The length of the target string.
	 * @return The String after appending zeros.
	 */
	public static String appendZero(String s, int len) {
		return appendCharacter(s, '0', len);
	}

	/**
	 * Appends spaces to the given String to the specified length. Nothing will
	 * be done if the length of the given String is equal to or greater than the
	 * specified length.
	 * 
	 * @param s
	 *            The source string.
	 * @param len
	 *            The length of the target string.
	 * @return
	 * 				The String after appending spaces.
	 */
	public static String appendSpace(String s, int len) {
		return appendCharacter(s, ' ', len);
	}

	/**
	 * Appends specified characters to the given String to the specified length.
	 * Nothing will be done if the length of the given String is equal to or
	 * greater than the specified length.
	 * 
	 * @param s
	 *            The source string.
	 * @param c
	 *            The character(s) to be appended.
	 * @param len
	 *            The length of the target string.
	 * @return
	 * 		The String after appending the specified character(s).
	 */
	public static String appendCharacter(String s, char c, int len) {
		if (s != null) {
			StringBuffer sb = new StringBuffer().append(s);
			while (sb.length() < len) {
				sb.append(c);
			}
			return sb.toString();
		} else {
			return null;
		}
	}

	/**
	 * Replaces all the occurences of a search string in a given String with a
	 * specified substitution.
	 * 
	 * @param text
	 *            The String to be searched.
	 * @param src
	 *            The search String.
	 * @param tar
	 *            The replacement String.
	 * @return The result String after replacing.
	 */
	public static String replace(String text, String src, String tar) {
		StringBuffer sb = new StringBuffer();

		if (text == null || src == null || tar == null) {
			return text;
		} else {
			int size = text.length();
			int gap = src.length();

			for (int start = 0; start >= 0 && start < size;) {
				int i = text.indexOf(src, start);
				if (i == -1) {
					sb.append(text.substring(start));
					start = -1;
				} else {
					sb.append(text.substring(start, i)).append(tar);
					start = i + gap;
				}
			}
			return sb.toString();
		}
	}
	
	/**
	 * Converts object to a string.<br>
	 * If object is null then return null
	 * 
	 * @param obj
	 * @return
	 */
	public static String toString(Object obj) {
		if (null == obj) {
			return null;
		} else {
			return obj.toString();
		}
	}

	/**
	 * Calculate the MD5 checksum of the input string
	 * @param inString Input string
	 * @return MD5 checksum of the input string in hexadecimal value
	 */
	public static String md5sum(String inString) {
		MessageDigest algorithm = null;

		try {
			algorithm = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException nsae) {
			System.err.println("Cannot find digest algorithm");
			return null;
		}

		byte[] defaultBytes = inString.getBytes();
		algorithm.reset();
		algorithm.update(defaultBytes);
		byte messageDigest[] = algorithm.digest();
		return hexToString(messageDigest);
	}

	/**
	 * Get throwable stack trace string
	 * 
	 * @param tb
	 * @return
	 */
	public static String getErrorStackTrace(Throwable tb) {
		StringWriter sw = new StringWriter();
		tb.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}
	
	/**
	 * Check if input string is a null or empty value
	 * 
	 * @param outstr
	 * @return
	 */
	public static boolean isNotEmpty(String outstr){ 
		if(outstr!=null&&outstr.trim().length()>0){
			return true;
		}
		return false;
	}
	
	/**
	 * URL encode UTF8 charset string.
	 * 
	 * @param str
	 * @return
	 */
	public static String urlEncode(String str) {
		if (null == str) {
			str = "";
		}

		try {
			return URLEncoder.encode(str, Constants.CHARSET_UTF8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return str;
		}
	}

	/**
	 * URL decode UTF8 charset string
	 * 
	 * @param str
	 * @return
	 */
	public static String urlDecode(String str) {
		if (null == str) {
			str = "";
		}

		try {
			return URLDecoder.decode(str, Constants.CHARSET_UTF8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return str;
		}
	}
	
	/**
	 * Encrypt string. 
	 * 
	 * @param inputStr
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String inputStr) throws Exception {
//		String result = null;
//		if (StringUtils.isNotEmpty(inputStr)) {
//			try {
///*
//				CryptoManager crypt = CryptoManager.getInstance();
//				byte[] bytes = crypt.encrypt(inputStr.getBytes(), Constants.ENCRYPTED_CONFIG_KEY); 
//*/ 
//				CryptoManager crypt = CryptoManager.getNewInstance();
//				byte[] bytes = crypt.encrypt(inputStr.getBytes());
//				result = new String(Base64Utils.encode(bytes), Constants.DEFAULT_CHAR_ENCODING);
//			} catch (Exception ex) {
//				throw ex;
//			} 
//			// remove newline character
//			StringReader sr = new StringReader(result);   
//			BufferedReader br = new BufferedReader(sr);   
//			String line = null;   
//			String temp = "";   
//			while((line=br.readLine())!=null){   
//			    temp += line;    
//			}   
//			result = temp;
//		}		
//		return result;
		return inputStr;
	}
	
	/**
	 * Decrypt string
	 * 
	 * @param inputStr
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String inputStr) throws Exception {
//		String result = null;
//		if (StringUtils.isNotEmpty(inputStr)) {
//			try {
///*				CryptoManager crypt = CryptoManager.getInstance(); */
//				CryptoManager crypt = CryptoManager.getNewInstance();
//				byte[] bytes = Base64Utils.decode(inputStr.getBytes(Constants.DEFAULT_CHAR_ENCODING));
///*				result = new String(crypt.decrypt(bytes, Constants.ENCRYPTED_CONFIG_KEY));*/
//				result = new String(crypt.decrypt(bytes));
//			} catch (Exception ex) {
//				throw ex;
//			} 
//		}
//		
//		return result;
		return inputStr;
	}
	
//	public static void main(String[] args) throws Exception {
//		System.out.println(md5sum("Hello, Terrence!"));
//		String en = encrypt("陈浙芳 vs 陈泽平");
//		System.out.println(en);
//		System.out.println(decrypt(en));
//		String test = "KLMdpS+W6wIQKHy69pZvmUUMF/wZ1KNU5ybuGWlDceBWItGeY1Nqt4zrXXT0Dnbljbb3x8Y3Xe8UbcTHr/fRNQ3CyHC5UoznvnVF/GcSdvvQ0Ji8mHVInIQaK2JfekWeeE+ei4fW/EjXrwZHdWGb+q5cRhzVpZUbIrWVUBC3G3hj4nT16Tt8vs6OwYkd/tJl7Bv3saDUYvY9AfbDkq9QtwwkG1/iFLO6s6kuJ8Mftqr4n1c2AImkqRIAv6HRbW0fUZGQ/oxogsSAg0VRTQNC9g==";
//		test = "kBlZXbajiRuC9hI0lWvBvjBWNZ4MNpFCTgo+Y+guuWpQEt/LDRO+KRBlBtgfHSKF+pIimAHzul0eyeZG6kZuT1Gzj++5t3XV8YILUR+W74Mi+iTFNRjC7mRX8WBIixsRmR0bKPNx/Az29yOvtDUXqvY7wfeJ47mOFIbv5QNvepxSV6sDn/rC7ezt9ykraUmL";
//		test = "kBlZXbajiRuC9hI0lWvBvndZaAhTFlzBWfio5CqiTrWz9hzjyqJyzel5Sks/OcNTHcuAFKr0IuchKd19YzUHRh3SFfLAGZ2gPVP+6bSqCoUzlGJCETjl1Ee9pTwBRv+2FOe5fn+Rz/R4SO1m+XxM92Opur79ik/SXbFOQeL7/KbgNV0/bmnKLcvy3yh2XOqS";
//		System.out.println(decrypt(test));
//	}

         public static String getStringFromArray(String[] values){
           String valueList=EMPTY;
            if(values!=null && values.length>0){
                    for(int i=0;i<values.length;i++){
                        String value=values[i];
                        if(i>0){
                            valueList+=Constants.SPLIT_BY_SEMICOLON;
                        }
                        valueList+=value;
                    }
                }
           return valueList;
        }

        public static String[] getStringArrayFromString(String valueList){
            String[] orgCodes=null;
            if(isNotEmpty(valueList)){
                    orgCodes=valueList.split(Constants.SPLIT_BY_SEMICOLON);
                }
            return orgCodes;
        }
        
        public static String getStringFromArray(Object[] values){
            String valueList=EMPTY;
             if(values!=null && values.length>0){
                     for(int i=0;i<values.length;i++){
                         String value=(values[i] == null) ? null : values[i].toString();
                         if(i>0){
                             valueList+=Constants.SPLIT_BY_SEMICOLON;
                         }
                         valueList+=value;
                     }
                 }
            return valueList;
        }
        
        public static boolean isNumber(String value){
        	boolean rslt = false;
        	
        	if (value!=null )
        		rslt = value.matches("^[-+]?\\d+(\\.\\d+)?$");
        	
        	return rslt;
        }
        
        public static String getSignature(Map<String, Object[]> param, boolean isUrlEncode) {    		
    		String result = null; 
    		if (null != param.get("secretkey")) {    			
    	    	String secretkey = (String)param.get("secretkey")[0];
    			SortedMap<String, Object> need2Sign = new TreeMap<String, Object>();
    			for (String s : param.keySet()) {
    				if(param.get(s)[0] instanceof Boolean){
    					need2Sign.put(s,param.get(s)[0]);
    				}else{
    					need2Sign.put(s, encoder(((String)param.get(s)[0]).toLowerCase()).replaceAll("\\+", "%20"));
    				}
    				
    			}
    			
    			StringBuilder sb2 = new StringBuilder();
    			List<String> keylist = new ArrayList<String>();
    			keylist.addAll(need2Sign.keySet());
    			Collections.sort(keylist, new Comparator<String>(){
    				public int compare(String o1, String o2) {
    					return o1.compareTo(o2);
    				}
    			});
    			
    			Map<String, String> cmdParamToFirstMap = ServiceOptionUtil.obtainCommandParamOrderToFirstMap();
    			
    			for (String s : keylist) {
    				if (false == "secretkey".equalsIgnoreCase(s) && false == "sessionkey".equalsIgnoreCase(s)) {
    					String cmd = (String)need2Sign.get("command");
    					if (cmdParamToFirstMap.containsKey(cmd) && null != need2Sign.get(cmdParamToFirstMap.get(cmd))) {
    						if (cmdParamToFirstMap.get(cmd).equalsIgnoreCase(s)) {
    							sb2.insert(0, s.toLowerCase() + "=" + need2Sign.get(s) + "&");
    						} else {
    							sb2.append(s.toLowerCase()).append("=").append(need2Sign.get(s)).append("&");
    						}
    					} else {
    						sb2.append(s.toLowerCase()).append("=").append(need2Sign.get(s)).append("&");
    					}
    				}
    				
    			}
    			sb2.deleteCharAt(sb2.length() - 1);
    			result = HmacSha.encodEncrypt(sb2.toString().toLowerCase(), secretkey, isUrlEncode);
    			
    		}
    		return result;
    	}

    	/**
    	 * URL Encoder tool
    	 * @param uncode
    	 * @return
    	 */
        public static String encoder(final String uncode) {
    	    try {
                return URLEncoder.encode(uncode, "UTF-8");
            } catch (UnsupportedEncodingException e) {
            	System.out.print(e);
                return uncode;
            }
    	    
    	}
        //add 2015-01-11@mashaowei
    	// jsobj.getString(HOST_ID) != null ? jsobj.getString(HOST_ID) : null
    	public static String getJsonString(JSONObject jsonObj, String key) {
    		try {
    			return jsonObj.getString(key) != null ? jsonObj.getString(key) : null;
    		} catch (Exception e) {
    			return null;
    		}
    	}
    	public static String convertHz(String hz) {
    		if (StringUtil.isNullString(hz)) {
    			return null;
    		}

    		// NumberFormat format = new DecimalFormat("##0.00");
    		BigDecimal _1000 = new BigDecimal(1000);
    		BigDecimal value = new BigDecimal(hz);

    		if (value.compareTo(_1000) == -1) {
    			return value + " MHz";
    		} else {
    			return value.divide(_1000, 2, RoundingMode.HALF_UP) + " GHz";
    		}
    	}
    	
    	public static String convertBytes(String bytes) {
    		if (StringUtil.isNullString(bytes)) {
    			return null;
    		}

    		// NumberFormat format = new DecimalFormat("##0.00");
    		BigDecimal _1024 = new BigDecimal(1024);
    		BigDecimal value = new BigDecimal(bytes);

    		if (value.compareTo(_1024) == -1) {
    			return value.divide(new BigDecimal(1), 2, RoundingMode.HALF_UP) + " KB";
    		} else if (value.compareTo(_1024.multiply(_1024)) == -1) {
    			return value.divide(_1024, 2, RoundingMode.HALF_UP) + " MB";
    		} else if (value.compareTo(_1024.multiply(_1024).multiply(_1024)) == -1) {
    			return value.divide(_1024).divide(_1024, 2, RoundingMode.HALF_UP) + " GB";
    		} else {
    			return value.divide(_1024).divide(_1024).divide(_1024, 2, RoundingMode.HALF_UP) + " TB";
    		}
    	}
    	//end

    	
    }


    class HmacSha {

    	private static final String MAC_NAME = "HmacSHA1";
    	private static final String ENCODING = "UTF-8";

    	static String encodEncrypt(String encryptText, String encryptKey, boolean isUrlEncode) {
    		return encode2Base64(encrypt(encryptText, encryptKey), isUrlEncode);
    	}

    	static byte[] encrypt(String encryptText, String encryptKey) {
    		try {
    			byte[] data = encryptKey.getBytes(ENCODING);
    			SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);
    			Mac mac = Mac.getInstance(MAC_NAME);
    			mac.init(secretKey);

    			byte[] text = encryptText.getBytes(ENCODING);
    			return mac.doFinal(text);
    		} catch (Exception e) {
    			throw new RuntimeException(e);
    		}
    	}

    	static String encode2Base64(byte[] content, boolean isUrlEncode) {
    		Base64 base64 = new Base64();
    		byte[] tmp = base64.encode(content);
    		if (isUrlEncode) {
    			return new String(tmp).replaceAll("[/]", "%2f").replaceAll("[=]", "%3d").replaceAll("[+]", "%2b");
    		} else {
    			return new String(tmp);
    		}
    	}
    
}
