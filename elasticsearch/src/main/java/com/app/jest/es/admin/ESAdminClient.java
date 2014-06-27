package com.app.jest.es.admin;

import com.app.jest.es.util.ESDocumentNotFoundException;
import com.app.jest.es.util.ESOperationFailException;
import com.app.jest.es.util.ESSourceMapping;
import com.app.jest.es.util.ESTermAggregationItem;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.searchbox.annotations.JestId;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
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
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author YangQing
 * @version 1.0
 * @since 2014-6-19
 * @file ESAdminClient
 * @date 2014-6-19
 * @brief ESAdminClient serves for administrator to operate index including
 * @since 1.0
 * create, delete, update and so on.
 */
public class ESAdminClient {
    JestClient client;
    static Logger logger = LoggerFactory.getLogger(ESAdminClient.class);

    /**
     * Init admin client for correct hosts
     * @param hosts Host ip and port
     * @throws IllegalArgumentException
     */
    public ESAdminClient(Map<String, Integer> hosts)
            throws IllegalArgumentException {
        if (null == hosts) {
            throw new NullPointerException("Hosts should not be null");
        }

        if (0 == hosts.size()) {
            logger.error("elasticsearch admin client config not correct");
            throw new IllegalArgumentException();
        }
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

    public JestClient getClient() {
        return this.client;
    }
}
