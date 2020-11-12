package com.example.touchdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class DrawView extends View

{
    private int mWidth;
    private Canvas mcanvas = new Canvas();
    private Paint mpaint = new Paint();
    private Bitmap bitmap;
    private int mHeight;
    public float currentX = 40;

    public float currentY = 50;

    /**

     * @param context

     */

    public DrawView(Context context , AttributeSet set)

    {

        super(context , set);

    }

    @Override

    public void onDraw (Canvas canvas)

    {

        super.onDraw(canvas);

        //创建画笔

        Paint p = new Paint();

        //设置画笔的颜色

        p.setColor(Color.RED);

        //绘制一个小圆（作为小球）

//        canvas.drawCircle(currentX , currentY , 15 , p);
       canvas.drawLine(this.currentX, 0, this.currentX, mHeight, p);
    }

    @Override

    public boolean onTouchEvent(MotionEvent event)

    {
        bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        mpaint.setColor(Color.RED);
        //当前组件的currentX、currentY两个属性

        this.currentX = event.getX();

        this.currentY = event.getY();

        //通知改组件重绘

        this.invalidate();

        //返回true表明处理方法已经处理该事件
        if (event.getAction() == MotionEvent.ACTION_UP){
            mcanvas.setBitmap(bitmap);
            mcanvas.drawLine(event.getX(), 0, event.getX(), mHeight, mpaint);
            Log.d("TAG", event.getX() + "");
        }

        return true;

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
    }

}
