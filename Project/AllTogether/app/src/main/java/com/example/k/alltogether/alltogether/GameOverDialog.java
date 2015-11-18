package com.example.k.alltogether.alltogether;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.k.alltogether.R;


/**
 * Created by K on 2015-10-15.
 */
public class GameOverDialog extends Dialog {
    GameStageActivity gameStageActivity;
    ImageButton btnReplay, btnMain;
    EditText etName;
    TextView tvScore;
    Button btnSubmit;
    Music musicGameOver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.game_over_dialog);
        setCanceledOnTouchOutside(false);

        btnReplay = (ImageButton) findViewById(R.id.btnGameOverRePlay);
        btnMain = (ImageButton) findViewById(R.id.btnGameOverMain);
        etName = (EditText) findViewById(R.id.etName);
        tvScore = (TextView) findViewById(R.id.tvScore);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        tvScore.setText(""+gameStageActivity.m_nScore);

        btnReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameStageActivity.resetState();
                musicGameOver.stop();
                dismiss();
                gameStageActivity.m_bGameState = true;
            }
        });

        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameStageActivity.m_bThreadState =false;
                Intent i = new Intent(gameStageActivity.getApplicationContext(), MainActivity.class);
                gameStageActivity.startActivity(i);
                gameStageActivity.finish();
                musicGameOver.stop();
                dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RankWriteThread rankWriteThread = new RankWriteThread(gameStageActivity.getApplicationContext(), etName.getText().toString(), gameStageActivity.m_nScore);
                rankWriteThread.start();
            }
        });
    }

    public void musicStart(){
        musicGameOver.prepare();
        musicGameOver.start();
    }

    public GameOverDialog(Context context, GameStageActivity gameStageActivity) {
        super(context);
        this.gameStageActivity = gameStageActivity;
        musicGameOver = new Music(gameStageActivity.getApplicationContext(), Music.MusicType.GAME_OVER_SOUND);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {// 뒤로가기 누를시

        }
        return false;
    }

}
