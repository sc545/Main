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
    GameStageActivity gameStageActivity;
    GameStageActivity.GameView gameView;
    ArrayList<Bubble> arrayList;
    Rect rectBubbleArea;
    GameOverDialog gameOverDialog;

    public BubbleThread(GameStageActivity gameStageActivity){
        handler = new Handler();
        this.gameStageActivity = gameStageActivity;
        gameView = gameStageActivity.gameView;
        arrayList = gameStageActivity.m_arraylistBubble;
        rectBubbleArea = gameStageActivity.m_rectBubbleArea;
        gameOverDialog = gameStageActivity.m_dlgGameOverDialog;
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
    void createBubble(){
        try {
            int x = (int) ((Math.random() * rectBubbleArea.width() + rectBubbleArea.left));
            int y = (int) ((Math.random() * rectBubbleArea.height() + rectBubbleArea.top));
            int r = (int) (Math.random()*2);
            Bubble bubble = new Bubble(x, y, gameStageActivity.m_nBubbleRadius, gameStageActivity);
//            switch (r){
//                case 0:
//                    bubble = new Bubble(x, y, gameStageActivity.m_nBubbleRadius*1, gameStageActivity);
//                    break;
//                case 1:
//                    bubble = new Bubble(x, y, gameStageActivity.m_nBubbleRadius*2, gameStageActivity);
//                    break;
//            }
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
                    case 20:case 21:case 22:case 23:case 24:
                        Thread.sleep(150);
                        aniNewBubble();
                        arrayList.add(bubble);
                        break;
                    case 25:case 26:case 27:case 28:case 29:
                        Thread.sleep(140);
                        aniNewBubble();
                        arrayList.add(bubble);
                        break;
                    case 30:case 31:case 32:case 33:case 34:
                        Thread.sleep(130);
                        aniNewBubble();
                        arrayList.add(bubble);
                        break;
                    case 35:case 36:case 37:case 38:case 39:
                        Thread.sleep(120);
                        aniNewBubble();
                        arrayList.add(bubble);
                        break;
                    case 40:case 41:case 42:case 43:case 44:
                        Thread.sleep(110);
                        aniNewBubble();
                        arrayList.add(bubble);
                        break;
                    default:
                        Thread.sleep(100);
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
                if (arrayList.size() == gameStageActivity.m_nBubbleMaxSize) { // 게임 실패시
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
            Bubble bubble = new Bubble(x, y, gameStageActivity.m_nBubbleRadius, gameStageActivity, true);
            gameStageActivity.m_NewBubble = bubble;
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
                    case 20:case 21:case 22:case 23:case 24:
                        Thread.sleep(150);
                        aniNewBombBubble();
                        arrayList.add(bubble);
                        break;
                    case 25:case 26:case 27:case 28:case 29:
                        Thread.sleep(140);
                        aniNewBombBubble();
                        arrayList.add(bubble);
                        break;
                    case 30:case 31:case 32:case 33:case 34:
                        Thread.sleep(130);
                        aniNewBombBubble();
                        arrayList.add(bubble);
                        break;
                    case 35:case 36:case 37:case 38:case 39:
                        Thread.sleep(120);
                        aniNewBombBubble();
                        arrayList.add(bubble);
                        break;
                    case 40:case 41:case 42:case 43:case 44:
                        Thread.sleep(110);
                        aniNewBombBubble();
                        arrayList.add(bubble);
                        break;
                    default:
                        Thread.sleep(100);
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
                if (arrayList.size() == gameStageActivity.m_nBubbleMaxSize) { // 게임 실패시
                    gameStageActivity.m_bGameState = false;
                    gameOverDialog.show();
                }
                gameStageActivity.m_bBombBubbleState = gameStageActivity.m_nCombo > 10 && arrayList.size() > 20;
            }
        });
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

}
