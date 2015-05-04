package com.rideshare.rideshare.entity;

import com.google.gson.Gson;
import org.json.JSONObject;
import java.lang.reflect.Type;

public class Convertor {

    public static <T> T convert(JSONObject json, Type type){
        Gson gson = new Gson();
        T obj = gson.fromJson(json.toString(), type);
        return obj;
    }
}
