package com.fiends.moneyview.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration properties to drive various APIs.
 * Stored in a folder at the same level as the project so it NEVER gets saved.
 *
 */
public class ClientConfigMap extends Properties {

	static final String SECURE_FOLDER_PATH = "../SecureCredentials/";

	ClientConfigMap() {}

	static public ClientConfigMap load( String filename ) throws IOException {

		File file = new File( SECURE_FOLDER_PATH + filename );
		InputStream stream = new FileInputStream( file );

		ClientConfigMap config = new ClientConfigMap();
		config.load( stream );
		return config;
	}

	public String getString(String key){return get(key).toString(); }
	public Integer getInteger(String key){return Integer.parseInt( getString(key) ); }

}
