/**
 * @file ESSearchResource.java
 * @author YangQing
 * @date 2014/5/14
 * @brief Implementation for search resource
 */
package com.app.jest.es;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Get;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

/**
 * Search resource
 * @author yangq
 *
 */
public class ESSearchResource implements ESBasicSearch{
	final String RESOURCE_NAME_FIELD = "n";
	final String RESOURCE_ID_FIELD = "_id";
	final int offset = 0;
	final int limit = 30;
	final String RESOURCE_DEST_INDEX;
	
	
	public ESSearchResource(String resourceIndex) {
		RESOURCE_DEST_INDEX = resourceIndex;
	}
	
	/**
	 * 
	 */
	public List<ESResource> search(JestClient jc,
			String text, int offset, int limit) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchQuery(RESOURCE_NAME_FIELD, text));
		searchSourceBuilder.from(offset);
		searchSourceBuilder.size(limit);
		
		List<ESResource> retValue = new ArrayList<ESResource>();
		Search search = new Search.Builder(searchSourceBuilder.toString())
		                                .addIndex(RESOURCE_DEST_INDEX)
		                                .build();
		
		try {
			SearchResult result = jc.execute(search);
			if (!result.isSucceeded()){
				throw new Exception("Fail to find resources");
			}
			List<SearchResult.Hit<ESResource, Void>> hits = result
					.getHits(ESResource.class);
			for (SearchResult.Hit<ESResource, Void> hit : hits) {
				ESResource u = hit.source;
				retValue.add(u);
			}
			
		} catch (Exception e) {
			System.out.println("Something wrong with elasticsearch");
			e.printStackTrace();
		}
		return retValue;
	}
	
	
	public List<ESResource> search(JestClient jc,
			String text) {
		return search(jc, text, offset, limit);
	}


	public ESResource get(JestClient jc, String id) {
		Get get = new Get.Builder(this.RESOURCE_DEST_INDEX, id).build();
		ESResource rs = null;
		try {
			 JestResult result = jc.execute(get);
			 rs = result.getSourceAsObject(ESResource.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rs;
	}
}
