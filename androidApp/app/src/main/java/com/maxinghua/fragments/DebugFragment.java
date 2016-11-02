package com.maxinghua.fragments;

import android.app.Fragment;
import android.net.Uri;
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

import com.maxinghua.main.R;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;


/**
 * Debug Fragment that helps testing partial functions and debug
 */
public class DebugFragment extends Fragment{

    private TextView textView;
    private EditText editView;

    public DebugFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
