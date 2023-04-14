package elasticsearchdecryptor.logs.statistics;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
class StatisticsData {
  String driverId;
  int races;
  int wins;
  int titles;
}
