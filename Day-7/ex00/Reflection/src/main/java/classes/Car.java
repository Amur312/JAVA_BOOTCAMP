package classes;


public class Car {
    private String model;
    private String color;
    private int year;

    public Car() {
        this.model = "Default model";
        this.color = "Default color";
        this.year = 0;
    }

    public Car(String model, String color, int year) {
        this.model = model;
        this.color = color;
        this.year = year;
    }

    public void drive() {
        System.out.println("Car " + model + " is driving!");
    }

    public void stop() {
        System.out.println("Car " + model + " stopped.");
    }

    @Override
    public String toString() {
        return "Car: " +
                "model - " + model +
                ", color - " + color +
                ", year - " + year;
    }

}
