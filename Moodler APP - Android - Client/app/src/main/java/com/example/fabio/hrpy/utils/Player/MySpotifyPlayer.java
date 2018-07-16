package com.example.fabio.hrpy.utils.Player;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import com.example.fabio.hrpy.dtos.UserData;
import com.example.fabio.hrpy.ui.activitys.FinalActivity;
import com.example.fabio.hrpy.ui.activitys.MainActivity;
import com.example.fabio.hrpy.utils.FeedBack;
import com.example.fabio.hrpy.utils.connections.FirebaseConnection;
import com.example.fabio.hrpy.utils.connections.PythonServerConnection;
import com.example.fabio.hrpy.utils.enums.ActivityRequestCode;
import com.example.fabio.hrpy.utils.enums.Globals;
import com.example.fabio.hrpy.wrappers.PythonRequestWrapper;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MySpotifyPlayer implements
        SpotifyPlayer.NotificationCallback, ConnectionStateCallback {

    public static SpotifyPlayer mPlayer;
    private String user;
    private FirebaseConnection fdb;
    private ArrayList<String> hrv = new ArrayList<>();
    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    private Call mCall;
    private String userId;
    String track = "";
    String artist = "";
    private TextView artista;
    private TextView musica;
    private boolean isFirst = true;
    private String firstSongName = "";
    private Activity mainActivity;
    private boolean train;
    private String token;

    private String currentTrackUri;
    private String currentArtistUti;


    public void myLogIn(Activity myActivity){
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(
                Globals.CLIENT_ID.getValue(),
                AuthenticationResponse.Type.TOKEN,
                Globals.REDIRECT_URI.getValue());
        builder.setScopes(new String[]{"user-read-private", "streaming", "playlist-modify-public", "playlist-modify-private"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(myActivity, ActivityRequestCode.REQUEST_CODE.getCode(), request);
    }

    public void getPlayer(Context c, int requestCode, int resultCode, Intent intent){
        // Check if result comes from the correct activity
        if (requestCode == ActivityRequestCode.REQUEST_CODE.getCode()) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                Config playerConfig = new Config(c, response.getAccessToken(), Globals.CLIENT_ID.getValue());
                setToken(response.getAccessToken());
                makeRequestToId();
                Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                    @Override
                    public void onInitialized(SpotifyPlayer spotifyPlayer) {
                        mPlayer = spotifyPlayer;
                        mPlayer.addConnectionStateCallback(MySpotifyPlayer.this);
                        mPlayer.addNotificationCallback(MySpotifyPlayer.this);
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
                    }
                });
            }
        }
    }

    public void setMainActivity(Activity mainActivity){
        this.mainActivity = mainActivity;
    }

    public void setUser(String user){
        this.user = user;
    }

    public void setThings(TextView musica, TextView artista,boolean train){
        this.musica = musica;
        this.artista = artista;
        this.train = train;
    }

    private void fillDataForPython(){
        this.currentArtistUti = mPlayer.getMetadata().currentTrack.artistUri;
        this.currentTrackUri = mPlayer.getMetadata().currentTrack.uri;
    }

    private void fillData(){
        track = getCurrentTrack();
        musica.setText(track);
        artist = getCurrentArtist();
        artista.setText(artist);
        FeedBack.likeCounter = 0;
        hrv.clear();
    }

    @Override
    public void onLoggedIn() {
        Log.d("MySpotifyPlayer", "User logged in");
        mPlayer.playUri(null, "spotify:user:11146909782:playlist:4kdDdyx5HqER1wQJl50AY8", 0, 0);
    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        Log.d("MySpotifyPlayer", "Playback event received: " + playerEvent.name());
        boolean evaluation;
        switch (playerEvent) {
            case kSpPlaybackNotifyMetadataChanged:
                fillDataForPython();
                fillData();
                break;
            case kSpPlaybackNotifyTrackDelivered:
                if(train) {
                    evaluation = (FeedBack.likeCounter < 0) ? false : true;
                    fdb.write(new UserData(user, track, artist, evaluation, hrv, getUri().toString()));
                }else{
                    Log.d("Mytag","Send Request to python with hr");
                    String user_evaluation = (FeedBack.likeCounter < 0) ? "0" : "1";
                    new PythonServerConnection().makeRequest(new PythonRequestWrapper(getHrv(),getToken(),getUserId(),currentArtistUti, currentTrackUri,user_evaluation));
                }
                break;
            case kSpPlaybackNotifyAudioDeliveryDone:
                if(train) {
                    new PythonServerConnection().makeRequestToTrain();
                    this.mainActivity.startActivity(new Intent(mainActivity, FinalActivity.class));
                }
                break;
            default:
                break;
        }
    }

    private JSONObject getUri(){
        JSONObject json = new JSONObject();
        String trackUri = mPlayer.getMetadata().currentTrack.uri;
        String artistUri = mPlayer.getMetadata().currentTrack.artistUri;
        String albumUri = mPlayer.getMetadata().currentTrack.albumUri;
        try {
            json.put("trackUri", trackUri);
            json.put("artistUri",artistUri);
            json.put("albumUri",albumUri);
        } catch (JSONException e) {
            Log.d("Error Tag: ", "" + e);
        }
        return json;
    }


    private void makeRequestToId(){
        final Request pedido = new Request.Builder()
                .url("https://api.spotify.com/v1/me")
                .addHeader("Authorization", "Bearer " + getToken())
                .build();
        mCall = mOkHttpClient.newCall(pedido);
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Call", "failed");
            }
            @Override
            public void onResponse(Call call,Response response) throws IOException {
                try {
                    final JSONObject jsonObject = new JSONObject(response.body().string());
                    userId = jsonObject.getString("id");
                } catch (JSONException e) {
                    Log.d("failed", "Failed to parse data: " + e);
                }
            }
        });
    }

    public void setToken(String token){
        this.token = token;
    }

    public String getToken(){
        return this.token;
    }

    public String getUserId(){
        return this.userId;
    }

    public String getUser() {
        return user;
    }

    public void like(){
        FeedBack.likeCounter++;
    }
    public void dislike(){
        FeedBack.likeCounter--;
    }

    public  int getLikeCounter(){
        return FeedBack.likeCounter;
    }

    public ArrayList<String> getHrv() {
        return hrv;
    }

    public void setFdb(FirebaseConnection fdb) {
        this.fdb = fdb;
    }

    public String getCurrentTrack(){
        return mPlayer.getMetadata().currentTrack.name;
    }

    public String getCurrentArtist(){
        return mPlayer.getMetadata().currentTrack.artistName;
    }

    @Override
    public void onPlaybackError(Error error) {
        Log.d("MySpotifyPlayer", "Playback error received: " + error.name());
        switch (error) {
            // Handle error type as necessary
            default:
                break;
        }
    }

    @Override
    public void onLoggedOut() {
        Log.d("MySpotifyPlayer", "User logged out");
    }

    @Override
    public void onLoginFailed(Error error) {
        Log.d("MySpotifyPlayer", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("MySpotifyPlayer", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("MySpotifyPlayer", "Received connection message: " + message);
    }

}
