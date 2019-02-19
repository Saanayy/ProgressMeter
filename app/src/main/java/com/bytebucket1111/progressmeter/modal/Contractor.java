package com.bytebucket1111.progressmeter.modal;

import java.util.ArrayList;

public class Contractor {
    public String name, email, cid, uid;
    public String contact, address;
    public boolean blockStatus;
    public ArrayList<String>projectIds;

    public Contractor() {
    }

    public Contractor(String name, String email, String cid, String uid, String contact, String address, boolean blockStatus, ArrayList<String> projectIds) {
        this.name = name;
        this.email = email;
        this.cid = cid;
        this.uid = uid;
        this.contact = contact;
        this.address = address;
        this.blockStatus = blockStatus;
        this.projectIds = projectIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isBlockStatus() {
        return blockStatus;
    }

    public void setBlockStatus(boolean blockStatus) {
        this.blockStatus = blockStatus;
    }

    public ArrayList<String> getProjectIds() {
        return projectIds;
    }

    public void setProjectIds(ArrayList<String> projectIds) {
        this.projectIds = projectIds;
    }
}
