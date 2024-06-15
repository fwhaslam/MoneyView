package com.fiends.moneyview.tools;

import org.joda.time.DateTime;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TimeUtilsTest {

	@Test
	public void toDate_string(){

		DateTime results = TimeUtils.toDate( "2020-03-15");

		assertEquals( "2020-03-15T00:00:00.000-07:00", results.toString() );
	}

	@Test
	public void toDate_date(){

		DateTime from = new DateTime("2020-03-15T00:00:00.000-07:00");

		String results = TimeUtils.toDate( from );

		assertEquals( "2020-03-15", results );
	}
}
