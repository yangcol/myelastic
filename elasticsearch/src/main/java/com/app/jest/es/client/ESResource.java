/**
 * @file ESResource.java
 * @author YangQing
 * @date 2014/5/14
 * @brief ESResource entity
 */
package com.app.jest.es.client;

import java.util.Comparator;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;


import io.searchbox.annotations.JestId;

public class ESResource extends AbstractSearchResult{
	public static final Comparator<ESResource> BY_NAME = new ByName();
	
	@JestId
	public String id;

	public String n;

    public int[] tags;

	public String getN() {
		return n;
	}

	public void setN(String n) {
		this.n = n;
	}

	public ESResource(String rid, String rname, int[] tags) {
		this.id = rid;
		this.n = rname;
        this.tags = tags;
	}
	
	public ObjectNode toJson() {
		ObjectMapper objectMapper = new ObjectMapper(); 
		ObjectNode resource = objectMapper.createObjectNode();
		resource.put("_id", id);
		resource.put("n", n);
		return resource;
	}
	
	public static String TOTAL = "#s";
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	public static JsonNode toJson(long total, List<ESResource> rs) {
		if (total == 0) {
			return null;
		}
		
		ObjectNode node =  objectMapper.createObjectNode();
		ArrayNode resourceOb = objectMapper.createArrayNode();
		for (ESResource oneresource:rs) {
			resourceOb.add(oneresource.toJson());
		}
		node.put(TOTAL, resourceOb.size());
		node.put("data", resourceOb);
		return node;
	}
	
	private static class ByName implements Comparator<ESResource> {
        public int compare(ESResource v, ESResource w) {
           return v.n.length() - w.n.length();
        }
    }
}
