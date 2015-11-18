package com.example.k.alltogether.alltogether;

import android.os.Handler;

/**
 * Created by K on 2015-11-13.
 */
public class FeverThread extends Thread {
    Handler handler; // 핸들러 사용 이유 : 메인스레드를 제외하고는 View 를 건드릴 수 없기 때문에 핸들러를 통해서 화면 갱신 => invalidate()
    GameStageActivity gameStageActivity;
    Music musicFever;
    public FeverThread(GameStageActivity gameStageActivity){
        handler = new Handler();
        this.gameStageActivity = gameStageActivity;
        musicFever = gameStageActivity.m_musicFever;
    }
    public void run(){
        gameStageActivity.m_bFeverState = true;
        musicFever.start();
        try {
            Thread.sleep(10000);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        musicFever.pause();
        gameStageActivity.m_nFeverGauge = 0;
        gameStageActivity.m_bFeverState = false;
    }
}
