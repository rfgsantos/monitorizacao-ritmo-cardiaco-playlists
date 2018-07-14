package com.example.fabio.hrpy.utils.enums;

/**
 * Created by Denga on 27-Mar-18.
 */

public enum ActivityRequestCode {

    REQUEST_CODE(1337);

    private int code;

    ActivityRequestCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
