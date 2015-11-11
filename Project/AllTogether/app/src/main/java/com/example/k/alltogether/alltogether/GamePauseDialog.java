package com.example.k.alltogether.alltogether;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import com.example.k.alltogether.R;

/**
 * Created by K on 2015-10-15.
 */

public class GamePauseDialog extends Dialog {
    GameStageActivity gameStageActivity;
    ImageButton btnReplay, btnAgain, btnMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_pause_dialog);
        setCanceledOnTouchOutside(false);

        btnReplay = (ImageButton) findViewById(R.id.btnGamePauseRePlay);
        btnAgain = (ImageButton) findViewById(R.id.btnGamePauseAgain);
        btnMain = (ImageButton) findViewById(R.id.btnGamePauseMain);

        btnReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameStageActivity.resetState();
                dismiss();
                gameStageActivity.gameState=true;
            }
        });

        btnAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                gameStageActivity.gameState=true;
            }
        });

        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameStageActivity.threadState=false;
                Intent i = new Intent(gameStageActivity.getApplicationContext(), MainActivity.class);
                gameStageActivity.startActivity(i);
                gameStageActivity.finish();
                dismiss();
            }
        });
    }

    public GamePauseDialog(Context context, GameStageActivity gameStageActivity) {
        super(context);
        this.gameStageActivity = gameStageActivity;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {// 뒤로가기 누를시
            dismiss();
            gameStageActivity.gameState=true;
        }
        return false;
    }
}
