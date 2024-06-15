package com.fiends.moneyscan;

import com.fiends.moneyscan.reader.ChaseCreditCardReportPdfReader;
import com.fiends.moneyscan.reader.USBankSavingsReader;
import com.fiends.moneyscan.reader.USBankCreditCardReader;
import com.fiends.moneyscan.reader.USBankCheckingReader;

import com.fiends.moneyscan.reader.PdfReaderIf;
import com.fiends.tools.MoneyViewConfig;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 */
public class PdfReaderMain {

	static public void main(String ... args ) throws IOException {
		new PdfReaderMain().run();
	}

	public void run() throws IOException {

		MoneyViewConfig config = MoneyViewConfig.load();

		// Chase Bank credit card report
//		parsePDFFolder(
//				config.getString( "moneyscan.chasebank.credit.root" ),
//				new ChaseCreditCardReportPdfReader()
//		);

		// US Bank credit card report
//		parsePDFFolder(
//				config.getString( "moneyscan.usbank.credit.root" ),
//				new USBankCreditCardReader()
//		);

		// US Bank savings account report
		parsePDFFolder(
				config.getString( "moneyscan.usbank.savings.root" ),
				new USBankSavingsReader()
		);

		// US Bank credit card report
//		parsePDFFolder(
//				config.getString( "moneyscan.usbank.checking.root" ),
//				new USBankCheckingReader()
//		);

	}

	/**
	 * Use a PDF Reader class on a folder full of PDF files.
	 *
	 * @param folderPath
	 * @param reader
	 * @throws IOException
	 */
	void parsePDFFolder( String folderPath, PdfReaderIf reader, int fileLimit ) throws IOException {

		// read PDF files
		File folder = new File(folderPath);
		if (!folder.exists()) throw new RuntimeException("can't find ["+folderPath+"]");

		for (File file : folder.listFiles() ) {

//if (!file.getName().contains("20191221")) continue;
			if (file.getName().endsWith(".pdf")) {
				System.out.println("Parsing :: "+file);
				reader.readFile(file);
			}

			// limit the number of files parsed
			if (fileLimit>0 && --fileLimit==0) {
				System.out.println("File limit hit, exiting");
				break;
			}
//if (true) break;
		}

		// output as TSV
		WriteToFile( folderPath, reader );
//		System.out.println( reader.toReport() );
	}
	void parsePDFFolder( String folderPath, PdfReaderIf reader ) throws IOException {
		parsePDFFolder( folderPath, reader, 0 );
	}

	void WriteToFile(  String folderPath, PdfReaderIf reader ) {

		File folder = new File(folderPath);
		String filename = folder.getName()+".tsv";
		File fullPath = new File( folder.getParent(), filename );

		try {
			System.out.println("Writing Report to ["+fullPath+"]");
			BufferedWriter writer = new BufferedWriter(new FileWriter(fullPath, false));
			writer.append( reader.toReport() );
			writer.close();
			System.out.println("Report Complete");
		}
		catch (Exception ex ) {
			System.out.println("FAILURE: "+ex );
		}
	}
//
//	/**
//	 *
//	 * @param folderPath
//	 * @throws IOException
//	 */
//	void parseCheckingFiles( String folderPath ) throws IOException {
//
//		USBankCheckingReaderV2 reader = new USBankCheckingReaderV2();
//
//		// read PDF files
//		File folder = new File(folderPath);
//		if (!folder.exists()) throw new RuntimeException("can't find ["+folderPath+"]");
//
//		for (File file : folder.listFiles() ) {
////if (!file.getName().contains("20191212")) continue;
//			if (file.getName().endsWith(".pdf")) reader.readFile(file);
////if (true) break;
//		}
//
//		// output as TSV
//		System.out.println( reader.toReport() );
//	}
//
//	/**
//	 *
//	 * @param folderPath
//	 * @throws IOException
//	 */
//	void parseSavingsFiles( String folderPath ) throws IOException {
//
//		USBankSavingsReader reader = new USBankSavingsReader();
//
//		// read PDF files
//		File folder = new File(folderPath);
//		if (!folder.exists()) throw new RuntimeException("can't find ["+folderPath+"]");
//
//		for (File file : folder.listFiles() ) {
//			if (file.getName().endsWith(".pdf")) reader.readFile(file);
////if (true) return;
//		}
//
//		// output as TSV
//		System.out.println( reader.toReport() );
//	}
//
//	/**
//	 *
//	 * @param folderPath
//	 * @throws IOException
//	 */
//	void parseCreditFiles( String folderPath ) throws IOException {
//
//		USBankCreditCardReader reader = new USBankCreditCardReader();
//
//		// read PDF files
//		File folder = new File(folderPath);
//		if (!folder.exists()) throw new RuntimeException("can't find ["+folderPath+"]");
//
//		for (File file : folder.listFiles() ) {
//			if (file.getName().endsWith(".pdf")) reader.readFile(file);
////if (true) break;
//		}
//
//		// output as TSV
//		System.out.println( reader.toReport() );
//	}

}
