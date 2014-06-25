package com.app.jest.es.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.app.jest.es.client.ESClient;
import com.app.jest.es.client.ESResource;

import junit.framework.TestCase;

public class TestGetResource extends TestCase {

	static Map<String, Integer> hosts = new HashMap<String, Integer>();
	static ESClient es = null;
	static {
		hosts.put("192.168.127.129", 9200);

		try {
			es = new ESClient(hosts, "nana2", "fastooth");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testGetResourceByName() {
		List<ESResource> resources = es.getResourceByName("刀郎", 0, 20);
		for (ESResource rs:resources){
			System.out.println(rs.toJson());
			assertNotNull(rs.getN());
		}
		
		assertNotNull(resources);
		assertTrue(resources.size() > 0);
	}

}
