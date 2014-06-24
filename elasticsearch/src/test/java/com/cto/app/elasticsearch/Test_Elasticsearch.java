package com.cto.app.elasticsearch;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.search.rescore.RescoreBuilder;
import org.elasticsearch.search.rescore.Rescorer;

public class Test_Elasticsearch {
	public static void main(String[] args) {
		Client client = new TransportClient()
				.addTransportAddress(new InetSocketTransportAddress(
						"192.168.127.129", 9200));

		RescoreBuilder rb = new RescoreBuilder();

//		rb.rescorer(rescorer);
	}
}
