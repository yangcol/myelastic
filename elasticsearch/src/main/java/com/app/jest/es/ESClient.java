/**
 * @file ESClient.java
 * @author YangQing
 * @date 2014/5/14
 * @brief Elastic search client
 */
package com.app.jest.es;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;

public class ESClient {

	JestClient client;
	ESSearchUser searchUser;
	ESSearchResource searchResource;
	
	/**
	 * Build elastic search client
	 * @param hosts
	 * @param resourceIndex
	 * @param userIndex
	 * @throws Exception if configuration is not correct
	 */
	public ESClient(Map<String, Integer> hosts, String resourceIndex,
			String userIndex) throws Exception {
		if (null == hosts) {
			throw new Exception("hosts shouldn't be null");
		}
		
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
		searchUser = new ESSearchUser(userIndex);
		searchResource = new ESSearchResource(resourceIndex);
	}

	/**
	 * Get user by name
	 * @param name Name
	 * @param offset Offset
	 * @param limit Required list size
	 * @return <code>ESUser</code> list
	 */
	public List<ESUser> getUserByName(String name, int offset, int limit) {
		return searchUser.search(client, name, offset, limit
						,ESSearchUser.ORDER.BYNAME);
	}

	/**
	 * Get user by id
	 * @param id Id
	 * @return <code>ESUser</code> Null if not found.
	 */
	public ESUser getUserById(String id) {
		return searchUser.get(client, id);
	}

	
	/**
	 * Get resource by name
	 * @param name Name
	 * @param offset Offset
	 * @param limit Required list size
	 * @return <code>ESResource</code> list
	 */
	public List<ESResource> getResourceByName(String name, int offset, int limit) {
		return searchResource.search(client, name, offset, limit);
	}
}
