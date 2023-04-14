package elasticsearchdecryptor.logs.driver;

import lombok.RequiredArgsConstructor;

import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import elasticsearchdecryptor.logs.api.Filters;
import elasticsearchdecryptor.logs.elastic.ElasticsearchReadService;
import elasticsearchdecryptor.logs.model.LogsData;

@RequiredArgsConstructor
@Component
public class LogDataProcessor {

  // private final ElasticsearchStorageService<Driver> storageService;
  private final ElasticsearchReadService searchService;
  private final DriverDataQueryProvider driverDataQueryProvider;

  private final LogAggregationResultMapper logAggregationResultMapper;

  @Value("#{'${elasticsearch.index.name.drivers}'}")
  private String driversRawIndexName;

  // @Value("#{'${elasticsearch.index.name.enriched-drivers}'}")
  // private String driversEnrichedIndexName;

  @Value("#{'${elasticsearch.pipeline.name}'}")
  private String pipelineName;

  public LogsData getLogsData(Filters filters) {
    // SearchResponse searchResponse = searchService.read(driversEnrichedIndexName,
    // driverDataQueryProvider.getQuery(filters),
    // driverAggregationProvider.getAggregations());
    System.out.println("nama index :" + driversRawIndexName);

    SearchResponse searchResponse = searchService.read(driversRawIndexName,
        driverDataQueryProvider.getQuery(filters));
    return logAggregationResultMapper.map(searchResponse);
  }
}
