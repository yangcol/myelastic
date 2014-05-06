package com.cto.app.elasticsearch;

import org.elasticsearch.action.search.SearchResponse;

public interface ESResourceSearch extends ESBasicSearch{
	final String RESOURCE_FIELD = "n";
	final String RESOURCE_ID_FIELD = "_id";
	SearchResponse searchResourceById(String id, int pageNo);
	SearchResponse searchResourceByName(String name, int pageNo);
	SearchResponse searchResourceById(String id, int offset, int limit);
	SearchResponse searchResourceByName(String name, int offset, int limit);
}
