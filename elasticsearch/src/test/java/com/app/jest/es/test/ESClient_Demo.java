package com.app.jest.es.test;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import com.app.jest.es.ESUser;

public class ESClient_Demo {
	static final float BOOST_SEARCH_FACTOR = 0.5f;
	static int offset = 0;
	static int limit = 30;
	static final String text = "黄";
	static final String USER_DEST_INDEX = "fastooth";
	
	public static void main(String[] args) {

		String source = generateMatchQuery(text);
		System.out.println(source);
		List<ESUser> retValue = new ArrayList<ESUser>();
		Search search = new Search.Builder(source)
		                                .addIndex(USER_DEST_INDEX)
		                                .addType("all")
		                                .build();
		LinkedHashSet<String> set = new LinkedHashSet<String>();
		

		JestClientFactory factory = new JestClientFactory();
		factory.setHttpClientConfig(new HttpClientConfig
		                        .Builder("http://ldkjserver0014:9200")
		                        .multiThreaded(true)
		                        .build());
		JestClient client = factory.getObject();
		try {
			SearchResult result = client.execute(search);
			
			System.out.println(result.getJsonObject().toString());
			
		} catch (Exception e) {
			System.out.println("Something wrong with elasticsearch");
			//e.printStackTrace();
		}
	}
	
	static String generatePrefixQuery(String text) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.prefixQuery("name", text));
		searchSourceBuilder.from(0);
		searchSourceBuilder.size(10);
		return searchSourceBuilder.toString();
	}
	
	static String generateMatchQuery(String text) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchQuery("name", text)
				.operator(MatchQueryBuilder.Operator.AND)
				.prefixLength(text.length())
				.maxExpansions(text.length())
				.cutoffFrequency(0)
				.boost(text.length() * BOOST_SEARCH_FACTOR));
		searchSourceBuilder.from(offset);
		searchSourceBuilder.size(limit);
		return searchSourceBuilder.toString();
	}
	
	static String generateWildChardQuery(String text) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.wildcardQuery("name", text + "*")
				.boost(text.length() * BOOST_SEARCH_FACTOR));
		searchSourceBuilder.from(offset);
		searchSourceBuilder.size(limit);
		return searchSourceBuilder.toString();
	}
	
	static String generateFunzzyQuery(String text) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.fuzzyQuery("name", text )
				.boost(text.length() * BOOST_SEARCH_FACTOR));
		searchSourceBuilder.from(offset);
		searchSourceBuilder.size(limit);
		return searchSourceBuilder.toString();
	}
}
