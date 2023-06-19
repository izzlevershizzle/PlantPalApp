package com.example.plantpalapp;

public class PlantRegistration {

    private long id;
    private String username;
    private String password;

    public PlantRegistration (long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public long getIdPlant() {
        return id;
    }
    public void setIdPlant(long id) {
        this.id = id;
    }

    public String toString() {
        String output = id + " " + username + " " + "password";
        return output;
    }
}
