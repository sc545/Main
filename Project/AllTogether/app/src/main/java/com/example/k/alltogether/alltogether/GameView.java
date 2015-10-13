package com.example.k.alltogether.alltogether;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.k.alltogether.R;

import java.util.ArrayList;
import java.util.jar.Attributes;

/**
 * Created by K on 2015-10-06.
 */
public class GameView extends View {
    Bitmap m_BackGroundImage;
    int combo=0;
    boolean comboFlag=false;
    boolean gameState=MainActivity.gameState;
    ArrayList<Bubble> arrayList = MainActivity.arrayList;

    public GameView(Context context) {
        super(context);
        m_BackGroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.stage_background);

        /*
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activity_main = (LinearLayout)inflater.inflate(R.layout.activity_main, null);
        layout = (LinearLayout) activity_main.findViewById(R.id.layout_stage);

        LinearLayout layout_main = (LinearLayout) activity_main.findViewById(R.id.layout_main);
    `   */
        // inflater로 xml로 만든 레이아웃을 가져와서 적용시킬순 없을까?

    }

    public GameView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    class Bubble{
        int x;
        int y;
        int r;
        Bitmap imgBubble;

        public Bubble(int x, int y, int r){
            this.x = x;
            this.y = y;
            this.r = r;

            imgBubble = BitmapFactory.decodeResource(getResources(), R.drawable.bubble);
            imgBubble = Bitmap.createScaledBitmap(imgBubble, r*2, r*2, false);
        }

        public boolean contains(int x, int y){
            return Math.pow(this.x-x, 2)+Math.pow(this.y-y, 2) <= Math.pow(r, 2);
        }

    }

    @Override
    protected void onDraw(final Canvas canvas) {
        int screenWidth = canvas.getWidth();
        int screenHeight = canvas.getHeight();

        m_BackGroundImage = Bitmap.createScaledBitmap(m_BackGroundImage, canvas.getWidth(), canvas.getHeight(), false);
        canvas.drawBitmap(m_BackGroundImage, 0, 0, new Paint());

        if(arrayList.size()==0)
            gameState=false;

/*
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    int a = (int) (Math.random() * screenWidth); //1079
                    int b = (int) (Math.random() * screenHeight);//1844

                    Bubble bubble = new Bubble(a, b, 50);

                    arrayList.add(bubble);

                    gameState = true;
                }catch (Throwable t){}
            }
        });
        thread.start();
        for(int i=0; i<arrayList.size(); i++) {
            Bubble bubble = arrayList.get(i);
            canvas.drawBitmap(bubble.imgBubble, bubble.x - bubble.r, bubble.y-bubble.r, new Paint());
        }
*/

        if(!gameState){
            for(int i=0; i<10; i++) {

                int a = (int) (Math.random()*screenWidth);
                int b = (int) (Math.random()*screenHeight);

                Bubble bubble = new Bubble(a, b, 50);

                arrayList.add(i, bubble);
                canvas.drawBitmap(bubble.imgBubble, bubble.x - bubble.r, bubble.y-bubble.r, new Paint());

                gameState=true;
            }
        }else{
            for(int i=0; i<arrayList.size(); i++) {
                Bubble bubble = arrayList.get(i);
                canvas.drawBitmap(bubble.imgBubble, bubble.x - bubble.r, bubble.y-bubble.r, new Paint());
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int px = (int) event.getX();
        int py = (int) event.getY();

        if(event.getAction() == MotionEvent.ACTION_DOWN){
            for(int i=0; i<arrayList.size(); i++){
                if (arrayList.get(i).contains(px, py)) {
                    arrayList.remove(i);
                    comboFlag=true;
                    invalidate();
                    break;
                }else {
                    comboFlag = false;
                }
            }
            if(comboFlag){
                String tmp = (++combo)+" COMBO!!";
                Toast.makeText(MainActivity.main, tmp, Toast.LENGTH_SHORT).show();
            }else{
                combo=0;
                String tmp = "콤보 실패!!";
                Toast.makeText(MainActivity.main, tmp, Toast.LENGTH_SHORT).show();
            }
        }

        return true;
    }
}
