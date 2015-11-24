package com.example.k.alltogether.alltogether;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.k.alltogether.R;


/**
 * Created by K on 2015-10-15.
 */
public class RankWriteDialog extends Dialog {
    GameStageActivity gameStageActivity;
    ImageButton btnCancel, btnRegister;
    EditText etName;
    TextView tvScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.rank_write_dialog);
        setCanceledOnTouchOutside(false);

        btnCancel = (ImageButton) findViewById(R.id.btnCancel);
        btnRegister = (ImageButton) findViewById(R.id.btnRegister);
        etName = (EditText) findViewById(R.id.etName);
        tvScore = (TextView) findViewById(R.id.tvScore);

        tvScore.setText(""+gameStageActivity.m_nScore);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                gameStageActivity.m_dlgGameOverDialog.show();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RankWriteThread rankWriteThread = new RankWriteThread(gameStageActivity.getApplicationContext(), etName.getText().toString(), gameStageActivity.m_nScore);
                rankWriteThread.start();
                etName.setText("");
                dismiss();
                gameStageActivity.m_dlgGameOverDialog.show();
            }
        });

    }

    public void musicStart(){
        gameStageActivity.m_musicGameOverSound.start();
    }

    public RankWriteDialog(Context context, GameStageActivity gameStageActivity) {
        super(context);
        this.gameStageActivity = gameStageActivity;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {// 뒤로가기 누를시

        }
        return false;
    }

}
