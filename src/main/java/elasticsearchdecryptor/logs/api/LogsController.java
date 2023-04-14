package elasticsearchdecryptor.logs.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import elasticsearchdecryptor.logs.driver.LogDataProcessor;
import elasticsearchdecryptor.logs.driver.LogDataProcessorNonDecrypt;
import elasticsearchdecryptor.logs.model.BsmApiConfig;
import elasticsearchdecryptor.logs.model.CorpSecretKeyData;
import elasticsearchdecryptor.logs.model.LogsData;
import elasticsearchdecryptor.logs.model.LogsDataNonDecrypt;
import elasticsearchdecryptor.logs.service.ACacheService;
import elasticsearchdecryptor.logs.service.ApplicationContextHolder;
import elasticsearchdecryptor.logs.service.CacheService;

import static org.springframework.http.HttpStatus.OK;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class LogsController {

  private final LogDataProcessor logDataProcessor;
  private final LogDataProcessorNonDecrypt logDataProcessorNonDecrypt;

  // decrypt berdasarkan log dan filter
  @GetMapping("/log")
  ResponseEntity<LogsData> GetLogAll(@RequestParam(required = false) String eventType,
      @RequestParam(required = false) String from,
      @RequestParam(required = false) String to,
      @RequestParam(required = false) String apiName,
      @RequestParam(required = false) String sessionId,
      @RequestParam(required = false) String version,
      @RequestParam(required = false) String status,
      @RequestParam(required = false) String service

  ) {

    Filters filters = Filters.builder()
        .eventType(eventType)
        .apiName(apiName)
        .from(from)
        .to(to)
        .sessionId(sessionId)
        .version(version)
        .status(status)
        .service(service)
        .build();

    log.info("Fetch Logs Elastic  data started");
    LogsData logs = logDataProcessor.getLogsData(filters);
    // System.out.println(logs);
    System.out.println("SUKSES");
    return new ResponseEntity<>(logs, OK);
  }

  // decrypt berdasarkan log dan filter
  @GetMapping("/logNonDecrypt")
  ResponseEntity<LogsDataNonDecrypt> GetLogAllNonDecrypt(@RequestParam(required = false) String eventType,
      @RequestParam(required = false) String from,
      @RequestParam(required = false) String to,
      @RequestParam(required = false) String apiName,
      @RequestParam(required = false) String sessionId,
      @RequestParam(required = false) String version,
      @RequestParam(required = false) String status,
      @RequestParam(required = false) String service

  ) {

    Filters filters = Filters.builder()
        .eventType(eventType)
        .apiName(apiName)
        .from(from)
        .to(to)
        .sessionId(sessionId)
        .version(version)
        .status(status)
        .service(service)
        .build();

    log.info("Fetch Logs Elastic  data started");
    LogsDataNonDecrypt logs = logDataProcessorNonDecrypt.getLogsData(filters);
    // System.out.println(logs);
    return new ResponseEntity<>(logs, OK);
  }

  // @Autowired
  // CacheService aCacheService;

  @GetMapping("/bsmapiconfig/{keyname}")
  @ResponseBody
  public ResponseEntity<BsmApiConfig> getBsmApiConfig2(@PathVariable String keyname) throws Exception {

    ACacheService aCacheService = new ACacheService();
    aCacheService.getBsmApiConfigFromCahce(keyname);
    System.out.println("RestController.." + aCacheService.getBsmApiConfigFromCahce(keyname));
    return new ResponseEntity<BsmApiConfig>(aCacheService.getBsmApiConfigFromCahce(keyname), HttpStatus.OK);
  }

  // @Autowired
  // ACacheService injectCache;

  @GetMapping("/bsmapiconfig2/{keyname}")
  @ResponseBody
  public ResponseEntity<BsmApiConfig> getBsmApiConfig1(@PathVariable String keyname) throws Exception {
    ACacheService acs = ApplicationContextHolder.getContext().getBean(ACacheService.class);
    acs.getBsmApiConfigFromCahce(keyname);
    System.out.println("RestController.." + acs.getBsmApiConfigFromCahce(keyname));
    return new ResponseEntity<BsmApiConfig>(acs.getBsmApiConfigFromCahce(keyname), HttpStatus.OK);
  }

  @GetMapping("/corpsecretkey/{app_name}")
  @ResponseBody
  public ResponseEntity<CorpSecretKeyData> getCorpSecretKeyData(@PathVariable String app_name) throws Exception {
    ACacheService acs = ApplicationContextHolder.getContext().getBean(ACacheService.class);
    acs.getCorpSecretKeyData(app_name);
    System.out.println("RestController.." + acs.getCorpSecretKeyData(app_name));
    return new ResponseEntity<CorpSecretKeyData>(acs.getCorpSecretKeyData(app_name), HttpStatus.OK);
  }

}
