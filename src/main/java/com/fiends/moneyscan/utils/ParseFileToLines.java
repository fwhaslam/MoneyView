package com.fiends.moneyscan.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ParseFileToLines {

	/**
	 *
	 * @param file
	 * @return
	 * @throws IOException
	 */
	static public String toText( File file ) throws IOException {

		PDDocument document = PDDocument.load(file);

		PDFTextStripper pdfStripper = new PDFTextStripper();
		String text = pdfStripper.getText(document);
		document.close();

		return text;
	}

	/**
	 *
	 * @param file
	 * @return
	 * @throws IOException
	 */
	static public List<String> toLines( File file ) throws IOException {

		String text = toText(file);

		return Arrays.asList( StringUtils.split( text, "\r\n" ) );
	}
}
