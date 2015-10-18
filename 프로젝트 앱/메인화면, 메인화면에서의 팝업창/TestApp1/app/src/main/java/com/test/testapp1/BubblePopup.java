package com.test.testapp1;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.*;

import static com.test.testapp1.R.*;

/**
 * Created by 나두혁 on 2015-10-14.
 */

public class BubblePopup extends View {
    Bitmap m_BackGroundImage;

    public BubblePopup(Context context) {
        super(context);
        m_BackGroundImage = BitmapFactory.decodeResource(getResources(), drawable.how_to_popup);
    }

    protected void onDraw(Canvas canvas){
        final int screenWidth = (int)canvas.getWidth();
        final int screenHeight = (int)canvas.getHeight();

        int popupWidth = (int)(0.9*screenWidth);
        int popupHeight = (int)(0.7*screenHeight);
        int popupWidthMargin = (int)(0.05*screenWidth);
        int popupHeightMargin = (int)(0.15*screenWidth);
        m_BackGroundImage = Bitmap.createScaledBitmap(m_BackGroundImage, popupWidth,
                popupHeight, false);
        canvas.drawBitmap(m_BackGroundImage, popupWidthMargin, popupHeightMargin, null);

        Resources res = getResources();
    }

    /*
    protected void onDraw() {
        Resources lang_res = getResources();
        DisplayMetrics lang_dm = lang_res.getDisplayMetrics();
        int pop_width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 329, lang_dm);
        int pop_height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 453, lang_dm);
        popLinear = (LinearLayout) findViewById(R.id.popLinear);
        pop_Window = new PopupWindow(pop_View, pop_width, pop_height,true);

    }*/
}
