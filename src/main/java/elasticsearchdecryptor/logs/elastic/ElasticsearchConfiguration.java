package elasticsearchdecryptor.logs.elastic;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfiguration {
  @Value("${elasticsearch.host}")
  private String elasticsearchHost;

  @Value("${elasticsearch.port}")
  private int elasticsearchPort;

  @Bean
  RestHighLevelClient restHighLevelClient() {
    RestClientBuilder client = RestClient.builder(new HttpHost(elasticsearchHost, elasticsearchPort));

    return new RestHighLevelClient(client);
  }
}
