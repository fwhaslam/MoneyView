package com.fiends.moneyview;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

public class SamplerMain {

	static public void main(String ... args)  throws Exception  {
		doSomething();
	}

	static void doSomething() throws Exception {

		HttpResponse<String> response = Unirest.get("https://yahoo-finance-low-latency.p.rapidapi.com/v8/finance/chart/AAPL?comparisons=MSFT%2C%5EVIX")
				.header("x-rapidapi-key", "208e706a67msh7603b771801b72dp1a43aejsn783384664e30")
				.header("x-rapidapi-host", "yahoo-finance-low-latency.p.rapidapi.com")
				.asString();

		System.out.println("RESPONSE-"+response.getBody());

	}
}
