/**
 * @file ESClient.java
 * @author YangQing
 * @date 2014/5/14
 * @brief Elastic searchByName client
 */
package com.app.jest.es.client;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;

public class ESClient {

	JestClient client;
	
	/**
	 * Build elastic searchByName client
	 * @param hosts
	 * @param resourceIndex
	 * @param userIndex
	 * @throws Exception if configuration is not correct
	 */
	public ESClient(Map<String, Integer> hosts, String resourceIndex,
			String userIndex) throws IllegalArgumentException {
		if (null == hosts
				|| null == resourceIndex
				|| null == userIndex) {
			throw new IllegalArgumentException("Input Params should not be null");
		}
		
		LinkedHashSet<String> set = new LinkedHashSet<String>();
		for (String key : hosts.keySet()) {
			set.add("http://" + key + ":" + hosts.get(key));
		}

		if (0 == set.size()) {
			throw new IllegalArgumentException("ESClient configuration is not correct!");
		}

		JestClientFactory factory = new JestClientFactory();
		client = factory.getObject();
		client.setServers(set);
		ESSearchUser.USER_DEST_INDEX = userIndex;
        ESSearchResource.RESOURCE_DEST_INDEX = resourceIndex;
	}

	/**
	 * Get user by name
	 * @param name Name
	 * @param offset Offset
	 * @param limit Required list size
	 * @return <code>ESUser</code> list
	 * @throws IllegalArgumentException
	 */
	public List<ESUser> getUserByName(String name, int offset, int limit) throws IllegalArgumentException {
		if (null == name
				|| offset < 0
				|| limit <= 0) {
			throw new IllegalArgumentException("Invalid argument");
		}
		
		return ESSearchUser.search(client, name, offset, limit
						,ESSearchUser.ORDER.BYNAME);
	}

	/**
	 * Get user by id
	 * @param id Id
	 * @return <code>ESUser</code> Null if not found.
	 */
	public ESUser getUserById(String id) throws IllegalArgumentException {
		if (null == id) {
			throw new IllegalArgumentException("Invalid argument");
		}
		return ESSearchUser.get(client, id);
	}

	
	/**
	 * Get resource by name
	 * @param name Name
	 * @param offset Offset
	 * @param limit Required list size
	 * @return <code>ESResource</code> list
	 * @warn Not checked yet
	 */
	public List<ESResource> getResourceByName(String name, int offset, int limit) throws IllegalArgumentException {
		if (null == name
				|| offset < 0
				|| limit <= 0) {
			throw new IllegalArgumentException("Invalid argument");
		}
		return ESSearchResource.searchByName(client, name, offset, limit);
	}


    /**
     * Get resource by tag
     * @param tag
     * @param offset
     * @param limit
     * @return
     */
    public List<ESResource> getResourceByTag(int[] tag, int offset, int limit) {
        if (null == tag
                || offset < 0
                || limit <= 0) {
            throw new IllegalArgumentException("Invalid argument");
        }
        return ESSearchResource.searchByTag(client, tag, offset, limit);
    }

    /**
     * Return jest client
     * @return
     */
    public JestClient getClient() {
        return this.client;
    }
}
