package tech.itpark.test.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tech.itpark.test.manager.FlightManager;
import tech.itpark.test.manager.TicketManager;
import tech.itpark.test.model.Flight;
import tech.itpark.test.model.Ticket;

import java.util.List;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketManager manager;

    @GetMapping("/byFlight/{flightId}")
    public List<Ticket> getAll(@PathVariable long flightId) {
        return manager.getAllByFlightId(flightId);
    }

    @GetMapping("{ticketId}")
    public Ticket getById(@PathVariable long ticketId) {
        return manager.getById(ticketId);
    }

    @PostMapping
    public Ticket save(@RequestBody Ticket item) {
        return manager.save(item);
    }

    @DeleteMapping("/{ticketId}")
    public Ticket removeById(@PathVariable long ticketId) {
        return manager.removeById(ticketId);
    }

    @GetMapping("/search")
    public List<Ticket> search(
            @RequestParam String passengerName
    ) {
        return manager.search(passengerName);
    }
}
