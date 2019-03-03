package com.bytebucket1111.progressmeter.modal;

public class Update {

    String title, desc, imageUrl,isStopped,userweather,date, projectId,updateId;
    String apiWeather, location;
    boolean resolved, conflict;

    public Update() {
    }

    public Update(String title, String desc, String imageUrl, String isStopped, String userweather, String date, String projectId, String updateId, String apiWeather, String location, boolean resolved, boolean conflict) {
        this.title = title;
        this.desc = desc;
        this.imageUrl = imageUrl;
        this.isStopped = isStopped;
        this.userweather = userweather;
        this.date = date;
        this.projectId = projectId;
        this.updateId = updateId;
        this.apiWeather = apiWeather;
        this.location = location;
        this.resolved = resolved;
        this.conflict = conflict;
    }

    public String getUpdateId() {
        return updateId;
    }

    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    public boolean isConflict() {
        return conflict;
    }

    public void setConflict(boolean conflict) {
        this.conflict = conflict;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getIsStopped() {
        return isStopped;
    }

    public void setIsStopped(String isStopped) {
        this.isStopped = isStopped;
    }

    public String getUserweather() {
        return userweather;
    }

    public void setUserweather(String userweather) {
        this.userweather = userweather;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getApiWeather() {
        return apiWeather;
    }

    public void setApiWeather(String apiWeather) {
        this.apiWeather = apiWeather;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
