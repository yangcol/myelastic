package com.app.jest.es.admin.token;

import com.app.jest.es.admin.ESAdminClient;
import com.app.jest.es.util.ESUtil;
import io.searchbox.client.JestClient;

/**
 * Created with IntelliJ IDEA.
 *
 * @author yangq
 * @version 1.0
 *          <br>qing.yang@dewmobile.net</br>
 * @file TODO: file name
 * @date 14-6-27
 */
public class ESTokenManager {

    public void reBuildAffctedDocs(JestClient client, String word, String index, String[] fields) throws Exception {
        for (String field: fields) {
            String[] tokens = ESUtil.analyzeTextByField(client, index, field, word);
            // find token affected.
        }
    }
}
