package com.cto.app.elasticsearch;

import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

public class ESUsers {
public static String TOTAL = "#n";
	
	public static JsonNode toJson(long total, List<ESUserContent> us) {
		if (total == 0) {
			//return noContent();
			return null;
		}
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode node =  objectMapper.createObjectNode();
		ArrayNode resourceOb = objectMapper.createArrayNode();
		for (ESUserContent oneresource:us) {
			resourceOb.add(oneresource.toJson());
		}
		node.put(TOTAL, resourceOb.size());
		node.put("data", resourceOb);
		return node;
	}
}
