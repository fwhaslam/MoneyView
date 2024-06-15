package com.fiends.moneyview.reader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fiends.moneyview.reader.impl.AbstractStockReaderImpl;
import com.fiends.moneyview.tools.ClientConfigMap;

import static com.fiends.moneyview.tools.JsonUtils.getDouble;
import static com.fiends.moneyview.tools.JsonUtils.getString;
import static com.fiends.moneyview.tools.JsonUtils.toPrettyString;

/**
 * Lots of numbers, but no dividend ?
 */
public class FmpCloudStockReader extends AbstractStockReaderImpl {

	static final String CLIENT_CONFIG_KEY = "fmpcloud";

	public FmpCloudStockReader(ClientConfigMap config) {
		super(config,CLIENT_CONFIG_KEY);
	}

	public void display(){
		System.out.println("QUOTE="+toPrettyString(getQuote()));
		System.out.println("PROFILE="+toPrettyString(getProfile()));
		System.out.println("INCOME="+toPrettyString(getIncome()));
		System.out.println("RATIOS="+toPrettyString(getRatios()));
		System.out.println("METRICS="+toPrettyString(getMetrics()));
	}

//======================================================================================================================

	// cost = 1 ?
	JsonNode getQuote() {
		if (symbol==null) throw new RuntimeException("Missing Symbol" );
		String path = String.format("/quote/%s", symbol );
		return getClient().get(path);
	}

	JsonNode getProfile() {
		if (symbol==null) throw new RuntimeException("Missing Symbol" );
		String path = String.format("/profile/%s", symbol );
		return getClient().get(path);
	}

	JsonNode getIncome() {
		if (symbol==null) throw new RuntimeException("Missing Symbol" );
		String path = String.format("/income-statement/%s", symbol );
		return getClient().get(path);
	}
	JsonNode getLatestIncome(){return getLatestIncome();}

	JsonNode getRatios() {
		if (symbol==null) throw new RuntimeException("Missing Symbol" );
		String path = String.format("/ratios/%s", symbol );
		return getClient().get(path);
	}
	JsonNode getLatestRatios(){return getRatios().get(0);}

	JsonNode getMetrics() {
		if (symbol==null) throw new RuntimeException("Missing Symbol" );
		String path = String.format("/key-metrics/%s", symbol );
		return getClient().get(path);
	}

//======================================================================================================================


	// accessors
	public String getName(){return getString(getQuote(),"name");}

	public Double getPrice(){return getDouble(getQuote(),"price");}
	public Double getPrice52High(){return getDouble( getQuote(), "yearHigh" );}
	public Double getPrice52Low(){return  getDouble( getQuote(), "yearLow" );}

	// fundamentals
//	public Double getMarketCap(){return getDouble( getProfile(), "mktCap"); }
	public Double getMarketCap(){
		Double value = getDouble( getQuote(), "marketCap");
		if (value==null) value = getDouble( getProfile(), "mktCap");
		return value;
	}

	public Double getAnnualDividend(){return 0d;}
	public Double getAnnualRevenue(){return getDouble( getLatestIncome(), "revenue"); }
	public Double getAnnualProfit(){return getDouble( getLatestIncome(), "grossProfit" );}

	public Double getAnnualDebt(){return 0d;}
	public Double getAnnualEquity(){return 0d;}
	public Double getAnnualCapital(){return 0d;}
	public Double getAnnualAssets(){return 0d;}

	// ratios
	public Double getPriceToEarningsRatio(){return 0d;}
	public Double getEarningsPerShare(){return 0d;}

	// funds
	public Double getFundR2(){return null;}
	public Double getFundBeta(){return getDouble( getProfile(), "beta" );}
	public Double getFundAlpha(){return null;}

}
