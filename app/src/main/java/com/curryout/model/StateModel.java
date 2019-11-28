package com.curryout.model;

public class StateModel {

      String statesID,name,country_id;

    public StateModel() {
    }

    public StateModel(String statesID, String name, String country_id) {
        this.statesID = statesID;
        this.name = name;
        this.country_id = country_id;
    }

    public String getStatesID() {
        return statesID;
    }

    public void setStatesID(String statesID) {
        this.statesID = statesID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }


    @Override
    public String toString() {
        return name;
    }
}
