package elasticsearchdecryptor.logs.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class LogsDataNonDecrypt {
  // int count;
  List<LogInfoNonDecrypt> logs;
  // Map<String, NationalityInfo> byNationality;
  // Map<String, ShortDriverInfoWithTotal> byTitles;
  // Map<String, ShortDriverInfoWithTotal> byActive;
}
