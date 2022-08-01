package com.Estancia2.ttl.models;

public class Driver {
    String id;
    String name;
    String phone;
    String email;
    String vehiclePlate;

    public Driver(){

    }

    public Driver(String id, String name,String phone, String email, String vehiclePlate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.vehiclePlate = vehiclePlate;
    }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

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


    public String getVehiclePlate() {
        return vehiclePlate;
    }

    public void setVehiclePlate(String vehiclePlate) {
        this.vehiclePlate = vehiclePlate;
    }
}
