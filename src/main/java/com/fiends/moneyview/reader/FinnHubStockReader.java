package com.fiends.moneyview.reader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fiends.moneyview.reader.impl.AbstractStockReaderImpl;
import com.fiends.moneyview.tools.ClientConfigMap;
import org.joda.time.DateTime;

import static com.fiends.moneyview.tools.JsonUtils.toPrettyString;

public class FinnHubStockReader extends AbstractStockReaderImpl {

	static final String CLIENT_CONFIG_KEY = "finnhub";

	public FinnHubStockReader(ClientConfigMap config) {
		super(config,CLIENT_CONFIG_KEY);
	}

	public void display(){
		System.out.println("QUOTE="+toPrettyString(getQuote()));
//		System.out.println("COMP="+toPrettyString(getCompanyProfile()));
		System.out.println("METVAL="+toPrettyString(getMetricValuation()));
		System.out.println("METPRC="+toPrettyString(getMetricPrice()));
//		System.out.println("DIV="+toPrettyString(getDividend()));
//		System.out.println("RevenueEstimate="+toPrettyString(getRevenueEstimate()));
	}

//======================================================================================================================

	JsonNode getQuote() {
		if (symbol==null) throw new RuntimeException("Missing Symbol" );
		String path = String.format("/quote?symbol=%s", symbol );
		return getClient().get(path);
	}

	JsonNode getMetricValuation() {
		if (symbol==null) throw new RuntimeException("Missing Symbol" );
		String path = String.format("/stock/metric?symbol=%s&metric=valuation", symbol );
		return getClient().get(path);
	}

	/**
	 * Premium access or exceeds limits.
	 * @return
	 */
//	JsonNode getCompanyProfile() {
//		if (symbol==null) throw new RuntimeException("Missing Symbol" );
//		String path = String.format("/stock/profile?symbol=%s", symbol );
//		return getClient().get(path);
//	}

	JsonNode getMetricPrice() {
		if (symbol==null) throw new RuntimeException("Missing Symbol" );
		String path = String.format("/stock/metric?symbol=%s&metric=price", symbol );
		return getClient().get(path);
	}

	String getTimeFrame(){
		DateTime now = new DateTime();
		int thisYear = now.getYear();
		int nextYear = thisYear+1;
		return "from="+thisYear+"-01-01&to="+nextYear+"-01-01";
	}

//	JsonNode getDividend() {
//		if (symbol==null) throw new RuntimeException("Missing Symbol" );
//		String path = String.format("/stock/dividend?symbol=%s&"+getTimeFrame(), symbol );
//		return getClient().get(path);
//	}

//	JsonNode getRevenueEstimate(){
//		if (symbol==null) throw new RuntimeException("Missing Symbol" );
//		String path = String.format("/stock/revenue-estimate?symbol=%s&freq=annual", symbol );
//		return getClient().get(path);
//	}

//======================================================================================================================

//
//	// accessors
//	public String getName(){return null;}
//
//	public Double getPrice(){return 0d;}
//	public Double getPrice52High(){return 0d;}
//	public Double getPrice52Low(){return 0d;}
//
//	// fundamentals
//	public Double getMarketCap(){return 0d;}
//
//	public Double getAnnualDividend(){return 0d;}
//	public Double getAnnualRevenue(){return 0d;}
//	public Double getAnnualProfit(){return 0d;}
//
//	public Double getAnnualDebt(){return 0d;}
//	public Double getAnnualEquity(){return 0d;}
//	public Double getAnnualCapital(){return 0d;}
//	public Double getAnnualAssets(){return 0d;}
//
//	// ratios
//	public Double getPriceToEarningsRatio(){return 0d;}
//
//	// funds
//	public Double getFundR2(){return 0d;}
//	public Double getFundBeta(){return 0d;}
//	public Double getFundAlpha(){return 0d;}

}
