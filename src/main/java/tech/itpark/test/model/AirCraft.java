package tech.itpark.test.model;

import lombok.Value;

@Value
public class AirCraft {
    long id;
    AirLine airline;
    String model;
    int capacity;

}

