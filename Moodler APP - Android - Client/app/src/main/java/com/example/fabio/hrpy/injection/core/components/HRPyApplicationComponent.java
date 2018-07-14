package com.example.fabio.hrpy.injection.core.components;


import com.example.fabio.hrpy.injection.core.modules.ConnectionModule;
import com.example.fabio.hrpy.ui.activitys.LoginActivity;
import com.example.fabio.hrpy.ui.activitys.MainActivity;
import com.example.fabio.hrpy.utils.Player.MySpotifyPlayer;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = {
        ConnectionModule.class
})
public interface HRPyApplicationComponent {
    //inject modules in this activity
    void inject(MainActivity mainActivity);
}
