package com.rideshare.rideshare.entity.app;

import com.rideshare.rideshare.entity.AppEntity;

public class Session extends AppEntity {

    private String _id;
    private User user;
    private String keepAlive;

    public static Session fromJson(String sessionJson){
        return (Session) AppEntity.fromJson(sessionJson, Session.class);
    }
}
