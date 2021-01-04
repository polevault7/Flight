package tech.itpark.test.model;

import lombok.Value;

@Value
public class Ticket {
    long id;
    Flight flight;
    Passenger passenger;
}
