package com.shield.resident.model;

public class ChildItem extends ListItem {

    private ActivityChild activityChild;

    public ActivityChild getActivityChild() {
        return activityChild;
    }

    public void setActivityChild(ActivityChild activityChild) {
        this.activityChild = activityChild;
    }

    @Override
    public int getType() {
        return TYPE_CHILDS;
    }

}