package tech.itpark.test.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import tech.itpark.test.exception.NotFoundException;
import tech.itpark.test.mapper.AirLineRowMapper;
import tech.itpark.test.model.AirLine;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AirLineManager {
    private final NamedParameterJdbcTemplate template;
    private final AirLineRowMapper rowMapper = new AirLineRowMapper();

    public List<AirLine> getAll() {
        return template.query(
                "select id, name from airLines order by id limit 10",
                rowMapper
        );
    }

    public AirLine getById(long id) {
        return template.queryForObject(
                "select id, name from airLines where id = :id",
                Map.of("id", id),
                rowMapper
        );
    }

    public AirLine save(AirLine item) {
        if (item.getId() == 0) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            template.update(
                    "insert into airLines (name) values (:name)",
                    new MapSqlParameterSource(
                            Map.of(
                                    "name", item.getName()
                            )),
                    keyHolder
            );

            long id = keyHolder.getKey().longValue();
            return getById(id);
        }
        template.update(
                "update airLines set name = :name where id = :id",
                Map.of(
                        "name", item.getName(),
                        "id", item.getId()
                )
        );
        return getById(item.getId());
    }

    public AirLine removeById(long id) {
        AirLine item = getById(id);
        template.update(
                "delete from airLines where id = :id",
                Map.of(
                        "id", item.getId()
                )
        );
        return item;
    }

    public List<AirLine> search(String airLine) {
        if (airLine.isEmpty()) {
            throw new NotFoundException("String is empty.\nPlease type at least one character");
        }
       return template.query(
                "select id, name from airLines where airLines.name like :name",
                new MapSqlParameterSource(
                        Map.of(
                                "name", "%" + airLine + "%"
                        )),
                        new AirLineRowMapper()
                );

    }
}
