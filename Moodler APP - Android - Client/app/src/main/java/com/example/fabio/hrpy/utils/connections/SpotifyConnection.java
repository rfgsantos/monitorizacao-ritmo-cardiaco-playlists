package com.example.fabio.hrpy.utils.connections;

import android.util.Log;

import com.example.fabio.hrpy.utils.interfaces.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SpotifyConnection implements HttpRequest {


        private final OkHttpClient mOkHttpClient = new OkHttpClient();

        private Call mCall;

        private String answer;

        public  String request(String mAccessToken) {


            final Request pedido = new Request.Builder()
                    .url("https://api.spotify.com/v1/me")
                    .addHeader("Authorization", "Bearer " + mAccessToken)
                    .build();
            cancelCall();
            mCall = mOkHttpClient.newCall(pedido);

            mCall.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("Call", "failed");
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        final JSONObject jsonObject = new JSONObject(response.body().string());
                        answer = jsonObject.toString(3).toString();
                    } catch (JSONException e) {
                        Log.d("failed", "Failed to parse data: " + e);
                    }
                }
            });

            return answer;
        }



        private void cancelCall() {
            if (mCall != null) {
                mCall.cancel();
            }
        }


    }


