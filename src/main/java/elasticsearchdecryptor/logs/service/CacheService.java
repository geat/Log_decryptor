package elasticsearchdecryptor.logs.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import elasticsearchdecryptor.logs.driver.JdbcConfiguration;
import elasticsearchdecryptor.logs.model.BsmApiConfig;
import elasticsearchdecryptor.logs.model.CorpSecretKeyData;
import javax.sql.DataSource;
@Service

public class CacheService {
    static JdbcTemplate jdbcTemplate;
    
    public CacheService(DataSource dataSource) {
        System.out.println("data source ambil dari application");
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    /*
     * list table yang di gunakan (bsm_api_config,bsm_api_secret,bsm_api_key)
     * pengambilan bsmsecretKey untuk decrypt CorpSecretkey per corp_id / apiKey
     */
    @Cacheable(value = "bsmSecretKeyCache", key = "#keyname")
    public BsmApiConfig getBsmApiConfig(String keyname) throws Exception {
    
        System.out.println("Query To DB getBsmApiConfig: jdbc setting from application.xml");
    
        // jdbcTemplate = new JdbcTemplate(JdbcConfiguration.mysqlDataSource());
        String query = "SELECT VALUE,KEYNAME FROM bsm_api_config WHERE KEYNAME=?";
    
        
        try {
            return jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<>(BsmApiConfig.class),
                    new Object[] { keyname });
    
        } catch (EmptyResultDataAccessException e) {
            /* Untuk secret key yang tidak ada di database akan di set null pada cache */
            BsmApiConfig bac_data = new BsmApiConfig();
            bac_data.setKeyname(keyname);
            bac_data.setValue(null);
            return bac_data;
        }
    }
    // pengambilan corp secret key permasing masing app name

    @Cacheable(value = "CorpSecretKeyCache", key = "#app_name")
    public CorpSecretKeyData getCorpSecretKeyData(String app_name) throws Exception {

         System.out.println("Query To DB getCorpSecretKeyData:");

        // jdbcTemplate = new JdbcTemplate(JdbcConfiguration.mysqlDataSource());
        String query = "SELECT bsm_api_key.APP_NAME,bsm_api_secret.SECRET_KEY FROM bsm_api_key join bsm_api_secret on bsm_api_key.CORP_ID = bsm_api_secret.CORP_ID where  LOWER(APP_NAME) = ? limit 1";

        try {
            return jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<>(CorpSecretKeyData.class),
                    new Object[] { app_name });

        } catch (EmptyResultDataAccessException e) {
            /* Untuk secret key yang tidak ada di database akan di set null pada cache */
            CorpSecretKeyData csk_data = new CorpSecretKeyData();
            csk_data.setApp_name(app_name);
            csk_data.setSecret_key(null);
            return csk_data;
        }

    }

}
