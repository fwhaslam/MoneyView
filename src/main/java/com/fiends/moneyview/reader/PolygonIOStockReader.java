package com.fiends.moneyview.reader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fiends.moneyview.reader.impl.AbstractStockReaderImpl;
import com.fiends.moneyview.tools.ClientConfigMap;
import com.fiends.moneyview.tools.TimeUtils;
import org.joda.time.DateTime;

import static com.fiends.moneyview.tools.JsonUtils.getDoubleAt;
import static com.fiends.moneyview.tools.JsonUtils.getString;
import static com.fiends.moneyview.tools.JsonUtils.toPrettyString;

public class PolygonIOStockReader extends AbstractStockReaderImpl {

	static final String CLIENT_CONFIG_KEY = "polygonio";

	public PolygonIOStockReader(ClientConfigMap config) {
		super(config,CLIENT_CONFIG_KEY);
	}

	public void display(){
		System.out.println("COMPANY="+toPrettyString(getCompany()));
		System.out.println("DIVS="+toPrettyString(getDividend())) ;
		System.out.println("FINANCIALS="+toPrettyString(getFinancials())) ;
		System.out.println("TRADE="+toPrettyString(getLastTicker())) ;
	}

//======================================================================================================================

	JsonNode getCompany(){
		if (symbol==null) throw new RuntimeException("Missing Symbol");
		String path = String.format("/v1/meta/symbols/%s/company", symbol );
		return getClient().get(path);
	}

	JsonNode getDividend(){
		if (symbol==null) throw new RuntimeException("Missing Symbol");
		String path = String.format("/v2/reference/dividends/%s", symbol );
		return getClient().get(path);
	}

	JsonNode getFinancials(){
		if (symbol==null) throw new RuntimeException("Missing Symbol");
		String path = String.format("/v2/reference/financials/%s?limit=1&sort=-calendarDate", symbol );
		return getClient().get(path);
	}

	JsonNode getLastTicker(){
		if (symbol==null) throw new RuntimeException("Missing Symbol");
		String path = String.format("/v2/aggs/ticker/%s/prev", symbol );
		return getClient().get(path);
	}

//======================================================================================================================

	/*
	  "results" : [
    {
      "ticker" : "ARCC",
      "exDate" : "2020-06-12",
      "paymentDate" : "2020-06-30",
      "recordDate" : "2020-06-15",
      "amount" : 0.4
    },
	 */
	Double sumOfDividendsForLastYear( ) {

		Double sum = 0d;
		DateTime cutoff = new DateTime().minusYears(1);

		JsonNode node = getDividend();
		ArrayNode divs = (ArrayNode)node.get("results");

		for (JsonNode div : divs ) {

			String dateStr = div.get("paymentDate").asText();
			DateTime when = new DateTime( dateStr );
			if (cutoff.isAfter(when)) continue;

			Double payment = div.get("amount").asDouble();
			sum += payment;
		}

System.out.println("SUM="+sum);
		return sum;
	}

//======================================================================================================================

	public String getName(){return getString( getCompany(), "name" );}
//
	public Double getPrice(){return getDoubleAt( getLastTicker(), "/results/0/c" );}
	public Double getPrice52High(){return 0d; }
	public Double getPrice52Low(){return 0d; }
//
//	public Double getMarketCap(){return getDouble( getQuote(), "marketCap" );}

	public Double getAnnualDividend(){return sumOfDividendsForLastYear();}
	public Double getAnnualRevenue(){return 0d;}
	public Double getAnnualDebt(){return 0d;}
	public Double getAnnualEquity(){return 0d;}
	public Double getAnnualCapital(){return 0d;}
	public Double getAnnualAssets(){return 0d;}

//	public Double getPriceToEarningsRatio(){return getDouble( getQuote(), "peRatio");}

}
