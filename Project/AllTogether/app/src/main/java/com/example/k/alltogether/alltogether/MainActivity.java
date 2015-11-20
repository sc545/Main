package com.example.k.alltogether.alltogether;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mainActivity = this;

        m_nScreenWidth = getApplicationContext().getResources().getDisplayMetrics().widthPixels; // 스마트폰 화면 사이즈 가져오는 함수
        m_nScreenHeight = getApplicationContext().getResources().getDisplayMetrics().heightPixels;

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
