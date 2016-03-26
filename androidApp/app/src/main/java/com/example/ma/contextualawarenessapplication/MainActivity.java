package com.example.ma.contextualawarenessapplication;

import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private ListView mainView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainView = (ListView) findViewById(R.id.main_list);
        // Defined Array values to show in ListView
        String[] values = new String[] { "Micro-location",
                "Geofencing",
                "Proximity",
                "Our Website",
                "Debug Panel",
        };

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        // Assign adapter to ListView
        mainView.setAdapter(adapter);

        // ListView Item Click Listener
        mainView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                if(itemPosition == 0){
                    //"Micro-location"
                }
                else if(itemPosition == 1){
                    //"Geofencing"

                }
                else if(itemPosition == 2){
                    //"Proximity"

                }
                else if(itemPosition == 3){
                    //Our Website"

                }
                else if(itemPosition == 4){
                    //"Debug Panel"
                    startActivity(new Intent(MainActivity.this, DebugActivity.class));
                }

                // ListView Clicked item value
                String  itemValue    = (String) mainView.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :" + itemPosition + "  ListItem : " + itemValue , Toast.LENGTH_LONG)
                        .show();
            }
        });

        //=============================================================
        Intent i = new Intent(this, BackgroundService.class);
        startService(i);

    }


}
