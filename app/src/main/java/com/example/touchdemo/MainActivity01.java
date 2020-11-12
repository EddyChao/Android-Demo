package com.example.touchdemo;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DisplayCutout;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.List;

public class MainActivity01 extends Activity implements View.OnClickListener{

    private TextView tvTouchShowStart;
    private TextView tvTouchShow;
    private LinearLayout llTouch;
    private DisplayMetrics dm;
    private TextView textView;
    private Test myView;
    private Button button1;
    private Button button2;
    private Button button3;
    private int width;
    private int height;
    private int daohang;
    private Rect notch;

    @Override
    protected void onResume() {
        super.onResume();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        Log.d("TAG", width + "_______" + height);
        statusBarHide(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        notch = Notch();
        width = dm.widthPixels;
        height = dm.heightPixels;
        daohang = getDaoHangHeight(this);
        int[] mainInfo = new int[3];
        mainInfo[0] = width;
        mainInfo[1] = height;
        mainInfo[2] = daohang;
        myView.setInfo(textView, mainInfo, notch);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dm = new DisplayMetrics();
        myView = (Test) findViewById(R.id.custom1);
        textView =(TextView) findViewById(R.id.textView);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        init();
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
    }



    private void init() {
//        tvTouchShowStart = (TextView) findViewById(R.id.touch_show_start);
//        tvTouchShow = (TextView) findViewById(R.id.touch_show);
//        llTouch = (LinearLayout) findViewById(R.id.ll_touch);
        TextView viewById = (TextView) findViewById(R.id.textView);
//        Rect notch = Notch();
//        viewById.setText("状态栏高度：" + notch.bottom);
        getWindowManager().getDefaultDisplay().getMetrics(dm);

    }


    @TargetApi(28)
    public Rect Notch(){
        final View decorView = getWindow().getDecorView();
        WindowInsets rootWindowInsets = decorView.getRootWindowInsets();
        List<Rect> rects = null;
        if (rootWindowInsets == null) {
            Log.e("TAG", "rootWindowInsets为空了");
            return null;
        }
        try {
            DisplayCutout displayCutout = rootWindowInsets.getDisplayCutout();
            Log.e("TAG", "安全区域距离屏幕左边的距离 SafeInsetLeft:" + displayCutout.getSafeInsetLeft());
            Log.e("TAG", "安全区域距离屏幕右部的距离 SafeInsetRight:" + displayCutout.getSafeInsetRight());
            Log.e("TAG", "安全区域距离屏幕顶部的距离 SafeInsetTop:" + displayCutout.getSafeInsetTop());
            Log.e("TAG", "安全区域距离屏幕底部的距离 SafeInsetBottom:" + displayCutout.getSafeInsetBottom());

            rects = displayCutout.getBoundingRects();
            if (rects == null || rects.size() == 0) {
                Log.e("TAG", "不是刘海屏");
            } else {
                Log.e("TAG", "刘海屏数量:" + rects.size());
                for (Rect rect : rects) {
                    Log.e("TAG", "刘海屏区域：" + rect);
                }
            }
            return rects.get(0);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
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

    /**
     * 设置Activity的statusBar隐藏
     * @param activity
     */
    public static void statusBarHide(Activity activity){
        // 代表 5.0 及以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = activity.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            ActionBar actionBar = activity.getActionBar();
            actionBar.hide();
            return;
        }

        // versionCode > 4.4  and versionCode < 5.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

    }

    @Override
    public void onClick(View view) {
//        myView.onClick(myView);
//        myView.clear();
        switch (view.getId()){
            case R.id.button1:
                myView.clear(1);
                myView.initialize();
                myView.invalidate();
                break;
            case R.id.button2:
//                myView.clear(1);
                myView.setFlag(0);
                break;
            case R.id.button3:
//                myView.clear(1);
                myView.setFlag(1);
                break;
        }

    }




//    @Override
//    public boolean onTouch(View view, MotionEvent motionEvent) {
//        if (motionEvent.getAction() == MotionEvent.ACTION_UP){
//            float[] view1 = myView.getView();
//            Log.d("man", String.valueOf(view1[0]) + "");
//            Log.d("man", String.valueOf(view1[1]) + "");
//        }
//        return false;
//    }
}