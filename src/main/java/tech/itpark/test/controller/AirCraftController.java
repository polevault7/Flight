package tech.itpark.test.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tech.itpark.test.manager.AirCraftManager;
import tech.itpark.test.model.AirCraft;


import java.util.List;

@RestController
@RequestMapping("/airCrafts")
@RequiredArgsConstructor
public class AirCraftController {
    private final AirCraftManager manager;

    @GetMapping
    public List<AirCraft> getAll() {
        return manager.getAll();
    }

    @GetMapping("/{id}")
    public AirCraft getById(@PathVariable long id) {
        return manager.getById(id);
    }

    @PostMapping
    public AirCraft save(@RequestBody AirCraft item) {
       return manager.save(item);
    }

    @DeleteMapping("{id}")
    public AirCraft removeById(@PathVariable long id) {
       return manager.removeById(id);
    }

    @GetMapping("/search")
    public List<AirCraft> search(@RequestParam String airLine){
        return manager.search(airLine);
    }
}
