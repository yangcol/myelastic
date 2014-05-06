package com.cto.app.elasticsearch;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;


public class ESResourceModel {
	/**
	 * Find resource by name
	 * @param name
	 * @param pageNo
	 * @return
	 */
	public static List<ESResourceContent> findResouceByName(String name, int pageNo)
	{
		SearchResponse response = ESBaseModel.esclient.searchUserByName(name, pageNo);
		SearchHits hits = response.getHits();
		List<ESResourceContent> result = new ArrayList<ESResourceContent>();
		for (SearchHit hit:hits){
			if (null == hit.getSource()) {
				break;
			}
			result.add(new ESResourceContent(
					hit.getSource().get(ESUserSearch.USER_ID_FIELD).toString(),
					hit.getSource().get(ESUserSearch.USER_NAME_FIELD).toString()));
		}
		return result;	
	}
	
	/**
	 * Find resource by id
	 * @param id
	 * @param pageNo
	 * @return
	 */
	public static List<ESResourceContent> findResouceById(String id, int pageNo)
	{
		SearchResponse response = ESBaseModel.esclient.searchUserById(id, pageNo);
		SearchHits hits = response.getHits();
		List<ESResourceContent> result = new ArrayList<ESResourceContent>();
		for (SearchHit hit:hits){
			if (null == hit.getSource()) {
				break;
			}
			result.add(new ESResourceContent(
					hit.getSource().get(ESUserSearch.USER_ID_FIELD).toString(),
					hit.getSource().get(ESUserSearch.USER_NAME_FIELD).toString()));
		}
		return result;	
	}
}
