package com.Estancia2.ttl.models;

public class Client {
    String id;
    String name;
    String email;
    String phone;
    String image;

    public  Client(){

    }


    public Client(String id, String name, String phone,String email) {
        this.id = id;
        this.name = name;
        this.phone= phone;
        this.email = email;

    }

    public Client(String id, String name, String email, String phone, String image) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
