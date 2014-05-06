package com.cto.app.elasticsearch;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.elasticsearch.action.search.SearchResponse;

import com.cto.app.elasticsearch.ESBaseModel;


public class Test_ESClient extends TestCase {

	static ESClient es = ESBaseModel.esclient;
	static final String RESOURCEID = "2529945";
	static final String RESOURCENAME = "周杰伦";
	static final String USERID = "10001";
	static final String USERNAME = "刀郎";
	
	public void setUp() throws Exception {
		System.out.println("Start Test_ESClient");

    }

    public void tearDown() throws Exception {
    	System.out.println("Exit Test_ESClient");
    }

	
	public void test_searchSourceById() {
		SearchResponse response = es.searchResourceById(RESOURCEID, 0);
		System.out.println("********************Result for Resource By ID*************\n");
		System.out.println(response.toString());
		assertTrue(response.getHits().getTotalHits() > 0);
	}

	public void test_searchSourceByName() {
		System.out.println("********************Result for Resource By Name*************\n");
		SearchResponse response = es.searchResourceByName(RESOURCENAME, 0);
		System.out.println(response.toString());
		Assert.assertTrue(response.getHits().getTotalHits() > 0);	
	}
	

	public void test_searchUserByName() {
		System.out.println("********************Result for User By Name*************\n");
		SearchResponse response = es.searchUserByName(USERNAME, 0);
		System.out.println(response.toString());
		Assert.assertTrue(response.getHits().getTotalHits() > 0);
		
	}
	

	public void test_searchUserByID() {
		System.out.println("********************Result for User By ID*************\n");
		SearchResponse response = es.searchUserById(USERID, 0);
		System.out.println(response.toString());
		Assert.assertTrue(response.getHits().getTotalHits() > 0);
	}
	

	public void test_serachUserByID_Json() {
		System.out.println("test_serachUserByID_Json");
		Assert.assertTrue(es.getResourceById(RESOURCEID, 0).size() > 0);
	}
	

	public void test_serachUserByName_Json() {
		System.out.println("test_serachUserByName_Json");
		Assert.assertTrue(es.getUserByName(USERNAME, 0).size() > 0);
	}
	

	public void test_serachResourceByName_Json() {
		System.out.println("test_serachResourceByName_Json");
		Assert.assertTrue(es.getResourceByName(RESOURCENAME, 0).size() > 0);
	}
	

	public void test_serachResourceById_Json() {
		Assert.assertTrue(es.getUserById(USERID, 0).size() > 0);
	}
}
