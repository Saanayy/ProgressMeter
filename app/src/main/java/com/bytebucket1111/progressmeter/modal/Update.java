package com.bytebucket1111.progressmeter.modal;

public class Update {

    String title, desc, imageUrl,isStopped,weather;

    public Update() {
    }

    public Update(String title, String desc, String imageUrl, String isStopped, String weather) {
        this.title = title;
        this.desc = desc;
        this.imageUrl = imageUrl;
        this.isStopped = isStopped;
        this.weather = weather;
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

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }
}
