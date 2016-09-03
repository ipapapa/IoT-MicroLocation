package com.maxinghua.main;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.maxinghua.application.App;
import com.maxinghua.fragments.DebugFragment;
import com.maxinghua.fragments.GeofencingFragment;
import com.maxinghua.fragments.MicroLocationFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerlist;
    private ArrayList<String> menuLists;
    private ArrayAdapter<String> adapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mTitle;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment fragment = (Fragment) new MicroLocationFragment();
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.content_frame, fragment).commit();

        final String log = ((App) getApplicationContext()).getLog();

        mTitle = (String) getTitle();

        // Show the Up button in the action bar.
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
            actionBar.setTitle(mTitle);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerlist = (ListView) findViewById(R.id.left_drawer);
        menuLists = new ArrayList<String>();
        menuLists.add("Micro-Location");
        menuLists.add("Geofencing");
        menuLists.add("Proximity");
        menuLists.add("Our Website");
        menuLists.add("Debug-Panel");

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, menuLists);
        mDrawerlist.setAdapter(adapter);
        mDrawerlist.setOnItemClickListener(this);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                actionBar.setTitle("");
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if(actionBar.getTitle().toString().equals(""))
                {
                    actionBar.setTitle(mTitle);
                }

            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        mDrawerToggle.syncState();
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        mDrawerToggle.onConfigurationChanged(newConfig);
//    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Show Alert
//        Toast.makeText(getApplicationContext(),
//                "Position :" + position + "  ListItem : " + parent.toString(), Toast.LENGTH_LONG)
//                .show();
//        System.out.println(position);

        FragmentManager fm = getFragmentManager();

        switch (position)
        {
            case 0: {
                actionBar.setTitle("Micro-Location");
                Fragment fragment = (Fragment) new MicroLocationFragment();
                fm.beginTransaction().replace(R.id.content_frame, fragment).commit();
                break;
            }
            case 1: {
                actionBar.setTitle("Geofencing");
                Fragment fragment = (Fragment) new GeofencingFragment();
                fm.beginTransaction().replace(R.id.content_frame, fragment).commit();
                break;
            }
            case 2: {
                actionBar.setTitle("Proximity");
                break;
            }
            case 3: {
                actionBar.setTitle("Our Website");
                break;
            }
            //Debug
            case 4: {
                actionBar.setTitle("Debug Panel");
                Fragment fragment = new DebugFragment();
                fm.beginTransaction().replace(R.id.content_frame, fragment).commit();
                break;
            }
        }
    }
}
