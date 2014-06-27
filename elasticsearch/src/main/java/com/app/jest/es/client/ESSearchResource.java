/**
 * @file ESSearchResource.java
 * @author YangQing
 * @date 2014/5/14
 * @brief Implementation for searchByName resource
 */
package com.app.jest.es.client;

import com.app.jest.es.util.ESSourceMapping;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Get;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Search resource
 * @author yangq
 *
 */
public class ESSearchResource {
    static Logger logger = org.slf4j.LoggerFactory.getLogger(ESSearchResource.class);
	static final String RESOURCE_NAME_FIELD = "n";
    static final String RESOURCE_TAG_FIELD = "tags";
	//static final String RESOURCE_ID_FIELD = "_id";
	//static final int offset = 0;
	//static final int limit = 30;
	static String RESOURCE_DEST_INDEX = "video";


    /**
     * Search resource by name
     * @param jc Jest client
     * @param text Text
     * @param offset Offset
     * @param limit Size
     * @return ESResource[List]
     */
	public static List<ESResource> searchByName(JestClient jc,
                                         String text, int offset, int limit) {
        SearchResult result;
        try {
            result = match(jc, RESOURCE_NAME_FIELD, text, offset, limit);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(String.format("Search [%s] failed, detail: %s", text, e.getMessage()));
            return null;
        }

        return getResult(result);
	}

    /**
     * Get resource
     * @param jc JestClient
     * @param id Id to get
     * @return ESResource
     */
    public static ESResource get(JestClient jc, String id) {
        Get get = new Get.Builder(RESOURCE_DEST_INDEX, id).build();
        ESResource rs = null;
        try {
            JestResult result = jc.execute(get);
            rs = ESSourceMapping.getSourceAsObject(result, ESResource.class);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(String.format("Get resource id: [%s] failed, detail: %s", id, e.getMessage()));
        }

        return rs;
    }

    /**
     * Search resource by tag
     * @param jc
     * @param tag
     * @param offset
     * @param limit
     * @return ESResource[List]
     * @throws Exception
     */
    public static List<ESResource> searchByTag(JestClient jc, int[] tag, int offset, int limit) {
        SearchResult result = null;
        try {
            result = match(jc, RESOURCE_TAG_FIELD, tag, offset, limit);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(String.format("Get resource by tag: [%s] failed, detail: %s", Arrays.toString(tag), e.getMessage()));
        }
        return  getResult(result);
    }

    private static SearchResult match(JestClient jc, String field, Object matchObject, int offset, int limit)
            throws Exception {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery(field, matchObject));
        searchSourceBuilder.from(offset);
        searchSourceBuilder.size(limit);
        Search search = new Search.Builder(searchSourceBuilder.toString())
                .addIndex(RESOURCE_DEST_INDEX)
                .build();
        return jc.execute(search);
    }

    private static List<ESResource> getResult(SearchResult sr) {
        List<ESResource> retValue = new ArrayList<ESResource>();
        List<SearchResult.Hit<ESResource, Void>> hits = sr
                .getHits(ESResource.class);
        for (SearchResult.Hit<ESResource, Void> hit : hits) {
            ESResource u = hit.source;
            retValue.add(u);
        }
        return retValue;
    }


}
