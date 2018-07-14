package com.example.fabio.hrpy.dtos;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.util.List;

public class UserList implements Parcelable {

    public static final Creator<UserList> CREATOR = new Creator<UserList>() {
        @Override
        public UserList createFromParcel(Parcel in) {
            return new UserList(in);
        }

        @Override
        public UserList[] newArray(int size) {
            return new UserList[size];
        }
    };
    private List<User> userList;

    public UserList(List<User> userList) {
        this.userList = userList;
    }

    protected UserList(Parcel in) {
        in.readTypedList(this.userList, User.CREATOR);
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(this.userList);
    }

    public JSONObject[] getJsonObject() {
        return this.userList.toArray(new JSONObject[this.userList.size()]);
    }
}
