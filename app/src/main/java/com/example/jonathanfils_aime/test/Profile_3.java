package com.example.jonathanfils_aime.test;

import android.Manifest;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import io.flic.poiclib.FlicButton;
import io.flic.poiclib.FlicButtonAdapter;
import io.flic.poiclib.FlicButtonListener;
import io.flic.poiclib.FlicButtonMode;
import io.flic.poiclib.FlicManager;
import io.flic.poiclib.FlicScanWizard;

public class Profile_3 extends AppCompatActivity {

    public TextView dadProfile;
    public TextView momProfile;
    public TextView child1Profile;
    public TextView child2Profile;
    public TextView progress;
    public TextView goal;
    public int amount = 0;
    public ProgressBar progressBar;
    boolean isFirstTime = true;

    HashMap<FlicButton, FlicButtonListener> listeners = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_3);

        System.out.println("\n\n\ncreated christopher profile\n\n\n");

        //Activity elements
        dadProfile = (TextView) findViewById(R.id.profile1);
        momProfile = (TextView) findViewById(R.id.profile2);
        child1Profile = (TextView) findViewById(R.id.profile3);
        child2Profile = (TextView) findViewById(R.id.profile4);
        progress = (TextView) findViewById(R.id.progress);
        goal = (TextView) findViewById(R.id.goal);
        goal.setText("$" + 100);
        progress.setText("$" + amount);

        //drop down list
        ArrayAdapter<CharSequence> first_adapter = ArrayAdapter.createFromResource(Profile_3.this,
                R.array.chores, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> second_adapter = ArrayAdapter.createFromResource(Profile_3.this,
                R.array.chores, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> third_adapter = ArrayAdapter.createFromResource(Profile_3.this,
                R.array.chores, android.R.layout.simple_spinner_item);

        //spinners
        final Spinner first_spinner = (Spinner) findViewById(R.id.first_spinner);
        first_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        first_spinner.setAdapter(first_adapter);

        final Spinner second_spinner = (Spinner) findViewById(R.id.second_spinner);
        second_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        second_spinner.setAdapter(second_adapter);

        final Spinner third_spinner = (Spinner) findViewById(R.id.third_spinner);
        third_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        third_spinner.setAdapter(third_adapter);

        //progress bar
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setProgress(0);

        //button to activate drop down
        final Button first_button = (Button) findViewById(R.id.single_click_button);
        first_button.setText("Single CLick");

        final Button second_button = (Button) findViewById(R.id.double_click_button);
        second_button.setText("Double Click");

        final Button third_button = (Button) findViewById(R.id.press_and_hold_button);
        third_button.setText("Long Hold");

        //bluetooth permission for the button
        requestPermissions(new String[] {Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 4);

        //flic button
        for (FlicButton button : FlicManager.getManager().getKnownButtons())
        {
            setupEventListenerForButtonInActivity(button);
        }


        first_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            int check = 0;
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {

                if(++check > 1)
                {
                    Button button = (Button) findViewById(R.id.single_click_button);
                    button.setText(parentView.getItemAtPosition(position).toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {

            }

        });


        second_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            int check = 0;

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (++check > 1) {
                    Button button = (Button) findViewById(R.id.double_click_button);
                    button.setText(parentView.getItemAtPosition(position).toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {

            }
        });


        third_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            int check = 0;

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (++check > 1) {
                    Button button = (Button) findViewById(R.id.press_and_hold_button);
                    button.setText(parentView.getItemAtPosition(position).toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {

            }
        });

        first_spinner.setVisibility(View.INVISIBLE);
        second_spinner.setVisibility(View.INVISIBLE);
        third_spinner.setVisibility(View.INVISIBLE);



        first_button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                first_spinner.performClick();
            }
        });

        second_button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                second_spinner.performClick();
            }
        });

        third_button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                third_spinner.performClick();
            }
        });
    }

    //loading profile activity
    public void startProfile1(View v)
    {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        this.finish();
    }

    public void startProfile2(View v)
    {
        Intent intent = new Intent(this, Profile_2.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        this.finish();
    }

    public void startProfile3(View v)
    {
//        Intent intent = new Intent(this, Profile_3.class);
//        startActivity(intent);
//        overridePendingTransition(0,0);
//        this.finish();
    }

    public void startProfile4(View v)
    {
        Intent intent = new Intent(this, Profile_4.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        this.finish();
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

    //button press action
    private void setupEventListenerForButtonInActivity(FlicButton button)
    {
        FlicButtonListener listener = new FlicButtonAdapter()
        {
            @Override
            public void onButtonSingleOrDoubleClickOrHold(FlicButton button, boolean wasQueued, int timeDiff,
                                                          boolean isSingleClick, boolean isDoubleClick, boolean isHold){
                if(isFirstTime)
                {
                    isFirstTime = false;
                    Button scan_button = (Button) findViewById(R.id.scanNewButton);
                    scan_button.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Button Connected", Toast.LENGTH_LONG).show();
                    return;
                }
                if(isSingleClick)
                {
                    amount += 2;

                }
                else if(isDoubleClick)
                {
                    amount += 3;
                }
                else if (isHold)
                {
                    amount += 5;
                }

                progress.setText("$" + amount);
                progressBar.setProgress(amount);
            }

        };
        button.addEventListener(listener);
        button.setTemporaryMode(FlicButtonMode.SuperActive);
        listeners.put(button, listener);
    }

    //scanning for new button
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
