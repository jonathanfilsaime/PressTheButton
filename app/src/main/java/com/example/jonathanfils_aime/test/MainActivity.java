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
    boolean isFirstTime = true;

    HashMap<FlicButton, FlicButtonListener> listeners = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        requestPermissions(new String[] {Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 4);

        for (FlicButton button : FlicManager.getManager().getKnownButtons())
        {
            setupEventListenerForButtonInActivity(button);
        }


        //SPINNERS////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        final Spinner first_spinner = (Spinner) findViewById(R.id.first_spinner);
        ArrayAdapter<CharSequence> first_adapter = ArrayAdapter.createFromResource(MainActivity.this,
                R.array.chores, android.R.layout.simple_spinner_item);
        first_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        first_spinner.setAdapter(first_adapter);
        first_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        final Spinner second_spinner = (Spinner) findViewById(R.id.second_spinner);
        ArrayAdapter<CharSequence> second_adapter = ArrayAdapter.createFromResource(MainActivity.this,
                R.array.chores, android.R.layout.simple_spinner_item);
        second_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        second_spinner.setAdapter(second_adapter);
        second_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                     int check = 0;

                                                     @Override
                                                     public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                                                         if (++check > 1) {
                                                             Button button = (Button) findViewById(R.id.double_click_button);
                                                             button.setText(parentView.getItemAtPosition(position).toString());
                                                         }
                                                     }

                                                     @Override
                                                     public void onNothingSelected(AdapterView<?> parentView) {

                                                     }
                                                 });

        final Spinner third_spinner = (Spinner) findViewById(R.id.third_spinner);
        ArrayAdapter<CharSequence> third_adapter = ArrayAdapter.createFromResource(MainActivity.this,
                R.array.chores, android.R.layout.simple_spinner_item);
        third_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        third_spinner.setAdapter(third_adapter);
        third_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            int check = 0;

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (++check > 1) {
                    Button button = (Button) findViewById(R.id.press_and_hold_button);
                    button.setText(parentView.getItemAtPosition(position).toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });


        first_spinner.setVisibility(View.INVISIBLE);
        second_spinner.setVisibility(View.INVISIBLE);
        third_spinner.setVisibility(View.INVISIBLE);
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //BUTTONS/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        final Button first_button = (Button) findViewById(R.id.single_click_button);

        //TODO get form database
        first_button.setText("Sweep");

        first_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                first_spinner.performClick();
            }
        });

        final Button second_button = (Button) findViewById(R.id.double_click_button);

        //TODO get form database
        second_button.setText("Grades");

        second_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                second_spinner.performClick();
            }
        });

        final Button third_button = (Button) findViewById(R.id.press_and_hold_button);

        //TODO get form database
        third_button.setText("Trash");

        third_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                third_spinner.performClick();
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
                if(isFirstTime) {
                    isFirstTime = false;
                    Toast.makeText(getApplicationContext(), "Button Connected", Toast.LENGTH_LONG).show();
                    return;
                }
                if(isSingleClick){
                    amount += 2;

                } else if(isDoubleClick){
                    amount += 3;
                } else if (isHold){
                    amount += 5;
                }

                progressBar.setProgress(amount);
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
        System.out.println("Starting scan wizard");

        FlicManager.getManager().getScanWizard().start(new FlicScanWizard.Callback() {
            @Override
            public void onDiscovered(FlicScanWizard wizard, String bdAddr, int rssi, boolean isPrivateMode, int revision) {
                String text = isPrivateMode ? "Found private button. Hold down for 7 seconds." : "Found Flic, now connecting...";
            }

            @Override
            public void onBLEConnected(FlicScanWizard wizard, String bdAddr) {

            }

            @Override
            public void onCompleted(FlicScanWizard wizard, FlicButton button) {
                findViewById(R.id.scanNewButton).setEnabled(true);
                ((PbfSampleApplication)getApplication()).listenToButtonWithToast(button);

                setupEventListenerForButtonInActivity(button);
            }

            @Override
            public void onFailed(FlicScanWizard wizard, int flicScanWizardErrorCode) {
                findViewById(R.id.scanNewButton).setEnabled(true);
            }
        });
    }
}



