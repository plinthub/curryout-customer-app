package com.curryout.model;

public class CityModel {

    String citiesID,name,state_id;

    public CityModel() {
    }

    public CityModel(String citiesID) {
        this.citiesID = citiesID;
    }
    public CityModel(String citiesID, String name) {
        this.citiesID = citiesID;
        this.name = name;
    }
    public CityModel(String citiesID, String name, String state_id) {
        this.citiesID = citiesID;
        this.name = name;
        this.state_id = state_id;
    }

    public String getCitiesID() {
        return citiesID;
    }

    public void setCitiesID(String citiesID) {
        this.citiesID = citiesID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    @Override
    public String toString() {
        return name;
    }
}
