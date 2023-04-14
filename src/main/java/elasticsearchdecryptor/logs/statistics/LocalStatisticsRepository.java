package elasticsearchdecryptor.logs.statistics;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
class LocalStatisticsRepository implements StatisticsRepository {
  private final ObjectMapper objectMapper;
  private final Resource resourceFile;

  LocalStatisticsRepository(@Value("classpath:data/statistics.json") Resource resourceFile,
      ObjectMapper objectMapper) {
    this.resourceFile = resourceFile;
    this.objectMapper = objectMapper;
  }

  @Override
  public List<StatisticsData> getAll() {
    try {
      return objectMapper.readValue(resourceFile.getInputStream(), new TypeReference<>() {
      });
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
