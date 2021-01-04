package tech.itpark.test.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tech.itpark.test.manager.AirLineManager;
import tech.itpark.test.model.AirLine;

import java.util.List;

@RestController
@RequestMapping("/airlines")
@RequiredArgsConstructor
public class AirLineController {
    private final AirLineManager manager;

    @GetMapping
    public List<AirLine> getAll() {
        return manager.getAll();
    }

    @GetMapping("/{id}")
    public AirLine getById(@PathVariable long id) {
        return manager.getById(id);
    }

    @PostMapping
    public AirLine save(@RequestBody AirLine item) {
        return manager.save(item);
    }

    @DeleteMapping("/{id}")
    public AirLine removeById(@PathVariable long id) {
        return manager.removeById(id);
    }

    @GetMapping("/search")
    public List<AirLine> search(@RequestParam String airLine) {
        return manager.search(airLine);
    }
}