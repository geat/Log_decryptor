package elasticsearchdecryptor.logs.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import elasticsearchdecryptor.logs.driver.JdbcConfiguration;
import elasticsearchdecryptor.logs.model.BsmApiConfig;
import elasticsearchdecryptor.logs.model.CorpSecretKeyData;

@Service

public class CacheService {
    static JdbcTemplate jdbcTemplate;

    @Cacheable(value = "bsmSecretKeyCache", key = "#keyname")
    public BsmApiConfig getBsmApiConfig(String keyname) throws Exception {

        System.out.println("Query To DB :");

        jdbcTemplate = new JdbcTemplate(JdbcConfiguration.mysqlDataSource());
        String query = "SELECT VALUE,KEYNAME FROM bsm_api_config WHERE KEYNAME=?";

        return jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<>(BsmApiConfig.class),
                new Object[] { keyname });
    }

    @Cacheable(value = "CorpSecretKeyCache", key = "#app_name")
    public CorpSecretKeyData getCorpSecretKeyData(String app_name) throws Exception {

        System.out.println("Query To DB Corp secret key:");

        jdbcTemplate = new JdbcTemplate(JdbcConfiguration.mysqlDataSource());
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
