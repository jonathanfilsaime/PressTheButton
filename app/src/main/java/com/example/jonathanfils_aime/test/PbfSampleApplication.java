package com.example.jonathanfils_aime.test;

import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import io.flic.poiclib.*;

public class PbfSampleApplication extends Application {
    public static final String TAG = "PbfSampleApplication";

    @Override
    public void onCreate() {
        super.onCreate();

        startService(new Intent(this.getApplicationContext(), PbfSampleService.class));

        FlicManager.init(this.getApplicationContext(), "e5f8a6de-fef7-472c-a5cc-8fea17bd750d", "52519ad8-b6f1-4ee6-b75a-102902d9c67a");

        FlicManager manager = FlicManager.getManager();

        for (FlicButton button : manager.getKnownButtons()) {
            button.connect();
            listenToButtonWithToast(button);
        }
    }

    public void listenToButtonWithToast(FlicButton button) {
        button.addEventListener(new FlicButtonAdapter() {
            @Override
            public void onButtonUpOrDown(FlicButton button, boolean wasQueued, int timeDiff, boolean isUp, boolean isDown) {
                if (isDown) {
                    Toast.makeText(getApplicationContext(), "Button " + button + " was pressed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
