package com.shield.resident.model;

public class ComMemberItem extends ListItem {

    private ComMember comMember;

    public ComMember getComMember() {
        return comMember;
    }

    public ComMemberItem setComMember(ComMember comMember) {
        this.comMember = comMember;
        return this;
    }

    @Override
    public int getType() {
        return TYPE_CHILDS;
    }

}
