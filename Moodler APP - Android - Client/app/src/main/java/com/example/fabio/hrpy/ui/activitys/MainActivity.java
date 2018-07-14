package com.example.fabio.hrpy.ui.activitys;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.example.fabio.hrpy.HRPyApplication;
import com.example.fabio.hrpy.R;
import com.example.fabio.hrpy.utils.ECGInfoResponse;
import com.example.fabio.hrpy.utils.HRResponse;
import com.example.fabio.hrpy.utils.Player.MySpotifyPlayer;
import com.example.fabio.hrpy.utils.connections.FirebaseConnection;
import com.example.fabio.hrpy.utils.connections.PythonServerConnection;
import com.example.fabio.hrpy.utils.connections.SpotifyConnection;
import com.google.gson.Gson;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.movesense.mds.Mds;
import com.movesense.mds.MdsException;
import com.movesense.mds.MdsHeader;
import com.movesense.mds.MdsNotificationListener;
import com.movesense.mds.MdsResponseListener;
import com.movesense.mds.MdsSubscription;
import com.spotify.sdk.android.player.Spotify;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class MainActivity extends Activity implements CompoundButton.OnCheckedChangeListener {


    private final static ArrayList<Integer> HRV_TEST = new ArrayList<>();
    private String userName = "";
    static MainActivity s_INSTANCE = null;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final String SERIAL = "serial";
    String connectedSerial;
    private LineGraphSeries<DataPoint> mSeriesECG;
    private int mDataPointsAppended = 0;
    private MdsSubscription mECGSubscription;
    private MdsSubscription mHRSubscription;
    private ArrayAdapter<String> mSpinnerAdapter;
    private final List<String> mSpinnerRates = new ArrayList<>();
    public static final String URI_EVENTLISTENER = "suunto://MDS/EventListener";
    public static final String SCHEME_PREFIX = "suunto://";
    public static final String URI_ECG_INFO = "/Meas/ECG/Info";
    public static final String URI_ECG_ROOT = "/Meas/ECG/";
    public static final String URI_MEAS_HR = "/Meas/HR";
    Spinner mSpinnerSampleRates;
    private TextView myUser;
    private TextView artista;
    private TextView musica;
    private TextView hrView;
    private TextView likeTxt;
    private TextView likeTextfieldStatic;
    private boolean train;
    private ImageButton like,skip,previous,dislike;

    @Inject
    SpotifyConnection syc;

    @Inject
    MySpotifyPlayer msp;

    @Inject
    FirebaseConnection fdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        s_INSTANCE = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((HRPyApplication) getApplication()).getHrPyApplicationComponent().inject(this);


        msp.myLogIn(this);

        msp.setFdb(fdb);

        msp.setMainActivity(this);

        Bundle extra = getIntent().getExtras();

        if (extra != null) {
            userName = extra.getString("name");
            train = extra.getBoolean("train");
            connectedSerial = extra.getString(SERIAL);
        }

        hrView = findViewById(R.id.hr_view);

        artista = findViewById(R.id.artista_txt);

        musica = findViewById(R.id.musica_txt);

        likeTxt = findViewById(R.id.like);

        msp.setThings(musica,artista,train);

        myUser = findViewById(R.id.textViewUser);
        myUser.setText(userName);

        like = findViewById(R.id.LikeButton);
        skip = findViewById(R.id.skipButton);
        previous = findViewById(R.id.previousButton);
        dislike = findViewById(R.id.dislike);

        likeTextfieldStatic = findViewById(R.id.LikeTxtV);

        msp.setUser(userName);

        fetchECGInfo();

        if(train){
            previous.setEnabled(false);
            skip.setEnabled(false);
        }else{
            like.setEnabled(false);
            dislike.setEnabled(false);
            likeTxt.setVisibility(View.GONE);
            likeTextfieldStatic.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        msp.getPlayer(this, requestCode, resultCode, intent);
    }

    public void onClickPlay(View v) {
        MySpotifyPlayer.mPlayer.resume(null);
    }

    public void onClickStop(View v) {
        MySpotifyPlayer.mPlayer.pause(null);
    }

    public void onClickNext(View v) {
        MySpotifyPlayer.mPlayer.skipToNext(null);
    }

    public void onClickPrevious(View v) {
        MySpotifyPlayer.mPlayer.skipToPrevious(null);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        MySpotifyPlayer.mPlayer.destroy();
        Spotify.destroyPlayer(this);

        unsubscribeAll();
        MainActivity.s_INSTANCE = null;

    }

    public void onClickLike(View v) {
        msp.like();
    }

    public void onClickDislike(View v) {
        msp.dislike();
    }

    private Mds getMDS() {
        return ConnectSensor.mMds;
    }

    private void fetchECGInfo() {
        String uri = SCHEME_PREFIX + connectedSerial + URI_ECG_INFO;
        getMDS().get(uri, null, new MdsResponseListener() {
            @Override
            public void onSuccess(String data, MdsHeader header) {
                Log.i(LOG_TAG, "ECG info succesful: " + data);
                ECGInfoResponse infoResponse = new Gson().fromJson(data, ECGInfoResponse.class);
                // Fill sample rates to the spinner
                mSpinnerRates.clear();
                for (int sampleRate : infoResponse.content.availableSampleRates) {
                    mSpinnerRates.add(""+sampleRate);
                }
                //mSpinnerAdapter.notifyDataSetChanged();
                //mSpinnerSampleRates.setSelection(mSpinnerAdapter.getPosition(""+DEFAULT_SAMPLE_RATE));
                enableHRSubscription();
            }
            @Override
            public void onError(MdsException e) {
                Log.e(LOG_TAG, "ECG info returned error: " + e);
            }
        });
    }

    private void enableHRSubscription() {
        // Make sure there is no subscription
        unsubscribeHR();

        // Build JSON doc that describes what resource and device to subscribe
        StringBuilder sb = new StringBuilder();
        String strContract = sb.append("{\"Uri\": \"").append(connectedSerial).append(URI_MEAS_HR).append("\"}").toString();
        Log.d(LOG_TAG, strContract);
        mHRSubscription = Mds.builder().build(this).subscribe(URI_EVENTLISTENER,
                strContract, new MdsNotificationListener() {
                    @Override
                    public void onNotification(String data) {
                        Log.d(LOG_TAG, "onNotification(): " + data);
                        HRResponse hrResponse = new Gson().fromJson(
                                data, HRResponse.class);
                        if (hrResponse != null) {
                            int hr = (int) hrResponse.body.average;
                            msp.getHrv().add("" + hr);

                            hrView.setText("" + hr);
                            likeTxt.setText("" + msp.getLikeCounter());
                        }
                    }
                    @Override
                    public void onError(MdsException error) {
                        Log.e(LOG_TAG, "HRSubscription onError(): ", error);
                        unsubscribeHR();
                    }
                });
    }


    private void enableECGSubscription() {
        // Make sure there is no subscription
        unsubscribeECG();
        // Build JSON doc that describes what resource and device to subscribe
        StringBuilder sb = new StringBuilder();
        int sampleRate = Integer.parseInt("" + mSpinnerSampleRates.getSelectedItem());
        final int GRAPH_WINDOW_WIDTH = sampleRate * 3;
        String strContract = sb.append("{\"Uri\": \"").append(connectedSerial).append(URI_ECG_ROOT).append(sampleRate).append("\"}").toString();
        Log.d(LOG_TAG, strContract);
        // Clear graph
        mDataPointsAppended = 0;
        mECGSubscription = Mds.builder().build(this).subscribe(URI_EVENTLISTENER,
                strContract, new MdsNotificationListener() {
                    @Override
                    public void onNotification(String data) {
                        Log.d(LOG_TAG, "onNotification(): " + data);
                        ECGResponse ecgResponse = new Gson().fromJson(
                                data, ECGResponse.class);
                    }
                    @Override
                    public void onError(MdsException error) {
                        Log.e(LOG_TAG, "onError(): ", error);
                        unsubscribeECG();
                    }
                });
    }

    private void unsubscribeECG() {
        if (mECGSubscription != null) {
            mECGSubscription.unsubscribe();
            mECGSubscription = null;
        }
    }

    private void unsubscribeHR() {
        if (mHRSubscription != null) {
            mHRSubscription.unsubscribe();
            mHRSubscription = null;
        }
    }

    void unsubscribeAll() {
        Log.d(LOG_TAG, "unsubscribeAll()");
        unsubscribeECG();
        unsubscribeHR();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            enableECGSubscription();
        } else {
            unsubscribeECG();
        }
    }

}


