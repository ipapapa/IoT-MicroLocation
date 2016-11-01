package com.maxinghua.fragments;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maxinghua.application.App;
import com.maxinghua.application.BackgroundService;
import com.maxinghua.application.DBHandler;
import com.maxinghua.main.R;


/**
 * Geofencing Fragment read the nearby beacons value ,
 * and store them as a history.
 */
public class GeofencingFragment extends Fragment {
    private TextView textView = null;
    private DBHandler myDBHandler;
    private boolean locker;

    private OnFragmentInteractionListener mListener;

    public GeofencingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDBHandler = new DBHandler(getActivity().getApplicationContext(), null, null, 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_geofencing, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        textView = (TextView) getActivity().findViewById(R.id.geofencingLog);
        textView.setText(myDBHandler.databaseToString());
        locker = true;
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String logMessage = intent.getStringExtra(BackgroundService.getLogMessage());
                        if(locker) {
                            textView = (TextView) getActivity().findViewById(R.id.geofencingLog);
                            textView.setText(myDBHandler.databaseToString());
                        }
                        //if(textView != null) textView.append("\n" + logMessage);
                    }
                }, new IntentFilter(BackgroundService.getActionBroadcast())
        );
//        textView = (TextView) getActivity().findViewById(R.id.geofencingLog);
//        // create a loop updating the debug message
//        Thread thread=new Thread(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                while (true) {
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            String s = ((App) getActivity().getApplicationContext()).getLog();
//                            textView.append("\n" + s);
//                        }
//                    });
//
//                    try{
//                        Thread.sleep(500);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//        thread.start();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        locker = false;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
