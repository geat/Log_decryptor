package elasticsearchdecryptor.logs.driver;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
class Log {
  String driverId;
  String code;
  String givenName;
  String familyName;
  LocalDate dateOfBirth;
  String nationality;
  boolean active;
  Integer permanentNumber;
}
