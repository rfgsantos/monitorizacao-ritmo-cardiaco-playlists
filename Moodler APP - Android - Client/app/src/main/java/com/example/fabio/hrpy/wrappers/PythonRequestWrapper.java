package com.example.fabio.hrpy.wrappers;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class PythonRequestWrapper {
    private ArrayList<String> hrv;
    private String token;
    private String userId;
    private String artistURI;
    private String trackURI;

    public PythonRequestWrapper(ArrayList<String> hrv, String token, String userId, String artistuURI, String trackURI){
        this.hrv = hrv;
        this.token = token;
        this.userId = userId;
        this.artistURI = artistuURI;
        this.trackURI = trackURI;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject jsonWrapper = new JSONObject();
        jsonWrapper.put("hrv", Arrays.toString(hrv.toArray()));
        jsonWrapper.put("userId", userId);
        jsonWrapper.put("artistURI",artistURI);
        jsonWrapper.put("playlistId","4kdDdyx5HqER1wQJl50AY8");
        jsonWrapper.put("trackURI", trackURI);
        return jsonWrapper;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ArrayList<String> getHrv() {
        return hrv;
    }

    public void setHrv(ArrayList<String> hrv) {
        this.hrv = hrv;
    }
}
