package com.fiends.moneyview.reader.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fiends.moneyview.tools.JsonUtils;
import com.fiends.moneyview.tools.TimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * Client implementation with configuration values.
 */
abstract public class AbstractClientImpl implements ClientIf {

	abstract protected String getUrl();
	abstract protected String getQuery();
	abstract protected String getSecret();
	abstract protected int getPause();

	/**
	 * Construct the full URL for making http requests.
	 *
	 * @param relPath :: relative path, including any partial query after a question mark
	 * @return
	 */
	public String getFullPath( String relPath ) {

		String fullPath = getUrl() + relPath;
		String addToken = getQuery();
		if (StringUtils.isEmpty(addToken) ) return fullPath;

		// append token using appropriate join
		addToken = String.format( addToken, getSecret() );
		if (fullPath.contains("?")) return fullPath + "&" + addToken;
		return fullPath +"?"+ addToken;
	}

	/**
	 * Wait a little while between calls to account for service limitations.
	 */
	void doPause(){
		int seconds = getPause();
		if (seconds>0) TimeUtils.pause( seconds );
	}

	/**
	 * @param relativePath
	 * @return
	 */
	public JsonNode get(String relativePath) {

		// we have a rate limit per minute
		doPause();

		CloseableHttpClient httpClient = HttpClients.createDefault();

		try {

			String path = getFullPath( relativePath );
//			System.out.println("FULL URL = "+path);

			HttpGet request = new HttpGet(path);

			CloseableHttpResponse response = httpClient.execute(request);
			String body = EntityUtils.toString( response.getEntity() );
			int code = response.getStatusLine().getStatusCode();
			response.close();

			System.out.println("ASK["+path+"] CODE["+code+"] BODY:::\n"+body+"\n:::");
			if (code>=300) {
				throw new RuntimeException("Failed with status ["+code+"] ::: "+body);
			}
			return JsonUtils.toJsonNode(body);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		finally {
			try {httpClient.close();}
			catch (Exception ex) {ex.printStackTrace();}
		}
	}

}
