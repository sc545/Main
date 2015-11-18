package com.example.k.alltogether.alltogether;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.k.alltogether.R;

/**
 * Created by K on 2015-10-14.
 */

public class MainActivity extends Activity {
    int m_nScreenWidth, m_nScreenHeight; // 스마트폰 화면 사이즈를 담을 변수
    ImageButton btnHowTo, btnSetting, btnStart, btnRank;
    static Music music=null;
    HowToDialog howToDialog;
    RankDialog rankDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        m_nScreenWidth = getApplicationContext().getResources().getDisplayMetrics().widthPixels; // 스마트폰 화면 사이즈 가져오는 함수
        m_nScreenHeight = getApplicationContext().getResources().getDisplayMetrics().heightPixels;

        if(music == null)
            music = new Music(MainActivity.this);
        if(!music.isPlaying())
            music.start();

        howToDialog = new HowToDialog(this);
        rankDialog = new RankDialog(this);

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

            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), GameStageActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rankDialog.show();
            }
        });
    }

    private void showHotTo(){

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.game_stage1, null);
    }

    @Override
    protected void onDestroy() {
        System.gc();
        music.pause();
        super.onDestroy();
    }
}
