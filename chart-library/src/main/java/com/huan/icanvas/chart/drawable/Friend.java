package com.huan.icanvas.chart.drawable;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.CallSuper;

/**
 * Created by tjy on 2017/3/28 0028.
 * 绘画的基类
 */

public abstract class Friend implements Drawing {

    public interface OnInitedListener {
        void onInited(Friend who);
    }

    /**
     * 超出屏幕的监听
     */
    public interface OutScreenListener {
        int getX_append();
        int getY_append();
    }

    /**
     * 当发生改变时调用
     */
    public interface OnSetChangeListener {
        void onSetChange(Friend who);
    }

    public interface CallUiListener {
        void callUi();
        void outScreen(Friend who, int orientation);
    }

    private Context context;

    // 宽度
    private int width;
    // 高度
    private int height;
    // 减掉两边margin的宽度
    private int uiWidth;
    // 减掉两边margin的高度
    private int uiHeight;
    // 垂直最大
    private float yMax;
    // 水平最大
    private float xMax;

    private boolean alive;
    // 间距
    private Margin margin;
    // ui初始化后的回调接口，用来初始化ui
    private CallUiListener callUiListener = new CallUiListener() {
        @Override
        public void callUi() {

        }

        @Override
        public void outScreen(Friend who, int orientation) {

        }
    };

    // 当内容超出屏幕程序会自动按照设定的值追进行缩放显示
    private OutScreenListener outScreenListener = new OutScreenListener() {
        @Override
        public int getX_append() {
            return 0;
        }

        @Override
        public int getY_append() {
            return 0;
        }
    };

    private OnSetChangeListener onSetChangeListener = new OnSetChangeListener() {
        @Override
        public void onSetChange(Friend who) {

        }
    };

    public Friend(Context context, int width, int height) {
        this.context = context;
        this.width = width;
        this.height = height;
    }

    @CallSuper
    public void onInited() {
        alive = true;
    }

    @CallSuper
    public void onDied() {
        alive = false;
    }

    protected void reMeasure() {
        uiWidth = getWidth() - getMargin().left - getMargin().right;
        uiHeight = getHeight() - getMargin().top - getMargin().bottom;
    }

    public void reset() {}

    public Rect getTextRect(String text, Paint paint) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect;
    }

    /**
     * 转换为y坐标的显示位置
     * @param y
     * @return
     */
    public float getUiY(float y) {
        return getHeight()-y/(getyMax()*1.0f/getUiHeight())-getMargin().bottom;
    }

    /**
     * 转换为x坐标的显示位置
     * @param x
     * @return
     */
    public float getUiX(float x) {
        return x/(getxMax()*1.0f/getUiWidth())+getMargin().left;
    }

    public Margin getMargin() {
        return margin;
    }

    public void setMargin(Margin margin) {
        this.margin = margin;
        reMeasure();
    }

    /**
     * 通知ui
     */
    protected void callUi() {
        callUiListener.callUi();
    }

    /**
     * 超出屏幕的通知
     * 0 x轴 1 y轴
     */
    @CallSuper
    protected void outScreen(int orientation) {
        callUiListener.outScreen(this, orientation);
    }


    public Context getContext() {
        return context;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getUiWidth() {
        return uiWidth;
    }

    public int getUiHeight() {
        return uiHeight;
    }

    public float getyMax() {
        return yMax;
    }

    public void setyMax(float yMax) {
        this.yMax = yMax;
    }

    public float getxMax() {
        return xMax;
    }

    public void setxMax(float xMax) {
        this.xMax = xMax;
    }

    public boolean isAlive() {
        return alive;
    }

    /**
     * 注册ui回掉接口
     * @param callUiListener
     */
    public void setCallUiListener(CallUiListener callUiListener) {
        this.callUiListener = callUiListener;
    }

    public void setOutScreenListener(OutScreenListener outScreenListener) {
        this.outScreenListener = outScreenListener;
    }

    public OutScreenListener getOutScreenListener() {
        return outScreenListener;
    }

    public OnSetChangeListener getOnSetChangeListener() {
        return onSetChangeListener;
    }

    public void setOnSetChangeListener(OnSetChangeListener onSetChangeListener) {
        this.onSetChangeListener = onSetChangeListener;
    }
}
