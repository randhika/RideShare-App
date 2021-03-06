package com.rideshare.rideshare.manager;

import com.rideshare.rideshare.communication.Http;
import com.rideshare.rideshare.entity.AppResponse;

public abstract class Manager {

    protected final static String APPLICATION_URL = "http://rideshare-server.herokuapp.com/";
    //protected final static String APPLICATION_URL = "http://132.73.200.58:3000/";

    protected Http httpHandler;

    public Manager(){
        httpHandler = Http.init();
    }

    public String buildUrl(String host, String path){
        return host + path;
    }


}
