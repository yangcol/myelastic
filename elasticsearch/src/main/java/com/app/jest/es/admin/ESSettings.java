package com.app.jest.es.admin;
/**
 * 
 * @file ESSettings
 * @author YangQing
 * @date 2014-6-23
 * @brief TODO
 * @version 1.0
 */
public class ESSettings {
	/**
	 * Replica number
	 */
	int num_replica;
	
	/**
	 * Shard number
	 */
	int num_shards;
	
	public ESSettings(int num_replica, int num_shards) {
		this.num_replica = num_replica;
		this.num_shards = num_shards;
	}
}
