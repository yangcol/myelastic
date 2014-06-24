package com.app.jest.es.admin;

import java.util.HashMap;
import java.util.Map;

/**
 * Filed Property
 * @file ESFieldProperty
 * @author YangQing
 * @date 2014-6-23
 * @brief TODO
 * @version 1.0
 */
public class ESFieldProperty {
	private String field;
	private ESFieldProperty(Builder builder) {
		this.field = builder.field;
	}
	
	public static class Builder {
		private String field;
		private Map<String, String> settings = new HashMap<String, String>();
		public Builder(String field) {
			this.field = field;
		}
		
		public void putIndexQuery(String index_query) {
			settings.put("index_query", index_query);
		}
		
		public void putIndexSearch(String index_search) {
			settings.put("index_search", index_search);
		}
	}
}
