package tv.huan.com.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.huan.icanvas.chart.drawable.Friend;

/**
 * Created by tjy on 2017/6/22 0022.
 */

public class Bg extends Friend {
    public Bg(Context context, int width, int height) {
        super(context, width, height);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawRect(getMargin().left, getMargin().top, getWidth()-getMargin().right, getHeight()-getMargin().bottom, new Paint() {
            {this.setColor(Color.RED);}
        });
    }
}
