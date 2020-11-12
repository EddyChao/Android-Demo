package com.example.touchdemo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DisplayCutout;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnTouchListener {

    private TextView tvTouchShowStart;
    private TextView tvTouchShow;
    private LinearLayout llTouch;
    private DisplayMetrics dm;

    //画图
    private Paint mPaint;
    private Paint mPointPaint = new Paint();
    private Canvas mCanvas;//画布
    private Bitmap bitmap;//位图

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dm = new DisplayMetrics();
        init();
    }

    private void init() {
//        tvTouchShowStart = (TextView) findViewById(R.id.touch_show_start);
//        tvTouchShow = (TextView) findViewById(R.id.touch_show);
//        llTouch = (LinearLayout) findViewById(R.id.ll_touch);
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        llTouch.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            /**
             * 点击的开始位置
             */
            case MotionEvent.ACTION_DOWN:
                int width = dm.widthPixels;
                int height = dm.heightPixels;
                Rect notch = Notch();
                int daohang = getDaoHangHeight(this);
                if (notch != null){
                    tvTouchShowStart.setText("当前屏幕的高是：" + (height + notch.bottom + daohang) + "\n"
                            + "当前屏幕的宽是：" + width + "\n"
                            + "状态栏高度：" + notch.bottom + "\n"
                            + "挖孔屏左侧距左侧屏幕边缘：" + notch.left +"\n"
                            + "刘海右侧距左侧屏幕边缘：" + notch.right +"\n"
                            + "挖孔区域大小：" + (notch.right - notch.left) + "\n"
                            );
                    int x = (int) event.getX();
                    int y = (int) event.getY();

                }
                break;
            /**
             * 触屏实时位置
             */
            case MotionEvent.ACTION_MOVE:
                tvTouchShow.setText("实时位置：(" + event.getX() + "," + event.getY() + ")");
                break;
            /**
             * 离开屏幕的位置
             */
            case MotionEvent.ACTION_UP:
                tvTouchShow.setText("结束位置：(" + event.getX() + "," + event.getY() + ")");
                break;
            default:
                break;
        }
        /**
         * 注意返回值
         * true：view继续响应Touch操作；
         * false：view不再响应Touch操作，故此处若为false，只能显示起始位置，不能显示实时位置和结束位置
         */
        return true;
    }
    @TargetApi(28)
    public Rect Notch(){
        final View decorView = getWindow().getDecorView();
        WindowInsets rootWindowInsets = decorView.getRootWindowInsets();
        if (rootWindowInsets == null) {
            Log.e("TAG", "rootWindowInsets为空了");
            return null;
        }
        DisplayCutout displayCutout = rootWindowInsets.getDisplayCutout();
        Log.e("TAG", "安全区域距离屏幕左边的距离 SafeInsetLeft:" + displayCutout.getSafeInsetLeft());
        Log.e("TAG", "安全区域距离屏幕右部的距离 SafeInsetRight:" + displayCutout.getSafeInsetRight());
        Log.e("TAG", "安全区域距离屏幕顶部的距离 SafeInsetTop:" + displayCutout.getSafeInsetTop());
        Log.e("TAG", "安全区域距离屏幕底部的距离 SafeInsetBottom:" + displayCutout.getSafeInsetBottom());

        List<Rect> rects = displayCutout.getBoundingRects();
        if (rects == null || rects.size() == 0) {
            Log.e("TAG", "不是刘海屏");
        } else {
            Log.e("TAG", "刘海屏数量:" + rects.size());
            for (Rect rect : rects) {
                Log.e("TAG", "刘海屏区域：" + rect);
            }
        }
        return rects.get(0);
    }

    /**
     * 获取导航栏高度
     * @param context
     * @return
     */
    public static int getDaoHangHeight(Context context) {
        int resourceId = 0;
        int rid = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        if (rid != 0) {
            resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            return context.getResources().getDimensionPixelSize(resourceId);
        } else
            return 0;
    }
}