package com.shield.resident.model;

public class OwnerItem extends ListItem {

    private Owner owner;

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    @Override
    public int getType() {
        return TYPE_CHILDS;
    }

}
