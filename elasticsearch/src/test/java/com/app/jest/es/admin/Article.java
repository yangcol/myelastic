package com.app.jest.es.admin;

import io.searchbox.annotations.JestId;

/**
 * Created with IntelliJ IDEA.
 *
 * @author yangq
 * @version 1.0
 *          <br>qing.yang@dewmobile.net</br>
 * @file TODO: file name
 * @date 14-6-25
 */
class Article {
    @JestId
    public String id;

    public String author;
    public String content;

    public Article() {}

    public Article(String id, String author, String content) {
        this.id = id;
        this.author = author;
        this.content = content;
    }

    public Article(Article a) {
        this.id = a.id;
        this.author = a.author;
        this.content = a.content;
    }
}