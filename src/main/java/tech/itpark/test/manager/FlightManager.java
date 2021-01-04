package tech.itpark.test.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import tech.itpark.test.exception.NotFoundException;
import tech.itpark.test.mapper.FlightRowMapper;
import tech.itpark.test.model.Flight;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class FlightManager {
    private final NamedParameterJdbcTemplate template;
    private final FlightRowMapper rowMapper = new FlightRowMapper();

    public List<Flight> getAll() {
        return template.query("select id , aircraft_id, source_city, destination_city, departure_time, arrival_time," +
                " journey_duration, price  from flights order by id limit 50", rowMapper
        );

    }

    public Flight getById(long id) {
        return template.queryForObject("select id, aircraft_id, source_city," +
                        " destination_city, departure_time, arrival_time,journey_duration, price" +
                        " from flights where id = :id",
                Map.of("id", id),
                rowMapper
        );
    }

    public List<Flight> getByAircraftId(long aircraftId) {
        return template.query(
                "select id, aircraft_id, source_city," +
                        " destination_city, departure_time, arrival_time,journey_duration, price" +
                        " from flights where aircraft_id = :aircraft_id",
                Map.of("aircraft_id", aircraftId),
                rowMapper
        );
    }

    public Flight save(Flight item) {
        if (item.getId() == 0) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            template.update("insert into flights(aircraft_id, source_city, destination_city," +
                            " departure_time, arrival_time, journey_duration, price)" +
                            " VALUES (:aircraft_id, :source_city, :destination_city, :departure_time, :arrival_time," +
                            " :journey_duration, :price)",
                    new MapSqlParameterSource(Map.of(
                            "aircraft_id", item.getAircraftId(),
                            "source_city", item.getSourceCity(),
                            "destination_city", item.getDestCity(),
                            "departure_time", item.getDepartureTime(),
                            "arrival_time", item.getArrivalTime(),
                            "journey_duration", item.getJourneyDuration(),
                            "price", item.getPrice()
                    )),
                    keyHolder
            );
            long id = keyHolder.getKey().longValue();
            return getById(id);
        }
        template.update(
                "update flights set aircraft_id = :aircraft_id," +
                        " source_city = :source_city, destination_city= :destination_city," +
                        " departure_time = :departure_time, arrival_time = :arrival_time," +
                        " journey_duration = :journey_duration, price = :price where id = :id",
                Map.of(
                        "aircraft_id", item.getAircraftId(),
                        "source_city", item.getSourceCity(),
                        "destination_city", item.getDestCity(),
                        "departure_time", item.getDepartureTime(),
                        "arrival_time", item.getArrivalTime(),
                        "journey_duration", item.getJourneyDuration(),
                        "price", item.getPrice()
                )
        );
        return getById(item.getId());
    }

    public Flight removeById(long id) {
        Flight item = getById(id);

        template.update(
                "delete from tickets where flight_id = :id",
                Map.of("id", item.getId())
        );

        template.update(
                "delete from flights where id = :id",
                Map.of("id", item.getId())
        );
        return item;
    }

    public List<Flight> search(String sourceCity, String destCity, String departureTime) {
        if (sourceCity.isEmpty()) {
            throw new NotFoundException();
        }

        if (destCity.isEmpty()) {
            throw new NotFoundException();
        }
        sourceCity = sourceCity.toUpperCase();
        destCity = destCity.toUpperCase();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate date = LocalDate.parse(departureTime, dtf);
        long millisInUtc = date.atStartOfDay(ZoneOffset.UTC)
                .toInstant()
                .toEpochMilli();
        return template.query(
                "select id, aircraft_id, source_city," +
                        " destination_city, departure_time, arrival_time,journey_duration, price" +
                        " from flights where source_city like :source_city and destination_city like :destination_city" +
                        " and departure_time = :departure_time order by price  limit 10",
                new MapSqlParameterSource(Map.of(
                        "source_city", "%" + sourceCity + "%",
                        "destination_city", "%" + destCity + "%",
                        "departure_time", millisInUtc
                )), new FlightRowMapper()
        );

    }

    public List<Flight> searchReturnTicket(
            String sourceCity,
            String destCity,
            String departureTime,
            String departureTimeBack) {

        final var flights = search(sourceCity, destCity, departureTime);
        flights.addAll(search(destCity, sourceCity, departureTimeBack));
        return flights;
    }

    public List<Flight> sortByPrice(String sourceCity, String destCity, String departureTime) {
        List<Flight> result = search(sourceCity, destCity, departureTime);
        result.sort((o1, o2) -> -(o1.getPrice() - o2.getPrice()));
        return result;
    }

    public List<Flight> sortByJourneyDuration(String sourceCity, String destCity, String departureTime) {
        List<Flight> result = search(sourceCity, destCity, departureTime);
        result.sort(Comparator.comparingLong(Flight::getJourneyDuration));
        return result;
    }

    private Flight mapRow(ResultSet rs, int rowNum) throws SQLException {
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
