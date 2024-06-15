package com.fiends.moneyview.reader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fiends.moneyview.reader.impl.AbstractStockReaderImpl;
import com.fiends.moneyview.tools.ClientConfigMap;

import static com.fiends.moneyview.tools.JsonUtils.*;

public class IEXCloudStockReader extends AbstractStockReaderImpl {

	static final String CLIENT_CONFIG_KEY = "iexcloud";

	public IEXCloudStockReader(ClientConfigMap config) {
		super(config,CLIENT_CONFIG_KEY);
	}

	public void display(){
		System.out.println("BOOK="+toPrettyString(getBook()));
//		System.out.println("BalanceSheet="+toPrettyString(getBalanceSheet()));
		System.out.println("DIVS="+toPrettyString(getDividends())) ;
	}

//======================================================================================================================

	JsonNode getBook(){
		if (symbol==null) throw new RuntimeException("Missing Symbol");
		String path = String.format("/stock/%s/book", symbol );
		return getClient().get(path);
	}

	JsonNode getQuote(){return getBook().get("quote");}

	/**
	 * ( PREMIUM ) no access ...
	 * @return
	 */
	JsonNode getBalanceSheet(){
		if (symbol==null) throw new RuntimeException("Missing Symbol");
		String path = String.format("/stock/%s/balance-sheet?period=annual", symbol );
		return getClient().get(path);
	}

	JsonNode getDividends(){
		if (symbol==null) throw new RuntimeException("Missing Symbol");
		String path = String.format("/stock/%s/dividends/1y", symbol );
		return getClient().get(path);
	}

//======================================================================================================================

	public String getName(){return getString( getQuote(), "companyName" );}

	public Double getPrice(){return getDouble( getQuote(), "previousClose" );}
	public Double getPrice52High(){return getDouble( getQuote(), "week52High");}
	public Double getPrice52Low(){return getDouble( getQuote(), "week52Low");}

	public Double getMarketCap(){return getDouble( getQuote(), "marketCap" );}

	public Double getAnnualDividend(){return 0d;}
	public Double getAnnualRevenue(){return 0d;}
	public Double getAnnualDebt(){return 0d;}
	public Double getAnnualEquity(){return 0d;}
	public Double getAnnualCapital(){return 0d;}
	public Double getAnnualAssets(){return 0d;}

	public Double getPriceToEarningsRatio(){return getDouble( getQuote(), "peRatio");}

}
