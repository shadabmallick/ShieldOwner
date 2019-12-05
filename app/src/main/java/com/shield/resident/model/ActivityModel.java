package com.shield.resident.model;

import java.util.ArrayList;

public class ActivityModel {

    private String date;
    private ArrayList<ActivityChild> listChild;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<ActivityChild> getListChild() {
        return listChild;
    }

    public void setListChild(ArrayList<ActivityChild> listChild) {
        this.listChild = listChild;
    }
}