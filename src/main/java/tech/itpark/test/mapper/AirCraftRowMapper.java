package tech.itpark.test.mapper;

import org.springframework.jdbc.core.RowMapper;
import tech.itpark.test.model.AirCraft;
import tech.itpark.test.model.AirLine;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AirCraftRowMapper implements RowMapper<AirCraft> {
    @Override
    public AirCraft mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new AirCraft(
                rs.getLong("id"),
                new AirLine(
                        rs.getLong("airline_id"),
                        rs.getString("name")
                ),
                rs.getString("model"),
                rs.getInt("capacity")
        );
    }
}
