package elasticsearchdecryptor.logs.model;

import lombok.Builder;
import lombok.Value;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import elasticsearchdecryptor.logs.service.ACacheService;
import elasticsearchdecryptor.logs.service.ApplicationContextHolder;

import elasticsearchdecryptor.logs.driver.DriverQueryParameters;
import elasticsearchdecryptor.logs.driver.DriverRequestHeaders;

import java.util.Map;

import org.apache.lucene.analysis.miscellaneous.SetKeywordMarkerFilter;
import org.json.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;
import java.text.*;

import java.util.Iterator;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.fasterxml.jackson.databind.node.ValueNode;

import java.util.HashMap;
import elasticsearchdecryptor.logs.decrypt.Decryptor;

@Value
@Builder
public class LogInfoNonDecrypt {
  static JdbcTemplate jdbcTemplate;

  String eventType;
  String apiId;
  String apiName;
  String apiVersion;
  String applicationName;
  String httpMethod;

  String id;

  String responseCode;
  String reqPayload;
  String resPayload;

  private JsonNode requestHeaders;

  Long creationDate;
  String creationFormatDateFormated;
  String sessionId;
  Long totalTime;
  String ResponseTime;
  String ReqPayloadDecrypted;
  String ResPayloadDecrypted;
  String CorpID;
  String transactionID;
  DriverQueryParameters queryParameters;
  String status;
  String operationName;
  String groupService;
  String service;

  // get service name from operationname
  public String getGroupService() {
    String string = operationName;
    String[] split = string.split("/");

    String value = split[1];

    // System.out.println(value);
    return value;
  }

  // get service name from operationname
  public String getService() {
    String string = operationName;
    String last = string.substring(string.lastIndexOf('/') + 1);
    return last;
  }

  // DriverRequestHeaders requestHeaders;
  // Get CORP_ID untuk filter
  public String getCorpID() {
    String app_name = applicationName;
    Decryptor dc = new Decryptor();
    String namevalue;
    Set<String> notDecrptedService = new HashSet<String>(
        Arrays.asList(new String[] { "signatureX", "generateTokenJWT", "signatureServiceSnap", "signatureBSM" }));
    if (!notDecrptedService.contains(apiName) && apiVersion.toUpperCase().intern() != "3.0") {
      // check type post get from reqbody and decrypt
      if (httpMethod.equals("post") && reqPayload != null) {
        String jsonStr = reqPayload;
        ObjectMapper objectMapper = new ObjectMapper();
        if (jsonStr.contains("Corp_ID")) {
          namevalue = "Corp_ID";
        } else if (jsonStr.contains("corpId")) {
          namevalue = "corpId";
        } else if (jsonStr.contains("CorpID")) {
          namevalue = "CorpID";
        } else {
          namevalue = "false";
        }

        try {
          if (namevalue == "false") {
            return "=";
          } else {
            JsonNode node = objectMapper.readValue(jsonStr, JsonNode.class);
            JsonNode nameNode = node.get(namevalue);
            return dc.decryptAES128(nameNode.asText(), getSecretKeyDecrypted(app_name));
          }

        } catch (IOException e) {
          String a = "-";
          return a;
        }
      } else {
        // type get post
        if (queryParameters.getcorpId() != null) {

          return dc.decryptAES128(queryParameters.getcorpId(),
              getSecretKeyDecrypted(app_name));

        } else {

          return dc.decryptAES128(queryParameters.getCorp_ID(),
              getSecretKeyDecrypted(app_name));
        }

      }
    } else {
      String a = "-";
      return a;
    }

  }

  public String getresponseTime() {
    if (totalTime != null) {
      long onSecond = totalTime / 1000;
      String ResponseTime = (Long.toString(onSecond)) + " Second";
      return ResponseTime;
    } else {
      return "0";
    }

  }

  public String getcreationFormatDateFormated() {
    Date date = new Date(creationDate);
    DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    format.setTimeZone(TimeZone.getTimeZone("GMT"));
    // format.localNow.atZone(ZoneId.of("UTC");
    String formatted = format.format(date);
    return formatted;
  }

  // QueryParameters queryParameters;
  public boolean isJSONValid(String test) {
    try {
      new JSONObject(test);
    } catch (JSONException ex) {
      // edited, to include @Arthur's comment
      // e.g. in case JSONArray is valid as well...
      try {
        new JSONArray(test);
      } catch (JSONException ex1) {
        return false;
      }
    }
    return true;
  }

  /*
   * Get BSM_API_CONFIG from DB CACHEABLE
   * menggunakan manual inject context
   */
  public String getKeyBSM(String keyname) throws Exception {
    ACacheService acs = ApplicationContextHolder.getContext().getBean(ACacheService.class);
    return acs.getBsmApiConfigFromCahce(keyname).getValue();
  }

  /*
   * Get CorpSecretKey cacheable from DB CACHEABLE
   * menggunakan manual inject context
   */
  public String getCorpSecretKeyCacheable(String app_name) throws Exception {
    String appNamex = app_name;
    /* karena ada perbedaan app_name dari gateway dan database */
    if (app_name.equals("demo-SNAP-API")) {
      appNamex = "demo SNAP API";
    }
    ACacheService acs = ApplicationContextHolder.getContext().getBean(ACacheService.class);
    return acs.getCorpSecretKeyData(appNamex).getSecret_key();
  }

  // SIGIT pengambilan Data CORP_SECRET_KEY dinamic / static
  public String getSecretKeyDecrypted(final String APPLICATION_NAME) {
    String corp_secret_key_encrypted = null;
    String corp_secret_key_decrypted = null;
    String secret_key_Hardcode = null;
    // uncoment kode dibawah jika ingin ambil dari DB
    Boolean getSecretkeyFromDB = true;

    // System.out.println(entry.getValue());
    // ambil data BSM_API_CONFIG dari table bsm_api_config
    if (getSecretkeyFromDB == true) {

      String bsm_secret_key;

      try {

        bsm_secret_key = getKeyBSM("BSM_SECRET_KEY");

      } catch (Exception e) {
        bsm_secret_key = null;
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

      // ambil data CORP_SECRET_KEY dari table bsm_api_secret

      try {
        corp_secret_key_encrypted = getCorpSecretKeyCacheable(APPLICATION_NAME);
      } catch (Exception e) {
        // TODO Auto-generated catch block
        corp_secret_key_encrypted = null;
        e.printStackTrace();
      }
      // Decrypt CORP_SECRET_KEY menggunakan KEY BSM_SCRET_KEY
      if (corp_secret_key_encrypted != null) {
        Decryptor dc = new Decryptor();

        corp_secret_key_decrypted = dc.decryptAES128(corp_secret_key_encrypted, bsm_secret_key);
      } else {
        corp_secret_key_decrypted = null;
      }
      return corp_secret_key_decrypted;
    } else {
      return secret_key_Hardcode = "m4nd1r1sy4r14hku";
    }

  }

  public String getresPayloadStatusCode() {

    if (resPayload != null && !resPayload.contains("Exception")) {

      // String json = "{ \"f1\" : \"v1\" } ";
      String json = resPayload;
      String namevalue = null;
      if (json.contains("Status_Code")) {
        namevalue = "Status_Code";
      } else if (json.contains("responseCode")) {
        namevalue = "responseCode";
      } else if (json.contains("statusCode")) {
        namevalue = "statusCode";
      } else if (json.contains("StatusCode")) {
        namevalue = "StatusCode";
      } else {
        namevalue = "ResponseCode";
      }
      if (isJSONValid(json)) {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode;
        try {
          jsonNode = objectMapper.readTree(json);
          // System.out.println(jsonNode.get("Status_Code"));
          if (jsonNode.get(namevalue) != null) {
            String corp_secret_key_decrypted = getSecretKeyDecrypted(applicationName);
            String x = jsonNode.get(namevalue).asText();
            if (x.length() > 16 && corp_secret_key_decrypted != null) {
              Decryptor dc = new Decryptor();
              return dc.decryptAES128(jsonNode.get(namevalue).asText(), corp_secret_key_decrypted);
            } else {

              return jsonNode.get(namevalue).asText();
            }
          }

          return null;

        } catch (JsonMappingException e) {
          // TODO Auto-generated catch block
          return null;
        } catch (JsonProcessingException e) {
          // TODO Auto-generated catch block
          return null;
        }

      }
    }

    // check valid json

    return "{}";

  }

  public String getresPayloadStatusMessage() {

    if (resPayload != null) {
      // String json = "{ \"f1\" : \"v1\" } ";
      String json = resPayload;
      String namevalue = null;
      if (json.contains("Status_Message")) {
        namevalue = "Status_Message";
      } else if (json.contains("responseMessage")) {
        namevalue = "responseMessage";
      } else if (json.contains("statusMessage")) {
        namevalue = "statusMessage";
      } else {
        namevalue = "StatusMessage";
      }
      if (isJSONValid(json)) {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode;
        try {
          jsonNode = objectMapper.readTree(json);
          // System.out.println(jsonNode.get("Status_Message"));
          if (jsonNode.get(namevalue) != null) {

            String x = jsonNode.get(namevalue).asText();
            String corp_secret_key_decrypted = getSecretKeyDecrypted(applicationName);
            if (x.length() > 16 && corp_secret_key_decrypted != null) {
              Decryptor dc = new Decryptor();
              return dc.decryptAES128(jsonNode.get(namevalue).asText(), corp_secret_key_decrypted);
              // return decrypt(jsonNode.get("Status_Message").asText(),
              // corp_secret_key_decrypted);
            } else {

              return jsonNode.get(namevalue).asText();
            }
          }
          
          return null;

        } catch (JsonMappingException e) {
          // TODO Auto-generated catch block
          return null;
        } catch (JsonProcessingException e) {
          // TODO Auto-generated catch block
          return null;
        }

      }
    }

    // check valid json

    return null;

  }

}
