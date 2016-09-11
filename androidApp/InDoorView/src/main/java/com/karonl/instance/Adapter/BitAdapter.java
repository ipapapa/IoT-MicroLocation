package com.karonl.instance.Adapter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.karonl.instance.Interface.BitBuffer;
import com.karonl.instance.Unit.PathUnit;

import java.util.List;

/**
 * Created by karonl on 16/4/1.
 * Transfer bitmap output to view, and clean the bitmap to prevent overflow
 */
public abstract class BitAdapter implements BitBuffer {

    private Bitmap bitmap = null;
    private Canvas bufferCanvas = null;
    private List<PathUnit> pathUnitList = null;
    private AttrListener listener;

    public BitAdapter(){

    }

    public interface AttrListener {
        void onRefresh();
    }

    @Override
    public void setOnAdapterListener(BitAdapter.AttrListener listener) {
        this.listener = listener;
    }

    public void drawBitmap(BitAdapter child){
        Bitmap bg = child.getBgBitmap();
        if(bg != null) {
            //According base map get buffers
            bitmap = Bitmap.createBitmap(bg.getWidth(), bg.getHeight(), Bitmap.Config.RGB_565);//Create bitmap
            //Create a blank drawing canvas
            bufferCanvas = new Canvas(bitmap);
            //After drawing underlay came into the buffer
            bufferCanvas.drawBitmap(bg, new Rect(0, 0, bg.getWidth(), bg.getHeight()), new Rect(0, 0, bg.getWidth(), bg.getHeight()), null);
            //Change the background change the zoom
            if (listener != null) listener.onRefresh();
            //Refresh listener Zoom
        }
    }

    public void drawBuffer(BitAdapter child) {
        //Fill data
        pathUnitList = child.getPathUnit();
        //Draw on canvas
        for (PathUnit path : pathUnitList) {
            bufferCanvas.drawPath(path.path, getPaint());
        }
        bufferCanvas = null;
    }

    public abstract List<PathUnit> getPathUnit();
    public abstract Bitmap getBgBitmap();

    @Override
    public Bitmap getBitBuffer() {
        return bitmap;
    }

    //Get paint
    private Paint paint;//Paint attribute
    private Paint getPaint(){
        if(paint == null) {
            paint = new Paint();
            paint.setColor(Color.BLUE);
            paint.setStyle(Paint.Style.FILL);
            paint.setAlpha(30);
        }
        return paint;
    }
}
