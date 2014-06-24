package com.app.jest.es.test;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import com.app.jest.es.ESClient;
import com.app.jest.es.ESResource;
import com.app.jest.es.ESUser;

public class ESClient_Recommend {
	static Map<String, Integer> hosts = new HashMap<String, Integer>();
	static ESClient es = null;
	static {
		hosts.put("ldkjserver0015", 9200);

		try {
			es = new ESClient(hosts, "videos", "fastooth");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {

		// System.out.println(source);
		
		System.out.println("Input Resource Name:");
		String text = "《金玉良缘》15集预告片—在线播放—《金玉良缘》—电视剧—优酷网，视频高清在线观看";
		List<ESResource> results = es.getResourceByName(text, 0, 15);
		for (ESResource result:results) {
			System.out.println(result.getN());
		}
	}

	
	static String getRegularString(String text) {
		String resultString = null;
		try {
			resultString = text.replaceAll(
					"(?sm)第(\\[【\\s)?(\\d{0,3}|\\W{0,3})(】\\]\\s)?集", "")
					.replaceAll("在线播放", "")
					.replaceAll("优酷网", "")
					.replaceAll("在线观看", "")
					.replaceAll("视频高清", "");
		} catch (PatternSyntaxException ex) {
			// Syntax error in the regular expression
		} catch (IllegalArgumentException ex) {
			// Syntax error in the replacement text (unescaped $ signs?)
		} catch (IndexOutOfBoundsException ex) {
			// Non-existent backreference used the replacement text
		}
		return resultString;
	}
	
	static String generateQueryString(String text) {
		String resultString = null;
		try {
			resultString = text.replaceAll(
					"(?sm)第(\\[【\\s)?(\\d{0,3}|\\W{0,3})(】\\]\\s)?集", "");
		} catch (PatternSyntaxException ex) {
			// Syntax error in the regular expression
		} catch (IllegalArgumentException ex) {
			// Syntax error in the replacement text (unescaped $ signs?)
		} catch (IndexOutOfBoundsException ex) {
			// Non-existent backreference used the replacement text
		}

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders
				.matchQuery("n", resultString).operator(
						MatchQueryBuilder.Operator.AND));
		// .prefixLength(text.length())
		// .maxExpansions(text.length())
		// .cutoffFrequency(0)
		// .slop(0)
		// .boost(text.length() * BOOST_SEARCH_FACTOR));
		searchSourceBuilder.from(0);
		searchSourceBuilder.size(15);
		return searchSourceBuilder.toString();

		// return resultString;
	}
}
