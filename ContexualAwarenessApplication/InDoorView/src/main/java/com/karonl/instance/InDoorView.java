package com.karonl.instance;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.karonl.instance.Adapter.BitAdapter;
import com.karonl.instance.Interface.BitBuffer;
import com.karonl.instance.Unit.PathUnit;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by karonl on 16/3/21.
 * Sketchpad drawing pattern can be generated directly through the xml objects , draw a blank page when
 * no setting canvas, it can be downloaded into the image and then update
 */
public class InDoorView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {


    private static final int DRAG = 1;// Draw
    private static final int ZOOM = 2;// Zoom
    private int mStatus = 0;//Status
    private int mClick = 0;//Click Status
    private float mStartDistance; //Initial Distance
    private float mPicWidth, mPicHeight;
    private float screenWidth, screenHeight;
    private PointF mStartPoint = new PointF(); //Finger Point
    private float scale, scaleFirst; //Scale
    private float bx, by; //Initial position
    private Canvas c = null;
    private Thread drawThread;//Draw thread
    private final SurfaceHolder surfaceHolder;
    private BitBuffer adapter;
    private boolean canPaint = false;

    public InDoorView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.setOnTouchListener(this);
        getHolder().addCallback(this);
        surfaceHolder = getHolder();
    }

    public void setAdapter(BitBuffer adapter) {
        this.adapter = adapter;
        //Recalibration position and magnification
        if(screenHeight>0 && screenWidth>0) setAdapterInit();

    }

    private void setAdapterInit(){
        adapter.setOnAdapterListener(new BitAdapter.AttrListener() {
            @Override
            public void onRefresh() {
                setScale(true);
                setPicInit();
            }
        });
    }

    private void setPicInit() {
        if (bx != 0 && by != 0) return;
        //Init picture size
        mPicWidth = adapter.getBitBuffer().getWidth() * scale;
        mPicHeight = adapter.getBitBuffer().getHeight() * scale;
        //Init position
        bx = (screenWidth - mPicWidth) / 2;
        by = (screenHeight - mPicHeight) / 2;
    }

    private void setScale(boolean changeFirst){
        float scaleWidth = screenWidth / adapter.getBitBuffer().getWidth();
        float scaleHeight = screenHeight / adapter.getBitBuffer().getHeight();
        scale = scaleWidth > scaleHeight ? scaleHeight : scaleWidth;
        if (changeFirst) scaleFirst = scale;
    }

    /**
     * Setting event listeners frames
     */
    public FramesListener listener;

    public interface FramesListener {
        void onRefresh(float number);
    }

    public void onFramesListener(FramesListener listener) {
        this.listener = listener;
    }

    /**
     * Draw
     */
    private void looperRun() {
        drawThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (_run) {
                    showBit();//Sketch
                }
            }
        });
        drawThread.start();
    }


    /**
     * Draw
     */
    private void showBit() {
        if(canPaint) {
            long startTime = System.currentTimeMillis();
            synchronized (surfaceHolder) {

                c = surfaceHolder.lockCanvas();
                if (c != null && adapter != null && adapter.getBitBuffer() != null) {
                    c.drawColor(Color.GRAY);
                    c.scale(scale, scale);
                    c.drawBitmap(adapter.getBitBuffer(), bx / scale, by / scale, new Paint());
                }
                try {
                    surfaceHolder.unlockCanvasAndPost(c);
                } catch (IllegalStateException e) { //Released
                    Log.e("error:", "" + e);
                }
            }
            long endTime = System.currentTimeMillis();

            /**Calculate the time for one sketch**/
            int diffTime = (int) (endTime - startTime);
            if (diffTime < 16) { //default:18
                try {
                    Thread.sleep(16 - diffTime);
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                }
            }
            sendToInterface(System.currentTimeMillis() - startTime);//Send the time spend for one loop
        } else {
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }
            sendToInterface(1000f);//Send the time spend for one loop
        }
    }

    //Show frame
    private boolean sendAble = true;
    private ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);

    private void sendToInterface(final float diffTime) {
        if (listener != null && sendAble) {
            sendAble = false;
            scheduledThreadPool.schedule(new Runnable() {
                @Override
                public void run() {
                    listener.onRefresh(diffTime);
                    sendAble = true;
                }
            }, 500, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * Gesture for zoom
     */
    private void zoomMap(MotionEvent event) {
        synchronized (InDoorView.class) {
            float newDist = spacing(event);
            float scale1 = newDist / mStartDistance;
            mStartDistance = newDist;
            float tmp = scale * scale1;//zoom in
            if (tmp < scaleFirst * 3 && tmp > scaleFirst * 0.6) {//zoom in scale
                scale = tmp;
            } else {
                return;
            }
            mPicHeight *= scale1;
            mPicWidth *= scale1;
            float fx = (event.getX(1) - event.getX(0)) / 2 + event.getX(0);//middle point position
            float fy = (event.getY(1) - event.getY(0)) / 2 + event.getY(0);
            float XIn = fx - bx;//get the middle point in map
            float YIn = fy - by;
            XIn *= scale1;//move coordinate based on scale
            YIn *= scale1;
            bx = fx - XIn;//the coordinate of top left corner equals the middle point + bia position
            by = fy - YIn;
        }
    }

    /**
     * gesture event
     */
    private void drawMap(MotionEvent event) {
        synchronized (InDoorView.class) {
            PointF currentPoint = new PointF();
            currentPoint.set(event.getX(), event.getY());
            int offsetX = (int) (currentPoint.x - mStartPoint.x);
            int offsetY = (int) (currentPoint.y - mStartPoint.y);
            mStartPoint = currentPoint;
            bx += offsetX;
            by += offsetY;
        }
    }

    /**
     * OnClickMapListener
     */
    public void setOnClickMapListener(onClickMapListener maplistener) {
        this.maplistener = maplistener;
    }

    public interface onClickMapListener {
        void onClick(PathUnit unit);
    }

    private onClickMapListener maplistener;

    private void clickMap(MotionEvent event) {
        if(adapter!=null)
        for (PathUnit region : adapter.getPathUnit()) {
            if (region.region.contains((int) ((event.getX() - bx) / scale), (int) ((event.getY() - by) / scale))) {
                if (maplistener != null) maplistener.onClick(region);
            }
        }
    }

    private int x, y;
    /**
     * OnTouch Listener
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(adapter != null)
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                x = (int) event.getX();
                y = (int) event.getY();
                mClick = 0;
                mStartPoint.set(event.getX(), event.getY());
                mStatus = DRAG;
                canPaint = true;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                float distance = spacing(event); //Init distance
                if (distance > 5f) {
                    mStatus = ZOOM;
                    mStartDistance = distance;
                }
                canPaint = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if( Math.abs(x - event.getX()) < 3 || Math.abs(y - event.getY()) < 3 ){
                    mClick = 0;
                } else {
                    if (mStatus == DRAG) {
                        drawMap(event);
                        mClick = 1;
                    } else {
                        if (event.getPointerCount() == 1) return true;
                        zoomMap(event);
                        mClick = 1;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if(mClick == 0)
                    clickMap(event);
                canPaint = false;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            default:
                break;
        }
        return true;
    }

    // Init surface created
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        screenWidth = this.getWidth();
        screenHeight = this.getHeight();
        if(adapter!=null) setAdapterInit();
        stopThread(false);
        canPaint = true;
        looperRun();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopThread(true);
        drawThread.interrupt();
        drawThread = null;
    }

    //calculating distance
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    //control canvas thread
    private boolean _run = true;

    public void stopThread(boolean run) {
        this._run = !run;
    }
}
