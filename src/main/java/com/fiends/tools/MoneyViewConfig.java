package com.fiends.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MoneyViewConfig extends Properties {

	static final String FILEPATH = "moneyview.cfg";

	MoneyViewConfig() {}

	static public MoneyViewConfig load() throws IOException {

		File file = new File( FILEPATH );
		InputStream stream = new FileInputStream( file );

		MoneyViewConfig config = new MoneyViewConfig();
		config.load( stream );
		return config;
	}

	public String getString(String key){return (String)get(key); }
	public Integer getInteger(String key){return Integer.parseInt( getString(key) ); }

}
