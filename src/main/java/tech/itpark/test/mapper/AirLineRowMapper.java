package tech.itpark.test.mapper;

import org.springframework.jdbc.core.RowMapper;
import tech.itpark.test.model.AirLine;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AirLineRowMapper implements RowMapper<AirLine> {
    @Override
    public AirLine mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new AirLine(
                rs.getLong("id"),
                rs.getString("name")
        );
    }
}
