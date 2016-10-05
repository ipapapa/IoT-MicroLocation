package com.maxinghua.fragments;

import android.app.Fragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.maxinghua.application.App;
import com.maxinghua.main.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;



import javax.net.ssl.HttpsURLConnection;


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
    private EditText editView;
    private View view;

    private OnFragmentInteractionListener mListener;
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

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String s = (String) msg.obj;
            editView.setText(s);
        }
    };

    String stringValue = "";

    @Override
    public void onStart() {
        super.onStart();

        textView = (TextView) getActivity().findViewById(R.id.textView);
        editView = (EditText) getActivity().findViewById(R.id.editText);

        Button button = (Button) getActivity().findViewById(R.id.test_button);
        button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()){
                        case R.id.test_button:
                            new Thread(new Runnable() {
                                public void run() {
                                    try{
                                        // set up connection
                                        //URL url = new URL("http://10.0.2.2:8080/CASServer/MobileListener");
                                        URL url = new URL("http://52.24.73.201/CASServer/MobileListener");
                                        URLConnection connection = url.openConnection();

                                        String inputString = editView.getText().toString();
                                        inputString = "UUID/" + inputString;
                                        //inputString = URLEncoder.encode(inputString, "UTF-8");
                                        //object for storing Json
                                        JSONObject data = new JSONObject();
                                        data.put("UUID", inputString);

                                        Log.d("inputString", inputString);

                                        connection.setDoOutput(true);
                                        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                                        out.write(inputString);
                                        out.close();

                                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                                        String returnString="";

                                        while ((returnString = in.readLine()) != null)
                                        {
                                            stringValue = returnString;
                                        }
                                        in.close();

                                        getActivity().runOnUiThread(new Runnable() {
                                            public void run() {

                                                textView.setText(stringValue);

                                            }
                                        });

                                    }catch(Exception e)
                                    {
                                        Log.d("Exception",e.toString());
                                    }

                                }
                            }).start();

                            break;
                    }
                }
//                new AsyncTask<String, Void, String>() {
//                    @Override
//                    protected String doInBackground(String... params) {
//                        try {
//
//                            URL url = new URL(params[0]);
//                            URLConnection connection = (URLConnection)url.openConnection();
//                            connection.connect();
//                            connection.setDoOutput(true);
//                            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
//                            out.write("Hello, There!");
//                            out.close();
//                            InputStream is = connection.getInputStream();
//                            InputStreamReader isr = new InputStreamReader(is, "utf-8");
//                            BufferedReader br = new BufferedReader(isr);
//                            String line;
//                            while ((line = br.readLine()) != null) {
//                                System.out.println(line);
//                                Message msg = Message.obtain(); // Creates an new Message instance
//                                msg.obj = line; // Put the string into Message, into "obj" field.
//                                mHandler.sendMessage(msg);
//                            }
//                            String reply = is.toString();
//                            br.close();
//                            isr.close();
//                            is.close();
//                            return reply;
//                        } catch (MalformedURLException e) {
//                            e.printStackTrace();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        return null;
//                    }
//                }.execute("http://152.7.224.4:8080/Contextual2/letsgo?name=mxh&password=123qwe");
//            }
        });
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
