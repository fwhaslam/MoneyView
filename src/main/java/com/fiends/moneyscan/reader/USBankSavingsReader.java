package com.fiends.moneyscan.reader;

import com.fiends.moneyscan.utils.ParseFileToLines;
import com.fiends.moneyscan.utils.ScanUtils;
import com.fiends.moneyview.tools.Tuple;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class USBankSavingsReader implements PdfReaderIf {

	List<Tuple> entries;
	DateTime keyDate;

	public USBankSavingsReader(){
		entries = new ArrayList<>();
	}

	static final String KEY_LINE_0 = "Statement Period:";
	static final String KEY_LINE_1 = "Number of Days in Statement Period";

	static final DateTimeFormatter KEYDATE_FORMAT = DateTimeFormat.forPattern("MMM dd, YYYY");
	static final DateTimeFormatter SHORTDATE_FORMAT = DateTimeFormat.forPattern("MMM dd");
	static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormat.forPattern("YYYY/MM/dd");

	/**
	 * Read PDF lines, pick out the entry information for later reporting.
	 * @param file
	 * @throws IOException
	 */
	public void readFile( File file ) throws IOException {

		List<String> lines = ParseFileToLines.toLines( file );

		int index = 0;
		int limit = lines.size();

//System.out.println("LIMIT="+limit);
//int count=0;
//for ( String line: lines ) {
//	++count;
//	if (line.contains(KEY_LINE_0)) System.out.println("("+count+") KEY0="+line);
//	if (line.contains(KEY_LINE_1)) System.out.println("("+count+") KEY1="+line);
//	if (line.contains(KEY_LINE_2)) System.out.println("("+count+") KEY2="+line);
//	if (line.contains("1500003349")) {
//		System.out.println("("+count+")[["+line+"]]");
//		String date = ScanUtils.getBeginDate(line);
//		String money = ScanUtils.getEndMoney(line);
//		System.out.println("  startDate="+date+"   endMoney="+money);
//	}
//}

		// find starting date of document
		index = skipToKey( index, lines, KEY_LINE_0 );
//System.out.println("Key0 Found At "+index);
		keyDate = getKeyDate( lines.get(index) );
//System.out.println(">>>>>>>>>>>>>>>>>>> KEY DATE ("+lines.get(index)+") = "+keyDate );

		// skip to entry headers
		index = skipToKey( index, lines, KEY_LINE_1);
//System.out.println("Key1 Found At "+index);

		// scan for entry rows
		for ( ; index<limit; index++ ) {

			String line = lines.get(index);
//System.out.println("LINE=["+line+"]");
//if (line.contains("1500003349")) {
//	System.out.println("Parsing::: "+line);
//}

			if (ScanUtils.beginsWithDate( line ) ) {

				Tuple tuple = parseToTuple(line);

				// try with up to five lines
				if (tuple==null && index+1<limit) {

					int max = limit-index;
					if (max>5) max = 5;

					List<String> subList = lines.subList( index, index + max );
					tuple = parseToTuple( subList );

				}

				if (tuple!=null) {
					entries.add(tuple);
System.out.println(">>>>> TUPLE["+entries.size()+"]=" + StringUtils.join(tuple, "/"));
				}
			}
		}
	}

	/**
	 * Skip lines until we see some key value
	 * @param index
	 * @param lines
	 * @param key
	 * @return
	 */
	int skipToKey( int index, List<String> lines, String key ) {
		int limit = lines.size();
		while( index<limit && !lines.get(index++).contains( key ) ) {
//System.out.println("LINE=["+lines.get(index-1)+"]");
		}
		return index;
	}

	/**
	 * Parse the starting date to DateTime object
	 * @param dateString
	 * @return
	 */
	DateTime getKeyDate( String dateString ) {
		return KEYDATE_FORMAT.parseDateTime( dateString.trim() );
	}

	/**
	 * Produce as tab separated report.
	 * @return
	 */
	public String toReport(){

		StringBuffer buf = new StringBuffer();
		buf.append("#\tdate\tamount\tdescription\n");

		for (int ix=0;ix<entries.size();ix++) {
			Tuple entry = entries.get(ix);
			buf.append(ix).append('\t');
			buf.append(entry.get(0)).append('\t');
			buf.append( asDollars(entry.get(2)) ).append('\t');
			buf.append(entry.get(1)).append('\n');
		}

		return buf.toString();
	}

	/**
	 *
	 * @return
	 */
	String asDollars( String value ) {

		boolean negative = value.endsWith("-");

		if (negative) {
			value = value.substring(0, value.length() - - "-".length() );
		}
		value = value.replaceAll( ",", "" );

		return (negative ? "-" : "" ) + value;
	}


	/**
	 * Starts with date, has dollar sign, parse from single line.
	 * @param line
	 * @return
	 */
	Tuple parseToTuple( String line ) {

		String date = ScanUtils.getBeginDate(line);
		if (date==null) return null;
		String money = ScanUtils.getEndMoney(line);
		if (money==null) return null;

//System.err.println("LINE=["+line+"]");
		// skip lines without descriptions
		if ( date.length()+money.length()+1 >= line.length() ) return null;

		String description = line.substring( date.length()+1, line.length()-(1+money.length()) );
		return new Tuple( asDateString(date), description, money );
	}

	/**
	 * Parse from multiple lines.  Expect first starts with date, and last ends with 'numeric'.
	 * @param lines
	 * @return
	 */
	Tuple parseToTuple( List<String> lines ) {

		// find money line
		int index = 1;
		int limit = lines.size();
		for ( ; index<limit; index++ ) {
			if (ScanUtils.endsWithMoney(lines.get(index))) break;
		}
		if (index==limit) return null;

		// date
		String work = lines.get(0);
		String date = ScanUtils.getBeginDate( work );
		int cut = date.length();
		lines.set( 0,  ( cut>=work.length() ? "" : work.substring( 1 + cut ) ) );

		// money
		work = lines.get(index);
		String money = ScanUtils.getEndMoney( work );
		cut = money.length();
		lines.set( index, ( cut >= work.length() ? "" : work.substring(0,work.length()-(1+cut)) ) );

		// description
		String description = mergeLinesForDescription( index, lines );

		// skip entries with 'money' descriptions
		if ( ScanUtils.beginsWithMoney(description) ) return null;

		// cleanup
		return new Tuple( asDateString(date), description, money );
	}

	/**
	 * Ensure that the date has the right year attached.
	 * @param date
	 * @return
	 */
	String asDateString( String date ) {
		DateTime when = SHORTDATE_FORMAT.parseDateTime( date );
		while (when.isBefore(keyDate)) when = when.plusYears(1);
		return when.toString( DISPLAY_FORMAT );
	}

	String mergeLinesForDescription( int index, List<String> lines ) {

		List<String> useLines = new ArrayList( lines.subList( 0, index ) );

		// move 'REF' element to end of list
		for (int ix=0;ix<useLines.size();ix++) {
			if (useLines.get(ix).startsWith("REF")) {
				String value = useLines.remove(ix);
				useLines.add( value );
				break;
			}
		}

		return StringUtils.join( useLines, " : " );
	}
}
