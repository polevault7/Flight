package tech.itpark.test.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import tech.itpark.test.exception.DataAccessException;
import tech.itpark.test.exception.NotFoundException;
import tech.itpark.test.mapper.AirCraftRowMapper;
import tech.itpark.test.mapper.AirLineRowMapper;
import tech.itpark.test.model.AirCraft;


import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AirCraftManager {
    private final NamedParameterJdbcTemplate template;
    private final AirCraftRowMapper rowMapper = new AirCraftRowMapper();

    public List<AirCraft> getAll() {
        return template.query(
                "select airCrafts.id, airline_id, name, model, capacity from airCrafts" +
                        " inner join airLines aL on aL.id = airCrafts.airline_id",
                rowMapper
        );
    }

    public AirCraft getById(long id) {

        return template.queryForObject(
                "select airCrafts.id, airline_id, name, model, capacity from airCrafts" +
                        " inner join airLines aL on aL.id = airCrafts.airline_id where airCrafts.id = :id",
                Map.of("id", id),
                rowMapper
        );
    }

    public AirCraft save(AirCraft item) {
        if (item.getId() == 0) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            template.update("insert into airCrafts(airline_id, model, capacity) values (:airline_id, :model, :capacity)",
                    new MapSqlParameterSource(
                            Map.of(
                                "airline_id", item.getAirline().getId(),
                                    "model", item.getModel(),
                                    "capacity", item.getCapacity()
                            )
                    ),
                    keyHolder
            );
            long id = keyHolder.getKey().longValue();
            return getById(id);
        }
        template.update(
                "update airCrafts set airline_id = :airline_id, model = :model, capacity = :capacity where id = :id",
                Map.of(
                        "airline_id", item.getAirline().getId(),
                        "model", item.getModel(),
                        "capacity", item.getCapacity()
                )
        );
        return getById(item.getId());
    }
//
    public AirCraft removeById(long id) {
        AirCraft item = getById(id);
         template.update(
                "delete from airCrafts where id = :id",
                Map.of(
                        "id", item.getId()
                ));
         return item;
    }

    public List<AirCraft> search(String airline){
        if (airline.isEmpty()){
            throw new NotFoundException("Value is empty.\nPlease type at least once character");
        }
       return template.query("select airCrafts.id, airline_id, model, capacity, name from airCrafts" +
                        " inner join airLines aL on aL.id = airCrafts.airline_id where name like :name",
                new MapSqlParameterSource(
                        Map.of(
                                "name", "%" + airline + "%"
                        )),
                rowMapper
        );
    }


}

