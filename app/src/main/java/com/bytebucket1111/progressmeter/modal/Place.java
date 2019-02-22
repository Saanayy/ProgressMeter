package com.bytebucket1111.progressmeter.modal;

import java.io.Serializable;

public class Place implements Serializable {
    private double lat, lng;
    private String name;

    public Place(String name,double lat, double lng) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
