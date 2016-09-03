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
import com.maxinghua.main.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GeofencingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GeofencingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GeofencingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView textView;

    private OnFragmentInteractionListener mListener;

    public GeofencingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GeofencingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GeofencingFragment newInstance(String param1, String param2) {
        GeofencingFragment fragment = new GeofencingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String logMessage = intent.getStringExtra(BackgroundService.getLogMessage());
                        //ViewGroup viewGroup = (ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
                        //LayoutInflater inflater = getLayoutInflater();
                        //TextView textView = (TextView) inflater.inflate(R.layout.detail_log, viewGroup, false);
                        TextView textView = (TextView) getActivity().findViewById(R.id.geofencingLog);
                        if(textView != null) textView.append("\n" + logMessage);
                    }
                }, new IntentFilter(BackgroundService.getActionBroadcast())
        );
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
