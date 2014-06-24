package com.app.jest.es.admin;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import io.searchbox.annotations.JestId;
import io.searchbox.client.JestResult;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;

import com.app.jest.es.ESClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import junit.framework.Assert;
import junit.framework.TestCase;

public class TestESAdminClient extends TestCase {
	private ESAdminClient client;
	final String tindex = "my_index";
	final String ttype = "my_type";

	public void setUp() throws Exception {
		Map<String, Integer> hosts = new HashMap<String, Integer>();
		hosts.put("192.168.127.129", 9200);
		this.client = new ESAdminClient(hosts);

		if (!this.client.indiceExists(tindex)) {
			this.client.createIndex(tindex);
		}
	}

	/**
	 * Get setting can't be used now. TODO No settings can be used
	 */
	public void testputSetting() {
		try {
			this.client.deleteIndex(tindex);
			this.client.putSetting(tindex, 1, 1);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Fail to put setting");
		}
	}

	public void testmapping() {
		XContentBuilder analyzer_ansj = null;
		try {
			analyzer_ansj = jsonBuilder().startObject().field("my_type")
					.startObject().field("properties").startObject().field("n")
					.startObject().field("index_analyzer", "index_ansj")
					.field("search_analyzer", "query_ansj")
					.field("type", "string").endObject().endObject()
					.endObject().endObject();
		} catch (IOException e1) {
			e1.printStackTrace();
			Assert.fail("Build json string failed");
		}

		try {
			this.client.putMapping("my_index", "my_type", analyzer_ansj);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}

		try {
			JestResult jr = this.client.getMapping("my_index");
			JsonObject jo = jr.getJsonObject();
			jo = jo.get("my_index").getAsJsonObject().get("mappings")
					.getAsJsonObject().get("my_type").getAsJsonObject()
					.get("properties").getAsJsonObject().get("n")
					.getAsJsonObject();
			Assert.assertEquals("\"index_ansj\"", jo.get("index_analyzer")
					.toString());
			Assert.assertEquals("\"query_ansj\"", jo.get("search_analyzer")
					.toString());
			Assert.assertEquals("\"string\"", jo.get("type").toString());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	public void testTemplate() {
		XContentBuilder template = null;
		try {
			template = jsonBuilder().startObject()
					.field("template", "mytesttemplate").field("mapings")
					.startObject().endObject().field("aliases").startObject()
					.field("aliasname").startObject().endObject().endObject()
					.endObject();
			try {
				this.client.putTemplate("my_template_1", template);
			} catch (Exception e) {
				Assert.fail("Fail action, Fail to build a template");
			}
			// System.out.println(template.string());
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail("Fail to build a template");
		}

		try {
			JestResult jr = this.client.getTemplate("my_template_1");
			JsonObject jo = jr.getJsonObject();
			jo = jo.get("my_template_1").getAsJsonObject();
			Assert.assertEquals("\"mytesttemplate\"", jo.get("template")
					.toString());
			Assert.assertEquals("{}",
					jo.get("aliases").getAsJsonObject().get("aliasname")
							.toString());
			// jo.get("template")
			// System.out.println(jo.toString());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Fail to build a template");
		}
	}

	public void testindiceExists() {
		try {
			if (!this.client.indiceExists("a_test_string")) {
				this.client.createIndex("a_test_index");
				Assert.assertTrue(this.client.indiceExists("a_test_index"));
				this.client.deleteIndex("a_test_index");
				Assert.assertFalse(this.client.indiceExists("a_test_index"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	public void testcreateIndex() {
		try {
			this.client.deleteIndex(tindex);
		} catch (Exception e1) {
			e1.printStackTrace();
			Assert.fail();
		}
		try {
			this.client.indiceExists(tindex);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Fail to put setting");
		}
	}

	public void testAnalyze() throws Exception {
		final String tstring = "你好啊朋友";
		// Use stand tokenizer to test
		String[] tokens = this.client.analyze(tindex, "standard", tstring);
		for (int i = 0; i < tokens.length; i++) {
			Assert.assertEquals(tstring.substring(i, i + 1), tokens[i]);
		}
		this.client.deleteIndex(tindex);
	}

	public void testAddDoc_String_WithOutId() {
		final String addindex = "test_add1";
		try {
			if (!this.client.indiceExists(addindex)) {
				this.client.createIndex(addindex);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			Assert.fail("Fail to create index");
		}
		
		try {
			final String idnum = "3";
			String source = jsonBuilder().startObject().field("user", "qingy")
					.endObject().string();
			this.client.addDoc(addindex, ttype, source);
			//Assert.assertEquals(true, this.client.docExists(addindex, ttype, idnum));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	public void testAddDoc_String_WithId(){
		final String addindex = "test_add";
		try {
			if (!this.client.indiceExists(addindex)) {
				this.client.createIndex(addindex);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			Assert.fail("Fail to create index");
		}

		int[] tags = { 4, 9, 5 };
		JestResult jr = null;
		final String idnum = "2";
		try {		
			String source = jsonBuilder().startObject().field("user", "kimchy")
					.field("tags", tags).endObject()
					.string();
			this.client.addDoc(addindex, ttype, idnum, source);
			
		} catch (IOException e1) {
			e1.printStackTrace();
			Assert.fail("Fail to insert a doc");
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Fail to insert a doc");
		} finally {
			try {
				this.client.deleteDocument(addindex, ttype, idnum);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Assert.fail("Fail to delete a document");
			}
		}
		
		Assert.assertEquals(true, this.client.docExists(addindex, ttype, idnum));
		System.out.println("Get document: " + jr.getJsonString());
		Assert.assertEquals("kimchy", jr.getJsonObject().get("user")
				.getAsString());
	}
	
	

	public void testAddDoc_Object() {
		final String addindex = "test_add";
		class Doc {
			String n;
			int[] tags;
			@JestId
			String id;

			public Doc(String id, int[] tags, String n) {
				this.id = id;
				this.tags = tags;
				this.n = n;
			}
		}
		int[] tags = { 5, 7, 9 };
//		Doc d = new Doc("1", tags, "nihao");
//		try {
//			if (!this.client.indiceExists(addindex)) {
//				this.client.createIndex(addindex);
//			}
//			this.client.addDoc(addindex, ttype, d);
//		} catch (Exception e1) {
//			e1.printStackTrace();
//			Assert.fail("Fail to create index");
//		}
	}

	public void testAggregations() {
		/*
		 * class doc { String[] tags;
		 * 
		 * @JestId String id; public doc(String id, String[] tags) { this.id =
		 * id; this.tags = tags; } }
		 * 
		 * //this.client.addDoc(index, type, source) TermsBuilder tb =
		 * AggregationBuilders.terms("my_aggre").field("tags").size(10); try {
		 * this.client.aggregations(tindex, ttype, tb); } catch (Exception e) {
		 * // TODO Auto-generated catch block e.printStackTrace(); }
		 */
	}
}
