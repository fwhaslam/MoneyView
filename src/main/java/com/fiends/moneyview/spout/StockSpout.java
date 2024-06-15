package com.fiends.moneyview.spout;

import org.apache.commons.codec.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * <pre>
 * A list of stock symbols that we want to track.
 *
 * Expected format for file is:   {header line}\n{content-rows}
 *
 *      Symbol  Type
 *      ARCC    Stock
 *      GLIFX   MUTF
 * </pre>
 */
public class StockSpout {

	static final String STOCK_KEY = "Stock";

	Set<String> symbols = new TreeSet<>();

	 StockSpout(){}

	static public StockSpout build( String filename ) {

	 	StockSpout spout = new StockSpout();

		try {
			Set<String> found = new TreeSet<>();

			File file = new File(filename);
			List<String> lines = FileUtils.readLines(file, Charsets.UTF_8);

			for (String line : lines) {
				String[] parts = StringUtils.split(line);
				if (STOCK_KEY.equals(parts[1])) {
					String symbol = StringUtils.upperCase(parts[0]);
					found.add(symbol);
				}
			}

			spout.symbols.clear();
			spout.symbols.addAll( found );
			return spout;
		}
		catch( IOException ex ){
			throw new IllegalStateException("Failed to read stock symbols",ex);
		}
	}

	public List<String> getSymbols() {
		return new ArrayList<>(symbols);
	}
}
