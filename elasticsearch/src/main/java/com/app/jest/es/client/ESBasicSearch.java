/**
 * @file ESBasicSearch.java
 * @author YangQing
 * @date 2014/5/14
 * @brief Interface for searchByName result
 */
package com.app.jest.es.client;

import io.searchbox.client.JestClient;

import java.util.List;

public interface ESBasicSearch {
	public List<? extends AbstractSearchResult> search(JestClient jc,
			String text, int offset, int limit);
	
	public List<? extends AbstractSearchResult> search(JestClient jc,
			String text);
	
	/**
	 * Interface for get an resource or user
	 * @param jc client
	 * @param id Id to get
	 * @return <code>AbstractSearchResult</code>
	 */
	public AbstractSearchResult get(JestClient jc,
			String id);
}
