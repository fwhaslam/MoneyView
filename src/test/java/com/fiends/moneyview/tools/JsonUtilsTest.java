package com.fiends.moneyview.tools;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JsonUtilsTest {

	@Test
	public void toJsonNode_toPrettyString() throws Exception {

		JsonNode node = JsonUtils.toJsonNode("{\"hi\":\"there\"}");

		String results = JsonUtils.toPrettyString( node );

		assertEquals( "{\n" +
				"  \"hi\" : \"there\"\n" +
				"}", results );
	}
}
