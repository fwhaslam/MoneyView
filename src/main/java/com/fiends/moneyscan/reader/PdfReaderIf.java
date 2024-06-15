package com.fiends.moneyscan.reader;

import java.io.File;
import java.io.IOException;

public interface PdfReaderIf {

	void readFile( File file ) throws IOException;

	String toReport();

}
