package com.test.testapp1;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by 나두혁 on 2015-10-08.
 */
public class BubbleView extends View {
    Bitmap m_BackGroundImage;
    Canvas can;
    Rect rect;
    int c=0;
    ArrayList<Rect> arrayList;

    public BubbleView(Context context) {
        super(context);
        m_BackGroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.startmain);
        //m_Bubble = BitmapFactory.decodeResource(getResources(), R.drawable.standard);
        arrayList = new ArrayList<Rect>();
    }

    public void onDraw(Canvas canvas) {
        can = canvas;
        //Paint pnt = new Paint();

        can.drawBitmap(m_BackGroundImage, 0, 0, null);
        Resources res = getResources();

        BitmapDrawable bubble = (BitmapDrawable)res.getDrawable(R.drawable.standard);
        Bitmap m_Bubble = bubble.getBitmap();

        //if (arrayList.size() == 0)
        //    c = 0;
        switch (c%10) {
            case 0:
                for (int i = 0; i < 10; i++) {
                    int a = (int) (Math.random() * 400);
                    int b = (int) (Math.random() * 850);
                    rect = new Rect(50 + 50 + a, 250 + 50 + b, 100 + 100 + a, 300 + 100 + b);
                    //can.drawRect(rect, new Paint());
                    can.drawBitmap(m_Bubble, null, rect, null);
                    arrayList.add(i, rect);
                }
                break;
            default:
                for (int i = 0; i < arrayList.size(); i++) {
                    //can.drawRect(arrayList.get(i), new Paint());
                    rect = arrayList.get(i);
                    can.drawBitmap(m_Bubble, null, rect, null);
                }
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int px = (int) event.getX();
        int py = (int) event.getY();

        for (int i = arrayList.size()-1; i >= 0; i--) {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                if ((px - arrayList.get(i).centerX()) * (px - arrayList.get(i).centerX())
                        + (py - arrayList.get(i).centerY()) * (py - arrayList.get(i).centerY())
                        <= 50 * 50) {     //반지름 만큼 거리 확인
                    arrayList.remove(i);
                    String tmp = (++c)+"콤보!";
                    Toast.makeText(MainActivity.main, tmp, Toast.LENGTH_SHORT).show();
                    invalidate();
                    break;
                }
                else {
                    String tmp = "콤보 실패!";
                    Toast.makeText(MainActivity.main, tmp, Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
        return true;
    }
}