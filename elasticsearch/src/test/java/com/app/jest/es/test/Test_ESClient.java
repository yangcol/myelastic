package com.app.jest.es.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.app.jest.es.client.ESClient;
import com.app.jest.es.client.ESUser;

public class Test_ESClient {
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
	
	public static void main(String[] args) {
		List<ESUser> users = es.getUserByName("黄", 0, 10);
		for(ESUser user:users) {
			System.out.println(user.toJson().toString());
		}
		System.out.println("结束");
	}
}
