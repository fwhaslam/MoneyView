package com.fiends.moneyscan.reader;

import com.fiends.moneyscan.utils.ParseFileToLines;
import com.fiends.moneyview.tools.Tuple;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class USBankCreditCardReader implements PdfReaderIf {

	List<Tuple> entries;
	DateTime keyDate;

	public USBankCreditCardReader(){
		entries = new ArrayList<>();
	}

	static final String KEY_LINE_0 = "Open Date: ";

	static final DateTimeFormatter KEYDATE_FORMAT = DateTimeFormat.forPattern("dd/MM/yyyy");
	static final DateTimeFormatter SHORTDATE_FORMAT = DateTimeFormat.forPattern("MM/dd");
	static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormat.forPattern("YYYY/MM/dd");

	static String ENTRY_REGEX =
			"^(\\d\\d/\\d\\d) (\\d\\d/\\d\\d) (\\d\\d\\d\\d) (.*) \\$((\\d{1,3})(,\\d{3})*\\.\\d\\d(CR)?)$";
	static String KEYDATE_REGEX =
			"^.*(\\d\\d/\\d\\d/\\d\\d\\d\\d).*$";

	/**
	 * Read PDF lines, pick out the entry information for later reporting.
	 * @param file
	 * @throws IOException
	 */
	public void readFile( File file ) throws IOException {

		List<String> lines = ParseFileToLines.toLines( file );
		Pattern pattern = Pattern.compile( ENTRY_REGEX );

		int index = 0;
		int limit = lines.size();

		// find starting date of document
		index = skipToKey( index, lines, KEY_LINE_0 );
//System.out.println(">>>>>>>>>>>>>>>>>>> "+lines.get(index) );

		keyDate = getKeyDate( lines.get(index) );
//System.out.println(">>>>>>>>>>>>>>>>>>> KEY DATE ("+lines.get(index)+") = "+keyDate );

		// skip to entry headers

		// scan for entry rows
		while ( index+5<limit ) {

			Tuple tuple = null;
			String line = lines.get(index++);

			Matcher matcher = pattern.matcher( line );
//System.out.println("LINE=["+line+"]");
			if (!matcher.matches()) continue;

			String date = asDateString( matcher.group(1) );
			String description = matcher.group(4) + "("+matcher.group(3)+")";
			String amount = asDollars( matcher.group(5) );

			// cleanup
			description = description.trim().replaceAll(" +", " ");
			tuple = new Tuple( date, description, amount );

			// cleanup
			if (tuple!=null) {
				entries.add(tuple);
//System.out.println(">>>>> TUPLE["+entries.size()+"]=" + StringUtils.join(tuple, "/"));
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
		while( index<limit && !lines.get(index).contains( key ) ) {
//System.out.println("LINE=["+lines.get(index-1)+"]");
			index++;
		}
		return index;
	}

	/**
	 * Parse the starting date to DateTime object
	 * @param line
	 * @return
	 */
	DateTime getKeyDate( String line ) {
//System.out.println("LINE=["+line+"]");
		int clip = KEY_LINE_0.length();
		String date = line.substring( clip, clip+10 );
		return KEYDATE_FORMAT.parseDateTime( date );
//		Pattern pattern = Pattern.compile( KEYDATE_REGEX );
//		Matcher matcher = pattern.matcher( line );
//		return KEYDATE_FORMAT.parseDateTime( matcher.group(1) );
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

		boolean negative = value.endsWith("CR");

		if (negative) {
			value = value.substring(0, value.length() - "CR".length() );
		}
		value = value.replaceAll( ",", "" );

		return (negative ? "-" : "" ) + value;
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

}
