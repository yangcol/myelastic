package com.cto.app.elasticsearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

public class ESClient implements ESUserSearch, ESResourceSearch {
	Map<String, Integer> HOST_PORT = new HashMap<String, Integer>();
	final String IDENTIFY_CLUSTER_NAME = "cluster.name";
	final String CLUSTER_NAME;
	Client client;
	final String RESOURCE_DEST_INDEX;
	final String USER_DEST_INDEX;

	public ESClient(Map<String, Integer> hosts) {
		client = new TransportClient();
		for (String host:hosts.keySet()) {
		
			((TransportClient) client).addTransportAddress(
					new InetSocketTransportAddress(
							host,
							hosts.get(host)));
		}
		
		RESOURCE_DEST_INDEX = "zapya_api";
		CLUSTER_NAME = "elasticsearch";
		USER_DEST_INDEX = "fastooth";
	}

	/**
	 * Default constructor
	 */
	public ESClient() {
		HOST_PORT.put("192.168.127.129", 9300);
		RESOURCE_DEST_INDEX = "nana2";
		CLUSTER_NAME = "elasticsearch";
		USER_DEST_INDEX = "fastooth";
		/*
		if (null != System.getenv("Qing")) {
			HOST_PORT.put("192.168.127.129", 9300);
			// HOST_PORT.put("211.151.121.183", 9300);
			RESOURCE_DEST_INDEX = "zapya_api";
			CLUSTER_NAME = "cto_demo";
			USER_DEST_INDEX = "fastooth";
		} else {
			HOST_PORT.put("10.10.0.183", 9300);
			HOST_PORT.put("10.10.0.184", 9300);
			RESOURCE_DEST_INDEX = "zapya_api";
			CLUSTER_NAME = "cto_demo";
			// TODO
			USER_DEST_INDEX = "fastooth";
		}*/

		if (!connectCluster()) {
			throw new java.lang.Error("Can't connect to elasticsearch cluster");
		}
	}

	/**
	 * Close es client and node
	 */
	public void close() {
		client.close();
	}

	/**
	 * Connect to cluster by cluster name
	 * 
	 * @param clusterName
	 * @return
	 */
	private boolean connectCluster() {
		
		// client = node.client();
		// Settings settings =
		// ImmutableSettings.settingsBuilder().put("cluster.name",
		// "cto_demo").build();
		// .addTransportAddress(new InetSocketTransportAddress("host1", 9300))
		// .addTransportAddress(new InetSocketTransportAddress("host2", 9300));
		/*
		node = nodeBuilder().client(true).node();
		Settings settings = ImmutableSettings.settingsBuilder()
				.put(IDENTIFY_CLUSTER_NAME, CLUSTER_NAME)
				.put("client.transport.sniff", true).build();
		client = new TransportClient(settings);

		for (String key : HOST_PORT.keySet()) {
			((TransportClient) client)
					.addTransportAddress(new InetSocketTransportAddress(key,
							HOST_PORT.get(key)));
		}
*/
		client = new TransportClient()
        .addTransportAddress(new InetSocketTransportAddress("192.168.127.129", 9300));
        //.addTransportAddress(new InetSocketTransportAddress("192.168.127.139", 9300));
		if (null != client) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Basic search interface
	 * 
	 * @param text
	 * @param index
	 * @param field
	 * @param pageNo
	 * @return
	 */
	private SearchResponse search(Object text, String index, String field,
			int pageNo) {
		QueryBuilder qb = QueryBuilders.matchQuery(field, text);
		return client.prepareSearch(index).setQuery(qb)
				.setFrom(COUNT_PER_PAGE * pageNo).setSize(COUNT_PER_PAGE)
				.execute().actionGet();
	}

	private SearchResponse search(Object text, String index, String field,
			int offset, int limit) {
		QueryBuilder qb = QueryBuilders.matchQuery(field, text);
		return client.prepareSearch(index).setQuery(qb).setFrom(offset)
				.setSize(limit).execute().actionGet();
	}


	public SearchResponse searchUserById(String id, int pageNo) {
		return search(id, USER_DEST_INDEX, ESUserSearch.USER_ID_FIELD,
				pageNo);
	}


	public SearchResponse searchUserById(String id, int offset, int limit) {
		return search(id, USER_DEST_INDEX, ESUserSearch.USER_ID_FIELD,
				offset, limit);
	}


	public SearchResponse searchUserByName(String name, int pageNo) {
		return search(name, USER_DEST_INDEX,
				ESUserSearch.USER_NAME_FIELD, pageNo);
	}

	public SearchResponse searchUserByName(String name, int offset, int limit) {
		return search(name, USER_DEST_INDEX,
				ESUserSearch.USER_NAME_FIELD, offset, limit);
	}


	public SearchResponse searchResourceById(String id, int offset, int limit) {
		return search(id, RESOURCE_DEST_INDEX,
				ESResourceSearch.RESOURCE_ID_FIELD, offset, limit);
	}


	public SearchResponse searchResourceByName(String name, int offset,
			int limit) {
		return search(name, RESOURCE_DEST_INDEX,
				ESResourceSearch.RESOURCE_FIELD, offset, limit);
	}


	public SearchResponse searchResourceById(String id, int pageNo) {
		return search(id, RESOURCE_DEST_INDEX,
				ESResourceSearch.RESOURCE_ID_FIELD, pageNo);
	}


	public SearchResponse searchResourceByName(String name, int pageNo) {
		return search(name, RESOURCE_DEST_INDEX,
				ESResourceSearch.RESOURCE_FIELD, pageNo);
	}

	/**
	 * Get user by id
	 * 
	 * @param id
	 * @param pageNo
	 * @return
	 */
	public List<ESUserContent> getUserById(String id, int pageNo) {
		SearchResponse response = searchUserById(id, pageNo);
		return digUserContent(response);
	}

	public List<ESUserContent> getUserById(String id, int offset, int limit) {
		SearchResponse response = searchUserById(id, offset, limit);
		return digUserContent(response);
	}
	
	/**
	 * Get user by name
	 * 
	 * @param name
	 * @param pageNo
	 * @return
	 */
	public List<ESUserContent> getUserByName(String name, int pageNo) {
		SearchResponse response = searchUserByName(name, pageNo);
		return digUserContent(response);
	}

	public List<ESUserContent> getUserByName(String name, int offset, int limit) {
		SearchResponse response = searchUserByName(name, offset, limit);
		return digUserContent(response);
	}

	/**
	 * Get resource by name
	 * 
	 * @param name
	 * @param pageNo
	 * @return
	 */
	public List<ESResourceContent> getResourceByName(String name, int pageNo) {
		SearchResponse response = searchResourceByName(name, pageNo);
		return digResourceContent(response);
	}
	
	public List<ESResourceContent> getResourceByName(String name, int offset, int limit) {
		SearchResponse response = searchResourceByName(name, offset, limit);
		return digResourceContent(response);
	}

	/**
	 * Get resource by id
	 * 
	 * @param id
	 * @param pageNo
	 * @return
	 */
	public List<ESResourceContent> getResourceById(String id, int pageNo) {
		SearchResponse response = searchResourceById(id, pageNo);
		return digResourceContent(response);
	}
	
	public List<ESResourceContent> getResourceById(String id, int offset, int limit) {
		SearchResponse response = searchResourceById(id, offset, limit);
		return digResourceContent(response);
	}

	/**
	 * Convert search result into user content list
	 * @param response
	 * @return
	 */
	private List<ESUserContent> digUserContent(SearchResponse response) {
		SearchHits hits = response.getHits();
		List<ESUserContent> result = new ArrayList<ESUserContent>();
		for (SearchHit hit : hits) {
			String id = "";
			String name = "";
			try {
				id = hit.getSource().get(ESUserSearch.USER_ID_FIELD).toString();
			} catch (Exception e)
			{
				System.out.println(ESClient.class.getSimpleName() + "----USER_ID_FIELD Empty---- " + e.toString());
			}
			try {		
				name = hit.getSource().get(ESUserSearch.USER_NAME_FIELD).toString();
			} catch (Exception e)
			{
				System.out.println(ESClient.class.getSimpleName() + "----USER_NAME_FIELD Empty---- " + e.toString());
			}
			
			result.add(new ESUserContent(id, name));
		}
		return result;
	}

	/**
	 * Convert search result into resource content list
	 * @param response
	 * @return
	 */
	private List<ESResourceContent> digResourceContent(SearchResponse response) {
		// Check input value
		if (null == response){
			return null;
		}
		
		SearchHits hits = response.getHits();
		List<ESResourceContent> result = new ArrayList<ESResourceContent>();
		for (SearchHit hit : hits) {
			String id = "";
			String name = "";
			try {
				id = hit.getSource().get(ESResourceSearch.RESOURCE_ID_FIELD).toString();
				
			} catch (Exception e)
			{
				System.out.println(ESClient.class.getSimpleName() + "----RESOURCE_ID_FIELD Empty---- " + e.toString());
			}
			
			try {
				name = hit.getSource().get(ESResourceSearch.RESOURCE_FIELD).toString();
			} catch (Exception e)
			{
				System.out.println(ESClient.class.getSimpleName() + "----RESOURCE_FIELD Empty---- " + e.toString());
			}
			result.add(new ESResourceContent(id, name));
		}
		return result;
	}

}
