package com.batchStudy.batchStudy;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.json.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeader;
import org.json.JSONObject;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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


//        IndexResponse response = esClient.index(request);

//        log.info("Indexed with version " + response.version());


        try {
            // 파일 읽기
            File file = new File("D:\\BOTH_lineF.vcf");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            // header 읽기
            String firstLine = bufferedReader.readLine();
            String[] headerList = firstLine.split("\t");

            Map<String, String> map = new HashMap<>();

            // 데이터 읽기
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                String[] lineList = line.split("\t");
                for (int i = 0; i < headerList.length; i++) {
                    map.put(headerList[i], lineList[i]);
                }
                JSONObject jsonObject = new JSONObject(map);
                System.out.println("json : " + jsonObject);
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

    }
}
