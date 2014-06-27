package com.app.jest.es.admin;

import com.app.jest.es.util.ESTermAggregationItem;
import com.google.gson.JsonObject;
import io.searchbox.client.JestResult;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

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
            analyzer_ansj = jsonBuilder().startObject().field(ttype)
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
            client.putMapping("my_index", ttype, analyzer_ansj);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }

        try {
            JestResult jr = client.getMapping("my_index");
            JsonObject jo = jr.getJsonObject();
            jo = jo.get("my_index").getAsJsonObject().get("mappings")
                    .getAsJsonObject().get(ttype).getAsJsonObject()
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


    public void testAddDoc_String_WithOutId() throws Exception {
        String source = jsonBuilder().startObject().field("user", "qingy")
                .endObject().string();
        client.addDoc(tindex, ttype, source);
        client.flushIndex();
        JestResult jr = client.query(tindex, ttype, "user", "qingy");
        //System.out.println(jr.getJsonString());
        Assert.assertTrue(client.getHits(jr) > 0);
    }

    public void testAddDoc_String_WithId() {

        int[] tags = {4, 9, 5};
        JestResult jr;
        final String idnum = "2";
        try {
            String source = jsonBuilder().startObject().field("user", "kimchy")
                    .field("tags", tags).endObject()
                    .string();
            client.addDoc(tindex, ttype, idnum, source);
            client.flushIndex();
            Assert.assertEquals(true, client.docExists(tindex, ttype, idnum));
            jr = client.getDoc(tindex, ttype, idnum);
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
                client.deleteIndex(tindex);
            } catch (Exception e) {
                e.printStackTrace();
                Assert.fail("Fail to delete a document");
            }
        }
    }


    public void testAddDoc() throws Exception {
        String id = "123";
        Article a = new Article();
        a.id = id;
        a.author = "yang";
        a.content = "my content";
        if (client.docExists(tindex, ttype, id)) {
            client.deleteDocument(tindex, ttype, id);
        }
        Assert.assertFalse(client.docExists(tindex, ttype, id));
        client.addDoc(tindex, ttype, a);
        client.flushIndex();
        Assert.assertTrue(client.docExists(tindex, ttype, id));
        client.deleteDocument(tindex, ttype, id);
    }

    public void testGetDoc() throws Exception {

        client.addDoc(tindex, ttype, "1", "{\"author\":\"qing\"," +
                "\"content\":\"this is content string\"}");
        Article a = client.getDoc(tindex, ttype, "1", Article.class);
        Assert.assertEquals(a.id, "1");
        Assert.assertEquals(a.author, "qing");
        Assert.assertEquals(a.content, "this is content string");
    }

    public void testQuery() throws Exception {
        Article a = new Article();
        a.id = "1234";
        a.author = "neverland";
        a.content = "my content";
        client.addDoc(tindex, ttype, a);
        JestResult jr = client.query(tindex, ttype, "author", a.author);
        Assert.assertTrue("Can't find resource", client.getHits(jr) > 0);
        client.deleteDocument(tindex, ttype, a.id);
    }

    public void testAggregations() throws Exception {
        Article[] articles = {
                new Article("1", "qing", "i say a word"),
                new Article("2", "yang", "i don't like you"),
                new Article("3", "yang", "i love you even you don't love me")
        };

        for (Article a: articles) {
            client.addDoc(tindex, ttype, a);
        }
        client.flushIndex();
        List<ESTermAggregationItem> result = client.wordCount(tindex, ttype, "content", 10);
        if (result == null) {
            System.out.println("Get nothing");
        } else {
            for (ESTermAggregationItem item: result) {
                //System.out.println(item.key + " " + item.doc_count);
            }
        }
        Assert.assertNotNull(result);
        Assert.assertTrue(result.size() > 0);
        //System.out.println(ja.toString());
    }

    public void testUpdateByObject() throws Exception {
        Article article = new Article("1", "qing", "i say a word");
        client.addDoc(tindex, ttype, article);
        client.flushIndex();
        Article esa = client.getDoc(tindex, ttype, article.id, Article.class);
        Assert.assertEquals(esa.id, article.id);
        Article b = new Article(article);
        b.author = "yang";
        client.update(tindex, ttype, b);
        b = client.getDoc(tindex, ttype, b.id, Article.class);
    }

    public void testCreateIndexWithShards() throws Exception {
        client.createIndex("nevermore", 1, 1);
        Assert.assertTrue(client.indiceExists("nevermore"));
        client.deleteIndex("nevermore");
    }

}
