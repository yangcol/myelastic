package com.cto.app.elasticsearch;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

final public class ESUserContent extends ESContent {	
	ESUserContent(String id, String name) {
		super.id = id;
		super.name = name;
	}
	
	ObjectNode toJson() {
		ObjectMapper objectMapper = new ObjectMapper(); 
		ObjectNode resource = objectMapper.createObjectNode();
		resource.put("_id", id);
		resource.put("n", name);
		return resource;
	}
}