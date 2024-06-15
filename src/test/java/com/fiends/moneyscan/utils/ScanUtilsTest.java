package com.fiends.moneyscan.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ScanUtilsTest {

	@Test
	public void endsMoney(){

		assertTrue( ScanUtils.endsWithMoney(  "0.45") );
		assertTrue( ScanUtils.endsWithMoney(  "123.45") );
		assertTrue( ScanUtils.endsWithMoney(  "9,123.45") );
		assertTrue( ScanUtils.endsWithMoney(  "16,789,123.45") );

		assertTrue( ScanUtils.endsWithMoney(  "0.45-") );
		assertTrue( ScanUtils.endsWithMoney(  "123.45-") );
		assertTrue( ScanUtils.endsWithMoney(  "9,123.45-") );
		assertTrue( ScanUtils.endsWithMoney(  "16,789,123.45-") );

		assertFalse( ScanUtils.endsWithMoney(  "1123.45") );
		assertFalse( ScanUtils.endsWithMoney(  "123") );
		assertFalse( ScanUtils.endsWithMoney(  "123.456") );
		assertFalse( ScanUtils.endsWithMoney(  "123.456-") );

		assertTrue( ScanUtils.endsWithMoney( "something $ 0.45") );
		assertTrue( ScanUtils.endsWithMoney(  "something $ 123.45-") );
		assertFalse( ScanUtils.endsWithMoney(  "something:23.45-") );

	}

	@Test
	public void getEndMoney(){

		assertEquals( "0.45", ScanUtils.getEndMoney(  "0.45") );
		assertEquals( "123.45", ScanUtils.getEndMoney(  "123.45") );
		assertEquals( "9,123.45", ScanUtils.getEndMoney(  "9,123.45") );
		assertEquals( "16,789,123.45", ScanUtils.getEndMoney(  "16,789,123.45") );

		assertEquals( "0.45-", ScanUtils.getEndMoney(  "0.45-") );
		assertEquals( "123.45-", ScanUtils.getEndMoney(  "123.45-") );
		assertEquals( "9,123.45-", ScanUtils.getEndMoney(  "9,123.45-") );
		assertEquals( "16,789,123.45-", ScanUtils.getEndMoney(  "16,789,123.45-") );

		assertNull( ScanUtils.getEndMoney(  "1123.45") );
		assertNull( ScanUtils.getEndMoney(  "123") );
		assertNull( ScanUtils.getEndMoney(  "123.456") );
		assertNull( ScanUtils.getEndMoney(  "123.456-") );

		assertEquals( "0.45", ScanUtils.getEndMoney( "something $ 0.45") );
		assertEquals( "123.45-", ScanUtils.getEndMoney(  "something $ 123.45-") );
		assertNull( ScanUtils.getEndMoney(  "something:23.45-") );

	}

	@Test
	public void beginsWithMoney(){

		assertTrue( ScanUtils.beginsWithMoney(  "0.45") );
		assertTrue( ScanUtils.beginsWithMoney(  "123.45") );
		assertTrue( ScanUtils.beginsWithMoney(  "9,123.45") );
		assertTrue( ScanUtils.beginsWithMoney(  "16,789,123.45") );

		assertTrue( ScanUtils.beginsWithMoney(  "0.45-") );
		assertTrue( ScanUtils.beginsWithMoney(  "123.45-") );
		assertTrue( ScanUtils.beginsWithMoney(  "9,123.45-") );
		assertTrue( ScanUtils.beginsWithMoney(  "16,789,123.45-") );

		assertFalse( ScanUtils.beginsWithMoney(  "1123.45") );
		assertFalse( ScanUtils.beginsWithMoney(  "123") );
		assertFalse( ScanUtils.beginsWithMoney(  "123.456") );
		assertFalse( ScanUtils.beginsWithMoney(  "123.456-") );

		assertTrue( ScanUtils.beginsWithMoney( "0.45 and something") );
		assertTrue( ScanUtils.beginsWithMoney(  "123.45- and something") );
		assertFalse( ScanUtils.beginsWithMoney( "0.45, something") );

	}

	@Test
	public void beginsWithNumber(){

		assertTrue( ScanUtils.beginsWithNumber(  "45") );
		assertTrue( ScanUtils.beginsWithNumber(  "12345") );
		assertTrue( ScanUtils.beginsWithNumber(  "912345") );
		assertTrue( ScanUtils.beginsWithNumber(  "1678912345") );

		assertTrue( ScanUtils.beginsWithNumber( "45 and something") );

		assertFalse( ScanUtils.beginsWithNumber( "hi 45 something") );
		assertFalse( ScanUtils.beginsWithNumber( "045 something") );
		assertFalse( ScanUtils.beginsWithNumber( "45, something") );

	}

	@Test
	public void beginsWithDate(){

		assertTrue( ScanUtils.beginsWithDate("Oct 1 hello world"));
		assertTrue( ScanUtils.beginsWithDate("Mar 31 hello world"));
		assertTrue( ScanUtils.beginsWithDate("Jan 10"));

		assertFalse( ScanUtils.beginsWithDate("Mar 31, hello world"));
	}

	@Test
	public void getBeginDate() {

		assertEquals( "Oct 1", ScanUtils.getBeginDate("Oct 1 hello world"));
		assertEquals( "Mar 31", ScanUtils.getBeginDate("Mar 31 hello world"));
		assertEquals( "Jan 10", ScanUtils.getBeginDate("Jan 10"));

		assertNull( ScanUtils.getBeginDate("Mar 31, hello world"));

	}
}
