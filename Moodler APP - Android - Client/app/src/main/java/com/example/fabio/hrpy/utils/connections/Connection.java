package com.example.fabio.hrpy.utils.connections;

import android.app.IntentService;

import org.json.JSONArray;

/**
 * Created by Denga on 27-Mar-18.
 */

public abstract class Connection {

    public Connection() {

    }

    public abstract void write(Object o);

    public abstract JSONArray read();

    public abstract void connect();

    public abstract void disconnect();


}
