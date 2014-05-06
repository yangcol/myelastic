package com.cto.app.elasticsearch;

public class ESPagination {
    public int limit;
    public int offset;

    public ESPagination() {
        limit = 20;
        offset = 0;
    }

    public ESPagination(int l, int o) {
        limit = l;
        offset = o;
    }
}
