package com.example.k.alltogether.alltogether;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.k.alltogether.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;


/**
 * Created by K on 2015-10-15.
 */
public class GameOverDialog extends Dialog {
    GameStageActivity gameStageActivity;
    ImageButton btnReplay, btnMain;
    EditText etName;
    TextView tvScore;
    Button btnSubmit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RankThread rankThread = new RankThread(gameStageActivity.getApplicationContext(), etName.getText().toString(), gameStageActivity.m_nScore);
                rankThread.start();
            }
        });
    }

    public GameOverDialog(Context context, GameStageActivity gameStageActivity) {
        super(context);
        this.gameStageActivity = gameStageActivity;
    }

}
