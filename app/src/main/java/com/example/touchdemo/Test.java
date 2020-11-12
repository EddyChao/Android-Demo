package com.example.touchdemo;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Test extends View{
    private static final int MAX_TOUCHPOINTS = 5;
    private static final String START_TEXT = "请随便触摸屏幕进行测试";
    private Paint mPaint;
    private Paint textPaint = new Paint();
    private Paint mPointPaint = new Paint();
    private Canvas mCanvas;//画布
    private Bitmap bitmap;//位图
    private float x = 0;      //点默认X坐标
    private float y = 0;      //点默认Y坐标
    private List<Float> cx = new ArrayList<Float>();
    private List<Float> cy = new ArrayList<Float>();
    private int mWidth, mHeight;
    private int pointerCount;// 屏幕触点数量
    private int touchFlag=0;
    private List<Float> gx = new ArrayList<>();
    private List<Float> gy = new ArrayList<>();
    private int[] pointXs = new int[4];
    private  float[] info = new float[4];
    private int width;
    private int height;
    private int daohang;
    private Rect notch;
    private TextView textView;
    private float temp1 = 0;
    private float temp2 = 0;
    private float right = 0;
    private float left = 0;
    private float top = 0;
    private float bottom = 0;
    private int flag = 1;
    private float t1;
    private float t2;

    public Test(Context context) {
        super(context);
        initialize();
    }
    public Test(Context context, AttributeSet attrs) {
        super(context, attrs);
// TODO Auto-generated constructor stub
    }
//    public interface myView{
//        void answer(float[] info);
//    }
//
//    private myView mv;
//
//    public void setMv(myView mv) {
//        this.mv = mv;
//    }



    @TargetApi(Build.VERSION_CODES.ECLAIR)
    @Override
    public boolean onTouchEvent(MotionEvent event){
        pointerCount = event.getPointerCount();
        if (pointerCount > MAX_TOUCHPOINTS) {
            pointerCount = MAX_TOUCHPOINTS;
        }
        if (event.getAction()==MotionEvent.ACTION_UP) {
            //最后一个起来了，用于取消十字
            if (flag == 1){
                gx.add(event.getRawX());
//                if (!gy.isEmpty()){
//                    for (int i = 0; i < gy.size(); i++) {
//
//
//                            drawLine(0, gy.get(i), mWidth, gy.get(i));
//
//                    }
//                }
                    for (int i = 0; i < gx.size(); i++) {
                        drawLine(gx.get(i), 0, gx.get(i), mHeight);
                    }

                Log.d("gx", gx + "");

                setText(textView, event.getRawX(), 1);
            } else {
                gy.add(event.getRawY());
                for (int i = 0; i < gy.size(); i++) {
                    drawLine(0, gy.get(i), mWidth, gy.get(i));
//                    if (i < gx.size()){
//                        drawLine(gx.get(i), 0, gx.get(i), mHeight);
//
//                    }
                }
                setText(textView, event.getRawY(), 0);
            }

//            if (gx.size() > 1) {
//                info[0] = gx.get(gx.size() - 1);
//                info[1] = gx.get(gx.size() - 2);
//            }
//
//                for (int i = 0; i < info.length; i++) {
//
//                Log.d("Test", String.valueOf(info[i]));
//                }
//            }
        }
        else if (event.getAction()==MotionEvent.ACTION_MOVE){
            for (int i = 0; i < pointerCount; i++) {
                x =  event.getX(i);
                y =  event.getY(i);
                drawLine(cx.get(i),cy.get(i),x,y);
            }
        }else if (event.getAction()==MotionEvent.ACTION_DOWN){
//当第一个手指按下时调用，在新的画布上画
//            mCanvas=new Canvas();
            bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888); //设置位图的宽高
            mCanvas.setBitmap(bitmap);
            touchFlag = 0;
        }
        for (int i = 0; i < pointerCount; i++) {
//记录 触摸点的坐标，并用于画十字
            cx.set(i, event.getX(i));
            cy.set(i, event.getY(i));
//            drawPoint(cx.get(i),cy.get(i));
        }

        return true;
    }
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (touchFlag==0) {
            if (flag == 1){
                for (int i = 0; i < pointerCount;i++) {
//画十字
                    canvas.drawLine(cx.get(i), 0, cx.get(i), mHeight, mPaint);
                }
            } else {
                for (int i = 0; i < pointerCount;i++) {
//画十字
                    canvas.drawLine(0, cy.get(i), mWidth, cy.get(i), mPaint);
                }
            }
//canvas画出来的会消失
//bitmap上面的会保存下来
//所以canvas mCanvas分开画
            canvas.drawBitmap(bitmap,0,0,null);
        }else if(touchFlag == 1){
//取消十字
            canvas.drawBitmap(bitmap,0,0,null);
        }
    }

    private void drawLine(float cx,float cy,float x, float y) {
        mCanvas.drawLine(cx, cy, x, y, mPaint);
        invalidate();
    }

    private void drawPoint(float x,float y) {
        mCanvas.drawPoint(x, y, mPointPaint);
        invalidate();
    }
    /**
     * 在 draw()之前调用
     * 用来初始化得到屏幕尺寸
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
//        info[2] = (float) w;
//        info[3] = (float) h;
        initialize();
    }

    public void initialize(){
        setFocusable(true); // 确保我们的View能获得输入焦点
        setFocusableInTouchMode(true); // 确保能接收到触屏事件
        // TODO Auto-generated constructor stub
        mPaint=new Paint(Paint.DITHER_FLAG);//创建一个画笔
        mCanvas=new Canvas();
        bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888); //设置位图的宽高
        mCanvas.setBitmap(bitmap);
        mPaint.setStrokeWidth(1);//笔宽1像素
        mPaint.setColor(Color.RED);//设置为红笔
        // mPaint.setAntiAlias(true);//锯齿不显示
        textPaint.setColor(Color.BLACK);
        textPaint.setAntiAlias(true);//锯齿不显示
        textPaint.setTextSize(26);
        mPointPaint.setStrokeWidth(4);//笔宽1像素
        mPointPaint.setColor(Color.GREEN);//设置为红笔
        if (mCanvas != null) {
//请随便触摸屏幕进行测试
            mCanvas.drawColor(Color.WHITE);
            float tWidth = textPaint.measureText(START_TEXT);
            mCanvas.drawText(START_TEXT, mWidth / 2 - tWidth / 2, mHeight / 2, textPaint);
        }
        for (int j = 0; j < 5; j++) {
            cx.add(j,(float)0);
            cy.add(j,(float)0);
        }
    }



    public void setInfo(TextView textView, int[] info1, Rect notch){
         width = info1[0];
         height = info1[1];
         daohang = info1[2];
         this.textView = textView;
         this.notch = notch;
    }

    public void setText(TextView textView, float num, int flag) {

        if (flag == 1){
            left = 0;
            right = 0;
             t1 = num;
            if (!gy.isEmpty()){
                for (int i = 0; i < gy.size(); i++) {


                    drawLine(0, gy.get(i), mWidth, gy.get(i));

                }
            }
            if (temp1 == 0){
                temp1 = num;
            } else {
                if (t1 < temp1){
                    left = t1;
                    right = temp1;
                } else {
                    left = temp1;
                    right = t1;
                }
            }


        } else {
            top = 0;
            bottom = 0;
             t2 = num;
            if (!gx.isEmpty()){
                    for (int i = 0; i < gx.size(); i++) {
                        drawLine(gx.get(i), 0, gx.get(i), mHeight);
                    }
                }
            if (temp2 == 0){
                temp2 = num;
            } else {
                if (t2 < temp2){
                    top = t2;
                    bottom = temp2;
                } else {
                    top = temp2;
                    bottom = t2;
                }
            }
//            textView.setText("当前屏幕的高是：" + (height + notch.bottom + daohang) + "\n"
//                    + "当前屏幕的宽是：" + width + "\n"
//                    + "状态栏高度：" + notch.bottom + "\n"
//                    + "挖孔屏上侧距上侧屏幕边缘：" + top +"\n"
//                    + "挖孔屏下侧距上侧屏幕边缘：" + bottom +"\n"
//                    + "挖孔区域大小：" + (Math.abs(bottom - top)) + "\n");
//            temp = t;
//
        }
        if (notch != null) {
            textView.setText("当前屏幕的高是：" + (height + notch.bottom + daohang) + "\n"
                    + "当前屏幕的宽是：" + width + "\n"
                    + "状态栏高度：" + notch.bottom + "\n"
                    + "挖孔屏左侧距左侧屏幕边缘：" + left + "\n"
                    + "挖孔屏右侧距左侧屏幕边缘：" + right + "\n"
                    + "挖孔屏上侧距上侧屏幕边缘：" + top + "\n"
                    + "挖孔屏下侧距上侧屏幕边缘：" + bottom + "\n"
                    + "挖孔区域高：" + (Math.abs(bottom - top)) + "\n"
                    + "挖孔区域宽：" + (Math.abs(right - left)) + "\n");
            temp1 = t1;
            temp2 = t2;
//        right = 0;
//        left = 0;
//        top = 0;
//        bottom = 0;
        } else {
            textView.setText("当前屏幕的高是：" + (height + daohang) + "\n"
                    + "当前屏幕的宽是：" + width + "\n"
                    + "挖孔屏左侧距左侧屏幕边缘：" + left + "\n"
                    + "挖孔屏右侧距左侧屏幕边缘：" + right + "\n"
                    + "挖孔屏上侧距上侧屏幕边缘：" + top + "\n"
                    + "挖孔屏下侧距上侧屏幕边缘：" + bottom + "\n"
                    + "挖孔区域高：" + (Math.abs(bottom - top)) + "\n"
                    + "挖孔区域宽：" + (Math.abs(right - left)) + "\n");
            temp1 = t1;
            temp2 = t2;
        }
    }

    public void clear(int flag){
        this.touchFlag = flag;
        gx.clear();
        gy.clear();
        textView.setText("屏幕信息");
        left = 0;
        right = 0;
        bottom = 0;
        top = 0;
        temp1 = 0;
        temp2 = 0;
    }

    public void setFlag(int flag){
        this.flag = flag;
    }


}

