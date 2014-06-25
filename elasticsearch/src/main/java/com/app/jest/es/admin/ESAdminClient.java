package com.app.jest.es.admin;

import java.util.*;
import java.util.concurrent.TimeUnit;

import com.app.jest.es.ESResource;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.ClientConfig;
import io.searchbox.core.Bulk;
import io.searchbox.core.Delete;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.Update;
import io.searchbox.indices.Analyze;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.IndicesExists;
import io.searchbox.indices.aliases.AddAliasMapping;
import io.searchbox.indices.mapping.PutMapping;
import io.searchbox.indices.mapping.GetMapping;
import io.searchbox.indices.template.GetTemplate;
import io.searchbox.indices.template.PutTemplate;

import org.codehaus.jackson.JsonNode;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.facet.FacetBuilders;
import org.elasticsearch.search.facet.terms.TermsFacetBuilder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * @author YangQing
 * @version 1.0
 * @file ESAdminClient
 * @date 2014-6-19
 * @brief ESAdminClient serves for administrator to operate index including
 * create, delete, update and so on.
 */
public class ESAdminClient {
    JestClient client;

    /**
     * Init admin client for correct hosts
     *
     * @param hosts
     * @throws IllegalArgumentException
     */
    public ESAdminClient(Map<String, Integer> hosts)
            throws IllegalArgumentException {
        LinkedHashSet<String> set = new LinkedHashSet<String>();
        for (String key : hosts.keySet()) {
            set.add("http://" + key + ":" + hosts.get(key));
        }

        if (0 == set.size()) {
            throw new IllegalArgumentException(
                    "ESClient configuration is not correct!");
        }

        JestClientFactory factory = new JestClientFactory();
        client = factory.getObject();
        client.setServers(set);
    }

    void createTemplate(String index) {

    }

    void BuilkExcute(Bulk b) throws Exception {
        client.execute(b);
    }

    /**
     * Passed
     *
     * @param index
     * @param type
     * @param id
     * @param source
     * @throws Exception
     */
    public void addDoc(String index, String type, String id, String source) throws Exception {
        Index index1 = new Index.Builder(source).index(index).type(type).id(id).build();
        client.execute(index1);
    }

    /**
     * Add source through string, but id is not used
     *
     * @param index
     * @param type
     * @param source
     * @throws Exception
     */
    public void addDoc(String index, String type, String source) throws Exception {
        Index index1 = new Index.Builder(source).index(index).type(type).build();
        client.execute(index1);
    }

    // TODO unchecked
    // Don't use it in this way. This api seems doesn't work
    public void addDoc(String index, String type, Object source)
            throws Exception {
        Index index1 = new Index.Builder(source).index(index).type(type)
                .build();
        client.execute(index1);
    }


    /**
     * TODO unchecked
     *
     * @param index
     * @param type
     * @param id
     * @return
     * @throws Exception
     */
    public JestResult getDoc(String index, String type, String id)
            throws Exception {
        Get get = new Get.Builder(index, id).type(type).build();
        return client.execute(get);
    }


    /**
     * Check if a doc with id exists
     * @param index
     * @param type
     * @param id
     * @return
     */
    public boolean docExists(String index, String type, String id) {
        JestResult jr = null;
        try {
            jr = getDoc(index, type, id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return jr.getJsonObject().get("found").getAsBoolean();
    }

    /**
     * TODO unchecked
     *
     * @param index
     * @param type
     * @param id
     * @param cl
     * @return
     * @throws Exception
     */
    public <T> T getDoc(String index, String type, String id, Class<T> cl)
            throws Exception {
        JestResult jr = getDoc(index, type, id);
        return jr.getSourceAsObject(cl);
    }


    /**
     * Create an index with default manual
     *
     * @param index
     * @throws Exception
     */
    public void createIndex(String index) throws Exception {
        client.execute(new CreateIndex.Builder(index).build());
    }

    /**
     * Unchecked
     *
     * @param index
     * @param settings
     * @throws Exception
     */
    public void createIndex(String index, Map<String, String> settings)
            throws Exception {
        client.execute(new CreateIndex.Builder(index).settings(settings)
                .build());
    }

    /**
     * Unchecked
     *
     * @param index
     * @param num_shards
     * @param num_replica
     * @throws Exception
     */
    public void crateIndex(String index, int num_shards, int num_replica)
            throws Exception {
        Map<String, String> settings = new HashMap<String, String>();
        settings.put("number_of_replicas", String.valueOf(num_replica));
        settings.put("number_of_shards", String.valueOf(num_shards));
        createIndex(index, settings);
    }

    /**
     * Delete an index
     *
     * @param index
     * @throws Exception
     */
    void deleteIndex(String index) throws Exception {
        client.execute(new DeleteIndex.Builder(index).build());
    }

    boolean indiceExists(String index) throws Exception {
        JestResult jr = client
                .execute(new IndicesExists.Builder(index).build());
        return jr.getJsonObject().get("found").getAsBoolean();
    }

    /**
     * Put setting will create an index automaticlly
     *
     * @param index
     * @param numshard
     * @param numreplica
     * @throws Exception
     */
    public void putSetting(String index, int numshard, int numreplica)
            throws Exception {
        ImmutableSettings.Builder settingsBuilder = ImmutableSettings
                .settingsBuilder();
        settingsBuilder.put("number_of_shards", numshard);
        settingsBuilder.put("number_of_replicas", numreplica);

        client.execute(new CreateIndex.Builder(index).settings(
                settingsBuilder.build().getAsMap()).build());
    }

    /**
     * Put mappings, prepare your mapping by yourself, refer to a demo.
     *
     * @param index
     * @param type
     * @param mappings
     * @throws Exception
     */
    public void putMapping(String index, String type, XContentBuilder mappings)
            throws Exception {
        // System.out.println(mappings.string());
        PutMapping putMapping = new PutMapping.Builder(index, type,
                mappings.string()).build();
        client.execute(putMapping);
    }

    public void putTemplate(String tempname, XContentBuilder content)
            throws Exception {
        PutTemplate puttemplate = new PutTemplate.Builder(tempname,
                content.string()).build();
        client.execute(puttemplate);
    }

    public JestResult getTemplate(String tempname) throws Exception {
        GetTemplate gettemplate = new GetTemplate.Builder(tempname).build();
        return client.execute(gettemplate);
    }

    /**
     * Analyze text with a certain analyzer
     *
     * @param index    Not null
     * @param analyzer Not null
     * @param text     Not null
     * @return JestResult
     * @throws Exception
     */
    public String[] analyze(String index, String analyzer, String text)
            throws Exception {
        Analyze analyze = new Analyze.Builder().index(index).analyzer(analyzer)
                .source(text).build();
        JestResult jr = client.execute(analyze);
        JsonObject jo = jr.getJsonObject();
        JsonArray ja = jo.get("tokens").getAsJsonArray();
        String[] result = new String[ja.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = ja.get(i).getAsJsonObject().get("token").getAsString();
        }
        return result;
        // Assert.assertEquals(token,
        // ja.get(i).getAsJsonObject().get("token").toString());
    }


    /**
     * Get hits
     * @param index
     * @param ttype
     * @param field
     * @param text
     * @return
     * @throws Exception
     */
    public int getHits(String index, String ttype, String field, String text)
            throws Exception  {
        JestResult jr = query(index, ttype, field, text);
        return jr.getJsonObject().get("hits").getAsJsonObject().get("total").getAsInt();
    }

    /**
     * Match text
     * @param index
     * @param type
     * @param field
     * @param text
     * @return
     * @throws Exception
     */
    public JestResult query(String index, String type, String field, String text)
            throws Exception {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery(field, text));
        //System.out.println("Query String:" + searchSourceBuilder.toString());
        Search search = new Search.Builder(searchSourceBuilder.toString())
                .addIndex(index)
                .addType(type)
                .build();

        return client.execute(search);
    }

    /**
     * Unchecked, use aggregations Demo:TermsBuilder tb =
     * AggregationBuilders.terms(aggrename).field(field).size(size);
     *
     * @param index
     * @param type
     * @param  tb
     * @return
     * @throws Exception
     */
    public JestResult aggregations(String index, String type, TermsBuilder tb)
            throws Exception {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        // TermsBuilder tb =
        // AggregationBuilders.terms(aggrename).field(field).size(size);
        searchSourceBuilder.aggregation(tb);
        searchSourceBuilder.toString();
        Search search = new Search.Builder(searchSourceBuilder.toString())
                .addIndex(index).addType(type).build();
        return client.execute(search);
    }

    /**
     * Get mappings, see demo or unit test for more usage
     *
     * @param index
     * @return
     * @throws Exception
     */
    public JestResult getMapping(String index) throws Exception {
        GetMapping getMapping = new GetMapping.Builder().addIndex(index)
                .build();
        return client.execute(getMapping);
    }

    void update() throws Exception {
        String script = "{\n"
                + "    \"script\" : \"ctx._source.tags += tag\",\n"
                + "    \"params\" : {\n" + "        \"tag\" : \"blue\"\n"
                + "    }\n" + "}";

        client.execute(new Update.Builder(script).index("twitter")
                .type("tweet").id("1").build());
    }

    /**
     * Delete a document.
     *
     * @param index
     * @param type
     * @param id
     * @throws Exception
     */
    void deleteDocument(String index, String type, String id) throws Exception {
        client.execute(new Delete.Builder(id).index(index).type(type).build());

    }

    void nodeDiscoverty() {
        ClientConfig clientConfig = new ClientConfig.Builder(
                "http://localhost:9200").discoveryEnabled(true)
                .discoveryFrequency(1l, TimeUnit.MINUTES).build();
    }

    void getDocument() throws Exception {
        Get get = new Get.Builder("twitter", "1").type("tweet").build();
        JestResult result = client.execute(get);
    }
}
