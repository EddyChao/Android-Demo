package com.example.touchdemo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.DisplayCutout;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;

import java.util.List;

public class NotchActivity extends Activity {
    private WindowManager wm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        //开局就一张背景图
//        setContentView(R.layout.notch);

        getNotchParams();
    }

    @TargetApi(28)
    public void getNotchParams() {
        final View decorView = getWindow().getDecorView();

        decorView.post(new Runnable() {
            @Override
            public void run() {
                WindowInsets rootWindowInsets = decorView.getRootWindowInsets();
                if (rootWindowInsets == null) {
                    Log.e("TAG", "rootWindowInsets为空了");
                    return;
                }
                int width = wm.getDefaultDisplay().getWidth();
                int height = wm.getDefaultDisplay().getHeight();
                DisplayCutout displayCutout = rootWindowInsets.getDisplayCutout();
                Log.e("TAG", "安全区域距离屏幕左边的距离 SafeInsetLeft:" + displayCutout.getSafeInsetLeft());
                Log.e("TAG", "安全区域距离屏幕右部的距离 SafeInsetRight:" + displayCutout.getSafeInsetRight());
                Log.e("TAG", "安全区域距离屏幕顶部的距离 SafeInsetTop:" + displayCutout.getSafeInsetTop());
                Log.e("TAG", "安全区域距离屏幕底部的距离 SafeInsetBottom:" + displayCutout.getSafeInsetBottom());
                Log.e("TAG", "屏幕大小：" + height + "  x  " + width);

                List<Rect> rects = displayCutout.getBoundingRects();
                if (rects == null || rects.size() == 0) {
                    Log.e("TAG", "不是刘海屏");
                } else {
                    Log.e("TAG", "刘海屏数量:" + rects.size());
                    for (Rect rect : rects) {
                        Log.e("TAG", "刘海屏区域：" + rect);
                        Log.e("TAG", "刘海屏区域：" + rect.left);
                        Log.e("TAG", "刘海屏区域：" + rect.right);
                    }
                }
            }
        });
    }

}