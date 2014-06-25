package com.app.jest.es.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.app.jest.es.client.ESClient;
import com.app.jest.es.client.ESUser;

import junit.framework.TestCase;

public class TestESClient extends TestCase {
	static Map<String, Integer> hosts = new HashMap<String, Integer>();
	static ESClient es = null;
	static {
		hosts.put("ldkjserver0014", 9200);

		try {
			es = new ESClient(hosts, "nana2", "fastooth");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testGetUserByName() {
		List<ESUser> users = es.getUserByName("123", 0, 20);
		for (ESUser user : users) {
			//System.out.println(user.toJson().toString());
		}
		assertTrue(users.size() > 0);
	}
	
	public void testGetUserById() {
		ESUser user = es.getUserById("10062950");

		assertTrue(null != user);
	}

}
