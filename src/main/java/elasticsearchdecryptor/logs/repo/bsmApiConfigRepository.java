package elasticsearchdecryptor.logs.repo;

import elasticsearchdecryptor.logs.driver.JdbcConfiguration;
import elasticsearchdecryptor.logs.model.BsmApiConfig;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import org.springframework.stereotype.Repository;

import org.springframework.jdbc.core.JdbcTemplate;

@Repository
public class bsmApiConfigRepository {

    // @Autowired
    // JdbcTemplate template;
    static JdbcTemplate jdbcTemplate;

    /* Getting a specific item by item id from table */
    public BsmApiConfig getbsmApiConfig(String keyname) {
        jdbcTemplate = new JdbcTemplate(JdbcConfiguration.mysqlDataSource());

        System.out.println("Reading Item From Repository..");
        String query = "SELECT VALUE,KEYNAME FROM bsm_api_config WHERE KEYNAME=?";

        return jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<>(BsmApiConfig.class),
                new Object[] { keyname });
    }

    /* delete an item from database */
    // public int deleteItem(int id){
    // String query = "DELETE FROM ITEM WHERE ID =?";
    // int size = template.update(query,id);
    // return size;
    // }

    // /*update an item from database*/
    // public void updateItem(Item item){
    // String query = "UPDATE ITEM SET name=?, category=? WHERE id =?";
    // template.update(query,
    // new Object[] {
    // item.getName(),item.getCategory(), Integer.valueOf(item.getId())
    // });
    // }

}
