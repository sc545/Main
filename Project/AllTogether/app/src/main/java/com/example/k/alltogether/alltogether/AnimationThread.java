package com.example.k.alltogether.alltogether;

import android.os.Handler;

/**
 * Created by K on 2015-11-13.
 */
public class AnimationThread extends Thread{
    Handler handler; // 핸들러 사용 이유 : 메인스레드를 제외하고는 View 를 건드릴 수 없기 때문에 핸들러를 통해서 화면 갱신 => invalidate()
    GameStageActivity gameStageActivity;
    GameStageActivity.GameView gameView;
    Music musicBubble, musicBombBubble;
    public AnimationThread(GameStageActivity gameStageActivity) {
        handler = new Handler();
        this. gameStageActivity =gameStageActivity;
        gameView = gameStageActivity.gameView;
        musicBubble = new Music(gameStageActivity.getApplicationContext(), Music.MusicType.BUBBLE_SOUND);
        musicBubble.prepare();
        musicBombBubble = new Music(gameStageActivity.getApplicationContext(), Music.MusicType.BOMB_BUBBLE_SOUND);
        musicBombBubble.prepare();
    }
    @Override
    public void run() {
        while (gameStageActivity.m_bThreadState) {
            if (gameStageActivity.m_bGameState) {
                if (gameStageActivity.m_bAnimationState[1]) {
                    musicBubble.spStart();
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
                    musicBubble.spStop();
                    gameStageActivity.m_bComboDraw = false;
                    gameStageActivity.m_bAnimationState[1] = false;
                }
                if (gameStageActivity.m_bAnimationState[3]) {
                    musicBombBubble.spStart();
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
                    musicBombBubble.spStart();
                    gameStageActivity.m_bComboDraw = false;
                    gameStageActivity.m_bAnimationState[3] = false;
                }
            }
        }
    }
}