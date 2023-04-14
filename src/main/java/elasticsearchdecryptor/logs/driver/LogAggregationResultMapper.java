package elasticsearchdecryptor.logs.driver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import elasticsearchdecryptor.logs.elastic.ElasticsearchReadException;
import elasticsearchdecryptor.logs.model.LogInfo;
import elasticsearchdecryptor.logs.model.LogsData;
import lombok.RequiredArgsConstructor;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;

import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Component
@RequiredArgsConstructor
class LogAggregationResultMapper {

  private static final TypeReference<LogInfo> DRIVER_DATA_TYPE_REFERENCE = new TypeReference<>() {
  };

  private final ObjectMapper objectMapper;

  LogsData map(SearchResponse searchResponse) {
    Aggregations aggregations = searchResponse.getAggregations();
    return LogsData.builder()
        .logs(getLogsList(searchResponse))
        // .byNationality(getByNationalityResults(aggregations.get("byNationality")))
        // .byTitles(getByTitlesResults(aggregations.get("byTitles")))
        // .byActive(getByActiveResults(aggregations.get("byActivity")))
        // .count(calculateCount(searchResponse))
        .build();
  }

  private List<LogInfo> getLogsList(SearchResponse searchResponse) {
    return stream(searchResponse.getHits().getHits())

        .map(hit -> toOutputObject(hit, DRIVER_DATA_TYPE_REFERENCE))
        // .sorted(Comparator.comparing(LogInfo::getDriverId))
        .sorted(Comparator.comparing(LogInfo::getApiId))
        .collect(toList());

  }

  // private Map<String, NationalityInfo> getByNationalityResults(Terms
  // aggregation) {
  // return aggregation.getBuckets().stream()
  // .collect(toMap(MultiBucketsAggregation.Bucket::getKeyAsString,
  // this::mapToNationalityResult, (a, b) -> b, TreeMap::new));
  // }

  // private NationalityInfo mapToNationalityResult(Terms.Bucket bucket) {
  // Sum totalTitles = (Sum) bucket.getAggregations().get("totalTitles");
  // Sum totalWins = (Sum) bucket.getAggregations().get("totalWins");
  // Avg avgRaces = (Avg) bucket.getAggregations().get("avgRaces");
  // TopHits byHits = (TopHits) bucket.getAggregations().get("byHits");

  // return NationalityInfo.builder()
  // .titles(((int) totalTitles.getValue()))
  // .wins((int) totalWins.getValue())
  // .count(((int) bucket.getDocCount()))
  // .avgRaces((int) avgRaces.getValue())
  // .drivers(getDriverInfos(byHits))
  // .build();
  // }

  // private List<ShortDriverInfo> getDriverInfos(TopHits byHits) {
  // return stream(byHits.getHits().getHits())
  // .map(it -> toOutputObject(it, DRIVER_INFO_TYPE_REFERENCE))
  // .collect(toList());
  // }

  private <T> T toOutputObject(SearchHit hit, TypeReference<T> typeRef) {
    try {
      return objectMapper.readValue(hit.getSourceAsString(), typeRef);

    } catch (JsonProcessingException e) {
      throw new ElasticsearchReadException(e);
    }
  }

  private int calculateCount(SearchResponse searchResponse) {
    return searchResponse.getHits().getHits().length;
  }

  // private Map<String, ShortDriverInfoWithTotal> getByTitlesResults(Terms
  // aggregation) {
  // return aggregation.getBuckets().stream()
  // .collect(toMap(MultiBucketsAggregation.Bucket::getKeyAsString,
  // this::mapToTitlesResult, (a, b) -> b, TreeMap::new));
  // }

  // private ShortDriverInfoWithTotal mapToTitlesResult(Terms.Bucket bucket) {
  // TopHits byHits = (TopHits) bucket.getAggregations().get("byHits");
  // List<ShortDriverInfo> shortDriverInfos = getDriverInfos(byHits);

  // return ShortDriverInfoWithTotal.builder()
  // .count(((int) bucket.getDocCount()))
  // .drivers(shortDriverInfos)
  // .build();
  // }

  // private Map<String, ShortDriverInfoWithTotal> getByActiveResults(Terms
  // aggregation) {
  // return aggregation.getBuckets().stream()
  // .collect(toMap(this::mapKey, this::mapToTitlesResult, (a, b) -> b,
  // TreeMap::new));
  // }

  private String mapKey(Terms.Bucket bucket) {
    return ((long) bucket.getKey()) == 1L ? "active" : "inactive";
  }

}
