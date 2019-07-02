package com.zhao.lazy.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 随机值工具
 * add by zhao of 2019年5月23日
 *
 * 功能描述：
 */
public class RandomUtils {

   public static final String[] num_str = { "2", "3", "4", "5", "6", 
	    "7", "8", "9" };
	  public static final String[] letter_str = { "a", "b", "c", "d", "e", "f", 
	    "g", "h", "j", "k", "m", "n", "p", "q", "r", "s", 
	    "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", 
	    "G", "H", "J", "K", "L", "M", "N", "P", "Q", "R", "S", 
	    "T", "U", "V", "W", "X", "Y", "Z" };
	  public static final String[] all_str = { "2", "3", "4", "5", "6", 
	    "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h", "j", 
	    "k", "m", "n", "p", "q", "r", "s", "t", "u", "v", "w", 
	    "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", 
	    "K", "L", "M", "N", "P", "Q", "R", "S", "T", "U", "V", "W", 
	    "X", "Y", "Z" };
	  public static final int WIDTH = 130;
	  public static final int HEIGHT = 48;
	  public static final String RANDOM_STR_SESSION = "random_str_session";

	  public static String getRandomStrByNum(int length) {
	    if (length < 1)
	      return "";
	    StringBuffer str = new StringBuffer();
	    for (int i = 0; i < length; ++i) {
	      int index = (int)(Math.random() * 8.0D);
	      str.append(num_str[index]);
	    }
	    return str.toString();
	  }

	  public static String getRandomStrByNum(int length, String separator, int space) {
	    StringBuffer str = new StringBuffer();
	    for (int i = 0; i < length; ++i) {
	      int index = (int)(Math.random() * 8.0D);
	      str.append(num_str[index]);
	      if ((i < length - 1) && ((i + 1) % space == 0))
	        str.append(separator);
	    }

	    return str.toString();
	  }

	  public static String getRandomStrByLetter(int length) {
	    if (length < 1)
	      return "";
	    StringBuffer str = new StringBuffer();
	    for (int i = 0; i < length; ++i) {
	      int index = (int)(Math.random() * 47.0D);
	      str.append(letter_str[index]);
	    }
	    return str.toString();
	  }

	  public static String getRandomStrByLetter(int length, String separator, int space) {
	    StringBuffer str = new StringBuffer();
	    for (int i = 0; i < length; ++i) {
	      int index = (int)(Math.random() * 47.0D);
	      str.append(letter_str[index]);
	      if ((i < length - 1) && ((i + 1) % space == 0))
	        str.append(separator);
	    }

	    return str.toString();
	  }

	  public static String getRandomStrByAll(int length) {
	    if (length < 1)
	      return "";
	    StringBuffer str = new StringBuffer();
	    for (int i = 0; i < length; ++i) {
	      int index = (int)(Math.random() * 55.0D);
	      str.append(all_str[index]);
	    }
	    return str.toString();
	  }

	  public static String getRandomStrByAll(int length, String separator, int space) {
	    StringBuffer str = new StringBuffer();
	    for (int i = 0; i < length; ++i) {
	      int index = (int)(Math.random() * 55.0D);
	      str.append(all_str[index]);
	      if ((i < length - 1) && ((i + 1) % space == 0))
	        str.append(separator);
	    }

	    return str.toString();
	  }

	  public static String getPrimaryKey() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	    return sdf.format(new Date()) + System.currentTimeMillis() + getRandomStrByAll(10);
	  }
	  
	  public static void main(String[] args) {
		System.out.println(getPrimaryKey());
	}
}
 