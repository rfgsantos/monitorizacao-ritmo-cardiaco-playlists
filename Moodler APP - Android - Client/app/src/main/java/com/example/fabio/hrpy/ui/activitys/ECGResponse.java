package com.example.fabio.hrpy.ui.activitys;

import com.google.gson.annotations.SerializedName;

class ECGResponse {

    @SerializedName("Body")
    public final Body body;

    public ECGResponse(Body body) {
        this.body = body;
    }

    public static class Body {
        @SerializedName("Samples")
        public final int samples[];
        public Body(int samples[]) {
            this.samples = samples;
        }
    }

}
