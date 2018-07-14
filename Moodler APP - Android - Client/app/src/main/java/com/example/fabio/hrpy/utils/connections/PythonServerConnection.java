package com.example.fabio.hrpy.utils.connections;

import android.util.Log;

import com.example.fabio.hrpy.wrappers.PythonRequestWrapper;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Class that provides connection to PythonDatabase
 *
 * @author Rui Santos
 */
public class PythonServerConnection {
    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    private Call mCall;
    private final String url = "https://moodlerisel.herokuapp.com/";
    public PythonServerConnection() {
    }
    public void makeRequest(PythonRequestWrapper p) {
        RequestBody body = null;
        try {
            body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),p.toJson().toString());

            final Request pedido = new Request.Builder()
                    .url(url + "hrv_modify_playlist")
                    .addHeader("Authorization", "Bearer " + p.getToken())
                    .post(body)
                    .build();
            mCall = mOkHttpClient.newCall(pedido);
            mCall.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("Call", "failed");
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void makeRequestToTrain(){
        final Request pedido = new Request.Builder()
                .url(url + "train_classifier")
                .get()
                .build();
        mCall = mOkHttpClient.newCall(pedido);
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Call", "failed");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

}
