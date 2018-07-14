package com.example.fabio.hrpy.injection.core.modules;


import com.example.fabio.hrpy.utils.Player.MySpotifyPlayer;
import com.example.fabio.hrpy.utils.connections.FirebaseConnection;
import com.example.fabio.hrpy.utils.connections.PythonServerConnection;
import com.example.fabio.hrpy.utils.connections.SensorConnection;
import com.example.fabio.hrpy.utils.connections.SpotifyConnection;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ConnectionModule {

    public ConnectionModule() {

    }

    @Provides
    @Singleton
    FirebaseConnection firebaseConnection() {
        return new FirebaseConnection();
    }

    @Provides
    @Singleton
    SpotifyConnection spotifyConnection(){return new SpotifyConnection();}


    @Provides
    @Singleton
    MySpotifyPlayer mySpotifyPlayer(){return  new MySpotifyPlayer();}


}
