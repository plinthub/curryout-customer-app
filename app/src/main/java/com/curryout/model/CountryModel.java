package com.curryout.model;

public class CountryModel {

     String countriesID,code,name;

    public CountryModel() {
    }

    public CountryModel(String countriesID, String code, String name) {
        this.countriesID = countriesID;
        this.code = code;
        this.name = name;
    }

    public String getCountriesID() {
        return countriesID;
    }

    public void setCountriesID(String countriesID) {
        this.countriesID = countriesID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String toString() {
        return name;
    }
}
