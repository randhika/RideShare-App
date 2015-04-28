package com.rideshare.rideshare.app;

import com.rideshare.rideshare.communication.JsonResponse;
import com.rideshare.rideshare.entity.AppEntity;
import com.rideshare.rideshare.entity.app.Error;
import com.rideshare.rideshare.entity.app.Session;

public class IdManager extends Manager {

    private final static String REGISTRATION_URI = "user";
    private final static String LOGIN_URI = "session";

    public IdManager(){
        super();
    }

    public AppEntity login(String email, String password){
        JsonResponse loginResponse = httpHandler.postJSON(buildJsonLogin(email, password),
                buildUrl(LOGIN_URI));
        if(loginResponse.isError()){
            return Error.fromJson(loginResponse.getjsonResponse());
        } else {
            return Session.fromJson(loginResponse.getjsonResponse());
        }
    }

    private String buildJsonLogin(String email, String password){
        return "{\"email\":\""+email+"\",\"password\":\""+password+"\"}";
    }

    private String buildUrl(String path){
        return super.buildUrl(APPLICATION_URL, path);
    }
}
