package com.fiends.moneyview.tools;

import org.apache.commons.lang3.ThreadUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class TimeUtils {

	static final String DAY_FORMAT = "yyyy-MM-dd";
	static final DateTimeFormatter DAY_FORMATTER = DateTimeFormat.forPattern(DAY_FORMAT);

	static public DateTime toDate(String value){
		return DAY_FORMATTER.parseDateTime( value );
	}

	static public String toDate(DateTime value){
		return value.toString( DAY_FORMAT );
	}

	static public void pause( int seconds ) {
		try {Thread.sleep( seconds * 1000L );}
		catch (InterruptedException ignored){}
	}
}
