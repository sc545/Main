package com.example.k.alltogether.alltogether;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.k.alltogether.R;

/**
 * Created by K on 2015-10-15.
 */
public class GameOverDialog extends Dialog {
    GameStageActivity gameStageActivity;
    ImageButton btnReplay, btnMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over_dialog);
        setCanceledOnTouchOutside(false);

        btnReplay = (ImageButton) findViewById(R.id.btnGameOverRePlay);
        btnMain = (ImageButton) findViewById(R.id.btnGameOverMain);

        btnReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameStageActivity.resetState();
                dismiss();
                gameStageActivity.m_bGameState =true;
            }
        });

        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameStageActivity.m_bThreadState =false;
                Intent i = new Intent(gameStageActivity.getApplicationContext(), MainActivity.class);
                gameStageActivity.startActivity(i);
                gameStageActivity.finish();
                dismiss();
            }
        });
    }

    public GameOverDialog(Context context, GameStageActivity gameStageActivity) {
        super(context);
        this.gameStageActivity = gameStageActivity;
    }

}
