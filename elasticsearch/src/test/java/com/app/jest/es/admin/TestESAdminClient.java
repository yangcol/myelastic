package com.app.jest.es.admin;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.searchbox.annotations.JestId;
import io.searchbox.client.JestResult;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.searchbox.core.Index;
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
    public static ESAdminClient client;
    final String tindex = "my_index";
    final String ttype = "my_type";
    static {
        Map<String, Integer> hosts = new HashMap<String, Integer>();
        hosts.put("192.168.127.129", 9200);
        client = new ESAdminClient(hosts);
    }
    public void setUp() throws Exception {

        if (!client.indiceExists(tindex)) {
            client.createIndex(tindex);
        }
    }

    /**
     * Get setting can't be used now. TODO No settings can be used
     */
    public void testputSetting() {
        try {
            client.deleteIndex(tindex);
            client.putSetting(tindex, 1, 1);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Fail to put setting");
        }
    }

    public void testMapping() {
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
            client.putMapping("my_index", "my_type", analyzer_ansj);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }

        try {
            JestResult jr = client.getMapping("my_index");
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
        XContentBuilder template;
        try {
            template = jsonBuilder().startObject()
                    .field("template", "mytesttemplate").field("mapings")
                    .startObject().endObject().field("aliases").startObject()
                    .field("aliasname").startObject().endObject().endObject()
                    .endObject();
            try {
                client.putTemplate("my_template_1", template);
            } catch (Exception e) {
                Assert.fail("Fail action, Fail to build a template");
            }
            // System.out.println(template.string());
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("Fail to build a template");
        }

        try {
            JestResult jr = client.getTemplate("my_template_1");
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
            if (!client.indiceExists("a_test_string")) {
                client.createIndex("a_test_index");
                Assert.assertTrue(client.indiceExists("a_test_index"));
                client.deleteIndex("a_test_index");
                Assert.assertFalse(client.indiceExists("a_test_index"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    public void testCreateIndex() {
        try {
            client.deleteIndex(tindex);
        } catch (Exception e1) {
            e1.printStackTrace();
            Assert.fail();
        }
        try {
            client.indiceExists(tindex);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Fail to put setting");
        }
    }

    public void testAnalyze() throws Exception {
        final String tstring = "你好啊朋友";
        // Use stand tokenizer to test
        String[] tokens = client.analyze(tindex, "standard", tstring);
        for (int i = 0; i < tokens.length; i++) {
            Assert.assertEquals(tstring.substring(i, i + 1), tokens[i]);
        }
        client.deleteIndex(tindex);
    }


    public void testAddDoc_String_WithOutId() {
        final String addindex = "test_add1";
        try {
            if (!client.indiceExists(addindex)) {
                client.createIndex(addindex);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            Assert.fail("Fail to create index");
        }

        try {
            String source = jsonBuilder().startObject().field("user", "qingy")
                    .endObject().string();
            client.addDoc(addindex, ttype, source);
            Assert.assertTrue(client.getHits(addindex, ttype, "user", "qingy") > 0);
            //System.out.println(jr.getJsonString());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    public void testAddDoc_String_WithId() {
        final String addindex = "test_add";
        try {
            if (!client.indiceExists(addindex)) {
                client.createIndex(addindex);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            Assert.fail("Fail to create index");
        }

        int[] tags = {4, 9, 5};
        JestResult jr;
        final String idnum = "2";
        try {
            String source = jsonBuilder().startObject().field("user", "kimchy")
                    .field("tags", tags).endObject()
                    .string();
            client.addDoc(addindex, ttype, idnum, source);
            Assert.assertEquals(true, client.docExists(addindex, ttype, idnum));
            jr = client.getDoc(addindex, ttype, idnum);
//            System.out.println("Get document: " + jr.getJsonString());
            Assert.assertEquals("kimchy", jr.getJsonObject().get("_source").getAsJsonObject().get("user")
                    .getAsString());
        } catch (IOException e1) {
            e1.printStackTrace();
            Assert.fail("Fail to insert a doc");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Fail to insert a doc");
        } finally {
            try {
                client.deleteIndex(addindex);
            } catch (Exception e) {
                e.printStackTrace();
                Assert.fail("Fail to delete a document");
            }
        }
    }


    public void testAddDoc() throws Exception {
        String index = "test_add";
        String type = "my_type";
        String id = "123";
        Article a = new Article();
        a.id = id;
        a.author = "yang";
        a.content = "my content";
        if (client.docExists(index, type, id)) {
            client.deleteDocument(index, type, id);
        }
        Assert.assertFalse(client.docExists(index, type, id));
        client.addDoc(index, type, a);
        Assert.assertTrue(client.docExists(index, type, id));
        client.deleteDocument(index, type, id);
    }

    public void testGetDoc() {
        final String addindex = "test_add";

        try {
            if (!client.indiceExists(addindex)) {
                client.createIndex(addindex);
            }

            client.addDoc(addindex, ttype, "1", "{\"author\":\"qing\"," +
                    "\"content\":\"this is content string\"}");
            Article a = client.getDoc(addindex, ttype, "1", Article.class);
            Assert.assertEquals(a.id, "1");
            Assert.assertEquals(a.author, "qing");
            Assert.assertEquals(a.content, "this is content string");
        } catch (Exception e1) {
            e1.printStackTrace();
            Assert.fail("Fail to create index");
        }
    }

    public void testQuery() throws Exception {
        Article a = new Article();
        a.id = "123";
        a.author ="yang";
        a.content = "my content";
        client.addDoc("test_add", "my_type", a);
        JestResult jr = client.query("test_add", "my_type", "author", "yang");
//        System.out.println(jr.getJsonString());
        Gson gson = new Gson();
        String js = jr.getJsonObject().get("took").getAsString();
//        System.out.println(js);
//        System.out.println(jr.getJsonObject().get("hits").getAsJsonObject().get("hits").toString());
        a.author = "qing";
        a.content = "constructor content";
        JsonArray hitarrays = jr.getJsonObject().get("hits").getAsJsonObject().get("hits").getAsJsonArray();
        for (JsonElement je : hitarrays) {
            a = gson.fromJson(je.getAsJsonObject().get("_source").toString(), Article.class);
//            System.out.println(a.author);
//            System.out.println(a.content);
            Assert.assertNotNull(a);
        }
    }

    public void testAggregations() {
        /*
		 * class doc { String[] tags;
		 * 
		 * @JestId String id; public doc(String id, String[] tags) { this.id =
		 * id; this.tags = tags; } }
		 * 
		 * //client.addDoc(index, type, source) TermsBuilder tb =
		 * AggregationBuilders.terms("my_aggre").field("tags").size(10); try {
		 * client.aggregations(tindex, ttype, tb); } catch (Exception e) {
		 * // TODO Auto-generated catch block e.printStackTrace(); }
		 */
    }
}
