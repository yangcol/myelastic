package com.app.jest.es.admin;

import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.CreateIndex.Builder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author YangQing
 * @version 1.0
 * @file ESMappings
 * @date 2014-6-23
 * @brief TODO
 */
public class ESMappings {
    private String index;

	/*TODO The following code are demoed
     *
	private ESMappings(Builder builder) {
		this.index = builder.index;
	}

	public static class Builder {
		private Map<String, String> settings = new HashMap<String, String>();
		private String index;

		public Builder(String index) {
			this.index = index;
		}

		public Builder settings(Map<String, String> settings) {
			this.settings = settings;
			return this;
		}

		public ESMappings build() {
			return new ESMappings(this);
		}
	}
	*/
}
