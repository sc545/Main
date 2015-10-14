package com.example.k.alltogether.alltogether;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.k.alltogether.R;

/**
 * Created by K on 2015-10-15.
 */
public class GameOverDialog extends Dialog {
    GameStageActivity gameStageActivity;
    GameOverDialog gameOverDialog;
    ImageButton btnReplay, btnMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameOverDialog = this;
        setContentView(R.layout.game_over_dialog);

        btnReplay = (ImageButton) findViewById(R.id.btnRePlay);
        btnMain = (ImageButton) findViewById(R.id.btnMain);

        btnReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameOverDialogRePlayThread gameOverDialogRePlayThread = new GameOverDialogRePlayThread(gameStageActivity, gameOverDialog);
                gameOverDialogRePlayThread.start();
            }
        });

        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameOverDialogMainThread gameOverDialogMainThread = new GameOverDialogMainThread(gameStageActivity, gameOverDialog);
                gameOverDialogMainThread.start();
            }
        });
    }

    public GameOverDialog(Context context, GameStageActivity gameStageActivity) {
        super(context);
        this.gameStageActivity = gameStageActivity;
    }


}
