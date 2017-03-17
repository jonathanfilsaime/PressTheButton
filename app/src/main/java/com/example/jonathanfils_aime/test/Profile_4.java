package com.example.jonathanfils_aime.test;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
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

public class Profile_4 extends AppCompatActivity implements NumberPicker.OnValueChangeListener {

    public TextView dadProfile;
    public TextView momProfile;
    public TextView child1Profile;
    public TextView child2Profile;
    public TextView progress;
    public TextView goal;
    public boolean isButtonConnected = false;
    public int amount = 0;
    public TextView profileName;
    public ProgressBar progressBar;
    boolean isFirstTime = true;

    public String checkName;
    public String the_first_name;
    public String the_last_name;
    public int the_phone_number;
    public int the_amount = 0;
    public int the_goal = 0;
    public int singleClickCounter;
    public int doubleClickCounter;
    public int longClickCounter;

    public String single_click_name = "Single Click";
    public String double_click_name = "Double Click";
    public String long_press_name = "Long Press";

    public String PARENTS_BUTTON = "F022cgOY";
    public String CHILDRENS_BUTTON = "F018cgRB";

    //////
    public Button goalButton;
    /////

    HashMap<FlicButton, FlicButtonListener> listeners = new HashMap<>();

    //database
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_4);

        databaseHelper = new DatabaseHelper(this);
        db = databaseHelper.getWritableDatabase();
        String testName = "Jennifer";

//        check if entry already exist
        Cursor cursor = db.rawQuery("SELECT first_name FROM records WHERE profile = '4'", null);
        while (cursor.moveToNext())
        {
            checkName = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_FIRST_NAME));
        }

        if(testName.equals(checkName))
        {

        }
        else
        {
            ContentValues values = new ContentValues();
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_FIRST_NAME, "Jennifer");
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LAST_NAME, "Smith");
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_PHONE_NUMBER, 469-999-7777);
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_CURRENT, the_amount);
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_GOAL, the_goal);
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SINGLE_CLICK, singleClickCounter);
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DOUBLE_CLICK, doubleClickCounter);
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LONG_PRESS, longClickCounter);
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_PROFILE, 4);
            /////
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SINGLE_CLICK_CHOICE, single_click_name);
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DOUBLE_CLICK_CHOICE, double_click_name);
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LONG_PRESS_CLICK_CHOICE, long_press_name);
            /////
            db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
        }


        //if exist now read an set the elements
        cursor = db.rawQuery("SELECT * FROM records WHERE profile = 4", null);
        while(cursor.moveToNext())
        {
            the_first_name = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_FIRST_NAME));
            the_last_name = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_LAST_NAME));
            the_phone_number = cursor.getInt(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_PHONE_NUMBER));
            the_amount = cursor.getInt(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_CURRENT));
            the_goal = cursor.getInt(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_GOAL));
            singleClickCounter = cursor.getInt(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_SINGLE_CLICK));
            doubleClickCounter = cursor.getInt(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_DOUBLE_CLICK));
            longClickCounter = cursor.getInt(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_LONG_PRESS));
            /////
            single_click_name = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_SINGLE_CLICK_CHOICE));
            double_click_name = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_DOUBLE_CLICK_CHOICE));
            long_press_name = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_LONG_PRESS_CLICK_CHOICE));
            ////
            System.out.println("first name " + the_first_name);
            System.out.println("last name " + the_last_name);
            System.out.println("Amount " + the_amount);
        }

        goal = (TextView) findViewById(R.id.whiteBox);
        goal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show();
            }
        });


        cursor.close();
        //Activity elements
        dadProfile = (TextView) findViewById(R.id.profile1);
        momProfile = (TextView) findViewById(R.id.profile2);
        child1Profile = (TextView) findViewById(R.id.profile3);
        child2Profile = (TextView) findViewById(R.id.profile4);
        progress = (TextView) findViewById(R.id.progress);
        goal = (TextView) findViewById(R.id.goal);
        profileName = (TextView) findViewById(R.id.profile4name);

        //set using database values
        profileName.setText(the_first_name);
        goal.setText("$" + the_goal);
        progress.setText("$" + the_amount);

        //drop down list
        ArrayAdapter<CharSequence> first_adapter = ArrayAdapter.createFromResource(Profile_4.this,
                R.array.chores, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> second_adapter = ArrayAdapter.createFromResource(Profile_4.this,
                R.array.chores, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> third_adapter = ArrayAdapter.createFromResource(Profile_4.this,
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
        progressBar.setProgress(the_amount);

        //button to activate drop down
        final Button first_button = (Button) findViewById(R.id.single_click_button);
        first_button.setText("Single CLick");

        final Button second_button = (Button) findViewById(R.id.double_click_button);
        second_button.setText("Double Click");

        final Button third_button = (Button) findViewById(R.id.press_and_hold_button);
        third_button.setText("Long Hold");

        //////
        first_button.setText(single_click_name);
        second_button.setText(double_click_name);
        third_button.setText(long_press_name);

        int reference = getApplicationContext().getResources().getIdentifier(single_click_name.replaceAll(" ", "_"),
                "drawable", getApplicationContext().getPackageName());
        first_button.setCompoundDrawablesWithIntrinsicBounds(reference, 0, R.drawable.image_two_dollars, 0);
        reference = getApplicationContext().getResources().getIdentifier(double_click_name.replaceAll(" ", "_"),
                "drawable", getApplicationContext().getPackageName());
        second_button.setCompoundDrawablesWithIntrinsicBounds(reference, 0, R.drawable.image_three_dollars, 0);
        reference = getApplicationContext().getResources().getIdentifier(long_press_name.replaceAll(" ", "_"),
                "drawable", getApplicationContext().getPackageName());
        third_button.setCompoundDrawablesWithIntrinsicBounds(reference, 0, R.drawable.image_five_dollars, 0);

        //////

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
                    String text = parentView.getItemAtPosition(position).toString();
                    text = text.replaceAll(" ", "_");

                    Button button = (Button) findViewById(R.id.single_click_button);
                    button.setText(parentView.getItemAtPosition(position).toString());
                    single_click_name = parentView.getItemAtPosition(position).toString();

                    int reference = getApplicationContext().getResources().getIdentifier(text,
                            "drawable", getApplicationContext().getPackageName());

                    button.setCompoundDrawablesWithIntrinsicBounds(reference, 0, R.drawable.image_two_dollars, 0);
                }
                //////////
                ContentValues valuesUpdated = new ContentValues();
//                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_FIRST_NAME, "Darren");
//                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LAST_NAME, "Smith");
//                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_PHONE_NUMBER, 469-999-7777);
//                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_GOAL, the_goal);
//                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_CURRENT, the_amount);
//                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SINGLE_CLICK, singleClickCounter);
//                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DOUBLE_CLICK, doubleClickCounter);
//                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LONG_PRESS, longClickCounter);
//                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LONG_PRESS, longClickCounter);
                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SINGLE_CLICK_CHOICE, single_click_name);
                db.update(FeedReaderContract.FeedEntry.TABLE_NAME, valuesUpdated, "profile = '4'", null);
                ////////
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
                    String text = parentView.getItemAtPosition(position).toString();
                    text = text.replaceAll(" ", "_");

                    Button button = (Button) findViewById(R.id.double_click_button);
                    button.setText(parentView.getItemAtPosition(position).toString());
                    double_click_name = parentView.getItemAtPosition(position).toString();

                    int reference = getApplicationContext().getResources().getIdentifier(text,
                            "drawable", getApplicationContext().getPackageName());

                    button.setCompoundDrawablesWithIntrinsicBounds(reference, 0, R.drawable.image_three_dollars, 0);
                }
                //////
                ContentValues valuesUpdated = new ContentValues();
//                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_FIRST_NAME, "Darren");
//                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LAST_NAME, "Smith");
//                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_PHONE_NUMBER, 469-999-7777);
//                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_GOAL, the_goal);
//                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_CURRENT, the_amount);
//                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SINGLE_CLICK, singleClickCounter);
//                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DOUBLE_CLICK, doubleClickCounter);
//                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LONG_PRESS, longClickCounter);
//                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LONG_PRESS, longClickCounter);
                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DOUBLE_CLICK_CHOICE, double_click_name);
                db.update(FeedReaderContract.FeedEntry.TABLE_NAME, valuesUpdated, "profile = '4'", null);
                /////
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
                    String text = parentView.getItemAtPosition(position).toString();
                    text = text.replaceAll(" ", "_");

                    Button button = (Button) findViewById(R.id.press_and_hold_button);
                    button.setText(parentView.getItemAtPosition(position).toString());

                    long_press_name = parentView.getItemAtPosition(position).toString();

                    int reference = getApplicationContext().getResources().getIdentifier(text,
                            "drawable", getApplicationContext().getPackageName());

                    button.setCompoundDrawablesWithIntrinsicBounds(reference, 0, R.drawable.image_five_dollars, 0);
                }
                ///////
                ContentValues valuesUpdated = new ContentValues();
//                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_FIRST_NAME, "Darren");
//                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LAST_NAME, "Smith");
//                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_PHONE_NUMBER, 469-999-7777);
//                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_GOAL, the_goal);
//                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_CURRENT, the_amount);
//                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SINGLE_CLICK, singleClickCounter);
//                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DOUBLE_CLICK, doubleClickCounter);
//                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LONG_PRESS, longClickCounter);
//                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LONG_PRESS, longClickCounter);
                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LONG_PRESS_CLICK_CHOICE, long_press_name);
                db.update(FeedReaderContract.FeedEntry.TABLE_NAME, valuesUpdated, "profile = '4'", null);
                //////
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

         if( getIntent().getExtras().getBoolean("isButtonConnected") ) {
            isButtonConnected = true;
         } else {
             isButtonConnected = false;
         }

        if(isButtonConnected) {
            Button scan_button = (Button) findViewById(R.id.scanNewButton);
            scan_button.setVisibility(View.GONE);
        }
    }

    public void show()
    {
        final Dialog d = new Dialog(Profile_4.this);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.dialog);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(100);
        np.setMinValue(0);
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(this);

        goalButton = (Button) d.findViewById(R.id.setGoal);

        System.out.println("my button" + goalButton.toString());
        goalButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                goal.setText(String.valueOf(np.getValue()));
                ContentValues valuesUpdated = new ContentValues();
//                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_FIRST_NAME, "Darren");
//                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LAST_NAME, "Smith");
//                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_PHONE_NUMBER, 469-999-7777);
                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_GOAL, String.valueOf(np.getValue()));
//                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_CURRENT, the_amount);
//                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SINGLE_CLICK, singleClickCounter);
//                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DOUBLE_CLICK, doubleClickCounter);
//                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LONG_PRESS, longClickCounter);
//                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LONG_PRESS, longClickCounter);
//                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DOUBLE_CLICK_CHOICE, double_click_name);
                db.update(FeedReaderContract.FeedEntry.TABLE_NAME, valuesUpdated, "profile = '4'", null);
                d.dismiss();
            }
        });
        d.show();

    }

    //loading profile activity
    public void startProfile1(View v)
    {

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("isButtonConnected", isButtonConnected);
        startActivity(intent);
        overridePendingTransition(0,0);
        this.finish();
    }

    public void startProfile2(View v)
    {
        Intent intent = new Intent(this, Profile_2.class);
        intent.putExtra("isButtonConnected", isButtonConnected);
        startActivity(intent);
        overridePendingTransition(0,0);
        this.finish();
    }

    public void startProfile3(View v)
    {
        Intent intent = new Intent(this, Profile_3.class);
        intent.putExtra("isButtonConnected", isButtonConnected);
        startActivity(intent);
        overridePendingTransition(0,0);
        this.finish();
    }

    public void startProfile4(View v)
    {
//        Intent intent = new Intent(this, Profile_4.class);
//        startActivity(intent);
//        overridePendingTransition(0,0);
//        this.finish();
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
                    isButtonConnected = true;
                    return;
                }
                if( button.getName().equals(PARENTS_BUTTON) ) {
                    if (isSingleClick) {
                        the_amount += 2;
                        singleClickCounter++;
                    } else if (isDoubleClick) {
                        the_amount += 3;
                        doubleClickCounter++;
                    } else if (isHold) {
                        the_amount += 5;
                        longClickCounter++;
                    }
                }

                progress.setText("$" + the_amount);
                progressBar.setProgress(the_amount);

                ContentValues valuesUpdated = new ContentValues();
                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_FIRST_NAME, "Jennifer");
                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LAST_NAME, "Smith");
                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_PHONE_NUMBER, 469-999-7777);
                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_GOAL, the_goal);
                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_CURRENT, the_amount);
                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SINGLE_CLICK, singleClickCounter);
                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DOUBLE_CLICK, doubleClickCounter);
                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LONG_PRESS, longClickCounter);
                db.update(FeedReaderContract.FeedEntry.TABLE_NAME, valuesUpdated, "profile = '4'", null);
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

    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {

    }
}
