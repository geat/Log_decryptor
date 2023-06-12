package elasticsearchdecryptor.logs.cache;

import elasticsearchdecryptor.logs.driver.JdbcConfiguration;
import elasticsearchdecryptor.logs.model.BsmApiConfig;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.stereotype.Component;

@Component
public class BsmSecretKeyCache {
    static JdbcTemplate jdbcTemplate;

    @Cacheable(value = "bsmSecretKeyCache", key = "#keyname")
    public BsmApiConfig getBsmApiConfig(String keyname) throws Exception {

        System.out.println("Query To DB bsmSecretKeyCache :");

        jdbcTemplate = new JdbcTemplate(JdbcConfiguration.mysqlDataSource());
        String query = "SELECT VALUE,KEYNAME FROM bsm_api_config WHERE KEYNAME=?";

        return jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<>(BsmApiConfig.class),
                new Object[] { keyname });
    }

}
