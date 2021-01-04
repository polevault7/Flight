package tech.itpark.test.model;

import lombok.Value;

@Value
public class Flight {
    long id;
    long aircraftId;
    String sourceCity;
    String destCity;
    long departureTime;
    long arrivalTime;
    long journeyDuration;
    int price;

}
