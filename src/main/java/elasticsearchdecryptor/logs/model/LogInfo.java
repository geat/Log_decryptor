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
import com.google.gson.stream.JsonWriter;

import java.util.HashMap;
import elasticsearchdecryptor.logs.decrypt.Decryptor;
import java.io.StringWriter;
import com.fasterxml.jackson.core.JsonFactory;



@Value
@Builder
public class LogInfo {
  static JdbcTemplate jdbcTemplate;

  String eventType;
  String apiId;
  String apiName;
  String apiVersion;
  String applicationName;
  String httpMethod;

  String id;
  // String queryParameters.Corp_ID;
  String responseCode;
  String reqPayload;
  String resPayload;
  String resPayloadStatusCode;
  String resPayloadStatusMessage;
  // DriverPayload resPayload;
  private JsonNode requestHeaders;
  // String Status_Message;
  Long creationDate;
  // String creationFormatDateFormated;
  String sessionId;
  Long totalTime;
  String ResponseTime;
  private JsonNode queryParameters;
  String QueryParametersDecrypted;
  String ReqPayloadDecrypted;
  String ResPayloadDecrypted;

  String CorpID;
  String transactionID;

  // DriverQueryParameters queryParameters;

  // String nilai1;
  String status;
  String operationName;
  String groupService;
  String service;

  // get service name from operationname
  public String getGroupService() {
    String string = operationName;
    String[] split = string.split("/");

    String value = split[1];

    return value;
  }

  public String getResPayload(){
    
    // limit biar ga penuh
    if (resPayload != null && resPayload.length() > 2000) {
      
      return resPayload.substring(0, 2000).toString();
    } else {
      System.out.println(resPayload);
        return resPayload.toString();
    }
  }
  // get service name from operationname
  public String getService() {
    String string = operationName;
    String last = string.substring(string.lastIndexOf('/') + 1);
    System.out.println(sessionId + " | " + apiName);
    return last;
  }

  
  public String getJsonStringDecrypt(String stringJson) {
    Boolean decrypt = false; // set decrypt false dulu

    if (apiVersion.toUpperCase().intern() != "3.0" || decrypt != false) {
      // System.out.println(sessionId + " | " + apiName + " | " + resPayload);
      // System.out.println(sessionId + " | " + apiName);
      String app_name = applicationName;

      String json = stringJson;
      /* List service yang tidak perlu di decrypt respayloadnya */
      Set<String> notDecrptedService = new HashSet<String>(
          Arrays.asList(new String[] { "signatureX", "generateTokenJWT", "signatureServiceSnap" }));
          // todo_1 list service seharusnya didalam application.yml
      if (!notDecrptedService.contains(apiName)) {
        if (json != null && apiName != "signatureX") {
          if (!json.contains("Exception")) {
            if (isJSONValid(json)) {
              String SecretKey = getSecretKeyDecrypted(app_name);
              Map<String, String> map = new LinkedHashMap<String, String>();
              try {
                addKeys("", new ObjectMapper().readTree(json), map, SecretKey);
              } catch (IOException e) {
                e.printStackTrace();
              }
              ObjectMapper mapper = new ObjectMapper();
              String jsonResult = null;
              try {
                jsonResult = mapper.writeValueAsString(map);
              } catch (JsonProcessingException e) {
                e.printStackTrace();
              }
              return jsonResult.substring(0, Math.min(jsonResult.length(), 2000));
              // return jsonResult;
            } else {
              return "Format JSON tidak didukung";
            }
          } else {
            return "Api Gateway response";
          }
        } else {
          return "-";
        }
      } else {
        return "Non Encrypt Log";
      }
    } else {
      return "SNAP VERSION";
    }

  }

// field yang akan di decrypt 
public String getQueryParametersDecrypted() {
    
    String prettyString = queryParameters.toPrettyString();
    return getJsonStringDecrypt(prettyString);
  }
 

  public String getReqPayloadDecrypted() {
    return getJsonStringDecrypt(reqPayload);

  }

  public String getResPayloadDecrypted() {
    return getJsonStringDecrypt(resPayload);
  }

 
  
  private void addKeys(String currentPath, JsonNode jsonNode, Map<String, String> map, String secretKey) {

    if (jsonNode.isObject()) {
      ObjectNode objectNode = (ObjectNode) jsonNode;
      Iterator<Map.Entry<String, JsonNode>> iter = objectNode.fields();
      String pathPrefix = currentPath.isEmpty() ? "" : currentPath + ".";

      while (iter.hasNext()) {
        Map.Entry<String, JsonNode> entry = iter.next();
        addKeys(pathPrefix + entry.getKey(), entry.getValue(), map, secretKey);
      }
    } else if (jsonNode.isArray()) {
      ArrayNode arrayNode = (ArrayNode) jsonNode;
      for (int i = 0; i < arrayNode.size(); i++) {
            addKeys(currentPath + "[" + i + "]", arrayNode.get(i), map, secretKey);
      }
    } else if (jsonNode.isValueNode()) {
      ValueNode valueNode = (ValueNode) jsonNode;

        if (secretKey != null && valueNode.asText().matches("[0-9a-fA-F]+") && valueNode.asText().length() > 16) {
            // Validasi HEX type to decrypt, length must >16 and secret key valid
        Decryptor dc = new Decryptor();
        map.put(currentPath, dc.decryptAES128(valueNode.asText(), secretKey));
      } else {
            map.put(currentPath, valueNode.asText());
      }
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

  // pengambilan Data CORP_SECRET_KEY dinamic / static
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
      /* Decrypt CORP_SECRET_KEY menggunakan KEY BSM_SCRET_KEY
      cek kondisi harus ada semua untuk bsm_secret_key dan corp_secret_key
      */ 

      if (corp_secret_key_encrypted != null && bsm_secret_key != null ) {
        Decryptor dc = new Decryptor();

        corp_secret_key_decrypted = dc.decryptAES128(corp_secret_key_encrypted, bsm_secret_key);
      } else {
        // untuk testing yang tidak ada appnamenya
        // corp_secret_key_decrypted = "m4nd1r1sy4r14hku";
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
