/**
 * @file ESSearchUser.java
 * @author YangQing
 * @date 2014/5/14
 * @brief Implementation for searchByName user
 */
package com.app.jest.es.client;

import com.app.jest.es.util.ESSourceMapping;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;


public class ESSearchUser {
    static Logger logger = org.slf4j.LoggerFactory.getLogger(ESSearchUser.class);
	public enum ORDER {
		BYNAME
	}
	
	static final String USER_NAME_FIELD = "name";
	static final String USER_ID_FIELD = "_id";
	static final int offset = 0;
	static final int limit = 30;
	static String USER_DEST_INDEX;
	static float BOOST_SEARCH_FACTOR = 1.0f;


	/**
	 * Search user and order it
	 * @param jc Jest Client
	 * @param text Text for searchByName
	 * @param offset Offset
	 * @param limit Limit
	 * @param order Order category
	 * @return
	 */
	public static List<ESUser> search(JestClient jc,
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
	public static List<ESUser> search(JestClient jc,
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
            e.printStackTrace();
            logger.error("Search user by name:[%s] error, detail: %s", text, e.getMessage());
		}
		return retValue;
	}

	public static List<ESUser> search(JestClient jc,
			String text) {
		return search(jc, text, offset, limit);
	}
	
	public static ESUser get(JestClient jc,
			String id) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchQuery(USER_ID_FIELD, id));
		searchSourceBuilder.from(0);
		searchSourceBuilder.size(1);
		
		List<ESUser> retValue = new ArrayList<ESUser>();
		Search search = new Search.Builder(searchSourceBuilder.toString())
		                                .addIndex(USER_DEST_INDEX)
		                                .build();


        SearchResult result = null;
        try {
            result = jc.execute(search);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Search user by name:[%s] error, detail: %s", id, e.getMessage());
        }
        return ESSourceMapping.getSourceAsObject(result, ESUser.class);

	}

}
