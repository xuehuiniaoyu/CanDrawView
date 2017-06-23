package com.huan.icanvas.chart.drawable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;


/**
 * Created by tjy on 2017/3/29 0029.
 */

public abstract class Screen extends MaskView {
    public Screen(Context context) {
        super(context);
    }

    public Screen(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 获取一张图片快照
     * @return
     */
    public Bitmap getBitmap() {
        return getBitmap(getWidth(), getHeight());
    }

    public Bitmap getBitmap(int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        doDraw(c);
        return bitmap;
    }
}
