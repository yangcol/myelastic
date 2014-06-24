/**
 * @file ESSearchUser.java
 * @author YangQing
 * @date 2014/5/14
 * @brief Implementation for search user
 */
package com.app.jest.es;

import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;


public class ESSearchUser implements ESBasicSearch{
	
	public enum ORDER {
		BYNAME
	}
	
	final String USER_NAME_FIELD = "name";
	final String USER_ID_FIELD = "_id";
	final int offset = 0;
	final int limit = 30;
	final String USER_DEST_INDEX;
	final float BOOST_SEARCH_FACTOR = 1.0f;
	public ESSearchUser(String userIndex) {
		USER_DEST_INDEX = userIndex;
	}

	public int getOffset() {
		return this.offset;
	}
	
	public int getLimit() {
		return this.limit;
	}

	/**
	 * Search user and order it
	 * @param jc Jest Client
	 * @param text Text for search
	 * @param offset Offset
	 * @param limit Limit
	 * @param order Order category
	 * @return
	 */
	public List<ESUser> search(JestClient jc,
			String text,
			int offset,
			int limit, ORDER order) {
		List<ESUser> results = search(jc,text, offset, limit);
		switch (order) {
		case BYNAME:
			Collections.sort(results, ESUser.BY_NAME);
			break;
		default:
			break;
		}
		
		return results;
	}
	
	/**
	 * Search Users
	 */
	public List<ESUser> search(JestClient jc,
			String text,
			int offset,
			int limit) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchQuery(USER_NAME_FIELD, text)
				.operator(MatchQueryBuilder.Operator.AND)
				.boost(text.length() * BOOST_SEARCH_FACTOR));
		searchSourceBuilder.from(offset);
		searchSourceBuilder.size(limit);
		System.out.println(searchSourceBuilder.toString());
		List<ESUser> retValue = new ArrayList<ESUser>();
		Search search = new Search.Builder(searchSourceBuilder.toString())
		                                .addIndex(USER_DEST_INDEX)
		                                .build();
		
		try {
			SearchResult result = jc.execute(search);
			List<SearchResult.Hit<ESUser, Void>> hits = result
					.getHits(ESUser.class);
			for (SearchResult.Hit<ESUser, Void> hit : hits) {
				ESUser u = hit.source;
				retValue.add(u);
			}
		} catch (Exception e) {
			System.out.println("Something wrong with elasticsearch");
			e.printStackTrace();
		}
		return retValue;
	}

	public List<ESUser> search(JestClient jc,
			String text) {
		return search(jc, text, this.offset, this.limit);
	}
	
	public ESUser get(JestClient jc,
			String id) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchQuery(USER_ID_FIELD, id));
		searchSourceBuilder.from(0);
		searchSourceBuilder.size(1);
		
		List<ESUser> retValue = new ArrayList<ESUser>();
		Search search = new Search.Builder(searchSourceBuilder.toString())
		                                .addIndex(USER_DEST_INDEX)
		                                .build();
		
		try {
			SearchResult result = jc.execute(search);
			List<SearchResult.Hit<ESUser, Void>> hits = result
					.getHits(ESUser.class);
			for (SearchResult.Hit<ESUser, Void> hit : hits) {
				ESUser u = hit.source;
				retValue.add(u);
			}
		} catch (Exception e) {
			System.out.println("Something wrong with elasticsearch");
			e.printStackTrace();
		}
		
		if (retValue.isEmpty()) {
			return null;
		} else {
			return retValue.get(0);
		}
	}

}
