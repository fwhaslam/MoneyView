package com.fiends.moneyview.reader.impl;

import com.fiends.moneyview.reader.client.BasicClientImpl;
import com.fiends.moneyview.reader.client.CachingClient;
import com.fiends.moneyview.reader.client.ClientIf;
import com.fiends.moneyview.tools.ClientConfigMap;

abstract public class AbstractStockReaderImpl implements StockReaderIf {

	protected ClientIf client;
	protected String symbol = null;

	public AbstractStockReaderImpl(ClientConfigMap config, String clientConfigKey) {
		client = new BasicClientImpl( config, clientConfigKey );
		client = new CachingClient( client );
	}

	/**
	 * For testing, displays a sample of objects.
	 * @return
	 */
	public void display(){}

	// control
	protected ClientIf getClient(){return client;}
	public void setSymbol(String symbol){this.symbol=symbol;}
	public String getSymbol(){return symbol;}

	// accessors
	public String getName(){return null;}

	public Double getPrice(){return null;}
	public Double getPrice52High(){return null;}
	public Double getPrice52Low(){return null;}

	// fundamentals
	public Double getMarketCap(){return null;}
	public Double getShares(){
		Double cap = getMarketCap();
		Double price = getPrice();
		if (cap==null || price==null) return null;
		return cap / price;
	}

	public Double getAnnualDividend(){return null;}
	public Double getAnnualRevenue(){return null;}
	public Double getAnnualProfit(){return null;}

	public Double getAnnualDebt(){return null;}
	public Double getAnnualEquity(){return null;}
	public Double getAnnualCapital(){return null;}
	public Double getAnnualAssets(){return null;}

	// ratios
	public Double getPriceToEarningsRatio(){return null;}
	public Double getEarningsPerShare(){return null;}

	// funds
	public Double getFundR2(){return null;}
	public Double getFundBeta(){return null;}
	public Double getFundAlpha(){return null;}

}
