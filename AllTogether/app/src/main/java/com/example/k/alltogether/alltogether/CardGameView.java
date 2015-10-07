package com.example.k.alltogether.alltogether;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
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
    int screenWidth, screenHeight;
    ArrayList<Rect> arrayList;
    public CardGameView(Context context) {
        super(context);

        m_BackGroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.stage_background);
        m_Star = BitmapFactory.decodeResource(getResources(), R.drawable.star);
        m_Bubble = BitmapFactory.decodeResource(getResources(), R.drawable.bubble);

        arrayList = new ArrayList<Rect>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        can = canvas;
        screenWidth = (int) (can.getWidth()*0.5);   //1080
        screenHeight = (int) (can.getHeight()*0.5); //1845
        Bitmap resize_bitmap = Bitmap.createScaledBitmap(m_BackGroundImage, can.getWidth(), can.getHeight(), true);
        can.drawBitmap(resize_bitmap, 0, 0, new Paint());
        if(arrayList.size()==0)
            c=0;
        switch (c){
            case 0:
                for(int i=0; i<10; i++) {
                    //can.drawCircle(500 + x * 90, 650 + y * 130, 35, new Paint());
                    int w = getContext().getResources().getDisplayMetrics().widthPixels;
                    int h = getContext().getResources().getDisplayMetrics().heightPixels;

                    //String tmp = screenWidth+", "+screenHeight+" "+w+", "+h;
                    //Toast.makeText(getContext(), tmp, Toast.LENGTH_SHORT).show();
                    /*
                    int a = (int) (Math.random()*w);
                    int b = (int) (Math.random()*h);
                    rect = new Rect((0 + a)%w, (10 + b)%h, (190 + a)%w, (200 + b)%h);
                    */
                    int a = (int) (Math.random()*screenWidth); //1079
                    int b = (int) (Math.random()*screenHeight);//1844
                    rect = new Rect((0 + a)%screenWidth, (0 + b)%screenHeight, (100 + a)%screenWidth, (100 + b)%screenHeight);
                    String tmp = (0 + a)%screenWidth+", "+(0 + b)%screenHeight+", "+(150 + a)%screenWidth+", "+(150 + b)%screenHeight;
                    Toast.makeText(getContext(), tmp, Toast.LENGTH_SHORT).show();
                    arrayList.add(i, rect);
                    //can.drawRect(rect, new Paint());
                    Paint p = new Paint();
                    p.setColor(Color.RED);
                    can.drawRect(rect, p);
                    can.drawBitmap(m_Bubble, null, rect, p);
                }
                break;
            default:
                for(int i=0; i<arrayList.size(); i++) {
//                    can.drawRect(arrayList.get(i), new Paint());

                    can.drawBitmap(m_Bubble, null, arrayList.get(i), new Paint());
                }
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
