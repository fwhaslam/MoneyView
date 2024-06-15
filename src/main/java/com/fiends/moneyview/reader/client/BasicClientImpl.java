package com.fiends.moneyview.reader.client;

import com.fiends.moneyview.tools.ClientConfigMap;

/**
 * Client implementation with configuration values.
 */
public class BasicClientImpl extends AbstractClientImpl {

	ClientConfigMap config;
	String configKey;

	public BasicClientImpl(ClientConfigMap config,String configKey) {
		this.config = config;
		this.configKey = configKey;
	}

	/**
	 * Base-url, prepended to relative path and any query-url.
	 * @return
	 */
	protected String getUrl(){return config.getString( configKey+".url");}

	/**
	 * Format containing secret element in url-query
	 * @return
	 */
	protected String getQuery(){return config.getString( configKey+".query");}

	/**
	 * ApiKey used to identify and secure these service calls.
	 * @return
	 */
	protected String getSecret(){return config.getString( configKey+".secret");}

	/**
	 * Time in seconds to delay between API calls
	 * @return
	 */
	protected int getPause(){return config.getInteger( configKey+".pause");}

}
