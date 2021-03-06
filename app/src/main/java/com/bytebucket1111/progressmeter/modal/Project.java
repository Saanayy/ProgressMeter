package com.bytebucket1111.progressmeter.modal;

import java.io.Serializable;
import java.util.ArrayList;

public class Project {

    public String title, description, geolocation, startDate, duration, userid,projectId;
    public ArrayList<String> updateId;
    public boolean finished;

    public Project() {
    }



    public Project(String title, String description, String geolocation, String startDate, String duration, String userid, String projectId, ArrayList<String> updateId, boolean finished) {
        this.title = title;
        this.description = description;
        this.geolocation = geolocation;
        this.startDate = startDate;
        this.duration = duration;
        this.userid = userid;
        this.projectId = projectId;
        this.updateId = updateId;
        this.finished = finished;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(String geolocation) {
        this.geolocation = geolocation;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public ArrayList<String> getUpdateId() {
        return updateId;
    }

    public void setUpdateId(ArrayList<String> updateId) {
        this.updateId = updateId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
