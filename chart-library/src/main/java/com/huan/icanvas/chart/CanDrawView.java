package com.huan.icanvas.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

import com.huan.icanvas.chart.drawable.Friend;
import com.huan.icanvas.chart.drawable.Margin;
import com.huan.icanvas.chart.drawable.Screen;
import com.huan.icanvas.utils.ErrorUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tjy on 2017/3/28 0028.
 * 为Debug专门定制的图形制作工具，插拔式，组合使用方便。
 * addFriend 交个朋友就一起玩耍一起画。
 *
 * @see #addFriend(String, Class)
 * @see #removeFriend(String)
 * @see #getFriend(String)
 * @see #setUiCallback(UiCallback)
 */

public class CanDrawView extends Screen {

    boolean inited;
    private HashMap<String, Mate> friends = new HashMap<String, Mate>();
    private List<String> multilayer = new ArrayList<String>();

    UiCallback uiCallback;
    // 如果绘图需要在内存中完成，请调用该对象的set方法进行变换
    private CanvasHolder canvasHolder;

    // 边距
    private Margin margin;


    public CanDrawView(Context context) {
        super(context);
    }

    public CanDrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCanvasHolder();
    }

    void initDrawRect(Friend superDraw) {
        superDraw.setMargin(margin);
        superDraw.onInited();
    }

    public void create(String name) {
        Mate mate = friends.get(name);
        try {
            Constructor constructor = mate.friendClass.getConstructor(Context.class, int.class, int.class);
            mate.friend = (Friend) constructor.newInstance(CanDrawView.this.getContext(), getWidth(), getHeight());
            initDrawRect(mate.friend);
            mate.friend.setCallUiListener(new Friend.CallUiListener() {
                @Override
                public void callUi() {
                    // 为了减轻重绘负担，这里不做处理，统一由调用invalidate()执行
                }

                @Override
                public void outScreen(Friend who, int orientation) {
                    if (orientation == 0) {
                        // x轴超出了范围
                        who.setxMax(who.getxMax() + who.getOutScreenListener().getX_append());
                    } else {
                        // y轴超出了范围
                        who.setyMax(who.getyMax() + who.getOutScreenListener().getY_append());
                    }
                    who.getOnSetChangeListener().onSetChange(who);
                }
            });
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    void create() {
        if (inited)
            return;
        for (String name : friends.keySet()) {
            create(name);
        }
        if (uiCallback != null) {
            uiCallback.onCallByUIInited(this);
        }
        inited = true;
    }

    public boolean isInited() {
        return inited;
    }

    /**
     * 释放
     */
    public final void release() {
        for (String name : friends.keySet()) {
            Mate mate = friends.get(name);
            mate.friend.onDied();
        }
        friends.clear();
    }

    /**
     * 添加一个小朋友（多一个绘画元素）
     *
     * @param name
     * @param friendClass
     */
    public void addFriend(String name, Class<? extends Friend> friendClass) {
        friends.put(name, new Mate(name, friendClass));
        multilayer.add(name);
    }

    /**
     * 添加小朋友（可现实隐藏添加）
     *
     * @param name
     * @param friendClass
     * @param enable
     */
    public void addFriend(String name, Class<? extends Friend> friendClass, boolean enable) {
        Mate mate = new Mate(name, friendClass);
        mate.enable = enable;
        friends.put(name, mate);
        multilayer.add(name);
    }

    public void removeAllFriends() {
        friends.clear();
    }

    /**
     * 删除小朋友
     *
     * @param name
     */
    public void removeFriend(String name) {
        friends.remove(name);
        multilayer.remove(name);
    }

    /**
     * 生效
     *
     * @param name
     */
    public void enable(String name) {
        if (friends.containsKey(name)) {
            friends.get(name).enable = true;
        }
    }

    /**
     * 生效
     *
     * @param names
     */
    public void enable(List<String> names) {
        for (String name : names) {
            enable(name);
        }
    }

    /**
     * 作废 不显示
     *
     * @param name
     */
    public void unEnable(String name) {
        if (friends.containsKey(name)) {
            friends.get(name).enable = false;
        }
    }

    /**
     * 作废 不显示
     *
     * @param names
     */
    public void unEnable(List<String> names) {
        for (String name : names) {
            unEnable(name);
        }
    }

    public <T extends Friend> T getFriend(String name) {
        if (friends.containsKey(name))
            return (T) friends.get(name).friend;
        return null;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        create();
        invalidate();
    }

    @Override
    public void invalidate() {
        postInvalidate();
    }

    @Override
    public synchronized void postInvalidate() {
        if (myHolder != null) {
            SurfaceHolder holder = myHolder;
            Canvas c = holder.lockCanvas();
            doDraw(canvasHolder.onCanvas(c));
            holder.unlockCanvasAndPost(c);
        }
    }

    /**
     * 更新
     */
    public void setChange() {
        for (String name : multilayer) {
            Mate mate = friends.get(name);
            mate.friend.getOnSetChangeListener().onSetChange(mate.friend);
        }
    }

    @Override
    protected synchronized void doDraw(Canvas canvas) {
        if (inited) {
            if (canvas != null) {
                // 画小朋友
                for (String name : multilayer) {
                    Mate mate = friends.get(name);
                    if (mate.enable) {
                        try {
                            mate.friend.getOnSetChangeListener().onSetChange(mate.friend);
                            mate.friend.onDraw(canvas);
                        } catch (Exception e) {
                            System.err.println(ErrorUtil.e("绘画中发生异常：", e));
                        }
                    }
                }
            }
        }
    }

    class Mate {
        String name;
        Class friendClass;
        Friend friend;
        boolean enable = true;

        public Mate(String name, Class friendClass) {
            this.name = name;
            this.friendClass = friendClass;
        }
    }

    /**
     * 获取显示的集合名称
     *
     * @return
     */
    public List<String> getEnableFriendsName() {
        List<String> list = new ArrayList<String>();
        for (String key : friends.keySet()) {
            Mate mate = friends.get(key);
            if (mate.enable) {
                list.add(mate.name);
            }
        }
        return list;
    }

    /**
     * 文字添加器
     */
    public static abstract class UiCallback {
        public abstract void onCallByUIInited(CanDrawView chart);
    }

    ///////


    public void setUiCallback(UiCallback uiCallback) {
        this.uiCallback = uiCallback;
    }

    /**
     * 设置边距
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setMargin(int left, int top, int right, int bottom) {
        this.margin = new Margin(left, top, right, bottom);
    }

    /**
     * 设置边距
     *
     * @param margin
     */
    public void setMargin(Margin margin) {
        this.margin = margin;
    }

    /**
     * Canvas 提取工具，具体是使用系统提供的canvas还是user自定义的canvas通过重写
     * onCanvas做变换
     */
    public interface CanvasHolder {
        Canvas onCanvas(Canvas canvas);
    }

    /**
     * 设置用户自定canvas
     *
     * @param holder
     */
    public void setCanvasHolder(CanvasHolder holder) {
        this.canvasHolder = holder;
    }

    /**
     * 设置默认canvas
     */
    public void setCanvasHolder() {
        this.canvasHolder = new CanvasHolder() {
            @Override
            public Canvas onCanvas(Canvas canvas) {
                return canvas;
            }
        };
    }
}
