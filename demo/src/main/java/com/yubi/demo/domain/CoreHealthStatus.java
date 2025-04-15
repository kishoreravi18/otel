package com.yubi.demo.domain;


public class CoreHealthStatus {

    private String name;
    private Boolean alive;
    private String message;
    private String url;

    public CoreHealthStatus(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public CoreHealthStatus setName(String name) {
        this.name = name;
        return this;
    }

    public boolean isAlive() {
        return alive;
    }

    public CoreHealthStatus setAlive(boolean alive) {
        this.alive = alive;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public CoreHealthStatus setMessage(String message) {
        this.message = message;
        return this;
    }
}
