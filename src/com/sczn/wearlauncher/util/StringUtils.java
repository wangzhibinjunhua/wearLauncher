/**
 * Project Name:XPGSdkV4AppBase
 * File Name:StringUtils.java
 * Package Name:com.gizwits.framework.utils
 * Date:2015-1-27 14:47:40
 * Copyright (c) 2014~2015 Xtreme Programming Group, Inc.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.sczn.wearlauncher.util;

import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;

import java.io.UnsupportedEncodingException;
import java.lang.Character.UnicodeBlock;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: Auto-generated Javadoc

/**
 * 锟街凤拷锟斤拷锟斤拷锟斤拷锟斤拷.
 * 
 * @author Lien
 */
public class StringUtils {
	public final static SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/**
	 * 锟斤拷锟街凤拷锟斤拷转锟狡筹拷锟斤拷锟斤拷.
	 * 
	 * @param num
	 *            the num
	 * @return the int
	 */
	public static int toInt(String num) {
		try {
			return Integer.parseInt(num);
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 锟叫讹拷锟街凤拷锟斤拷锟角凤拷为null锟斤拷锟斤拷为锟斤拷.
	 * 
	 * @param str
	 *            the str
	 * @return true, if is empty
	 */
	public static boolean isEmpty(String str) {
		if (str == null || str == "" || str.trim().equals("")
				|| str.trim().equals("null"))
			return true;
		return false;
	}

	/**
	 * 锟斤拷锟斤拷一锟斤拷StringBuffer锟斤拷锟斤拷.
	 * 
	 * @return the buffer
	 */
	public static StringBuffer getBuffer() {
		return new StringBuffer(50);
	}

	/**
	 * 锟斤拷锟斤拷一锟斤拷StringBuffer锟斤拷锟斤拷.
	 * 
	 * @param length
	 *            the length
	 * @return the buffer
	 */
	public static StringBuffer getBuffer(int length) {
		return new StringBuffer(length);
	}

	public static Date parseStrToDate(String dateStr) {
		try {
			Date date = SIMPLE_DATE_FORMAT.parse(dateStr);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Calendar parseStrToCalendar(String dateStr) {
		Calendar calendar = null;
		Date parseDate = parseStrToDate(dateStr);
		if (parseDate != null) {
			calendar = Calendar.getInstance();
			calendar.setTime(parseDate);
			return calendar;
		}
		return calendar;
	}

	/**
	 * 锟斤拷式一锟斤拷锟斤拷锟斤拷.
	 * 
	 * @param longDate
	 *            锟斤拷要锟斤拷式锟斤拷锟节的筹拷锟斤拷锟斤拷锟斤拷锟街凤拷锟斤拷锟斤拷式
	 * @param format
	 *            锟斤拷式锟斤拷锟斤拷锟斤拷
	 * @return 锟斤拷式锟斤拷锟斤拷锟斤拷锟斤拷锟�
	 */
	public static String getStrDate(String longDate, String format) {
		if (isEmpty(longDate))
			return "";
		long time = Long.parseLong(longDate);
		Date date = new Date(time);
		return getStrDate(date, format);
	}

	public static String getStrDateTime(long time) {
		Date date = new Date(time);
		return SIMPLE_DATE_FORMAT.format(date);
	}

	/**
	 * 锟斤拷式一锟斤拷锟斤拷锟斤拷.
	 * 
	 * @param time
	 *            the time
	 * @param format
	 *            锟斤拷式锟斤拷锟斤拷锟斤拷
	 * @return 锟斤拷式锟斤拷锟斤拷锟斤拷锟斤拷锟�
	 */
	public static String getStrDate(long time, String format) {
		Date date = new Date(time);
		return getStrDate(date, format);
	}

	/**
	 * 锟斤拷锟截碉拷前锟斤拷锟节的革拷式锟斤拷锟斤拷yyyy-MM-dd锟斤拷锟斤拷示.
	 * 
	 * @return the str date
	 */
	public static String getStrDate() {
		SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
		return dd.format(new Date());
	}

	/**
	 * 锟斤拷锟截碉拷前锟斤拷锟节的革拷式锟斤拷锟斤拷示.
	 * 
	 * @param date
	 *            指锟斤拷锟斤拷式锟斤拷锟斤拷锟斤拷锟斤拷
	 * @param formate
	 *            锟斤拷式锟斤拷锟斤拷锟斤拷
	 * @return the str date
	 */
	public static String getStrDate(Date date, String formate) {
		SimpleDateFormat dd = new SimpleDateFormat(formate);
		return dd.format(date);
	}

	/**
	 * sql锟斤拷锟斤拷锟街凤拷转锟斤拷.
	 * 
	 * @param keyWord
	 *            锟截硷拷锟斤拷
	 * @return the string
	 */
	public static String sqliteEscape(String keyWord) {
		keyWord = keyWord.replace("/", "//");
		keyWord = keyWord.replace("'", "''");
		keyWord = keyWord.replace("[", "/[");
		keyWord = keyWord.replace("]", "/]");
		keyWord = keyWord.replace("%", "/%");
		keyWord = keyWord.replace("&", "/&");
		keyWord = keyWord.replace("_", "/_");
		keyWord = keyWord.replace("(", "/(");
		keyWord = keyWord.replace(")", "/)");
		return keyWord;
	}

	/**
	 * sql锟斤拷锟斤拷锟街凤拷锟斤拷转锟斤拷.
	 * 
	 * @param keyWord
	 *            锟截硷拷锟斤拷
	 * @return the string
	 */
	public static String sqliteUnEscape(String keyWord) {
		keyWord = keyWord.replace("//", "/");
		keyWord = keyWord.replace("''", "'");
		keyWord = keyWord.replace("/[", "[");
		keyWord = keyWord.replace("/]", "]");
		keyWord = keyWord.replace("/%", "%");
		keyWord = keyWord.replace("/&", "&");
		keyWord = keyWord.replace("/_", "_");
		keyWord = keyWord.replace("/(", "(");
		keyWord = keyWord.replace("/)", ")");
		return keyWord;
	}

	/**
	 * 锟斤拷锟斤拷锟街凤拷锟斤拷
	 * 
	 * @param str
	 *            原始锟街凤拷锟斤拷
	 * @param length
	 *            锟斤拷锟斤拷锟街凤拷锟斤拷
	 * @param isPoints
	 *            锟角凤拷锟绞★拷院锟�
	 * @return 锟斤拷式锟斤拷锟斤拷锟斤拷锟斤拷锟�
	 */
	public static String getStrFomat(String str, int length, boolean isPoints) {
		String result = "";

		if (str.length() > length) {
			result = str.substring(0, length);
			if (isPoints) {
				result = result + "...";
			}
		} else {
			result = str;
		}

		return result;

	}

	public static boolean containsEmoji(String source) {
		int len = source.length();
		for (int i = 0; i < len; i++) {
			char codePoint = source.charAt(i);
			if (isEmojiCharacter(codePoint)) {
				// 锟斤拷锟斤拷锟斤拷锟狡ワ拷锟�,锟斤拷锟斤拷址锟斤拷锟紼moji锟斤拷锟斤拷
				return true;
			}
		}
		return false;
	}

	private static boolean isEmojiCharacter(char codePoint) {
		return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
				|| (codePoint == 0xD)
				|| ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
				|| ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
				|| ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
	}

	/**
	 * utf-8 转unicode
	 * 
	 * @param inStr
	 * @return String
	 */
	public static String utf8ToUnicode(String inStr) {
		char[] myBuffer = inStr.toCharArray();

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < inStr.length(); i++) {
			UnicodeBlock ub = UnicodeBlock.of(myBuffer[i]);
			if (ub == UnicodeBlock.BASIC_LATIN) {
				sb.append(myBuffer[i]);
			} else if (ub == UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
				int j = (int) myBuffer[i] - 65248;
				sb.append((char) j);
			} else {
				short s = (short) myBuffer[i];
				String hexS = Integer.toHexString(s);
				String unicode = "\\u" + hexS;
				sb.append(unicode.toLowerCase());
			}
		}
		return sb.toString();
	}

	public static String unicodeToUtf8(String theString) {
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					// Read the xxxx
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException(
									"Malformed   \\uxxxx   encoding.");
						}
					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't')
						aChar = '\t';
					else if (aChar == 'r')
						aChar = '\r';
					else if (aChar == 'n')
						aChar = '\n';
					else if (aChar == 'f')
						aChar = '\f';
					outBuffer.append(aChar);
				}
			} else
				outBuffer.append(aChar);
		}
		return outBuffer.toString();
	}

	public static ByteBuffer UTF8ToUnicode(String strChar) {
		try {
			byte[] arrByUTF8 = strChar.getBytes("utf-8");

			ByteBuffer arrByUnicode = ByteBuffer.allocate(arrByUTF8.length * 2);

			int nBuf = 0;
			int nBufPos = 0;
			int nNeed = 0;
			int nCur = 0;
			int i = 0;
			int nLen = arrByUTF8.length;
			while (i < nLen) {
				nCur = arrByUTF8[i];
				nCur &= 0xff;
				i++;
				// -----锟斤拷锟街斤拷
				if ((nCur & 0x80) == 0) {
					nNeed = 1;
					nBuf = 0;
					arrByUnicode.put((byte) nCur);
					arrByUnicode.put((byte) 0x00);
					continue;
				}

				// -----锟斤拷锟街斤拷
				else {
					// 头一锟斤拷锟街斤拷
					if ((nCur & 0x40) != 0) {
						nBuf = 0;
						nBufPos = 1;

						// 锟斤拷锟斤拷锟街凤拷锟斤拷要2锟斤拷utf-8锟街斤拷
						if ((nCur & 0x20) == 0) {
							nNeed = 2;
							nBuf = 0;

							nCur &= 0x1f;
							nCur <<= 6;
							nBuf += nCur;
						}
						// 锟斤拷锟斤拷锟街凤拷锟斤拷要3锟斤拷utf-8锟街斤拷
						else if ((nCur & 0x10) == 0) {
							nNeed = 3;
							nBuf = 0;

							nCur &= 0x0f;
							nCur <<= 12;
							nBuf += nCur;
						}
						// 锟斤拷锟斤拷锟街凤拷锟斤拷要4锟斤拷utf-8锟街斤拷
						else if ((nCur & 0x08) == 0) {
							nNeed = 4;
							nBuf = 0;

							nCur &= 0x07;
							nCur <<= 18;
							nBuf += nCur;
						}
						// 锟斤拷锟斤拷锟街凤拷锟斤拷要5锟斤拷utf-8锟街斤拷
						else if ((nCur & 0x04) == 0) {
							nNeed = 5;
							nBuf = 0;

							nCur &= 0x03;
							nCur <<= 24;
							nBuf += nCur;
						}
						// 锟斤拷锟斤拷锟街凤拷锟斤拷要6锟斤拷utf-8锟街斤拷
						else if ((nCur & 0x02) == 0) {
							nNeed = 6;
							nBuf = 0;

							nCur &= 0x01;
							nCur <<= 30;
							nBuf += nCur;
						}
					}

					// 锟斤拷锟街节猴拷锟斤拷锟斤拷纸锟�
					else {
						nCur &= 0x3f;

						nBufPos++;
						nCur <<= (nNeed - nBufPos) * 6;
						nBuf += nCur;

						// 一锟斤拷UNICODE锟街凤拷锟斤拷锟斤拷锟斤拷锟�
						if (nBufPos >= nNeed) {
							nBuf = InvertUintBit(nBuf);
							if (nNeed <= 3) {
								nBuf >>= 16;
								arrByUnicode.putShort((short) nBuf);
							} else {
								arrByUnicode.putInt(nBuf);
							}
							nBuf = 0;
						}
					}
				}
			}
			return arrByUnicode;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String UnicodeToUTF8(ByteBuffer arrByUnicode) {
		ByteBuffer arrByUTF8 = null;
		if (arrByUnicode.capacity() < 2) {
			return "";
		}
		// arrByUnicode.position(0);
		arrByUTF8 = ByteBuffer.allocate(arrByUnicode.capacity()
				- arrByUnicode.position());

		int byHeigh = 0;
		int byLow = 0;
		int u1 = 0;
		int u2 = 0;
		int u3 = 0;
		int nSum = 0;

		int nLen = 0;
		try {
			while (true) {
				// 锟斤拷锟斤拷锟斤拷锟斤拷锟截碉拷锟斤拷锟捷ｏ拷锟斤拷位锟斤拷前
				byLow = arrByUnicode.get();
				byLow &= 0xff;
				byHeigh = arrByUnicode.get();
				byHeigh &= 0xff;

				nLen++;

				// 锟斤拷尾
				if (byLow == 0 && byHeigh == 0) {
					break;
				}

				nSum = 0;
				nSum = (byHeigh << 8) & 0xff00;
				nSum += byLow;

				// 1位
				if (nSum <= 0x7f) {
					arrByUTF8.put((byte) nSum);
				}

				// 2位
				else if (nSum >= 0x80 && nSum <= 0x07ff) {
					u1 = 0xc0;
					u1 += ((nSum >> 6) & 0x1f);

					u2 = 0x80;
					u2 += (nSum & 0x3f);

					arrByUTF8.put((byte) u1);
					arrByUTF8.put((byte) u2);
				}

				// 3位
				else if (nSum >= 0x0800 && nSum <= 0xffff) {
					u1 = 0xe0;
					u1 += ((nSum >> 12) & 0x0f);

					u2 = 0x80;
					u2 += ((nSum >> 6) & 0x3f);

					u3 = 0x80;
					u3 += (nSum & 0x3f);

					arrByUTF8.put((byte) u1);
					arrByUTF8.put((byte) u2);
					arrByUTF8.put((byte) u3);
				}

			}

			String str = new String(arrByUTF8.array());
			return str;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	// 锟斤拷锟睫凤拷锟斤拷锟斤拷锟斤拷4锟斤拷锟街节低革拷位锟斤拷锟斤拷, 0x01 02 03 04 ---> 0x04 03 02 01
	public static int InvertUintBit(int nNum) {
		int nResult = 0;
		int nTemp = 0;

		// 0x00 00 00 04 ---> 0x04 00 00 00
		nTemp = nNum;
		nTemp <<= 24;
		nTemp &= 0xff000000;
		nResult += nTemp;

		// 0x00 00 03 00 ---> 0x00 03 00 00
		nTemp = nNum;
		nTemp <<= 8;
		nTemp &= 0xff0000;
		nResult += nTemp;

		// 0x00 02 00 00 ---> 0x00 00 02 00
		nTemp = nNum;
		nTemp >>= 8;
		nTemp &= 0xff00;
		nResult += nTemp;

		// 0x01 00 00 00 ---> 0x00 00 00 01
		nTemp = nNum;
		nTemp >>= 24;
		nTemp &= 0xff;
		nResult += nTemp;

		return nResult;
	}

	// --------------------------------------
	// 锟斤拷锟叫凤拷锟斤拷锟斤拷锟斤拷4锟斤拷锟街节低革拷位锟斤拷锟斤拷, 0x01 02 03 04 ---> 0x04 03 02 01
	public int InvertIntBit(int nNum) {
		int nResult = 0;
		int nTemp = 0;

		// 0x00 00 00 04 ---> 0x04 00 00 00
		nTemp = nNum;
		nTemp <<= 24;
		nTemp &= 0xff000000;
		nResult += nTemp;

		// 0x00 00 03 00 ---> 0x00 03 00 00
		nTemp = nNum;
		nTemp <<= 8;
		nTemp &= 0xff0000;
		nResult += nTemp;

		// 0x00 02 00 00 ---> 0x00 00 02 00
		nTemp = nNum;
		nTemp >>= 8;
		nTemp &= 0xff00;
		nResult += nTemp;

		// 0x01 00 00 00 ---> 0x00 00 00 01
		nTemp = nNum;
		nTemp >>= 24;
		nTemp &= 0xff;
		nResult += nTemp;

		return nResult;
	}

	public static String binary(byte[] bytes, int radix) {
		return new BigInteger(1, bytes).toString(radix);// 锟斤拷锟斤拷锟�1锟斤拷锟斤拷锟斤拷锟斤拷
	}

	public static SpannableStringBuilder createTextBold(String content) {
		SpannableStringBuilder spanBuilder = new SpannableStringBuilder(content);
		// style 为0 锟斤拷锟斤拷锟斤拷锟斤拷锟侥ｏ拷锟斤拷锟斤拷Typeface.BOLD(锟斤拷锟斤拷) Typeface.ITALIC(斜锟斤拷)锟斤拷
		// size 为0 锟斤拷锟斤拷锟斤拷原始锟斤拷锟斤拷锟斤拷锟斤拷 size锟斤拷小
		spanBuilder.setSpan(new StyleSpan(Typeface.BOLD), 0, content.length(),
				Spanned.SPAN_INCLUSIVE_INCLUSIVE);
		return spanBuilder;
	}

	public static boolean isEmail(String strEmail) {
		String strPattern = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(strEmail);
		return m.matches();
	}

	public static boolean isPhone(String strPhone) {
		String strPattern = "^1[0-9]{10}$";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(strPhone);
		return m.matches();
	}

	public static boolean isPhoneNumberValid(String phoneNumber) {
		boolean isValid = false;
		String expression = "(\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$";

		CharSequence inputStr = phoneNumber;
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}
}
