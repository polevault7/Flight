package tech.itpark.test.model;

import lombok.Value;

@Value
public class Passenger {
    long id;
    String name;
    int age;
    long passport;
    String registration;
    boolean luggage;
}
