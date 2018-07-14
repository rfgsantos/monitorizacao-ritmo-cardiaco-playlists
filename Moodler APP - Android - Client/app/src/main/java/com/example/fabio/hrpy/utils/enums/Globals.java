package com.example.fabio.hrpy.utils.enums;


/**
 * Created by Denga on 27-Mar-18.
 */

public enum Globals {

    REDIRECT_URI("http://hrpy.com/callback/"),
    CLIENT_ID("d61c2855dcd14165b401e7bc80d9f5e5"),
    URI_CONNECTEDDEVICES("suunto://MDS/ConnectedDevices"),
    URI_EVENTLISTENER("suunto://MDS/EventListener"),
    SERIAL("serial"),
    SCHEME_PREFIX("suunto://");

    private String value;



    Globals(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }


}
