package com.example.k.alltogether.alltogether;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.k.alltogether.R;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

/**
 * Created by K on 2015-10-06.
 */
public class CardGameView extends View {
    Bitmap m_BackGroundImage, m_Star, m_Bubble;
    Canvas can;
    Rect rect;
    int c=0;
    ArrayList<Rect> arrayList;
    public CardGameView(Context context) {
        super(context);
        m_BackGroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        m_Star = BitmapFactory.decodeResource(getResources(), R.drawable.star);
        m_Bubble = BitmapFactory.decodeResource(getResources(), R.drawable.bubble);
        arrayList = new ArrayList<Rect>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        can = canvas;
        can.drawBitmap(m_BackGroundImage, 0, 0, null);
        if(arrayList.size()==0)
            c=0;
        switch (c){
            case 0:
                for(int i=0; i<10; i++) {
                    //can.drawCircle(500 + x * 90, 650 + y * 130, 35, new Paint());
                    int a = (int) (Math.random()*600);
                    int b = (int) (Math.random()*800);
                    rect = new Rect(10 + 1 * a, 20 + 2 * b, 100 + 1 * a, 110 + 2 * b);
                    arrayList.add(i, rect);
                    //can.drawRect(rect, new Paint());
                    can.drawBitmap(m_Bubble, null, rect, new Paint());
                }
                break;
            default:
                for(int i=0; i<arrayList.size(); i++)
//                    can.drawRect(arrayList.get(i), new Paint());
                    can.drawBitmap(m_Bubble, null, arrayList.get(i), new Paint());
                break;
        }


        /*
        for(int y=0; y<2; y++)
            for(int x=0; x<3; x++) {
                //can.drawCircle(500 + x * 90, 650 + y * 130, 35, new Paint());
                rect[y][x] = new Rect(10 + x * 150, 20 + y * 150, 30 + x * 150, 40 + y * 150);
                can.drawRect(rect[y][x], new Paint());
            }
            */
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int px = (int) event.getX();
        int py = (int) event.getY();


        for(int i=0; i<arrayList.size(); i++){
            if(arrayList.get(i).contains(px, py)) {
                arrayList.remove(i);
                String tmp="성공"+(++c);
                Toast.makeText(MainActivity.main, tmp, Toast.LENGTH_SHORT).show();
                invalidate();
            }
        }
        return true;
    }
}
