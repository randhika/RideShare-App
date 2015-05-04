package com.rideshare.rideshare.entity;


import org.json.JSONObject;

public class AppResponse {

    private int status;
    private JSONObject json;

    public AppResponse(int status, JSONObject json){
        this.status = status;
        this.json = json;
    }

    public AppResponse(){
        this.status = -1;
        this.json = null;
    }

    public void init(int status, JSONObject json){
        this.status = status;
        this.json = json;
    }

    public JSONObject getJSON(){
        return json;
    }

    public int getStatus(){
        return status;
    }

    public boolean isValid(){
        return this.json != null && this.status != -1;
    }
}
