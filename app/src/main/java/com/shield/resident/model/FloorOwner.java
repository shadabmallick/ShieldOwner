package com.shield.resident.model;

import java.util.ArrayList;

public class FloorOwner {

    private String floor;
    private ArrayList<Owner> ownerArrayList;

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public ArrayList<Owner> getOwnerArrayList() {
        return ownerArrayList;
    }

    public void setOwnerArrayList(ArrayList<Owner> ownerArrayList) {
        this.ownerArrayList = ownerArrayList;
    }
}
