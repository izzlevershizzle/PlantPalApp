package com.example.plantpalapp;

public class PlantMaintenance {

    private long id;
    private String name;
    private String purchased;
    private String placement;
    private String watering;
    private String size;

    public PlantMaintenance(long id, String name, String purchased, String placement, String watering, String size) {
        this.id = id;
        this.name = name;
        this.purchased = purchased;
        this.placement = placement;
        this.watering = watering;
        this.size = size;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPurchased() {
        return purchased;
    }
    public void setPurchased(String purchased) {
        this.purchased = purchased;
    }
    public String getPlacement() {
        return placement;
    }
    public void setPlacement(String placement) {
        this.placement = placement;
    }
    public String getWatering() {
        return watering;
    }
    public void setWatering(String watering) {
        this.watering = watering;
    }
    public String getSize() {
        return size;
    }
    public void setSize(String size) {
        this.size = size;
    }


    @Override
    public String toString() {
        String output = id + " " + name + " " + purchased + " " + placement + " " + watering + " " + size;
        return output;
    }
}


