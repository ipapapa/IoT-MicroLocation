package com.maxinghua.fragments;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maxinghua.application.App;
import com.maxinghua.main.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DebugFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DebugFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DebugFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView textView;
    private View view;

    private OnFragmentInteractionListener mListener;
    private Handler mHandler = new Handler();
    private Thread thread;

    public DebugFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DebugFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DebugFragment newInstance(String param1, String param2) {
        DebugFragment fragment = new DebugFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        view = inflater.inflate(R.layout.fragment_debug, container, false)

        return inflater.inflate(R.layout.fragment_debug, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();

        textView = (TextView) getActivity().findViewById(R.id.logView);
        // create a loop updating the debug message
        thread=new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (true) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String s = ((App) getActivity().getApplicationContext()).getLog();
                            textView.append("\n" + s);
                        }
                    });

//                    new AsyncTask<Void, Void, String>() {
//                        @Override
//                        protected String doInBackground(Void... params) {
//                            String s = ((App) getActivity().getApplicationContext()).getLog();
//                            return s;
//                        }
//
//                        @Override
//                        protected void onPostExecute(String result) {
//                            textView.setText(result);
//                            super.onPostExecute(result);
//                        }
//                    }.execute();

                    try{
                        Thread.sleep(500);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
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
        thread.destroy();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
