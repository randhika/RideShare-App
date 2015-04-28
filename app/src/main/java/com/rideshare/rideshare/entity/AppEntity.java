package com.rideshare.rideshare.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rideshare.rideshare.entity.app.Error;

import org.json.JSONObject;

public abstract class AppEntity {

    protected static <T extends AppEntity> AppEntity fromJson(String json, Class<T> tClass){
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        JSONObject errorJsonObject;
        T entity;

        try{
            errorJsonObject = new JSONObject(json);
            entity = gson.fromJson(errorJsonObject.toString(), tClass);
        } catch (Exception e){
            return null;
        }
        return entity;
    }

    public static <T extends AppEntity> boolean isError(T entity){
        return entity instanceof Error;
    }
}
