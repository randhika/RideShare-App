package com.rideshare.rideshare.entity.app;

import com.rideshare.rideshare.entity.AppEntity;

public class Error extends AppEntity{

    private int code;
    private String msg;
    private String error;

    private Error(){}

    public static Error fromJson(String jsonError){
        Error error = (Error) AppEntity.fromJson(jsonError, Error.class);
        if(error.code < 1000){
            error.msg = "Server Error";
        }
        return error;
    }
}
