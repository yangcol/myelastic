package com.cto.app.elasticsearch;

import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

public class ESResources {
	public static String TOTAL = "#n";
	
	public static JsonNode toJson(long total, List<ESResourceContent> rs) {
		if (total == 0) {
			//return noContent();
			return null;
		}
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode node =  objectMapper.createObjectNode();
		ArrayNode resourceOb = objectMapper.createArrayNode();
		for (ESResourceContent oneresource:rs) {
			resourceOb.add(oneresource.toJson());
		}
		node.put(TOTAL, resourceOb.size());
		node.put("data", resourceOb);
		return node;
	}
}
