package tv.huan.com.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;

import com.huan.icanvas.chart.drawable.Friend;

/**
 * Created by tjy on 2017/3/28 0028.
 * 虚线，网格
 */

public class DottedLine extends Friend {
    // 垂直网格数量
    private int ySize = 10;
    // 水平网格数量
    private int xSize = 10;
    // 网格画笔
    private Paint mPaint;
    // 水平文字画笔
    private Paint xTextPaint;
    private Paint xTypeTextPaint;
    // 垂直文字画笔
    private Paint yTextPaint;
    // 文字距离
    int xTextMargin, yTextMargin;

    float maxRight;

    private String typeText = "-.-";

    private int driftY;
    private int dirftX;

    // 隐藏x坐标
    private boolean hiddenX;
    private boolean hiddenY;

    public int getTextHeight() {
        return getTextRect(typeText, xTypeTextPaint).height() + yTextMargin;
    }

    public DottedLine(Context context, int width, int height) {
        super(context, width, height);
        mPaint = new Paint();
        mPaint.setStrokeWidth(ResolutionUtil.dip2px(getContext(), 0.5f));
        mPaint.setColor(Color.GRAY);
        mPaint.setStyle(Paint.Style.STROKE);

        float interval = ResolutionUtil.dip2px(getContext(), 4f);
        mPaint.setPathEffect(new DashPathEffect(new float[]{interval, interval}, interval / 2));
        //
        xTextPaint = new Paint();
        xTextPaint.setColor(Color.WHITE);
        xTextPaint.setTextSize(ResolutionUtil.dip2px(getContext(), 14f));
        xTextPaint.setTextAlign(Paint.Align.CENTER);
        xTextPaint.setAntiAlias(true);

        xTypeTextPaint = new Paint(xTextPaint);
        xTypeTextPaint.setTextAlign(Paint.Align.LEFT);

        yTextPaint = new Paint(xTextPaint);
        yTextPaint.setTextAlign(Paint.Align.RIGHT);

        xTextMargin = ResolutionUtil.dip2px(context, 5);
        yTextMargin = ResolutionUtil.dip2px(context, 5);
    }

    /**
     * 画垂直文字
     *
     * @param canvas
     * @param y
     * @param avg
     */
    protected void drawyText(Canvas canvas, float y, float avg) {
        int value = (int) ((getyMax() * 1.0f) / (ySize * 1.0f) * y);
        String text = (getyMax() - value) + "";
        float top = y * avg;
        Rect rect = getTextRect(text, yTextPaint);
        canvas.drawText(text, dirftX + getMargin().left - yTextMargin, top + getMargin().top + rect.height() / 2, yTextPaint);
    }

    /**
     * 画水平文字
     *
     * @param canvas
     * @param x
     * @param avg
     */
    protected void drawxText(Canvas canvas, float x, float avg) {
        int value = (int) ((getxMax() * 1.0f) / (xSize * 1.0f) * x);
        String text = value + "";
        float left = x * avg;
        Rect rect = getTextRect(text, xTextPaint);
        canvas.drawText(text, left + getMargin().left, driftY + getHeight() - getMargin().bottom + rect.height() + xTextMargin, xTextPaint);
        maxRight = left + getMargin().left + rect.width();
    }

    @Override
    public void onDraw(Canvas canvas) {
        Path path = new Path();
        if (!hiddenY) {
            float yAvg = getUiHeight() * 1.0f / ySize;
            int right = getWidth() - getMargin().right;
            int y = 0;
            for (; y <= ySize; y++) {
                float top = yAvg * y + getMargin().top;
//            canvas.drawLine(0, top, width, top, mPaint);
                path.moveTo(getMargin().left, top);
                path.lineTo(right, top);
                drawyText(canvas, y, yAvg);
            }
        }
        if (!hiddenX) {
            float xAvg = getUiWidth() * 1.0f / xSize;
            int bottom = getHeight() - getMargin().bottom;
            for (int x = 0; x <= xSize; x++) {
                float left = xAvg * x + getMargin().left;
//            canvas.drawLine(left, 0, left, height, mPaint);
                path.moveTo(left, getMargin().top);
                path.lineTo(left, bottom);
                drawxText(canvas, x, xAvg);
            }
            String typeText = getTypeText();
            Rect rect = getTextRect(typeText, xTypeTextPaint);
            canvas.drawText(typeText, maxRight + xTextMargin, driftY + bottom + rect.height() + xTextMargin / 2, xTypeTextPaint);
        }
        canvas.drawPath(path, mPaint);
    }

    public int getySize() {
        return ySize;
    }

    public void setySize(int ySize) {
        this.ySize = ySize;
    }

    public int getxSize() {
        return xSize;
    }

    public void setxSize(int xSize) {
        this.xSize = xSize;
    }

    public Paint getDottedLinePaint() {
        return mPaint;
    }

    public Paint getxTextPaint() {
        return xTextPaint;
    }

    public Paint getyTextPaint() {
        return yTextPaint;
    }

    public Paint getxTypeTextPaint() {
        return xTypeTextPaint;
    }

    public String getTypeText() {
        return typeText;
    }

    public void setTypeText(String typeText) {
        this.typeText = typeText;
    }

    public int getDriftY() {
        return driftY;
    }

    public void setDriftY(int driftY) {
        this.driftY = driftY;
    }

    public int getDirftX() {
        return dirftX;
    }

    public void setDirftX(int dirftX) {
        this.dirftX = dirftX;
    }

    public int getxTextMargin() {
        return xTextMargin;
    }

    public int getyTextMargin() {
        return yTextMargin;
    }

    public void setHiddenX(boolean hiddenX) {
        this.hiddenX = hiddenX;
    }

    public void setHiddenY(boolean hiddenY) {
        this.hiddenY = hiddenY;
    }
}
