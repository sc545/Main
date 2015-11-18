package com.test.bubblepang_tabs;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class BubblePangMain extends Activity {

    LinearLayout startMain;
    //LinearLayout gameMain;
    LinearLayout popup_Layout;

    private PopupWindow pWHowTo, pWExplainBub, pWRankBub;
    private int mWidthPixels, mHeightPixels;

    private View how_to_popup, explain_bubble_popup, rank_buuble_popup;
    static Music music=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bubble_pang_main);

        if(music == null)
            music = new Music(BubblePangMain.this);
        if(!music.isPlaying())
            music.start();

        ImageButton btnStart, btnHowTo, btnSetting, btnRank;

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

        btnHowTo = (ImageButton) findViewById(R.id.btnHowTo);
        btnRank = (ImageButton) findViewById(R.id.btnRank);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), GameStageActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnHowTo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                howToPopup();
            }
        });

        btnRank.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                rankPopup();
            }
        });

    }

    private void rankPopup() {
        try {
            //  LayoutInflater 객체화 시킴
            LayoutInflater inflater = (LayoutInflater) BubblePangMain.this.getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);

            ImageButton pop_Rank_Close;
            int mPopWidth, mPopHeight;

            mPopWidth = (int) (0.9 * mWidthPixels);
            mPopHeight = (int) (0.7 * mHeightPixels);

            rank_buuble_popup = inflater.inflate(R.layout.rank_record, (ViewGroup) findViewById(R.id.popBubbleRank));

            pWRankBub = new PopupWindow(rank_buuble_popup, mPopWidth, mPopHeight, true);
            pWRankBub.showAtLocation(rank_buuble_popup, Gravity.CENTER, 0, 0);

            pop_Rank_Close = (ImageButton) rank_buuble_popup.findViewById(R.id.pop_Rank_Close);

            pop_Rank_Close.setOnClickListener(rank_cancel_button_click_listener);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private View.OnClickListener rank_cancel_button_click_listener = new View.OnClickListener() {
        public void onClick(View v) {
            pWRankBub.dismiss();
        }
    };

    private void howToPopup() {
        try {
            //  LayoutInflater 객체화 시킴
            LayoutInflater inflater = (LayoutInflater)BubblePangMain.this.getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);

            ImageButton pop_Close, pop_Bubble_Close, pop_Rank_Close, arrow_left, arrow_right;
            int mPopWidth, mPopHeight;

            mPopWidth = (int)(0.9*mWidthPixels);
            mPopHeight = (int)(0.7*mHeightPixels);

            how_to_popup = inflater.inflate(R.layout.popup_layout, (ViewGroup) findViewById(R.id.popLinear));
            explain_bubble_popup = inflater.inflate(R.layout.popup_bubble_layout, (ViewGroup) findViewById(R.id.popBubbleLinear));
            rank_buuble_popup = inflater.inflate(R.layout.rank_record, (ViewGroup) findViewById(R.id.popBubbleRank));

            pWHowTo = new PopupWindow(how_to_popup, mPopWidth, mPopHeight, true);
            pWExplainBub = new PopupWindow(explain_bubble_popup, mPopWidth, mPopHeight, true);
            pWHowTo.showAtLocation(how_to_popup, Gravity.CENTER, 0, 0);

            pop_Close = (ImageButton) how_to_popup.findViewById(R.id.pop_Close);
            pop_Bubble_Close = (ImageButton) explain_bubble_popup.findViewById(R.id.pop_Bubble_Close);
            pop_Rank_Close = (ImageButton) rank_buuble_popup.findViewById(R.id.pop_Rank_Close);

            arrow_right = (ImageButton) how_to_popup.findViewById(R.id.arrow_right);
            arrow_left = (ImageButton) explain_bubble_popup.findViewById(R.id.arrow_left);

            pop_Close.setOnClickListener(howto_cancel_button_click_listener);
            pop_Bubble_Close.setOnClickListener(howto_cancel_button_click_listener);
            pop_Rank_Close.setOnClickListener(howto_cancel_button_click_listener);

            arrow_right.setOnClickListener(arrow_right_click_listener);
            arrow_left.setOnClickListener(arrow_left_click_listener);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private View.OnClickListener howto_cancel_button_click_listener = new View.OnClickListener() {
        public void onClick(View v) {
            pWHowTo.dismiss();
            pWExplainBub.dismiss();
        }
    };

    private View.OnClickListener arrow_left_click_listener = new View.OnClickListener() {
        public void onClick(View v) {
            pWHowTo.showAtLocation(how_to_popup, Gravity.CENTER, 0, 0);
            pWExplainBub.dismiss();
        }
    };

    private View.OnClickListener arrow_right_click_listener = new View.OnClickListener() {
        public void onClick(View v) {
            pWExplainBub.showAtLocation(explain_bubble_popup, Gravity.CENTER, 0, 0);
            pWHowTo.dismiss();
        }
    };

    @Override
    protected void onDestroy() {
        System.gc();
        music.pause();
        super.onDestroy();
    }
}
