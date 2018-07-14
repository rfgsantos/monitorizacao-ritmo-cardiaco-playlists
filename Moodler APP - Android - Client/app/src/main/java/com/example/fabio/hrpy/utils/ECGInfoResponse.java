package com.example.fabio.hrpy.utils;

import com.google.gson.annotations.SerializedName;

public class ECGInfoResponse {
    @SerializedName("Content")
    public final Content content;

    public ECGInfoResponse(Content content) {
        this.content = content;
    }

    public static class Content {
        @SerializedName("AvailableSampleRates")
        public final int availableSampleRates[];

        @SerializedName("CurrentSampleRate")
        public final int currentSampleRate;

        @SerializedName("ArraySize")
        public final int arraySize;

        public Content(int[] availableSampleRates, int currentSampleRate, int arraySize) {
            this.availableSampleRates = availableSampleRates;
            this.currentSampleRate = currentSampleRate;
            this.arraySize = arraySize;
        }
    }
}
