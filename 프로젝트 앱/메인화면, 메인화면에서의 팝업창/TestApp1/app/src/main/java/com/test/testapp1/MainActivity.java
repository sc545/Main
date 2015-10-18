package com.test.testapp1;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class MainActivity extends Activity {

    LinearLayout startMain;
    //LinearLayout gameMain;
    LinearLayout popup_Layout;

    private ImageButton btnStart, btnHowTo, btnSetting;
    private ImageButton pop_Close, pop_Bubble_Close, arrow_left, arrow_right;
    
    private PopupWindow pWHowTo, pWExplainBub;
    private int mWidthPixels, mHeightPixels;
    private int mPopWidth, mPopHeight;

    private View how_to_popup, explain_bubble_popup;

    static Activity main;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(new BubbleView(this));
        main = this;
        setContentView(R.layout.activity_main);

        startMain = (LinearLayout)findViewById(R.id.startMain);
        startMain.setBackgroundResource(R.drawable.startmain);
        //main = (LinearLayout)findViewById(R.id.gameMain);
        //gameMain.setBackgroundResource (R.drawable.main);
        popup_Layout = (LinearLayout)findViewById(R.id.popLinear);

        btnStart = (ImageButton)findViewById (R.id.btnStart);
        btnSetting = (ImageButton)findViewById (R.id.btnSetting);

        WindowManager w = getWindowManager();
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
        // since SDK_INT = 1;
        mWidthPixels = metrics.widthPixels;
        mHeightPixels = metrics.heightPixels;

        // 상태바와 메뉴바의 크기를 포함해서 재계산
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
        try {
            mWidthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
            mHeightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
            } catch (Exception ignored) {
        }
        // 상태바와 메뉴바의 크기를 포함
        if (Build.VERSION.SDK_INT >= 17)
        try {
            Point realSize = new Point();
            Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
            mWidthPixels = realSize.x;
            mHeightPixels = realSize.y;
            } catch (Exception ignored) {
        }

        btnHowTo = (ImageButton)findViewById (R.id.btnHowTo);
        btnHowTo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bubblePopup();
//                Intent intent = new Intent(getApplicationContext(),BubblePopup2.class);
//                startActivity(intent);
            }
        });
    }

    private void bubblePopup() {
        try {
            //  LayoutInflater 객체와 시킴
            LayoutInflater inflater = (LayoutInflater)MainActivity.this.getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);

            mPopWidth = (int)(0.9*mWidthPixels);
            mPopHeight = (int)(0.7*mHeightPixels);

            how_to_popup = inflater.inflate(R.layout.popup_layout, (ViewGroup) findViewById(R.id.popLinear));
            explain_bubble_popup = inflater.inflate(R.layout.popup_bubble_layout, (ViewGroup) findViewById(R.id.popBubbleLinear));

            pWHowTo = new PopupWindow(how_to_popup, mPopWidth, mPopHeight, true);
            pWExplainBub = new PopupWindow(explain_bubble_popup, mPopWidth, mPopHeight, true);
            pWHowTo.showAtLocation(how_to_popup, Gravity.CENTER, 0, 0);

            pop_Close = (ImageButton) how_to_popup.findViewById(R.id.pop_Close);
            pop_Bubble_Close = (ImageButton) explain_bubble_popup.findViewById(R.id.pop_Bubble_Close);

            arrow_right = (ImageButton) how_to_popup.findViewById(R.id.arrow_right);
            arrow_left = (ImageButton) explain_bubble_popup.findViewById(R.id.arrow_left);

            pop_Close.setOnClickListener(cancel_button_click_listener);
            pop_Bubble_Close.setOnClickListener(cancel_button_click_listener);
            arrow_right.setOnClickListener(arrow_right_click_listener);
            arrow_left.setOnClickListener(arrow_left_click_listener);

            } catch (Exception e) {
                e.printStackTrace();
        }
    }

    private OnClickListener cancel_button_click_listener = new OnClickListener() {
        public void onClick(View v) {
            pWHowTo.dismiss();
            pWExplainBub.dismiss();
        }
    };

    private OnClickListener arrow_left_click_listener = new OnClickListener() {
        public void onClick(View v) {
            pWHowTo.showAtLocation(how_to_popup, Gravity.CENTER, 0, 0);
            pWExplainBub.dismiss();
        }
    };

    private OnClickListener arrow_right_click_listener = new OnClickListener() {
        public void onClick(View v) {
            pWExplainBub.showAtLocation(explain_bubble_popup, Gravity.CENTER, 0, 0);
            pWHowTo.dismiss();
        }
    };
}