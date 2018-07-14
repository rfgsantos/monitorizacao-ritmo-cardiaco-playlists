package com.example.fabio.hrpy.wrappers;

import android.os.Parcelable;

import org.json.JSONObject;

public interface InterfaceWrapper extends Parcelable {

    JSONObject toJsonObject();
}
