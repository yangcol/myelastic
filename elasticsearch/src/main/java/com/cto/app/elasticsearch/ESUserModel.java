package com.cto.app.elasticsearch;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

public class ESUserModel extends ESBaseModel{
	
	public static List<ESUserContent> findUserByName(String name) {
		SearchResponse response = ESBaseModel.esclient.searchUserByName(name, DEFAULT_PAGE_NO);
		SearchHits hits = response.getHits();
		List<ESUserContent> result = new ArrayList<ESUserContent>();
		for (SearchHit hit:hits){
			result.add(new ESUserContent(
					hit.getSource().get(ESUserSearch.USER_ID_FIELD).toString(),
					hit.getSource().get(ESUserSearch.USER_NAME_FIELD).toString()));
		}
		return result;	
	}
	
	public static List<ESUserContent> findUserByID(String id) {
		SearchResponse response = ESBaseModel.esclient.searchUserById(id, DEFAULT_PAGE_NO);
		SearchHits hits = response.getHits();
		List<ESUserContent> result = new ArrayList<ESUserContent>();
		for (SearchHit hit:hits){
			result.add(new ESUserContent(
					hit.getSource().get(ESUserSearch.USER_ID_FIELD).toString(),
					hit.getSource().get(ESUserSearch.USER_NAME_FIELD).toString()));
		}
		return result;	
	}
}
