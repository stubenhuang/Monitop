package com.stuben.monitop.server.configuration;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ElasticSearchProperties.class)
public class ElasticSearchClientConfiguration {

    private final ElasticSearchProperties elasticSearchProperties;

    public ElasticSearchClientConfiguration(ElasticSearchProperties elasticSearchProperties) {
        this.elasticSearchProperties = elasticSearchProperties;
    }

    @Bean
    public RestClient restClient() {
        RestClientBuilder builder = RestClient.builder(new HttpHost(elasticSearchProperties.getNode(), elasticSearchProperties.getPort(), "http"))
                .setRequestConfigCallback(requestConfigBuilder -> {
                    requestConfigBuilder.setConnectTimeout(3000);
                    requestConfigBuilder.setSocketTimeout(5000);
                    requestConfigBuilder.setConnectionRequestTimeout(1000);
                    return requestConfigBuilder;
                }).setMaxRetryTimeoutMillis(10000);

        return builder.build();
    }


    @Bean
    public RestHighLevelClient restHighLevelClient(RestClient restClient) {
        return new RestHighLevelClient(restClient);
    }

}
