package elasticsearchdecryptor.logs.mapper;

import org.springframework.jdbc.core.RowMapper;

import elasticsearchdecryptor.logs.model.BsmApiConfig;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BsmApiConfigRowMapper implements RowMapper<BsmApiConfig> {
    @Override
    public BsmApiConfig mapRow(ResultSet rs, int rowNum) throws SQLException {

        BsmApiConfig bsmApiConfig = new BsmApiConfig();
        bsmApiConfig.setKeyname(rs.getString("KEYNAME"));
        bsmApiConfig.setValue(rs.getString("VALUE"));

        return bsmApiConfig;

    }
}
