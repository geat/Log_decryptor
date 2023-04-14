package elasticsearchdecryptor.logs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Clock;

@SpringBootApplication

public class LogElasticsearchApplication {

  @Bean
  public Clock clock() {
    return Clock.systemUTC();
  }

  public static void main(String[] args) {
    SpringApplication.run(LogElasticsearchApplication.class, args);
  }

}
