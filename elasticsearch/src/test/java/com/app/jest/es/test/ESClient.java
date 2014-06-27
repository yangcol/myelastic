package com.app.jest.es.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import com.app.jest.es.client.AbstractSearchResult;
import com.app.jest.es.client.ESResource;
import com.app.jest.es.client.ESSearchResource;
import com.app.jest.es.client.ESSearchUser;
import com.app.jest.es.client.ESUser;

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
		List<? extends AbstractSearchResult> searchusers = ESSearchUser.search(client, name, offset, limit);
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
		return ESSearchUser.get(client, id);
	}

	
	/**
	 * Get resource by name
	 * @param name
	 * @param offset
	 * @param limit
	 * @return <code>ESResource</code> list
	 */
	public List<ESResource> getResourceByName(String name, int offset, int limit) {
		List<? extends AbstractSearchResult> searchresources = ESSearchResource.searchByName(client, name, offset, limit);
		List<ESResource> resources = new ArrayList<ESResource>();
		if (null == searchresources) {
			return resources;
		}
		for (AbstractSearchResult resource : searchresources) {
			resources.add((ESResource) resource);
		}
		return resources;
	}
}
