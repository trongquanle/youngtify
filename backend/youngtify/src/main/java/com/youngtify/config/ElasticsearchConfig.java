package com.youngtify.config;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.io.IOException;

@Configuration
public class ElasticsearchConfig {

    @Value("${elasticsearch.host}")
    private String host;

    @Value("${elasticsearch.port}")
    private int port;

    @Bean
    public RestHighLevelClient client() {
        return new RestHighLevelClient(
                RestClient.builder(new HttpHost(host, port, "http")));
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchRestTemplate(client());
    }

    public SearchHit[] search(QueryBuilder queryBuilder) throws IOException {
        String[] includeFields = new String[] {"id", "fullname", "phonenumber", "email", "avatar"};
        String[] excludeFields = new String[] {"innerObject.*"};
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
                .query(queryBuilder)
                .fetchSource(includeFields, excludeFields);
        SearchRequest searchRequest = new SearchRequest().source(sourceBuilder);
        SearchResponse searchResponse = client().search(searchRequest, RequestOptions.DEFAULT);
        return searchResponse.getHits().getHits();
    }

    public SearchHit[] search(QueryBuilder queryBuilder, String[] includeFields) throws IOException {
        String[] excludeFields = new String[] {"innerObject.*"};
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
                .query(queryBuilder)
                .fetchSource(includeFields, excludeFields);
        SearchRequest searchRequest = new SearchRequest().source(sourceBuilder);
        SearchResponse searchResponse = client().search(searchRequest, RequestOptions.DEFAULT);
        return searchResponse.getHits().getHits();
    }
}
