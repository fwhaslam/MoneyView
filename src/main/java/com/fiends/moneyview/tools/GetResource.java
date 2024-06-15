package com.fiends.moneyview.tools;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class GetResource {

	static public String toString( String path ) throws IOException {
		return IOUtils.toString( toStream(path), "UTF-8" );
	}

	static public InputStream toStream(String path ) throws IOException {
		path = path.replace( '/', File.separatorChar );
		URL url = GetResource.class.getClassLoader().getResource(path);
		return url.openStream();
	}

}
