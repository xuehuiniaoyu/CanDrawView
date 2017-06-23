package com.huan.icanvas.chart.drawable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by tjy on 2017/3/29 0029.
 */

public abstract class MaskView extends SurfaceView implements SurfaceHolder.Callback {

    protected abstract void doDraw(Canvas canvas);

    protected SurfaceHolder myHolder;
    public MaskView(Context context) {
        this(context, null);
    }

    public MaskView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setZOrderOnTop(true);
        this.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.myHolder = holder;
        this.invalidate();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        this.myHolder = null;
    }
}
