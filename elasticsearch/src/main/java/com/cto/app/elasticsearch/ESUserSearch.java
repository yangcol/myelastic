package com.cto.app.elasticsearch;

import org.elasticsearch.action.search.SearchResponse;

public interface ESUserSearch extends ESBasicSearch{
	final String USER_NAME_FIELD = "name";
	final String USER_ID_FIELD = "_id";
	SearchResponse searchUserById(String id, int pageNo);
	SearchResponse searchUserById(String id, int offset, int limit);
	SearchResponse searchUserByName(String name, int pageNo);
	SearchResponse searchUserByName(String name, int offset, int limit);
}
