package elasticsearchdecryptor.logs.driver;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration

// data source untuk pengambilan secret key
public class JdbcConfiguration {
    @Bean
    public static DataSource mysqlDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");

        dataSource.setUrl("jdbc:mysql://10.0.0.0:3306/databaseName");
        dataSource.setUsername("");
        dataSource.setPassword("");
        return dataSource;
    }

}