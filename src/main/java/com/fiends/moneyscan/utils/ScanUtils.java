package com.fiends.moneyscan.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScanUtils {

	static final String BEGIN_DATE_REGEX = "^((Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\s(\\d{1,2}))(|\\s.*)$";
	static final String BEGIN_NUMBER_REGEX = "^([1-9]\\d+)(|\\s.*)$";

	static final String BEGIN_MONEY_REGEX = "^((\\d{1,3})(,\\d{3})*\\.\\d\\d\\-?)(|\\s.*)$";
	static final String END_MONEY_REGEX = "^(|.*\\s)((\\d{1,3})(,\\d{3})*\\.\\d\\d\\-?)$";

	/**
	 * Does the line end with a 'money' value?
	 * ( triple digits joined by comma, followed by point and two digits with optional minus sign )
	 * ( eg.  1,234.56- )
	 *
	 * @param line
	 * @return
	 */
	static public boolean endsWithMoney(String line ) {
		return line.matches(END_MONEY_REGEX);
	}

	static public String getEndMoney( String line ) {
		try {
			Pattern pattern = Pattern.compile(END_MONEY_REGEX);
			Matcher matcher = pattern.matcher(line);
			if (matcher.matches()) return matcher.group(2);
		}
		catch (IllegalStateException ignore) {}
		return null;
	}

	/**
	 *
	 * @param line
	 * @return
	 */
	static public boolean beginsWithMoney(String line ) {
		return line.matches(BEGIN_MONEY_REGEX);
	}

	/**
	 *
	 * @param line
	 * @return
	 */
	static public boolean beginsWithDate(String line) {
		return line.matches(BEGIN_DATE_REGEX);
	}

	/**
	 *
	 * @param line
	 * @return
	 */
	static public String getBeginDate(String line) {
		try {
			Pattern pattern = Pattern.compile(BEGIN_DATE_REGEX);
			Matcher matcher = pattern.matcher(line);
			if (matcher.matches()) return matcher.group(1);
		}
		catch (IllegalStateException ignore) {}
		return null;
	}


	/**
	 *
	 * @param line
	 * @return
	 */
	static public boolean beginsWithNumber(String line) {
		return line.matches(BEGIN_NUMBER_REGEX);
	}

	/**
	 *
	 * @param line
	 * @return
	 */
	static public String getBeginNumber(String line) {
		try {
			Pattern pattern = Pattern.compile(BEGIN_NUMBER_REGEX);
			Matcher matcher = pattern.matcher(line);
			if (matcher.matches()) return matcher.group(1);
		}
		catch (IllegalStateException ignore) {}
		return null;
	}

}
