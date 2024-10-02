package vls;


public class Vehicle {
    private String plateNo;
    private String brand;
    private String model;

    public Vehicle(String plateNo, String brand, String model) {
        this.plateNo = plateNo;
        this.brand = brand;
        this.model = model;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }
}
