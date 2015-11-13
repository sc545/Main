package com.example.k.alltogether.alltogether;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Handler;

import java.util.ArrayList;

/**
 * Created by K on 2015-11-13.
 */
public class BubbleThread extends Thread {
    Handler handler; // 핸들러 사용 이유 : 메인스레드를 제외하고는 View 를 건드릴 수 없기 때문에 핸들러를 통해서 화면 갱신 => invalidate()
    Rect rectBubbleArea;
    Bitmap m_ImgBubble, m_ImgFeverBubble, m_ImgBombBubble;
    ArrayList<Bubble> arrayList;
    GameOverDialog gameOverDialog;
    GameStageActivity.GameView gameView;
    GameStageActivity gameStageActivity;

    public BubbleThread(GameStageActivity gameStageActivity){
        handler = new Handler();
        this.gameStageActivity = gameStageActivity;
        rectBubbleArea = gameStageActivity.m_rectBubbleArea;
        m_ImgBubble = gameStageActivity.m_btmpImgBubble;
        m_ImgFeverBubble = gameStageActivity.m_btmpImgFeverBubble;
        m_ImgBombBubble = gameStageActivity.m_btmpImgBombBubble;
        arrayList = gameStageActivity.m_arraylistBubble;
        gameOverDialog = gameStageActivity.m_dlgGameOverDialog;
        gameView = gameStageActivity.gameView;
    }

    public void run(){
        while(gameStageActivity.m_bThreadState) {
            if(gameStageActivity.m_bGameState) {
                if(!gameStageActivity.m_bBombBubbleState) {
                    createBubble();
                }else{
                    int random = (int) (Math.random()*7);
                    if(random == 6) {
                        createBombBubble();
                    }else{
                        createBubble();
                    }
                }
            }
        }
    }
    void aniNewBubble(){
        gameStageActivity.m_bAnimationState[0] = true;
        for (int i = 0; i < 4; i++) {
            gameStageActivity.m_iAnimationCount[0] = i;
            try {
                switch (gameStageActivity.m_nCombo) {
                    case 0:case 1:case 2:case 3:case 4:
                        Thread.sleep(80); // 스레드가 0.5초 동안 잠든다 => 0.5초 간격으로 버블 생성
                        break;
                    case 5:case 6:case 7:case 8:case 9:
                        Thread.sleep(60);
                        break;
                    case 10:case 11:case 12:case 13:case 14:
                        Thread.sleep(40);
                        break;
                    case 15:case 16:case 17:case 18:case 19:
                        Thread.sleep(20);
                        break;
                    default:
                        Thread.sleep(15);
                }
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    gameView.invalidate();
                }
            });

        }
        gameStageActivity.m_bAnimationState[0] = false;
    }
    void aniNewBombBubble(){
        gameStageActivity.m_bAnimationState[2] = true;
        for (int i = 0; i < 4; i++) {
            gameStageActivity.m_iAnimationCount[2] = i;
            try {
                switch (gameStageActivity.m_nCombo) {
                    case 0:case 1:case 2:case 3:case 4:
                        Thread.sleep(80); // 스레드가 0.5초 동안 잠든다 => 0.5초 간격으로 버블 생성
                        break;
                    case 5:case 6:case 7:case 8:case 9:
                        Thread.sleep(60);
                        break;
                    case 10:case 11:case 12:case 13:case 14:
                        Thread.sleep(40);
                        break;
                    case 15:case 16:case 17:case 18:case 19:
                        Thread.sleep(20);
                        break;
                    default:
                        Thread.sleep(15);
                }
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    gameView.invalidate();
                }
            });

        }
        gameStageActivity.m_bAnimationState[2] = false;
    }
    void createBubble(){
        try {
            int x = (int) ((Math.random() * rectBubbleArea.width() + rectBubbleArea.left));
            int y = (int) ((Math.random() * rectBubbleArea.height() + rectBubbleArea.top));
            Bubble bubble = new Bubble(x, y, gameStageActivity.m_nBubbleRadius, m_ImgBubble, m_ImgFeverBubble);
            gameStageActivity.m_NewBubble = bubble;
            if (rectBubbleArea.left < (bubble.x - bubble.bubbleR) && rectBubbleArea.top < (bubble.y - bubble.bubbleR) && rectBubbleArea.right > (bubble.x + bubble.bubbleR) && rectBubbleArea.bottom > (bubble.y + bubble.bubbleR)) {
                switch (gameStageActivity.m_nCombo) {
                    case 0:case 1:case 2:case 3:case 4:
                        Thread.sleep(500); // 스레드가 0.5초 동안 잠든다 => 0.5초 간격으로 버블 생성
                        aniNewBubble();
                        arrayList.add(bubble);
                        break;
                    case 5:case 6:case 7:case 8:case 9:
                        Thread.sleep(400);
                        aniNewBubble();
                        arrayList.add(bubble);
                        break;
                    case 10:case 11:case 12:case 13:case 14:
                        Thread.sleep(300);
                        aniNewBubble();
                        arrayList.add(bubble);
                        break;
                    case 15:case 16:case 17:case 18:case 19:
                        Thread.sleep(200);
                        aniNewBubble();
                        arrayList.add(bubble);
                        break;
                    default:
                        Thread.sleep(150);
                        aniNewBubble();
                        arrayList.add(bubble);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                gameView.invalidate(); // 핸들러를 통해서 화면 갱신
                if (arrayList.size() == 50) { // 게임 실패시
                    gameStageActivity.resetState();
                    gameStageActivity.m_bGameState = false;
                    gameOverDialog.show();
                }

                gameStageActivity.m_bBombBubbleState = gameStageActivity.m_nCombo > 10 && arrayList.size() > 20;

            }
        });
    }

    void createBombBubble(){
        try{
            int x = (int) ((Math.random() * rectBubbleArea.width() + rectBubbleArea.left));
            int y = (int) ((Math.random() * rectBubbleArea.height() + rectBubbleArea.top));
            Bubble bubble = new Bubble(x, y, gameStageActivity.m_nBubbleRadius, m_ImgBombBubble);
            gameStageActivity.m_NewBombBubble = bubble;
            if (rectBubbleArea.left < (bubble.x - bubble.bubbleR) && rectBubbleArea.top < (bubble.y - bubble.bubbleR) && rectBubbleArea.right > (bubble.x + bubble.bubbleR) && rectBubbleArea.bottom > (bubble.y + bubble.bubbleR)) {
                switch (gameStageActivity.m_nCombo) {
                    case 0:case 1:case 2:case 3:case 4:
                        Thread.sleep(500); // 스레드가 0.5초 동안 잠든다 => 0.5초 간격으로 버블 생성
                        aniNewBombBubble();
                        arrayList.add(bubble);
                        break;
                    case 5:case 6:case 7:case 8:case 9:
                        Thread.sleep(400);
                        aniNewBombBubble();
                        arrayList.add(bubble);
                        break;
                    case 10:case 11:case 12:case 13:case 14:
                        Thread.sleep(300);
                        aniNewBombBubble();
                        arrayList.add(bubble);
                        break;
                    case 15:case 16:case 17:case 18:case 19:
                        Thread.sleep(200);
                        aniNewBombBubble();
                        arrayList.add(bubble);
                        break;
                    default:
                        Thread.sleep(150);
                        aniNewBombBubble();
                        arrayList.add(bubble);
                }
            }
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                gameView.invalidate(); // 핸들러를 통해서 화면 갱신
                if (arrayList.size() == 50) { // 게임 실패시
                    gameStageActivity.resetState();
                    gameStageActivity.m_bGameState = false;
                    gameOverDialog.show();
                }
                gameStageActivity.m_bBombBubbleState = gameStageActivity.m_nCombo > 10 && arrayList.size() > 20;
            }
        });
    }
}
