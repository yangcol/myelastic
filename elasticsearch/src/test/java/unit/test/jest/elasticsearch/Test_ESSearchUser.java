package unit.test.jest.elasticsearch;

import java.util.HashMap;
import java.util.Map;

import com.app.jest.es.client.ESClient;
import com.app.jest.es.client.ESUser;

import junit.framework.Assert;
import junit.framework.TestCase;

public class Test_ESSearchUser extends TestCase {
	
	static Map<String, Integer> hosts = new HashMap<String, Integer>();
	static ESClient es = null;
	static {
		hosts.put("ldkjserver0014", 9200);

		try {
			es = new ESClient(hosts, "nana2", "fastooth");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testSearchUserByName() {
		Assert.assertNotNull(es);
		boolean pass = false;
		try {
			es.getUserByName(null, 0, 1);
		} catch (IllegalArgumentException e) {
			pass = true;
		}
		Assert.assertTrue(pass);
		pass = false;
		
		try {
			es.getUserByName("黄", -1, 1);
		} catch (IllegalArgumentException e) {
			pass = true;
		}
		Assert.assertTrue(pass);
		pass = false;
		
		try {
			es.getUserByName("黄", 1, 0);
		} catch (IllegalArgumentException e) {
			pass = true;
		}
		Assert.assertTrue(pass);
		pass = false;
		
		
		try {
			es.getUserByName("黄", 1, 1);
		} catch (IllegalArgumentException e) {
			pass = true;
		}
		Assert.assertFalse(pass);
	}
	
	
	public void testGetUserById() {
		Assert.assertNotNull(es);
		boolean pass = false;
		try {
			es.getUserById(null);
		} catch (IllegalArgumentException e) {
			pass = true;
		}
		Assert.assertTrue(pass);
		pass = false;
		ESUser user = null;
		try {
			user = es.getUserById("10042062");
		} catch (IllegalArgumentException e) {
			pass = true;
		}
		
		Assert.assertFalse(pass);
		
		if (null == user) {
			System.out.println("User id is empty");
		}
		System.out.println(user.toJson().toString());
		Assert.assertNotNull(user);
		
	}
}
