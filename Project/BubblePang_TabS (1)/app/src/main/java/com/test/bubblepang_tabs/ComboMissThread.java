package com.test.bubblepang_tabs;

import android.os.Handler;

/**
 * Created by K on 2015-11-14.
 */
public class ComboMissThread extends Thread {
    Handler handler; // 핸들러 사용 이유 : 메인스레드를 제외하고는 View 를 건드릴 수 없기 때문에 핸들러를 통해서 화면 갱신 => invalidate()
    GameStageActivity gameStageActivity;
    public ComboMissThread(GameStageActivity gameStageActivity){
        handler = new Handler();
        this.gameStageActivity = gameStageActivity;
    }
    public void run(){
        gameStageActivity.m_bComboMissState = true;
        try {
            Thread.sleep(100);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                gameStageActivity.gameView.invalidate();
            }
        });
        gameStageActivity.m_bComboMissState = false;
    }
}
