package unit.test.jest.elasticsearch;

import java.util.HashMap;
import java.util.Map;

import com.app.jest.es.client.ESClient;

import junit.framework.Assert;
import junit.framework.TestCase;

public class Test_ESClient extends TestCase {
	

	
	public void testCreateESClient() {
		Map<String, Integer> hosts = new HashMap<String, Integer>();
		ESClient es;
		// first param null
		boolean pass = false;
		try {
			es = new ESClient(hosts, "nana2", "fastooth");
		} catch (Exception e) {
			pass = true;
		}
		Assert.assertTrue(pass);
		pass = false;
		// second param null
		hosts.put("ldkjserver0014", 9200);
		try {
			es = new ESClient(hosts, null, "fastooth");
		} catch (Exception e) {
			pass = true;
		}
		
		Assert.assertTrue(pass);
		pass = false;
		
		// third param null
		try {
			es = new ESClient(hosts, "nana2", null);
			Assert.fail();
		} catch (Exception e) {
			pass = true;
		}
		
		Assert.assertTrue(pass);
		pass = false;
	}
}
