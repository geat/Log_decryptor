package elasticsearchdecryptor.logs.statistics;

import java.util.List;

interface StatisticsRepository {
  List<StatisticsData> getAll();
}
