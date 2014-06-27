package com.app.jest.es.test;

import io.searchbox.core.Search;
import io.searchbox.indices.CreateIndex;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.elasticsearch.action.admin.indices.optimize.OptimizeRequestBuilder;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.SpanFirstQueryBuilder;
import org.elasticsearch.index.query.SpanNearQueryBuilder;
import org.elasticsearch.index.query.SpanQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.facet.FacetBuilders;
import org.elasticsearch.search.facet.terms.TermsFacetBuilder;
import org.elasticsearch.search.sort.SortOrder;

import com.app.jest.es.client.ESUser;

public class ESClient_Demo {
	static final float BOOST_SEARCH_FACTOR = 0.5f;
	static int offset = 0;
	static int limit = 30;
	static final String text = "M000";
	static final String USER_DEST_INDEX = "fastooth";

	public static void main(String[] args) {
		System.out.println(generateAggreGationQuery());
		String source = null;
		System.out.println(source);
		List<ESUser> retValue = new ArrayList<ESUser>();
		Search search = new Search.Builder(source).addIndex(USER_DEST_INDEX)
				.build();
		LinkedHashSet<String> set = new LinkedHashSet<String>();

		/*
		 * JestClientFactory factory = new JestClientFactory();
		 * factory.setHttpClientConfig(new HttpClientConfig
		 * .Builder("http://ldkjserver0014:9200") .multiThreaded(true)
		 * .build());
		 * 
		 * JestClient client = factory.getObject();
		 */
		// BulkAction action = BulkAction.INSTANCE.newRequestBuilder(client);
		// BulkableAction action = new Bulk();
		// Bulk bulk = new Bulk(new Builder().addAction(action));
		/*
		 * try { SearchResult result = client.execute(searchByName);
		 * 
		 * System.out.println(result.getJsonObject().toString());
		 * 
		 * } catch (Exception e) {
		 * System.out.println("Something wrong with elasticsearch");
		 * //e.printStackTrace(); }
		 */
	}

	static String generatePrefixQuery(String text) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.prefixQuery("name", text));
		searchSourceBuilder.from(0);
		searchSourceBuilder.size(10);

		return searchSourceBuilder.toString();
	}

	static String generateMatchQuery(String text) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchQuery("name", text)
				.operator(MatchQueryBuilder.Operator.AND)
				.prefixLength(text.length()).maxExpansions(text.length())
				.cutoffFrequency(0).slop(0)
				.boost(text.length() * BOOST_SEARCH_FACTOR));
		searchSourceBuilder.from(offset);
		searchSourceBuilder.size(limit);
		ArrayList<String> fields = new ArrayList<String>();
		fields.add("n");
		fields.add("c@");
		searchSourceBuilder.fields(fields);
		return searchSourceBuilder.toString();
	}

	static String generateWildChardQuery(String text) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.wildcardQuery("name",
				text + "*").boost(text.length() * BOOST_SEARCH_FACTOR));
		searchSourceBuilder.from(offset);
		searchSourceBuilder.size(limit);
		return searchSourceBuilder.toString();
	}

	static String generateFunzzyQuery(String text) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.fuzzyQuery("name", text).boost(
				text.length() * BOOST_SEARCH_FACTOR));
		searchSourceBuilder.from(offset);
		searchSourceBuilder.size(limit);
		return searchSourceBuilder.toString();
	}

	static String generateFilterQuery(String text) {
		StringBuilder sb = new StringBuilder();
		sb.append("doc['name'].values.size() >= ");
		sb.append(1);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.filteredQuery(
				QueryBuilders.matchQuery("name", text),
				FilterBuilders.scriptFilter(sb.toString())));
		return searchSourceBuilder.toString();
	}

	static String generateRegularQuery(String text) {

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.regexpQuery("name", text));
		return searchSourceBuilder.toString();
	}

	static String generateMultiMatch(String text) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		String fieldNames[] = { "n^5", "snip^1", "key^10" };
		searchSourceBuilder.query(QueryBuilders.multiMatchQuery(text,
				fieldNames));
		return searchSourceBuilder.toString();
	}

	// Unavailable
	static String generateOptimizeRequest(String text) {
		OptimizeRequestBuilder orb = new OptimizeRequestBuilder(null);
		orb.setMaxNumSegments(1);

		return orb.toString();
	}

	static String generateTermsFacetRequest(String text) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchQuery("name", text)
				.operator(MatchQueryBuilder.Operator.AND)
				.prefixLength(text.length()).maxExpansions(text.length())
				.cutoffFrequency(0).slop(0)
				.boost(text.length() * BOOST_SEARCH_FACTOR));
		searchSourceBuilder.from(offset);
		searchSourceBuilder.size(limit);

		TermsFacetBuilder fb = FacetBuilders.termsFacet("f").field("brand")
				.size(10);
		searchSourceBuilder.facet(fb);

		return searchSourceBuilder.toString();
	}

	static String generateAggreGationQuery() {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		TermsBuilder tb = AggregationBuilders.terms("f1").field("n").size(10);
		searchSourceBuilder.aggregation(tb);
		return searchSourceBuilder.toString();
	}
	
	static String generateMatchAllQuery() {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		searchSourceBuilder.sort("c@", SortOrder.ASC);
		return searchSourceBuilder.toString();
	}

	static String generateBooleanQuery(String text1, String text2) {
		SearchSourceBuilder ssb = new SearchSourceBuilder();
		// Not finished yet
		// ssb.query(QueryBuilders.boolQuery().must(queryBuilder));
		return ssb.toString();
	}

	static String generateScroll() {
		return null;
	}

	static String generateTermQuery() {
		SearchSourceBuilder ssb = new SearchSourceBuilder();
		BoolQueryBuilder bb = QueryBuilders.boolQuery();
		bb.must(QueryBuilders.termQuery("n", "金玉良缘"));
		bb.must(QueryBuilders.termQuery("n", "16"));
		return ssb.query(bb).toString();
	}

	static String generateSpanSlopQuery() {
		SearchSourceBuilder ssb = new SearchSourceBuilder();
		SpanNearQueryBuilder sb = QueryBuilders.spanNearQuery();
		sb.slop(1);
		sb.inOrder(true);
		SpanQueryBuilder clause1 = QueryBuilders.spanTermQuery("n", "视频");
		SpanQueryBuilder clause2 = QueryBuilders.spanTermQuery("n", "高清");
		sb.clause(clause1);
		sb.clause(clause2);
		ssb.query(sb);
		return ssb.toString();
	}

	static String generateSpanFirstQuery() {
		SearchSourceBuilder ssb = new SearchSourceBuilder();
		SpanQueryBuilder clause1 = QueryBuilders.spanTermQuery("n", "金玉良缘");
		SpanFirstQueryBuilder sfqb = QueryBuilders.spanFirstQuery(clause1, 2);
		// sb.slop(1);
		// sb.inOrder(true);
		// SpanQueryBuilder clause1 = QueryBuilders.spanTermQuery("n", "视频");
		// SpanQueryBuilder clause2 = QueryBuilders.spanTermQuery("n", "高清");
		// sfqb.clause(clause1);
		// sfqb.clause(clause2);
		ssb.query(sfqb);
		return ssb.toString();
	}

	static CreateIndex createAnIndex(String text) {
		String settings = "{\"settings\" : {\n"
				+ "        \"number_of_shards\" : 5,\n"
				+ "        \"number_of_replicas\" : 1\n" + "    }\n}";
		CreateIndex cia = new CreateIndex.Builder("articles").settings(
				ImmutableSettings.builder().loadFromSource(settings).build()
						.getAsMap()).build();

		return cia;
	}

}
