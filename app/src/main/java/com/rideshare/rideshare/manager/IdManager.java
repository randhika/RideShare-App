package com.rideshare.rideshare.manager;

import com.rideshare.rideshare.entity.AppResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class IdManager extends Manager {

    private final static String REGISTRATION_URI = "account";
    private final static String LOGIN_URI = "account/authenticate";
    private final static String NOTIFICATION_URI = "notification";

    public IdManager(){
        super();
    }

    public void login(String email, String password, AppResponse appResponse){
        JSONObject loginJSON;

        String url = buildUrl(LOGIN_URI);
        try {
            loginJSON = loginJsonBuilder(email, password);
            httpHandler.postJSON(loginJSON, url, appResponse);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void registration(String fullName, String email, String phone, String password,
                                    AppResponse appResponse){
        JSONObject registrationJSON;

        String url = buildUrl(REGISTRATION_URI);
        try {
            registrationJSON = registrationJsonBuilder(fullName, email, phone, password);
            httpHandler.postJSON(registrationJSON, url, appResponse);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getNotifications(String userId, String lastDate, String count,
                                        AppResponse appResponse){
        String url = buildUrl(NOTIFICATION_URI) + "?user=" + userId;
        if(lastDate != null){
            url += "&lastDate=" + lastDate;
        }
        if(count != null){
            url += "&count=" + count;
        }
        httpHandler.getJSON(url, appResponse);
    }

    public void getNotifications(String url, AppResponse appResponse){
        httpHandler.getJSON(url, appResponse);
    }

    public JSONObject registrationJsonBuilder(String fullName, String email, String phone,
                                              String password) throws JSONException {
        JSONObject registrationJSON = new JSONObject();
        registrationJSON.accumulate("fullName", fullName);
        registrationJSON.accumulate("email", email);
        registrationJSON.accumulate("phone", phone);
        registrationJSON.accumulate("password", password);
        return registrationJSON;
    }

    public JSONObject loginJsonBuilder(String email, String password) throws JSONException {
        JSONObject registrationJSON = new JSONObject();
        registrationJSON.accumulate("email", email);
        registrationJSON.accumulate("password", password);
        registrationJSON.accumulate("source", "app");
        return registrationJSON;
    }

    private String buildUrl(String path){
        return super.buildUrl(APPLICATION_URL, path);
    }
}
