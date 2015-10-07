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

/**
 * Created by K on 2015-10-06.
 */
public class CardGameView extends View {
    Bitmap m_BackGroundImage, m_Star;
    Canvas can;

    public CardGameView(Context context) {
        super(context);
        m_BackGroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        m_Star = BitmapFactory.decodeResource(getResources(), R.drawable.star);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        can = canvas;
        can.drawBitmap(m_BackGroundImage, 0, 0, null);
        for(int y=0; y<2; y++)
            for(int x=0; x<3; x++)
                can.drawCircle(500 + x * 90, 650 + y * 130, 35, new Paint());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int px = (int) event.getX();
        int py = (int) event.getY();
        for(int y=0; y<2; y++){
            for(int x=0; x<3; x++){
                Rect r = new Rect(500+x*90, 650+y*130, 500+x*90+80, 650+y*130+115);
                if(r.contains(px, py))
                    Toast.makeText(MainActivity.main, "성공", Toast.LENGTH_LONG).show();
            }
        }
        invalidate();
        return true;
    }
}
