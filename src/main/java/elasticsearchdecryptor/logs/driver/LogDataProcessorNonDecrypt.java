package elasticsearchdecryptor.logs.driver;

import lombok.RequiredArgsConstructor;

import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import elasticsearchdecryptor.logs.api.Filters;
import elasticsearchdecryptor.logs.elastic.ElasticsearchReadService;
import elasticsearchdecryptor.logs.model.LogsDataNonDecrypt;

@RequiredArgsConstructor
@Component
public class LogDataProcessorNonDecrypt {

  // private final ElasticsearchStorageService<Driver> storageService;
  private final ElasticsearchReadService searchService;
  private final DriverDataQueryProvider driverDataQueryProvider;

  private final LogAggregationResultMapperNonDecrypt logAggregationResultMapperNonDecrypt;

  @Value("#{'${elasticsearch.index.name.drivers}'}")
  private String driversRawIndexName;

  // @Value("#{'${elasticsearch.index.name.enriched-drivers}'}")
  // private String driversEnrichedIndexName;

  @Value("#{'${elasticsearch.pipeline.name}'}")
  private String pipelineName;

  public LogsDataNonDecrypt getLogsData(Filters filters) {
    // SearchResponse searchResponse = searchService.read(driversEnrichedIndexName,
    // driverDataQueryProvider.getQuery(filters),
    // driverAggregationProvider.getAggregations());
    System.out.println("nama index :" + driversRawIndexName);

    SearchResponse searchResponse = searchService.read(driversRawIndexName,
        driverDataQueryProvider.getQuery(filters));
    return logAggregationResultMapperNonDecrypt.map(searchResponse);
  }
}
