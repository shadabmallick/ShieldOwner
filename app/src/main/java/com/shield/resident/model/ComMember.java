package com.shield.resident.model;

public class ComMember {

    private String name;
    private String mobile;
    private String image;
    private String roll;


    public String getName() {
        return name;
    }

    public ComMember setName(String name) {
        this.name = name;
        return this;
    }

    public String getMobile() {
        return mobile;
    }

    public ComMember setMobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public String getImage() {
        return image;
    }

    public ComMember setImage(String image) {
        this.image = image;
        return this;
    }

    public String getRoll() {
        return roll;
    }

    public ComMember setRoll(String roll) {
        this.roll = roll;
        return this;
    }
}
