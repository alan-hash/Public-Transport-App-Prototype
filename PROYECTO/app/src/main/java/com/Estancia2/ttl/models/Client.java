package com.Estancia2.ttl.models;

public class Client {
    String id;
    String name;
    String email;
    String phone;


    public Client(String id, String name, String phone,String email) {
        this.id = id;
        this.name = name;
        this.phone= phone;
        this.email = email;

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


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }







}
