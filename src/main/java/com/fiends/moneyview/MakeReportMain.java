package com.fiends.moneyview;

import com.fiends.moneyview.reader.PolygonIOStockReader;
import com.fiends.moneyview.reader.impl.StockReaderIf;
import com.fiends.moneyview.spout.StockSpout;
import com.fiends.moneyview.tools.ClientConfigMap;
import com.fiends.moneyview.tools.Tuple;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class MakeReportMain {

	static final String CONFIG_PATH = "money-view.cfg";

	static public void main(String[] args) throws Exception {
		ClientConfigMap config = ClientConfigMap.load(CONFIG_PATH);
		new MakeReportMain(config).run();
	}

//=======================================================================================================================

//	static final String STOCK_SYMBOL_FILE = "../docs/MyFi/StockKeysMgd.csv";
	static final String STOCK_SYMBOL_FILE = "docs/MyFi/StockKeysHeld.csv";

	StockReaderIf reader;

	MakeReportMain(ClientConfigMap config) {
		this.reader = new PolygonIOStockReader(config);
//		this.reader = new IEXCloudPipe( config );
//		this.reader = new FinnHubStockReader( config );
//		this.reader = new FmpCloudPipe( config );
	}

	void run() {
		try {
			StockSpout spout = StockSpout.build( STOCK_SYMBOL_FILE );
			List<String> symbols = spout.getSymbols();

			List<Tuple> report = new ArrayList<>();
			report.add( getHeader() );

			for (String symbol : symbols ) {
				Tuple row = findFields( symbol );
				report.add( row );
			}

			System.out.println("REPORT");
			List<String> rows = new ArrayList<>();
			for (Tuple row : report ) rows.add( row.toTabString() );
			String display = StringUtils.join( rows, "\n" );

			System.out.println( display );

		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	Tuple getHeader(){
		return new Tuple(
				"code","name","price","w52hi","w52lo",
				"dividend","p2e","p2a","p2b"
		);
	}

	/**
	 * Extract report fields using reader.
	 * @param symbol
	 * @return
	 */
	Tuple findFields( String symbol ) {

		reader.setSymbol( symbol );

		Tuple row = new Tuple();
		row.append( reader.getSymbol() );
		row.append( reader.getName() );
		row.append( reader.getPrice() );
		row.append( reader.getPrice52High() );
		row.append( reader.getPrice52Low() );
		row.append( reader.getAnnualDividend() );

		return row;
	}

}
