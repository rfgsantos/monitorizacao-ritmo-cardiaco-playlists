package com.example.fabio.hrpy.utils.connections;


import android.content.Intent;
import android.support.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;


public class FirebaseConnection extends Connection {

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    public FirebaseConnection() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("/evaluation");

    }

    @Override
    public void write(Object o) {
        myRef.push().setValue(o);

    }

    @Override
    public JSONArray read() {
        return null;
    }

    @Override
    public void connect() {
        database.goOnline();
    }

    @Override
    public void disconnect() {
        database.goOffline();

    }

}
