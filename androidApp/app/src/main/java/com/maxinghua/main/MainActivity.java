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
import android.webkit.WebViewFragment;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.maxinghua.application.App;
import com.maxinghua.fragments.DebugFragment;
import com.maxinghua.fragments.DialogFragment;
import com.maxinghua.fragments.GeofencingFragment;
import com.maxinghua.fragments.LoginFragment;
import com.maxinghua.fragments.MicroLocationFragment;
import com.maxinghua.fragments.WebsiteFragment;

import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerlist;
    private ArrayList<String> menuLists;
    private ArrayAdapter<String> adapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mTitle;
    private ActionBar actionBar;



    private Fragment microLocationFragment;
    private Fragment dialogFragment;
    private Fragment geofencingFragment;
    private Fragment loginFragment;
    private Fragment websiteFragment;
    private Fragment debugFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        FragmentManager fm = getFragmentManager();
        microLocationFragment = new MicroLocationFragment();
        geofencingFragment = new GeofencingFragment();
        loginFragment = new LoginFragment();
        websiteFragment = new WebsiteFragment();
        debugFragment = new DebugFragment();
        dialogFragment = new DialogFragment();
        fm.beginTransaction().replace(R.id.content_frame, loginFragment).commit();

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
        menuLists.add("Login");
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
                boolean isLogin = ((App) getApplication().getApplicationContext()).isLogin();
                if (isLogin) {

                    fm.beginTransaction().replace(R.id.content_frame, microLocationFragment).commit();
                }
                else {
                    fm.beginTransaction().remove(dialogFragment);
                    dialogFragment = DialogFragment.newInstance("Not sign in yet?","Please use the login panel to sign in.");
                    fm.beginTransaction().replace(R.id.content_frame, dialogFragment).commit();
                }
                break;
            }
            case 1: {
                actionBar.setTitle("Geofencing");
                fm.beginTransaction().replace(R.id.content_frame, geofencingFragment).commit();
                break;
            }
            case 2: {
                actionBar.setTitle("Login");
                boolean isLogin = ((App) getApplication().getApplicationContext()).isLogin();
                if (isLogin) {
                    fm.beginTransaction().remove(dialogFragment);
                    dialogFragment = DialogFragment.newInstance("Login Successful!","Welcome, #User Name");
                    fm.beginTransaction().replace(R.id.content_frame, dialogFragment).commit();
                }
                else {

                    fm.beginTransaction().replace(R.id.content_frame, loginFragment).commit();
                }
                break;
            }
            case 3: {
                actionBar.setTitle("Our Website");
                fm.beginTransaction().replace(R.id.content_frame, websiteFragment).commit();
                break;
            }
            //Debug
            case 4: {
                actionBar.setTitle("Debug Panel");
                fm.beginTransaction().replace(R.id.content_frame, debugFragment).commit();
                break;
            }
        }
        mDrawerLayout.closeDrawers();
    }
}
