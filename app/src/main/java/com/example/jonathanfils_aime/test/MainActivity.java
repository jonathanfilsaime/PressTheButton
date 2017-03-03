package com.example.jonathanfils_aime.test;

import android.Manifest;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.flic.poiclib.FlicButton;
import io.flic.poiclib.FlicButtonAdapter;
import io.flic.poiclib.FlicButtonListener;
import io.flic.poiclib.FlicButtonMode;
import io.flic.poiclib.FlicManager;
import io.flic.poiclib.FlicScanWizard;


public class MainActivity extends AppCompatActivity {

    public static int amount = 0;
    public static ProgressBar progressBar;
    public static TextView progress;

    HashMap<FlicButton, FlicButtonListener> listeners = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progress = (TextView) findViewById(R.id.progress) ;

        requestPermissions(new String[] {Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 4);

        for (FlicButton button : FlicManager.getManager().getKnownButtons())
        {
            setupEventListenerForButtonInActivity(button);
        }

        progressBar.setProgress(0);
        progress.setText("$" + amount);

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(MainActivity.this,
                R.array.chores, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            int check = 0;
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if(++check > 1) {
                    Button button = (Button) findViewById(R.id.single_click_button);
                    button.setText(parentView.getItemAtPosition(position).toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        spinner.setVisibility(View.INVISIBLE);
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        final Button first_button = (Button) findViewById(R.id.single_click_button);

        //TODO get form database
        first_button.setText("Sweep");

        first_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                spinner.performClick();
            }
        });
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        for (Map.Entry<FlicButton, FlicButtonListener> entry : listeners.entrySet()) {
            entry.getKey().removeEventListener(entry.getValue());
            entry.getKey().returnTemporaryMode(FlicButtonMode.SuperActive);
        }

        // Cancels the scan wizard, if it's running
        FlicManager.getManager().getScanWizard().cancel();
    }

    private void setupEventListenerForButtonInActivity(FlicButton button) {
        FlicButtonListener listener = new FlicButtonAdapter() {
            @Override
            public void onButtonSingleOrDoubleClickOrHold(FlicButton button, boolean wasQueued, int timeDiff,
                                                          boolean isSingleClick, boolean isDoubleClick, boolean isHold){
                if(isSingleClick){
//                    Toast.makeText(getApplicationContext(), "$2 has been transfered towards your Goal", Toast.LENGTH_SHORT).show();
                    amount += 2;

                } else if(isDoubleClick){
//                    Toast.makeText(getApplicationContext(), "$3 has been transfered towards your Goal", Toast.LENGTH_SHORT).show();
                    amount += 3;
                } else if (isHold){
//                    Toast.makeText(getApplicationContext(), "$5 has been transfered towards your Goal", Toast.LENGTH_SHORT).show();
                    amount += 5;
                } else {
//                    Toast.makeText(getApplicationContext(), "Something is broken", Toast.LENGTH_SHORT).show();
                }

                progressBar.setProgress(amount);
                progress.setText("$" + amount);
            }

        };

        button.addEventListener(listener);
        button.setTemporaryMode(FlicButtonMode.SuperActive);
        // Save the event listener so we can remove it later
        listeners.put(button, listener);
    }

    public void scanNewButton(View v) {
        // Disable the button until the scan wizard has finished
        v.setEnabled(false);
        System.out.println("Scan new button method");
        ((TextView)findViewById(R.id.scanWizardStatus)).setText("Press your Flic button");
        System.out.println("Starting scan wizard");

        FlicManager.getManager().getScanWizard().start(new FlicScanWizard.Callback() {
            @Override
            public void onDiscovered(FlicScanWizard wizard, String bdAddr, int rssi, boolean isPrivateMode, int revision) {
                String text = isPrivateMode ? "Found private button. Hold down for 7 seconds." : "Found Flic, now connecting...";
                ((TextView)findViewById(R.id.scanWizardStatus)).setText(text);
            }

            @Override
            public void onBLEConnected(FlicScanWizard wizard, String bdAddr) {
                ((TextView)findViewById(R.id.scanWizardStatus)).setText("Connected. Now pairing...");
            }

            @Override
            public void onCompleted(FlicScanWizard wizard, FlicButton button) {
                findViewById(R.id.scanNewButton).setEnabled(true);
                ((PbfSampleApplication)getApplication()).listenToButtonWithToast(button);

                ((TextView)findViewById(R.id.scanWizardStatus)).setText("Scan wizard success!");
                setupEventListenerForButtonInActivity(button);
            }

            @Override
            public void onFailed(FlicScanWizard wizard, int flicScanWizardErrorCode) {
                findViewById(R.id.scanNewButton).setEnabled(true);
                ((TextView)findViewById(R.id.scanWizardStatus)).setText("Scan wizard failed with code " + flicScanWizardErrorCode);
            }
        });
    }
}



