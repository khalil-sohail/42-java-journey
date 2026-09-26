package fr._42.reflection.classes;

import java.util.StringJoiner;

// Car
// PMW
// V4
// 2022
// model
// V8
// grow(int)

public class Car {
    private String  brand;
    private String  model;
    private int     year;

    public Car() {
        this.brand = "Default brand";
        this.model = "Default model";
        this.year = 0;
    }

    public Car(String brand, String model, int year) {
        this.brand = brand;
        this.model = model;
        this.year = year;
    }

    public int grow(int passedYears) {
        this.year += passedYears;
        return year;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Car.class.getSimpleName() + "[", "]")
                    .add("brand='" + brand + "'")
                    .add("model='" + model + "'")
                    .add("year=" + year)
                    .toString();
    }
}
