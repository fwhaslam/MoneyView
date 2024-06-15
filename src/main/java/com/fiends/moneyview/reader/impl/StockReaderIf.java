package com.fiends.moneyview.reader.impl;

public interface StockReaderIf {

	// control
	void setSymbol(String symbol);
	void display();

	// accessors
	String getSymbol();
	String getName();

	Double getPrice();
	Double getPrice52High();
	Double getPrice52Low();

	Double getMarketCap();
	Double getShares();

	Double getAnnualDividend();
	Double getAnnualRevenue();
	Double getAnnualProfit();

	Double getAnnualDebt();
	Double getAnnualEquity();
	Double getAnnualCapital();
	Double getAnnualAssets();

	Double getPriceToEarningsRatio();
	Double getEarningsPerShare();

	Double getFundR2();
	Double getFundBeta();
	Double getFundAlpha();

}
