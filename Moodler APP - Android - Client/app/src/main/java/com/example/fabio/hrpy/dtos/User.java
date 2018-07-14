package com.example.fabio.hrpy.dtos;

import android.os.Parcel;


import com.example.fabio.hrpy.wrappers.InterfaceWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class User implements InterfaceWrapper {

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
    private int id;
    private String name;
    private String access_token;
    private String refresh_token;
    private Date expires_at;

    public User(int id, String name, String access_token, String refresh_token, Date expires_at) {
        this.id = id;
        this.name = name;
        this.access_token = access_token;
        this.refresh_token = refresh_token;
        this.expires_at = expires_at;
    }

    protected User(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.access_token = in.readString();
        this.refresh_token = in.readString();
        this.expires_at = new Date(in.readLong());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeStringArray(new String[]{this.name, this.access_token, this.refresh_token});
        parcel.writeLong(this.expires_at.getTime());
    }

    @Override
    public JSONObject toJsonObject() {
        JSONObject json = new JSONObject();
        try {
            json.put("id", this.id);
            json.put("name", this.name);
            json.put("access_token", this.access_token);
            json.put("refresh_token", this.refresh_token);
            json.put("expires_at", this.expires_at);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public Date getExpires_at() {
        return expires_at;
    }

    public void setExpires_at(Date expires_at) {
        this.expires_at = expires_at;
    }
}
