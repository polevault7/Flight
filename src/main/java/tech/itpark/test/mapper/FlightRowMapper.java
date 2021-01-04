package tech.itpark.test.mapper;

import org.springframework.jdbc.core.RowMapper;
import tech.itpark.test.model.Flight;


import java.sql.ResultSet;
import java.sql.SQLException;

public class FlightRowMapper implements RowMapper<Flight> {

    public Flight mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Flight(
                rs.getLong("id"),
                rs.getLong("aircraft_id"),
                rs.getString("source_city"),
                rs.getString("destination_city"),
                rs.getLong("departure_time"),
                rs.getLong("arrival_time"),
                rs.getLong("journey_duration"),
                rs.getInt("price")
        );
    }
}
