package com.example.k.alltogether.alltogether;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

/**
 * Created by K on 2015-10-15.
 */
public class GameOverDialogMainThread extends Thread {
    GameStageActivity gameStageActivity;
    GameOverDialog gameOverDialog;
    Handler handler;
    public GameOverDialogMainThread(GameStageActivity gameStageActivity, GameOverDialog gameOverDialog){
        this.gameStageActivity = gameStageActivity;
        this.gameOverDialog = gameOverDialog;
        handler = new Handler();
    }

    @Override
    public void run() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(gameStageActivity.getApplicationContext(), MainActivity.class);
                gameStageActivity.startActivity(i);
                gameStageActivity.finish();
                gameOverDialog.dismiss();
            }
        });
    }
}
