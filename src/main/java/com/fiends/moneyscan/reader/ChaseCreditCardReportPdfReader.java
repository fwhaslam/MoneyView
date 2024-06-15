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

public class ChaseCreditCardReportPdfReader implements PdfReaderIf {

	List<Tuple> entries;
	DateTime keyDate;

	public ChaseCreditCardReportPdfReader(){
		entries = new ArrayList<>();
	}

//	"11/23 BILLYGANS ROADHOUSE VANCOUVER WA 20.65"
	static final DateTimeFormatter KEYDATE_FORMAT = DateTimeFormat.forPattern("MM/dd/YY");
	static final DateTimeFormatter SHORTDATE_FORMAT = DateTimeFormat.forPattern("MM/dd");
	static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormat.forPattern("YYYY/MM/dd");

	static final String RETURN_SYMBOL = ";";

	static final String MONTH_RGX = "(0[1-9]|1[0-2])";
	static final String DAY_RGX = "(0[1-9]|[12]\\d|30|31)";
	static final String DATE_RGX = "("+MONTH_RGX+"/"+DAY_RGX+")";

	static final String MONEY_RGX = "(\\-?(\\d{1,3})(,\\d{3})*\\.\\d\\d)";

	static final String DESCRIPTION_RGX = "([^"+RETURN_SYMBOL+"]*)";
	static final String ENTRY_RGX = "("+DATE_RGX+" "+DESCRIPTION_RGX+" "+MONEY_RGX+")";

	static final String FULL_DATE_RGX = "("+DATE_RGX+"/\\d\\d)";
	static final String KEY_DATE_RGX = "(Opening/Closing Date "+FULL_DATE_RGX+")";

	/**
	 * Read PDF lines, pick out the entry information for later reporting.
	 * @param file
	 * @throws IOException
	 */
	public void readFile( File file ) throws IOException {

		String text = ParseFileToLines.toText( file );
//System.out.println(">>>>>\n"+text+"\n>>>>>\n\n\n");

		text = text.replaceAll( "\r", "" ).replaceAll("\n", RETURN_SYMBOL );

		// find keydate
		Pattern keyDatePattern = Pattern.compile(KEY_DATE_RGX);
		Matcher keyDateMatcher = keyDatePattern.matcher( text );
		keyDateMatcher.find();
		keyDate = getKeyDate( keyDateMatcher.group(2) );

		// find entries
		Pattern pattern = Pattern.compile( ENTRY_RGX );
		Matcher matcher = pattern.matcher( text );

		while ( matcher.find() ) {

//for (int ix=0;ix<matcher.groupCount();ix++) {
//	System.out.println("["+ix+"]=("+matcher.group(ix)+")");
//}

			String date = matcher.group(2);
			String description = matcher.group(5);
			String amount = matcher.group(6);

			// add row
//System.out.println("GROUP="+matcher.group());
			entries.add( new Tuple( asDateString(date), description, asDollars(amount) ) );
		}
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
			buf.append(1+ix).append('\t');
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
			value = value.substring(0, value.length() - "-".length() );
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
