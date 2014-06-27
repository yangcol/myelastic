package com.app.jest.es.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author yangq
 * @version 1.0
 *          <br>qing.yang@dewmobile.net</br>
 * @file TODO: file name
 * @date 14-6-26
 */
public abstract class ESAbstractAggregationItem {
    static public <T> T getSourceAsObject(String s, Class<T> type) {
            Gson gson = new Gson();
            return gson.fromJson(s, type);
    }

    static public <T> List<T> getSourceListAsObjects(JsonArray ja, Class<T> type) {
        List<T> result = new LinkedList<T>();
        if (null == result) {
            return null;
        }
        for (JsonElement je: ja) {
            result.add(getSourceAsObject(je.toString(), type));
        }

        return result;
    }
}
