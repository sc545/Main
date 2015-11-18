package com.test.bubblepang_tabs;

import android.os.Handler;

/**
 * Created by K on 2015-11-13.
 */
public class AnimationThread extends Thread{
    Handler handler; // 핸들러 사용 이유 : 메인스레드를 제외하고는 View 를 건드릴 수 없기 때문에 핸들러를 통해서 화면 갱신 => invalidate()
    GameStageActivity gameStageActivity;
    GameStageActivity.GameView gameView;
    Music music;
    public AnimationThread(GameStageActivity gameStageActivity) {
        handler = new Handler();
        this. gameStageActivity =gameStageActivity;
        gameView = gameStageActivity.gameView;
        music = new Music(gameStageActivity.getApplicationContext(), 0);
    }
    @Override
    public void run() {
        while (gameStageActivity.m_bThreadState) {
            if (gameStageActivity.m_bGameState) {
                if (gameStageActivity.m_bAnimationState[1]) {
                    music.spStart();
                    for (int i = 0; i < 4; i++) {
                        gameStageActivity.m_iAnimationCount[1] = i;
                        try {
                            Thread.sleep(80);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                gameView.invalidate();
                            }
                        });
                    }
                    music.spStop();
                    gameStageActivity.m_bComboDraw = false;
                    gameStageActivity.m_bAnimationState[1] = false;
                }
                if (gameStageActivity.m_bAnimationState[3]) {
                    for (int i = 0; i < 5; i++) {
                        gameStageActivity.m_iAnimationCount[3] = i;
                        try {
                            Thread.sleep(80);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                gameView.invalidate();
                            }
                        });
                    }
                    gameStageActivity.m_bComboDraw = false;
                    gameStageActivity.m_bAnimationState[3] = false;
                }
            }
        }
    }
}