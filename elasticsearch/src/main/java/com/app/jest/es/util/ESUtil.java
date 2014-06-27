package com.app.jest.es.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.searchbox.annotations.JestId;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.ClientConfig;
import io.searchbox.core.*;
import io.searchbox.indices.*;
import io.searchbox.indices.mapping.GetMapping;
import io.searchbox.indices.mapping.PutMapping;
import io.searchbox.indices.template.GetTemplate;
import io.searchbox.indices.template.PutTemplate;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @author yangq
 * @version 1.0
 *          <br>qing.yang@dewmobile.net</br>
 * @file TODO: file name
 * @date 14-6-27
 */
public class ESUtil {
    static Logger logger = org.slf4j.LoggerFactory.getLogger(ESUtil.class);
    public static void flushIndex(JestClient client ) throws Exception {
        checkReturnValue(client.execute(new Flush.Builder().build()));
    }

    /**
     * Unchecked
     * @param client
     * @param b
     * @throws Exception
     */
    private void BuilkExcute(JestClient client, Bulk b) throws Exception {
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
    public static void addDoc(JestClient client, String index, String type, String id, String source) throws Exception {
        checkInput(index, type, id, source);
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
    public static void addDoc(JestClient client, String index, String type, String source) throws Exception {
        checkInput(index, type, source);
        Index index1 = new Index.Builder(source).index(index).type(type).build();
        client.execute(index1);
    }

    /**
     * Add document, no matter if it exits
     * @param index
     * @param type
     * @param source
     * @throws Exception
     */
    public static void addDoc(JestClient client, String index, String type, Object source)
            throws Exception {
        checkInput(index, type, source);
        assert(source.getClass().isAnnotationPresent(JestId.class));
        Index index1 = new Index.Builder(source).index(index).type(type)
                .build();
        checkReturnValue(client.execute(index1));
    }



    /**
     * Check if a doc with id exists
     * @param index
     * @param type
     * @param id
     * @return
     */
    public static boolean docExists(JestClient client, String index, String type, String id) throws Exception{
        JestResult jr = getDoc(client, index, type, id);
        checkReturnValue(jr);
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
    public static <T> T getDoc(JestClient client, String index, String type, String id, Class<T> cl)
            throws Exception {
        JestResult jr = getDoc(client, index, type, id);
        return ESSourceMapping.getSourceAsObject(jr, cl);
    }


    /**
     * Create an index with default manual
     *
     * @param index
     * @throws Exception
     */
    public static void createIndex(JestClient client, String index) throws Exception {
        client.execute(new CreateIndex.Builder(index).build());
    }

    /**
     * Unchecked
     *
     * @param index
     * @param settings
     * @throws Exception
     */
    public static void createIndex(JestClient client, String index, Map<String, String> settings)
            throws Exception {
        checkReturnValue(client.execute(new CreateIndex.Builder(index).settings(settings)
                .build()));
    }

    /**
     * Unchecked
     *
     * @param index
     * @param num_shards
     * @param num_replica
     * @throws Exception
     */
    public static void createIndex(JestClient client, String index, int num_shards, int num_replica)
            throws Exception {
        Map<String, String> settings = new HashMap<String, String>();
        settings.put("number_of_replicas", String.valueOf(num_replica));
        settings.put("number_of_shards", String.valueOf(num_shards));
        createIndex(client, index, settings);
    }

    /**
     * Delete an index
     *
     * @param index
     * @throws Exception
     */
    public static void deleteIndex(JestClient client, String index) throws Exception {
        client.execute(new DeleteIndex.Builder(index).build());
    }

    /**
     * Check if an indice exists
     * @param client
     * @param index
     * @return
     * @throws Exception
     */
    public static boolean indiceExists(JestClient client, String index) throws Exception {
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
    public static void putSetting(JestClient client, String index, int numshard, int numreplica)
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
    public static void putMapping(JestClient client, String index, String type, XContentBuilder mappings)
            throws Exception {
        PutMapping putMapping = new PutMapping.Builder(index, type,
                mappings.string()).build();
        client.execute(putMapping);
    }

    /**
     * Put template with XCOntentBuilder
     * @param tempname
     * @param content
     * @throws Exception
     */
    public static void putTemplate(JestClient client, String tempname, XContentBuilder content)
            throws Exception {
        PutTemplate puttemplate = new PutTemplate.Builder(tempname,
                content.string()).build();
        client.execute(puttemplate);
    }

    public static JestResult getTemplate(JestClient client, String tempname) throws Exception {
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
    public static String[] analyze(JestClient client, String index, String analyzer, String text)
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
    }


    /**
     * Get hits
     * @param jr
     * @return
     * @throws Exception
     */
    public static int getHits(JestClient client, JestResult jr)
            throws Exception  {
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
    public static JestResult query(JestClient client, String index, String type, String field, String text)
            throws Exception {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery(field, text));
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
     * @param index String
     * @param type  String
     * @param field String
     * @param maxSize int
     * @return
     * @throws Exception
     */
    public static List<ESTermAggregationItem> wordCount(JestClient client, String index, String type, String field, int maxSize)
            throws Exception {
        final String wordCount = "wordcount";
        TermsBuilder tb = AggregationBuilders.terms(wordCount).field(field).size(maxSize);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.aggregation(tb);
        Search search = new Search.Builder(searchSourceBuilder.toString())
                .addIndex(index).addType(type).build();
        JestResult jr =  client.execute(search);
        return ESSourceMapping.getAggregationObjects(jr, wordCount, ESTermAggregationItem.class);
    }



    /**
     * Get mappings, see demo or unit test for more usage
     *
     * @param index
     * @return JestResult
     * @throws Exception
     */
    public static JestResult getMapping(JestClient client, String index) throws Exception {
        GetMapping getMapping = new GetMapping.Builder().addIndex(index)
                .build();
        JestResult jr = client.execute(getMapping);
        checkReturnValue(jr);
        return jr;
    }


    /**
     * Get document by id
     * @param jc
     * @param index
     * @param type
     * @param id
     * @return
     * @throws Exception
     */
    public static JestResult getDoc(JestClient jc, String index, String type, String id) throws Exception {
        checkInput(jc, index, type, id);
        Get get = new Get.Builder(index, id).type(type).build();
        return checkReturnValue(jc.execute(get));
    }

    private static void checkInput(Object... inputs) {
        for (Object in: inputs) {
            if (null == in) {
                throw new NullPointerException();
            }
        }
    }

    private static JestResult checkReturnValue(JestResult jr) {
        if (null == jr) {
            throw new NullPointerException("Jest return null");
        }

        if (null != jr.getJsonObject().get("error")) {
            throw new ESOperationFailException("Operation failed");
        }

        return jr;
    }


    /**
     * Update a document
     * @param client
     * @param index
     * @param type
     * @param source
     * @throws Exception
     */
    public static void update(JestClient client, String index, String type, Object source) throws Exception {
        checkInput(index, type, source);
        boolean anoEixts = false;
        for (Field field: source.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(JestId.class)) {
                anoEixts = true;
                break;
            }
        }

        if (!anoEixts) {
            logger.debug(String.format("Try to update a model without id"));
            throw new ESOperationFailException("Try to update a model without id");
        }
        addDoc(client, index, type, source);
    }

    /**
     * Delete a document.
     * @param index
     * @param type
     * @param id
     * @throws Exception
     */
    public static void deleteDocument(JestClient client, String index, String type, String id) throws Exception {
        if (docExists(client, index, type, id)) {
            client.execute(new Delete.Builder(id).index(index).type(type).build());
        } else {
            logger.debug("You try to delete an unexisting document");
            throw new ESDocumentNotFoundException();
        }
    }

    /**
     * Unchecked
     */
    void nodeDiscoverty() {
        ClientConfig clientConfig = new ClientConfig.Builder(
                "http://localhost:9200").discoveryEnabled(true)
                .discoveryFrequency(1l, TimeUnit.MINUTES).build();
    }
}
