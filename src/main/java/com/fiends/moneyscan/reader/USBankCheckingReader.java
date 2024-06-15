package com.fiends.moneyscan.reader;

import com.fiends.moneyscan.utils.ParseFileToLines;
import com.fiends.moneyview.tools.Tuple;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class USBankCheckingReader implements PdfReaderIf {

	List<Tuple> entries;
	DateTime keyDate;

	public USBankCheckingReader(){
		entries = new ArrayList<>();
	}

	static final DateTimeFormatter KEYDATE_FORMAT = DateTimeFormat.forPattern("MMM dd, YYYY");
	static final DateTimeFormatter SHORTDATE_FORMAT = DateTimeFormat.forPattern("MMM dd");
	static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormat.forPattern("YYYY/MM/dd");

	static final String RETURN_SYMBOL = ";";

	static final String DATE_RGX = "((Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\s(\\d{1,2}))";
	static final String MONEY_RGX = "((\\d{1,3})(,\\d{3})*\\.\\d\\d\\-?)";
	static final String CHECK_NUMBER_RGX = "([1-9]\\d*\\*?)";
	static final String CONTAINS_DATE_RGX = "^.*"+DATE_RGX+".*$";

	static final String DESCRIPTION_RGX = "(.*?)";
	static final String DATE_MONEY_RGX = "("+DATE_RGX+" "+DESCRIPTION_RGX+" ?"+MONEY_RGX+")";
	static final String REF_DATE_MONEY_RGX = "("+CHECK_NUMBER_RGX+" "+DATE_RGX+" "+DESCRIPTION_RGX+" "+MONEY_RGX+")";
	static final String ENTRY_RGX = "("+DATE_MONEY_RGX+"|"+REF_DATE_MONEY_RGX+")";

	static final String FULL_DATE_RGX = "("+DATE_RGX+", (\\d\\d\\d\\d))";
	static final String KEY_DATE_RGX = "(Statement Period:"+RETURN_SYMBOL+FULL_DATE_RGX+")";

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

			String date,description,amount;
			String number = matcher.group( 11 );

			if (number==null) {
				date = matcher.group(3);
				description = matcher.group(6);
				amount = matcher.group(7);
			}
			else {
				date = matcher.group(12);
				description = matcher.group(15)+" [check #"+number+"]";
				amount = "-"+matcher.group(16);
			}

			// cleanup description
			if (description.length()<2) continue;
			if (description.matches(CONTAINS_DATE_RGX)) continue;
			description = moveRefToEnd( description );

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

	/**
	 * When the description contains 'REF=###', remove and move to end of line.
	 *
	 * @param desc
	 * @return
	 */
	String moveRefToEnd( String desc ) {

		String [] parts = StringUtils.split( desc, RETURN_SYMBOL );
		if (parts.length<3|| !parts[1].startsWith("REF=")) return desc;

		String ref = parts[1];
		for (int ix=1;ix<parts.length-1;ix++) parts[ix] = parts[ix+1];
		parts[parts.length-1] = ref;

		return StringUtils.join( parts, RETURN_SYMBOL );

	}

}
