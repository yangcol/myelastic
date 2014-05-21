package com.app.jest.es.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import com.app.jest.es.AbstractSearchResult;
import com.app.jest.es.ESResource;
import com.app.jest.es.ESSearchResource;
import com.app.jest.es.ESSearchUser;
import com.app.jest.es.ESUser;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;

public class ESClient {

	JestClient client;
	final String USER_DEST_INDEX;
	final String RESOURCE_DEST_INDEX;

	/**
	 * Build elascitsearch client
	 * @param hosts
	 * @param resourceIndex
	 * @param userIndex
	 * @throws Exception
	 */
	public ESClient(Map<String, Integer> hosts, String resourceIndex,
			String userIndex) throws Exception {
		LinkedHashSet<String> set = new LinkedHashSet<String>();
		for (String key : hosts.keySet()) {
			set.add("http://" + key + ":" + hosts.get(key));

		}

		if (0 == set.size()) {
			throw new Exception("ESClient configuration is now correct!");
		}

		JestClientFactory factory = new JestClientFactory();
		client = factory.getObject();
		client.setServers(set);
		USER_DEST_INDEX = userIndex;
		RESOURCE_DEST_INDEX = resourceIndex;
	}

	/**
	 * Get user by name
	 * @param name
	 * @param offset
	 * @param limit
	 * @return <code>ESUser</code>
	 */
	public List<ESUser> getUserByName(String name, int offset, int limit) {
		List<? extends AbstractSearchResult> searchusers = new ESSearchUser(
				this.USER_DEST_INDEX).search(client, name, offset, limit);
		List<ESUser> users = new ArrayList<ESUser>();
		if (null == searchusers) {
			return users;
		}
		for (AbstractSearchResult user : searchusers) {
			users.add((ESUser) user);
		}
		return users;
	}

	/**
	 * Get user by id
	 * @param id
	 * @return <code>ESUser</code>
	 */
	public ESUser getUserById(String id) {
		return (ESUser) new ESSearchUser(this.USER_DEST_INDEX).get(client, id);
	}

	
	/**
	 * Get resource by name
	 * @param name
	 * @param offset
	 * @param limit
	 * @return <code>ESResource</code> list
	 */
	public List<ESResource> getResourceByName(String name, int offset, int limit) {
		List<? extends AbstractSearchResult> searchresources = new ESSearchResource(
				this.RESOURCE_DEST_INDEX).search(client, name, offset, limit);
		List<ESResource> resources = new ArrayList<ESResource>();
		if (null == searchresources) {
			return resources;
		}
		for (AbstractSearchResult resource : searchresources) {
			resources.add((ESResource) resource);
		}
		return resources;
	}
	

	public static void main(String[] args) {
		Map<String, Integer> hosts = new HashMap<String, Integer>();
		hosts.put("ldkjserver0014", 9200);
		ESClient es = null;
		try {
			es = new ESClient(hosts, "nana2", "fastooth");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<ESUser> users = es.getUserByName("黄", 0, 20);
		for (ESUser user : users) {
			System.out.println(user.toJson().toString());
		}
		ESUser user = es.getUserById("10062950");
//		for (ESUser user : users) {
		System.out.println(user.toJson().toString());
//		}
		// es.test();
	}

}
