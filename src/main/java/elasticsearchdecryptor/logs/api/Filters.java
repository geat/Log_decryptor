package elasticsearchdecryptor.logs.api;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Filters {

  String eventType;
  String apiName;
  String version;
  String from;
  String to;
  String sessionId;
  String status;
  String service;
}
