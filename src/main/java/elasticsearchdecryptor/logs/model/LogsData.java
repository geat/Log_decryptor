package elasticsearchdecryptor.logs.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class LogsData {
  // int count;
  List<LogInfo> logs;
  // Map<String, NationalityInfo> byNationality;
  // Map<String, ShortDriverInfoWithTotal> byTitles;
  // Map<String, ShortDriverInfoWithTotal> byActive;
}
