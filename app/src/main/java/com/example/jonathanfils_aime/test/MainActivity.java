package com.example.jonathanfils_aime.test;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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


public class MainActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener {

    public TextView progress;
    public TextView goal;
    public boolean isButtonConnected = false;
    public boolean isFirstTime = false;
    public TextView profileName;
    public ProgressBar progressBar;

    public String checkName;
    public String the_first_name;
    public String the_last_name;

    public int the_phone_number;
    public int the_amount = 0;
    public int the_goal = 0;
    public int singleClickCounter;
    public int doubleClickCounter;
    public int longClickCounter;
    private int amount_per_day = 0;

    private String single_click_name = "Single Click";
    private String double_click_name = "Double Click";
    private String long_press_name = "Long Press";

    public String PARENTS_BUTTON = "F022cgOY";

    private Button first_button;
    private Button second_button;
    private Button third_button ;
    private Button goalButton;

    private Spinner first_spinner;
    private Spinner second_spinner;
    private Spinner third_spinner;
    HashMap<FlicButton, FlicButtonListener> listeners = new HashMap<>();

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator( getResources().getDrawable(R.drawable.logo) );

        // Set the background color of the activity
        View someView = findViewById(R.id.activity_main);
        View root = someView.getRootView();
        root.setBackgroundColor(getResources().getColor(R.color.main_activity_background_color));

        //DATABASE//////////////////////////////////////////////////////////////////////////////////
        databaseHelper = new DatabaseHelper(this);
        db = databaseHelper.getWritableDatabase();

        String testName = "Darren";

        //check if entry already exist
        Cursor cursor = db.rawQuery("SELECT first_name FROM records WHERE profile = '1'", null);
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
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_FIRST_NAME, "Darren");
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LAST_NAME, "Smith");
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_PHONE_NUMBER, 469-999-7777);
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_CURRENT, the_amount);
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_GOAL, the_goal);
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SINGLE_CLICK, singleClickCounter);
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DOUBLE_CLICK, doubleClickCounter);
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LONG_PRESS, longClickCounter);
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_PROFILE, 1);
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SINGLE_CLICK_CHOICE, single_click_name);
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DOUBLE_CLICK_CHOICE, double_click_name);
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LONG_PRESS_CLICK_CHOICE, long_press_name);
            db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
        }

        //if exist now read an set the elements
        cursor = db.rawQuery("SELECT * FROM records WHERE profile = 1", null);
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
            single_click_name = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_SINGLE_CLICK_CHOICE));
            double_click_name = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_DOUBLE_CLICK_CHOICE));
            long_press_name = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_LONG_PRESS_CLICK_CHOICE));
        }

        cursor.close();

        //END DATABASE//////////////////////////////////////////////////////////////////////////////

        //TOP CARD AND PROGRESS BAR/////////////////////////////////////////////////////////////////

        goal = (TextView) findViewById(R.id.whiteBox);
        goal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show();
            }
        });

        goal = (TextView) findViewById(R.id.goal);
        if(the_goal == 0){
            the_goal = 25;
        }
        goal.setText("$" + the_goal);

        progress = (TextView) findViewById(R.id.progress);
        progress.setText("$" + the_amount);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(the_goal);
        progressBar.setProgress(the_amount);

        profileName = (TextView) findViewById(R.id.profile1name);
        profileName.setText(the_first_name);

        ////////////////////////////////////////////////////////////////////////////////////////////

        //SPINNERS//////////////////////////////////////////////////////////////////////////////////
        ArrayAdapter<CharSequence> first_adapter = ArrayAdapter.createFromResource(MainActivity.this,
                R.array.adults, android.R.layout.simple_spinner_item);

        first_spinner = (Spinner) findViewById(R.id.first_spinner);
        first_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        first_spinner.setAdapter(first_adapter);

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

                    button.setCompoundDrawablesWithIntrinsicBounds(reference,  R.drawable.single_click, R.drawable.image_two_dollars, 0);
                }

                ContentValues valuesUpdated = new ContentValues();
                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SINGLE_CLICK_CHOICE, single_click_name);
                db.update(FeedReaderContract.FeedEntry.TABLE_NAME, valuesUpdated, "profile = '1'", null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {
            }
        });

        ArrayAdapter<CharSequence> second_adapter = ArrayAdapter.createFromResource(MainActivity.this,
                R.array.adults, android.R.layout.simple_spinner_item);

        second_spinner = (Spinner) findViewById(R.id.second_spinner);
        second_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        second_spinner.setAdapter(second_adapter);

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

                    button.setCompoundDrawablesWithIntrinsicBounds(reference,  R.drawable.double_click, R.drawable.image_three_dollars, 0);
                }

                ContentValues valuesUpdated = new ContentValues();
                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DOUBLE_CLICK_CHOICE, double_click_name);
                db.update(FeedReaderContract.FeedEntry.TABLE_NAME, valuesUpdated, "profile = '1'", null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {
            }
        });

        ArrayAdapter<CharSequence> third_adapter = ArrayAdapter.createFromResource(MainActivity.this,
                R.array.adults, android.R.layout.simple_spinner_item);

        third_spinner = (Spinner) findViewById(R.id.third_spinner);
        third_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        third_spinner.setAdapter(third_adapter);

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

                    button.setCompoundDrawablesWithIntrinsicBounds(reference, R.drawable.press_and_hold, R.drawable.image_five_dollars, 0);
                }

                ContentValues valuesUpdated = new ContentValues();
                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LONG_PRESS_CLICK_CHOICE, long_press_name);
                db.update(FeedReaderContract.FeedEntry.TABLE_NAME, valuesUpdated, "profile = '1'", null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {

            }
        });

        first_spinner.setVisibility(View.INVISIBLE);
        second_spinner.setVisibility(View.INVISIBLE);
        third_spinner.setVisibility(View.INVISIBLE);

        ////////////////////////////////////////////////////////////////////////////////////////////

        //BUTTONS FOR SPINNERS /////////////////////////////////////////////////////////////////////
        first_button = (Button) findViewById(R.id.single_click_button);
        first_button.setText("Single CLick");

        second_button = (Button) findViewById(R.id.double_click_button);
        second_button.setText("Double Click");

        third_button = (Button) findViewById(R.id.press_and_hold_button);
        third_button.setText("Long Hold");

        first_button.setText(single_click_name);
        second_button.setText(double_click_name);
        third_button.setText(long_press_name);

        int reference = getApplicationContext().getResources().getIdentifier(single_click_name.replaceAll(" ", "_"),
                "drawable", getApplicationContext().getPackageName());
        first_button.setCompoundDrawablesWithIntrinsicBounds(reference, R.drawable.single_click, R.drawable.image_two_dollars, 0);
        reference = getApplicationContext().getResources().getIdentifier(double_click_name.replaceAll(" ", "_"),
                "drawable", getApplicationContext().getPackageName());
        second_button.setCompoundDrawablesWithIntrinsicBounds(reference, R.drawable.double_click, R.drawable.image_three_dollars, 0);
        reference = getApplicationContext().getResources().getIdentifier(long_press_name.replaceAll(" ", "_"),
                "drawable", getApplicationContext().getPackageName());
        third_button.setCompoundDrawablesWithIntrinsicBounds(reference, R.drawable.press_and_hold, R.drawable.image_five_dollars, 0);

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

        if(isButtonConnected) {
            Button scan_button = (Button) findViewById(R.id.scanNewButton);
            scan_button.setVisibility(View.GONE);
        }

        ////////////////////////////////////////////////////////////////////////////////////////////

        //bluetooth permission for the button
        requestPermissions(new String[] {Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 4);

        //flic button
        for (FlicButton button : FlicManager.getManager().getKnownButtons())
        {
            setupEventListenerForButtonInActivity(button);
        }
    }

    public void show()
    {
        final Dialog d = new Dialog(MainActivity.this);
        d.setContentView(R.layout.dialog);
        d.getWindow().setBackgroundDrawableResource(R.color.dialog_background);

        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);

        np.setMaxValue(40);
        np.setMinValue(0);
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(this);

        // Creates and sets the array of values for the NumberPicker
        final String[] goalValues = new String[39];

        for (int i = 1; i < goalValues.length + 1; i++) {
            String number = Integer.toString(i*25);
            goalValues[i - 1] =  number;
        }
        np.setDisplayedValues(goalValues);

        goalButton = (Button) d.findViewById(R.id.setGoal);

        goalButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                goal.setText("$"+ goalValues[np.getValue()] );
                ContentValues valuesUpdated = new ContentValues();

                the_goal = Integer.parseInt(goalValues[np.getValue()]);
                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_GOAL, the_goal);

                db.update(FeedReaderContract.FeedEntry.TABLE_NAME, valuesUpdated, "profile = '1'", null);

                progressBar.setMax(the_goal);
                progressBar.setProgress(the_amount);

                d.dismiss();
            }
        });
        d.show();
    }

    public void startProfile1(View v) {

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
        Intent intent = new Intent(this, Profile_3.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        this.finish();
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
                System.out.println(button.getName());

                final Dialog d = new Dialog(MainActivity.this);
                d.setContentView(R.layout.dialog_congrats);

                if(isFirstTime)
                {
                    isFirstTime = false;
                    Button scan_button = (Button) findViewById(R.id.scanNewButton);
                    scan_button.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "First Button Connected", Toast.LENGTH_LONG).show();
                    isButtonConnected = true;
                    return;
                }
                if( button.getName().equals(PARENTS_BUTTON) ) {
                    if(amount_per_day < 10) {
                        if (isSingleClick) {
                            the_amount += 2;
                            amount_per_day += 2;
                            singleClickCounter++;
                        } else if (isDoubleClick) {
                            the_amount += 3;
                            amount_per_day += 3;
                            doubleClickCounter++;
                        } else if (isHold) {
                            the_amount += 5;
                            amount_per_day += 5;
                            longClickCounter++;
                        }

                        if (the_amount >= the_goal) {
                            d.setContentView(R.layout.dialog_congrats);
                            d.show();

                            the_amount = 0;

                            ContentValues valuesUpdated = new ContentValues();
                            valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_CURRENT, the_amount);
                            db.update(FeedReaderContract.FeedEntry.TABLE_NAME, valuesUpdated, "profile = '1'", null);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Cannot transfer more than $10 per day", Toast.LENGTH_LONG).show();
                    }
                }

                progress.setText("$" + the_amount);
                progressBar.setProgress(the_amount);

                ContentValues valuesUpdated = new ContentValues();
                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_FIRST_NAME, "Darren");
                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LAST_NAME, "Smith");
                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_PHONE_NUMBER, 469-999-7777);
                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_GOAL, the_goal);
                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_CURRENT, the_amount);
                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SINGLE_CLICK, singleClickCounter);
                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DOUBLE_CLICK, doubleClickCounter);
                valuesUpdated.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LONG_PRESS, longClickCounter);
                db.update(FeedReaderContract.FeedEntry.TABLE_NAME, valuesUpdated, "profile = '1'", null);
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

        FlicManager.getManager().getScanWizard().start(new FlicScanWizard.Callback() {
            @Override
            public void onDiscovered(FlicScanWizard wizard, String bdAddr, int rssi, boolean isPrivateMode, int revision) {
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



