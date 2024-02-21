package com.batchStudy.batchStudy;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeader;

import java.io.*;

@Slf4j
@JsonIgnoreProperties(ignoreUnknown=true)
public class ElasticConfig {
    public void main() throws IOException {

        // elastic 서버 연결
        String apiKey = "Ul9hVXE0MEJ2azQ1U1dmN193SzY6emptSjBsNHlRLW1uVU56QURJeGVhZw==";
        BasicCredentialsProvider provider = new BasicCredentialsProvider();
        provider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("gwu_user", "111111"));

        // low-level-rest-client 생성
        org.elasticsearch.client.RestClient restClient = org.elasticsearch.client.RestClient
                .builder(new HttpHost("poc-edu.es.us-east-2.aws.elastic-cloud.com", 443, "https"))
                .setDefaultHeaders(new Header[]{
                        new BasicHeader("Authorization", "ApiKey " + apiKey)
                })
                .setHttpClientConfigCallback(hc -> hc
                        .setDefaultCredentialsProvider(provider)
                )
                .build();

        // Jackson Mapper 생성
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        // API Client 생성
        ElasticsearchClient esClient = new ElasticsearchClient(transport);

        // single raw Json data 생성
        Reader input = new StringReader(
                "{'id': 1, '@timestamp': '2024-01-14T12:08:23Z', 'level': 'warn', 'message': 'Some log Message'}"
                        .replace('\'','"')
        );

        IndexRequest<JsonData> request = IndexRequest.of(i -> i
                .index("gwu_test_index")
                .withJson(input)
        );


        IndexResponse response = esClient.index(request);

        log.info("Indexed with version " + response.version());


    }
}
