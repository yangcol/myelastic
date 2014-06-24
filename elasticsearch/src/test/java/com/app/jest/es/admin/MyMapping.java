package com.app.jest.es.admin;

import io.searchbox.annotations.JestId;

public class MyMapping {
	@JestId
	private String id;
	private String index_analyzer;
	private String search_analyzer;
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(id);
		sb.append(",");
		sb.append(index_analyzer);
		sb.append(",");
		sb.append(search_analyzer);
		return sb.toString();
	}
	
	public static void main(String[] args) {
		String[] a = {"hello", "world", "qing"};
		String[] b = a;
		a[0] = "nihao";
		
		System.out.println(b[0]);
	}
}
