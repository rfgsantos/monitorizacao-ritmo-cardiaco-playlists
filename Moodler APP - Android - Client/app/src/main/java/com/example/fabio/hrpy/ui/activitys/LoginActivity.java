package com.example.fabio.hrpy.ui.activitys;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;


import com.example.fabio.hrpy.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends Activity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View v){
        Switch trainMode = this.findViewById(R.id.TestMode);
        Intent i = new Intent(getApplicationContext(),InputNameActivity.class);
        i.putExtra("train",trainMode.isChecked());
        startActivity(i);
    }

    public void instructions(View v){
        startActivity(new Intent(getApplicationContext(),InstructionsActivity.class));
    }
}
