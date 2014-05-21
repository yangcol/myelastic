package unit.test.jest.elasticsearch;

import java.util.HashMap;
import java.util.Map;

import com.app.jest.es.ESClient;

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
		try {
			es.getUserById("123");
		} catch (IllegalArgumentException e) {
			pass = true;
		}
		
		Assert.assertFalse(pass);
	}
}
