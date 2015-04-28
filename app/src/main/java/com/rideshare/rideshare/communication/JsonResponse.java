package com.rideshare.rideshare.communication;

import org.apache.http.HttpResponse;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.rideshare.rideshare.entity.app.Error;

public class JsonResponse {

    private int status;
    private String jsonResponse;

    public JsonResponse(HttpResponse response) throws Exception{
        this.status = response.getStatusLine().getStatusCode();
        InputStream stream = response.getEntity().getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        this.jsonResponse = reader.readLine();
    }

    public int getStatus() {
        return status;
    }

    public String getjsonResponse() {
        return jsonResponse;
    }

    public Error getError(){
        if(isError())
            return null;
        return com.rideshare.rideshare.entity.app.Error.fromJson(jsonResponse);
    }

    public boolean isError() {
        return this.status < 200 || this.status >= 300;
    }
}
