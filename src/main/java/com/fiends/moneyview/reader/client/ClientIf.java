package com.fiends.moneyview.reader.client;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Methods used by Pipes
 */
public interface ClientIf {

	public String getFullPath( String relativePath );
	public JsonNode get( String relativePath );

}
