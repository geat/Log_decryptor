package elasticsearchdecryptor.logs.driver;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration

// data source untuk pengambilan secret key
public class JdbcConfiguration {
    // @Bean
    public static DataSource mysqlDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://docker.besmart-universal.com:3307/bsmapi");
        dataSource.setUsername("root");
        dataSource.setPassword("CLS.1234");
        return dataSource;
    
    }

}