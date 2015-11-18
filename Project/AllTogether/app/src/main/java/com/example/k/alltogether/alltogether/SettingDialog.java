package com.example.k.alltogether.alltogether;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import com.example.k.alltogether.R;

/**
 * Created by K on 2015-11-18.
 */
public class SettingDialog extends Dialog {
    ImageButton btnExit;
    ToggleButton tbBgm, tbEffect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.setting_dialog);

//        btnExit = (ImageButton) findViewById(R.id.btnExit2);
        tbBgm = (ToggleButton) findViewById(R.id.tbBgm);
        tbEffect = (ToggleButton) findViewById(R.id.tbEffect);

//        btnExit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });

        tbBgm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tbBgm.setBackgroundResource(R.drawable.on);
                } else {
                    tbBgm.setBackgroundResource(R.drawable.off);
                }
            }
        });

        tbEffect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tbEffect.setBackgroundResource(R.drawable.on);
                } else {
                    tbEffect.setBackgroundResource(R.drawable.off);
                }
            }
        });
    }

    SettingDialog(Context context){
        super(context);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {// 뒤로가기 누를시
            dismiss();
        }
        return false;
    }
}
