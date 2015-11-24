package com.example.k.alltogether.alltogether;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.k.alltogether.R;

/**
 * Created by K on 2015-10-14.
 */

public class MainActivity extends Activity {
    int m_nScreenWidth, m_nScreenHeight; // 스마트폰 화면 사이즈를 담을 변수
    ImageButton btnHowTo, btnSetting, btnStart, btnRank;
    static Music music=null;
    Music m_musicButtonSound;
    HowToDialog howToDialog;
    SettingDialog settingDialog;
    RankReadDialog rankReadDialog;
    SelectModeDialog selectModeDialog;
    MainActivity mainActivity;
    boolean m_bMusicBgmState, m_bMusicEffectState;

    ExplainBubbleDialog explainBubbleDialog;

    private BackPressCloseHandler backPressCloseHandler;

    private PopupWindow pWHowTo, pWExplainBub;

    private View how_to_popup, explain_bubble_popup, rank_buuble_popup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        m_nScreenWidth = getApplicationContext().getResources().getDisplayMetrics().widthPixels; // 스마트폰 화면 사이즈 가져오는 함수
        m_nScreenHeight = getApplicationContext().getResources().getDisplayMetrics().heightPixels;

        if(m_nScreenWidth<1100 && m_nScreenHeight<2000)
            setContentView(R.layout.main_activity);
        else
            setContentView(R.layout.bubble_pang_main);

        mainActivity = this;


        Toast.makeText(getApplicationContext(), ""+m_nScreenWidth+", "+m_nScreenHeight, Toast.LENGTH_SHORT).show();
        // 1080 / 1920
        Intent intent = getIntent();
        m_bMusicBgmState = intent.getBooleanExtra("MusicBgmState", true);
        m_bMusicEffectState = intent.getBooleanExtra("MusicEffectState", true);
        if(m_bMusicBgmState) {
            if (music == null) {
                music = new Music(MainActivity.this, Music.MusicType.MAIN_SOUND);
                music.prepare();
            }
            if (!music.isPlaying())
                music.start();
        }
        m_musicButtonSound = new Music(getApplicationContext(), Music.MusicType.BUTTON_SOUND);
        m_musicButtonSound.prepare();

        howToDialog = new HowToDialog(this);
        settingDialog = new SettingDialog(this, mainActivity);
        rankReadDialog = new RankReadDialog(this);
        selectModeDialog = new SelectModeDialog(this, mainActivity);

        explainBubbleDialog = new ExplainBubbleDialog(this);

        btnHowTo = (ImageButton) findViewById(R.id.btnHowTo);
        btnSetting = (ImageButton) findViewById(R.id.btnSetting);
        btnStart = (ImageButton) findViewById(R.id.btnStart);
        btnRank = (ImageButton) findViewById(R.id.btnRank);

        btnHowTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                howToDialog.show();
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingDialog.show();
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectModeDialog.show();
            }
        });

        btnRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rankReadDialog.show();
            }
        });
    }

    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

    private void howToPopup() {
        try {
            //  LayoutInflater 객체화 시킴
            LayoutInflater inflater = (LayoutInflater)MainActivity.this.getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);

            ImageButton pop_Close, pop_Bubble_Close, arrow_left, arrow_right;
            int mPopWidth, mPopHeight;

            mPopWidth = (int)(0.9*m_nScreenWidth);
            mPopHeight = (int)(0.7*m_nScreenHeight);

            how_to_popup = inflater.inflate(R.layout.popup_layout, (ViewGroup) findViewById(R.id.popLinear));
            explain_bubble_popup = inflater.inflate(R.layout.popup_bubble_layout, (ViewGroup) findViewById(R.id.popBubbleLinear));

            pWHowTo = new PopupWindow(how_to_popup, mPopWidth, mPopHeight, true);
            pWExplainBub = new PopupWindow(explain_bubble_popup, mPopWidth, mPopHeight, true);
            pWHowTo.showAtLocation(how_to_popup, Gravity.CENTER, 0, 0);

            pop_Close = (ImageButton) how_to_popup.findViewById(R.id.btnExit);
            pop_Bubble_Close = (ImageButton) explain_bubble_popup.findViewById(R.id.btnExit);

            arrow_right = (ImageButton) how_to_popup.findViewById(R.id.btnRight);
            arrow_left = (ImageButton) explain_bubble_popup.findViewById(R.id.btnLeft);

            pop_Close.setOnClickListener(howto_cancel_button_click_listener);
            pop_Bubble_Close.setOnClickListener(howto_cancel_button_click_listener);

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

    public void musicBgmStart(){
        music.start();
        m_bMusicBgmState = true;
    }

    public void musicBgmStop(){
        music.pause();
        m_bMusicBgmState = false;
    }

    public void musicEffectStart(){
        m_bMusicEffectState = true;
    }

    public void musicEffectStop(){
        m_bMusicEffectState = false;
    }

    @Override
    protected void onDestroy() {
        System.gc();
        music.pause();
        super.onDestroy();
    }
}
