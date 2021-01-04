package tech.itpark.test.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import tech.itpark.test.exception.NotFoundException;
import tech.itpark.test.mapper.TicketRowMapper;
import tech.itpark.test.model.Ticket;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class TicketManager {
    private final NamedParameterJdbcTemplate template;
    private final TicketRowMapper rowMapper = new TicketRowMapper();

    public List<Ticket> getAllByFlightId(long flightId) {
        return template.query(
                "select tickets.id as ticket_id, flight_id , f.aircraft_id, f.source_city, f.destination_city," +
                        " f.departure_time, f.arrival_time, f.journey_duration, f.price, tickets.passenger_id," +
                        " p.id, p.name, p.age, p.registration, p.passport, p.luggage" +
                        " from tickets" +
                        " inner join flights f on tickets.flight_id = f.id" +
                        " inner join passengers p on p.id = tickets.passenger_id where flight_id = :flight_id",
                Map.of("flight_id", flightId),
                rowMapper
        );
    }

    public Ticket getById(long id) {
        return template.queryForObject("select tickets.id as ticket_id, tickets.flight_id," +
                        " f.aircraft_id, f.source_city, f.destination_city, f.departure_time, f.arrival_time," +
                        " f.journey_duration, f.price, tickets.passenger_id," +
                        " p.id, p.name, p.age, p.registration, p.passport, p.luggage" +
                        " from tickets" +
                        " inner join flights f on f.id = tickets.flight_id" +
                        " inner join passengers p on p.id = tickets.passenger_id where tickets.id = :tickets.id",
                Map.of("tickets.id", id),
                rowMapper
        );
    }

    public Ticket save(Ticket item) {
        if (item.getId() == 0) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            template.update("insert into tickets(flight_id, passenger_id)" +
                            " values ( :flight_id, :passenger_id ) ",
                    new MapSqlParameterSource(Map.of(
                            "flight_id", item.getFlight().getId(),
                            "passenger_id", item.getPassenger().getId()
                    )),
                    keyHolder
            );
            long id = keyHolder.getKey().longValue();
            return getById(id);
        }
        template.update("update tickets set flight_id = :flight_id, passenger_id = :passenger_id" +
                        " where id = :id",
                Map.of(
                        "flight_id", item.getFlight().getId(),
                        "passenger_id", item.getPassenger().getId(),
                        "id", item.getId()
                )
        );
        return getById(item.getId());
    }

    public Ticket removeById(long ticketId) {
        Ticket item = getById(ticketId);

        template.update("delete from tickets where id = :id",
                Map.of("id", item.getId())
        );
        return item;
    }

    public List<Ticket> search(String passengerName) {
        if (passengerName.isEmpty()) {
            throw new NotFoundException();
        }
        passengerName = passengerName.toUpperCase();

        return template.query(
                "select tickets.id, flight_id, passenger_id, aircraft_id, source_city, destination_city, departure_time, arrival_time," +
                        "journey_duration, price," +
                        " p.id, p.name, p.age, p.registration, p.passport, p.luggage" +
                        " from tickets inner join flights f on f.id = tickets.flight_id" +
                        " inner join passengers p on p.id = tickets.passenger_id" +
                        "  where p.name like :p.name",
                new MapSqlParameterSource(Map.of(
                        "p.name", "%" + passengerName + "%"
                )), new TicketRowMapper()
        );
    }


}
