/**
 * @file ESSearchUser.java
 * @author YangQing
 * @date 2014/5/14
 * @brief ESUser entity
 */
package com.app.jest.es.client;

import java.util.Comparator;
import java.util.List;

import io.searchbox.annotations.JestId;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

public class ESUser extends AbstractSearchResult{
	public static final Comparator<ESUser> BY_NAME = new ByName();
	 
	@JestId
	String id;

	String name;
	public ESUser(String uid, String uname) {
		id = uid;
		name = uname;
	}
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	public ObjectNode toJson() {
		//ObjectMapper objectMapper = new ObjectMapper(); 
		ObjectNode resource = objectMapper.createObjectNode();
		resource.put("_id", id);
		resource.put("name", name);
		return resource;
	}
	
	public static String TOTAL = "#s";
	/**
	 * Map <code>ESUser</code> to jsonnode according to api spec
	 * @param total
	 * @param us
	 * @return
	 */
	public static JsonNode toJson(long total, List<ESUser> us) {
		if (total == 0) {
			//return noContent();
			return null;
		}
		
		ObjectNode node =  objectMapper.createObjectNode();
		ArrayNode resourceOb = objectMapper.createArrayNode();
		for (ESUser oneuser:us) {
			resourceOb.add(oneuser.toJson());
		}
		node.put(TOTAL, resourceOb.size());
		node.put("data", resourceOb);
		return node;
	}
	
	private static class ByName implements Comparator<ESUser> {
        public int compare(ESUser v, ESUser w) {
           return v.name.length() - w.name.length();
        }
    }
}

