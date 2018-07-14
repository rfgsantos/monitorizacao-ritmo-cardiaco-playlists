package com.example.fabio.hrpy.ui.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fabio.hrpy.R;

import java.util.Locale;

public class InputNameActivity extends AppCompatActivity {


    private EditText e;
    private String name;
    private TextToSpeech t1;
    private boolean train;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_name);

        e = (EditText) findViewById(R.id.inputName);


        final Button chooseUser = this.findViewById(R.id.chooseName);


        Bundle extra = getIntent().getExtras();

        if(extra != null){
            train = extra.getBoolean("train");

        }


        this.t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.ENGLISH);
                }
            }
        });

    }


    public void onNameChangeClicked(View v){
        name = e.getText().toString();


        if(name.trim().length() != 0) {


            Intent intent = new Intent(getApplicationContext(), ConnectSensor.class);
            intent.putExtra("name", name);
            intent.putExtra("train",train);

            startActivity(intent);

        }else{
            textReader("Please enter a valid name");
        }

    }

    private void textReader(String toSpeak){
        Toast.makeText(getApplicationContext(),toSpeak,Toast.LENGTH_SHORT).show();
        t1.speak(toSpeak,TextToSpeech.QUEUE_FLUSH,null);
    }

}
