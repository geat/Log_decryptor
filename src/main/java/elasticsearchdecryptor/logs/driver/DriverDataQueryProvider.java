package elasticsearchdecryptor.logs.driver;

import lombok.RequiredArgsConstructor;

import org.apache.lucene.spatial3d.geom.Tools;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Component;

import elasticsearchdecryptor.logs.api.Filters;

import java.time.Clock;
import java.time.LocalDate;

import static java.time.LocalDate.of;
import static java.time.Month.DECEMBER;
import static java.time.Month.JANUARY;
import static java.time.YearMonth.now;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

@Component
@RequiredArgsConstructor
class DriverDataQueryProvider {

  private static final int LAST_DAY_OF_MONTH = 31;
  private static final int FIRST_DAY_OF_MONTH = 1;

  QueryBuilder getQuery(Filters filters) {
    BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

    if (nonNull(filters.getSessionId())) {

      boolQueryBuilder.filter(matchQuery("sessionId", filters.getSessionId()));

    }

    if (nonNull(filters.getEventType())) {

      boolQueryBuilder.filter(matchQuery("eventType", filters.getEventType()));

    }
    // filter service name or group service case sensitif
    if (nonNull(filters.getService())) {
      if (filters.getService().toUpperCase().intern() != "ALL" && filters.getService() != "") {
        System.out.println(filters.getService());
        boolQueryBuilder.must(QueryBuilders.wildcardQuery("operationName", "*" + filters.getService() + "*"));

      }

    }

    if (nonNull(filters.getApiName())) {
      if (filters.getApiName().toUpperCase().intern() != "ALL") {
        boolQueryBuilder.filter(matchQuery("apiName", filters.getApiName()));
      }

    }

    if (nonNull(filters.getEventType())) {

      boolQueryBuilder.filter(matchQuery("eventType", filters.getEventType()));

    }
    if (nonNull(filters.getFrom())) {
      // System.out.println(filters.getFrom() + " to " + filters.getTo());
      boolQueryBuilder
          .filter(rangeQuery("creationDate").gte(filters.getFrom()).lte(filters.getTo()).format("epoch_millis"));
    }

    if (nonNull(filters.getVersion())) {
      if (filters.getVersion().toUpperCase().intern() != "ALL") {
        System.out.println("filter version");
        boolQueryBuilder
            .filter(matchQuery("apiVersion", filters.getVersion()));
      }
    }

    if (nonNull(filters.getStatus())) {

      System.out.println("filter Status");
      boolQueryBuilder
          .filter(matchQuery("status", filters.getStatus()));

    }
    return boolQueryBuilder;

  }

  private LocalDate beginOfYear(Integer year) {
    return of(year, JANUARY, FIRST_DAY_OF_MONTH);
  }

  private LocalDate endOfYear(int year) {
    return of(year, DECEMBER, LAST_DAY_OF_MONTH);
  }
}
