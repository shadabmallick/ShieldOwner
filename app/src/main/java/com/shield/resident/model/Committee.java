package com.shield.resident.model;

import java.util.ArrayList;

public class Committee {

    private String committee_name;
    private String description;
    private ArrayList<ComMember> comMemberArrayList;

    public String getCommittee_name() {
        return committee_name;
    }

    public Committee setCommittee_name(String committee_name) {
        this.committee_name = committee_name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Committee setDescription(String description) {
        this.description = description;
        return this;
    }

    public ArrayList<ComMember> getComMemberArrayList() {
        return comMemberArrayList;
    }

    public Committee setComMemberArrayList(ArrayList<ComMember> comMemberArrayList) {
        this.comMemberArrayList = comMemberArrayList;
        return this;
    }
}
