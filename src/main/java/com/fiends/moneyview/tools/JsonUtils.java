package com.fiends.moneyview.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.Separators;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;

public class JsonUtils {

	static final ObjectMapper MAPPER = new ObjectMapper(){{

//		configure( DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false );
//		configure( DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true );
//		configure( MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true );
		configure( SerializationFeature.FAIL_ON_EMPTY_BEANS, false );

		// only use single carriage return, like linux systems
		DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
		DefaultPrettyPrinter.Indenter i = new DefaultIndenter("  ", "\n");
		pp.indentArraysWith(i);
		pp.indentObjectsWith(i);
		setDefaultPrettyPrinter( pp );

	}};
	static final ObjectWriter WRITER = MAPPER.writerWithDefaultPrettyPrinter();


	static public String toPrettyString(Object value ) {
		try {
			return WRITER.writeValueAsString(value);
		}
		catch( IOException ex){
			throw new RuntimeException("Failure in toPrettyString()",ex);
		}
	}

	static public JsonNode toJsonNode(String source) throws IOException {
		return MAPPER.readTree(source);
	}

	static public String getString( JsonNode node, String key ) {
		JsonNode child = node.get(key);
		if (child==null || child.isNull())return null;
		return child.asText();
	}

	static public Double getDouble( JsonNode node, String key ) {
		JsonNode child = node.get(key);
		if (child==null || child.isNull())return null;
		return child.asDouble();
	}

	static public Double getDoubleAt( JsonNode node, String path ) {
		JsonNode child = node.at( path );
		if (child==null || child.isNull())return null;
		return child.asDouble();
	}

}

