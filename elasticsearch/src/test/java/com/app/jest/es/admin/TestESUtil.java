package com.app.jest.es.admin;

import com.app.jest.es.util.ESTermAggregationItem;
import com.app.jest.es.util.ESUtil;
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

public class TestESUtil extends TestCase {
    public static ESAdminClient adminclient;

    final String tindex = "my_index";
    final String ttype = "my_type";
    static {
        Map<String, Integer> hosts = new HashMap<String, Integer>();
        hosts.put("192.168.127.129", 9200);
        adminclient = new ESAdminClient(hosts);
    }
    public void setUp() throws Exception {

        if (!ESUtil.indiceExists(adminclient.getClient(), tindex)) {
            ESUtil.createIndex(adminclient.getClient(), tindex);
            ESUtil.flushIndex(adminclient.getClient());
        } else {
            ESUtil.deleteIndex(adminclient.getClient(), tindex);
            ESUtil.createIndex(adminclient.getClient(), tindex);
            ESUtil.flushIndex(adminclient.getClient());
        }
    }

    public void tearDown() throws Exception {
        ESUtil.deleteIndex(adminclient.getClient(), tindex);
    }


    /**
     * Get setting can't be used now. TODO No settings can be used
     */
    public void testputSetting() {
        try {
            ESUtil.deleteIndex(adminclient.getClient(), tindex);
            ESUtil.putSetting(adminclient.getClient(), tindex, 1, 1);
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
            ESUtil.putMapping(adminclient.getClient(), tindex, ttype, analyzer_ansj);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }

        try {
            JestResult jr = ESUtil.getMapping(adminclient.getClient(), tindex);
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
                ESUtil.putTemplate(adminclient.getClient(), "my_template_1", template);
            } catch (Exception e) {
                Assert.fail("Fail action, Fail to build a template");
            }
            // System.out.println(template.string());
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("Fail to build a template");
        }

        try {
            JestResult jr = ESUtil.getTemplate(adminclient.getClient(),"my_template_1");
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
            if (!ESUtil.indiceExists(adminclient.getClient(), "a_test_string")) {
                ESUtil.createIndex(adminclient.getClient(),"a_test_index");
                Assert.assertTrue(ESUtil.indiceExists(adminclient.getClient(), "a_test_index"));
                ESUtil.deleteIndex(adminclient.getClient(),"a_test_index");
                Assert.assertFalse(ESUtil.indiceExists(adminclient.getClient(),"a_test_index"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    public void testCreateIndex() {
        try {
            ESUtil.deleteIndex(adminclient.getClient(), tindex);
        } catch (Exception e1) {
            e1.printStackTrace();
            Assert.fail();
        }
        try {
            ESUtil.indiceExists(adminclient.getClient(), tindex);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Fail to put setting");
        }
    }

    public void testAnalyze() throws Exception {
        final String tstring = "你好啊朋友";
        // Use stand tokenizer to test
        String[] tokens = ESUtil.analyze(adminclient.getClient(), tindex, "standard", tstring);
        for (int i = 0; i < tokens.length; i++) {
            Assert.assertEquals(tstring.substring(i, i + 1), tokens[i]);
        }
        ESUtil.deleteIndex(adminclient.getClient(), tindex);
    }


    public void testAddDoc_String_WithOutId() throws Exception {
        String source = jsonBuilder().startObject().field("user", "qingy")
                .endObject().string();
        ESUtil.addDoc(adminclient.getClient(), tindex, ttype, source);
        ESUtil.flushIndex(adminclient.getClient());
        JestResult jr = ESUtil.query(adminclient.getClient(), tindex, ttype, "user", "qingy");
        //System.out.println(jr.getJsonString());
        Assert.assertTrue(ESUtil.getHits(adminclient.getClient(), jr) > 0);
    }


    public void testGetHits() {

    }

    public void testAddDoc_String_WithId() {

        int[] tags = {4, 9, 5};
        JestResult jr;
        final String idnum = "2";
        try {
            String source = jsonBuilder().startObject().field("user", "kimchy")
                    .field("tags", tags).endObject()
                    .string();
            ESUtil.addDoc(adminclient.getClient(), tindex, ttype, idnum, source);
            ESUtil.flushIndex(adminclient.getClient());
            Assert.assertEquals(true, ESUtil.docExists(adminclient.getClient(), tindex, ttype, idnum));
            jr = ESUtil.getDoc(adminclient.getClient(), tindex, ttype, idnum);
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
                ESUtil.deleteIndex(adminclient.getClient(), tindex);
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
        if (ESUtil.docExists(adminclient.getClient(), tindex, ttype, id)) {
            ESUtil.deleteDocument(adminclient.getClient(), tindex, ttype, id);
        }
        Assert.assertFalse(ESUtil.docExists(adminclient.getClient(), tindex, ttype, id));
        ESUtil.addDoc(adminclient.getClient(), tindex, ttype, a);
        ESUtil.flushIndex(adminclient.getClient());
        Assert.assertTrue(ESUtil.docExists(adminclient.getClient(), tindex, ttype, id));
        ESUtil.deleteDocument(adminclient.getClient(), tindex, ttype, id);
    }

    public void testGetDoc() throws Exception {

        ESUtil.addDoc(adminclient.getClient(), tindex, ttype, "1", "{\"author\":\"qing\"," +
                "\"content\":\"this is content string\"}");
        ESUtil.flushIndex(adminclient.getClient());
        Article a = ESUtil.getDoc(adminclient.getClient(), tindex, ttype, "1", Article.class);
        Assert.assertEquals(a.id, "1");
        Assert.assertEquals(a.author, "qing");
        Assert.assertEquals(a.content, "this is content string");
    }

    public void testQuery() throws Exception {
        Article a = new Article();
        a.id = "1234";
        a.author = "neverland";
        a.content = "my content";
        ESUtil.addDoc(adminclient.getClient(), tindex, ttype, a);
        ESUtil.flushIndex(adminclient.getClient());
        JestResult jr = ESUtil.query(adminclient.getClient(), tindex, ttype, "author", a.author);
        Assert.assertTrue("Can't find resource", ESUtil.getHits(adminclient.getClient(), jr) > 0);
        ESUtil.deleteDocument(adminclient.getClient(), tindex, ttype, a.id);
    }

    public void testAggregations() throws Exception {
        Article[] articles = {
                new Article("1", "qing", "i say a word"),
                new Article("2", "yang", "i don't like you"),
                new Article("3", "yang", "i love you even you don't love me")
        };

        for (Article a: articles) {
            ESUtil.addDoc(adminclient.getClient(), tindex, ttype, a);
        }
        ESUtil.flushIndex(adminclient.getClient());
        List<ESTermAggregationItem> result = ESUtil.wordCount(adminclient.getClient(), tindex, ttype, "content", 10);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.size() > 0);
        //System.out.println(ja.toString());
    }

    public void testUpdateByObject() throws Exception {
        Article article = new Article("1", "qing", "i say a word");
        ESUtil.addDoc(adminclient.getClient(), tindex, ttype, article);
        ESUtil.flushIndex(adminclient.getClient());
        Article esa = ESUtil.getDoc(adminclient.getClient(), tindex, ttype, article.id, Article.class);
        Assert.assertEquals(esa.id, article.id);
        Article b = new Article(article);
        b.author = "yang";
        ESUtil.update(adminclient.getClient(), tindex, ttype, b);
        ESUtil.flushIndex(adminclient.getClient());
        article = ESUtil.getDoc(adminclient.getClient(), tindex, ttype, article.id, Article.class);
        Assert.assertEquals(b.author, article.author);
    }

    public void testCreateIndexWithShards() throws Exception {
        ESUtil.createIndex(adminclient.getClient(), "nevermore", 1, 1);
        Assert.assertTrue(ESUtil.indiceExists(adminclient.getClient(), "nevermore"));
        ESUtil.deleteIndex(adminclient.getClient(), "nevermore");
    }

}
