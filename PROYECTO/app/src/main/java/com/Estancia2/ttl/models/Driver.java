package com.Estancia2.ttl.models;

public class Driver {
    String id;
    String name;
    String email;
    String vehicleName;
    String vehiclePlate;

    public Driver(String id, String name, String email, String vehicleName, String vehiclePlate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.vehicleName = vehicleName;
        this.vehiclePlate = vehiclePlate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getVehiclePlate() {
        return vehiclePlate;
    }

    public void setVehiclePlate(String vehiclePlate) {
        this.vehiclePlate = vehiclePlate;
    }
}
