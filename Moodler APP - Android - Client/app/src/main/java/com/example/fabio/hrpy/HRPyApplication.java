package com.example.fabio.hrpy;

import android.app.Application;
import android.content.Context;


import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.fabio.hrpy.injection.core.components.DaggerHRPyApplicationComponent;
import com.example.fabio.hrpy.injection.core.components.HRPyApplicationComponent;
import com.example.fabio.hrpy.injection.core.modules.ConnectionModule;

import javax.inject.Singleton;

/**
 * Global em toda a aplicação providência o motor de dependencias
 *
 * @author Rui Santos
 */
public class HRPyApplication extends Application {

    public static RequestQueue requestQueue;
    private HRPyApplicationComponent hrPyApplicationComponent;

    @Singleton
    public static RequestQueue getRequestQueue(Context context) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        hrPyApplicationComponent = DaggerHRPyApplicationComponent.builder()
                .connectionModule(new ConnectionModule())
                .build();
    }

    public HRPyApplicationComponent getHrPyApplicationComponent() {
        return hrPyApplicationComponent;
    }
}
