package com.maxinghua.fragments;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.karonl.instance.Adapter.DataAdapter;
import com.karonl.instance.InDoorView;
import com.karonl.instance.Unit.PathUnit;
import com.maxinghua.errorhandle.ErrorHandler;
import com.maxinghua.main.DataJson;
import com.maxinghua.main.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;



public class MicroLocationFragment extends Fragment {

    private TextView textview;
    private DataAdapter adapter =  new DataAdapter();;
    private Bitmap bmp;
    List<PathUnit> unitList = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView textView;
    private View view;

    private DebugFragment.OnFragmentInteractionListener mListener;
    private Handler mHandler = new Handler();

    public MicroLocationFragment() {
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
    public static MicroLocationFragment newInstance(String param1, String param2) {
        MicroLocationFragment fragment = new MicroLocationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private NewHandler handler = new NewHandler(this);
    private static class NewHandler extends Handler {
        private WeakReference<MicroLocationFragment> weakReference;
        public NewHandler(MicroLocationFragment main){
            weakReference = new WeakReference<>(main);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(weakReference.get() != null){
                float fr = (float) (msg.arg1);
                if(fr == 1000f){
                    weakReference.get().textview.setText("FPS: stop");
                } else {
                    float fps = (int)(1000f/fr);
                    weakReference.get().textview.setText("FPS: " + fps + "");
                }
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        view = inflater.inflate(R.layout.fragment_debug, container, false)

        return inflater.inflate(R.layout.fragment_micro_loaction, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        InDoorView view = (InDoorView) getActivity().findViewById(R.id.surface);

        //New thread to load the data
       Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                }catch (InterruptedException e){}

                //Background Map
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inPreferredConfig = Bitmap.Config.RGB_565;
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.zxc, opt);//Picture Resources
                adapter.setBmp(bmp);//Set map
                bmp = null;
                getUnitList();
                adapter.setList(unitList);//set information list
                adapter.refreshData();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().findViewById(R.id.tip).setVisibility(View.GONE);
                    }
                });
            }
        });
        ErrorHandler handle = new ErrorHandler();
        thread.setUncaughtExceptionHandler(handle);
        thread.start();

        textview = (TextView) getActivity().findViewById(R.id.frames);

        view.setAdapter(adapter);//init view

        view.setOnClickMapListener(new InDoorView.onClickMapListener() {
            @Override
            public void onClick(PathUnit region) {
                Log.e(this.getClass().getName(),"click");
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("Store Information");
                dialog.setMessage(""+region.getName());
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
            }
        });


        view.onFramesListener(new InDoorView.FramesListener() {
            @Override
            public void onRefresh(float number) {
                handler.obtainMessage(0,(int) number,0).sendToTarget();
            }
        });

        // ================ My position spot =======================
        final ImageButton myPosition = (ImageButton) getActivity().findViewById(R.id.my_postion);
        DisplayMetrics dm=getResources().getDisplayMetrics();
        final int screenWidth=dm.widthPixels;
        final int screenHeight=dm.heightPixels-50;

//        myPosition.setX(200);
//        myPosition.setY(300);

        myPosition.setOnTouchListener(new View.OnTouchListener() {
            int lastX,lastY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int ea = event.getAction();
                Log.i("TAG", "Touch:" + ea);

                switch (ea) {
                    case MotionEvent.ACTION_DOWN:

                        lastX = (int) event.getRawX();//Get raw X and Y for the motion event
                        lastY = (int) event.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;

                        v.setTranslationX(dx);
                        v.setTranslationY(dy);

                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }

                return false;
            }


        });

    }

    //information list
    private void getUnitList(){
        DataJson data = new DataJson();
        for(int i = 0; i < data.size(); i++){
            JSONObject jsonObject = data.getArray(i);
            PathUnit unit = new PathUnit(getList(jsonObject));
            try {
                unit.setName(jsonObject.getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            unitList.add(unit);
        }
    }

    //Coordinate of list
    private List<PointF> getList(JSONObject jsonObject){
        float density = getResources().getDisplayMetrics().density;
        List<PointF> pointList = new ArrayList<>();
        JSONArray array;
        try {
            array = jsonObject.getJSONArray("area");
            for(int r = 0; r < array.length(); r++){
                float x = ((JSONObject)(array.get(r))).getInt("x");
                float y = ((JSONObject)(array.get(r))).getInt("y");
                pointList.add(new PointF( x * density, y * density));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return pointList;
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
