package com.example.k.alltogether.alltogether;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;

import com.example.k.alltogether.R;

import java.io.File;

/**
 * Created by K on 2015-11-19.
 */
public class SelectImageActivity extends Activity {
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_CAMERA = 2;

    private Uri mImageCaptureUri;

    ImageView ivBubble, ivBombBubble, ivFeverBubble;
    Button btnChangeBubble, btnChangeBombBubble, btnChangeFeverBubble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_image_activity);

        ivBubble = (ImageView) findViewById(R.id.ivBubble);
        ivBombBubble = (ImageView) findViewById(R.id.ivBombBubble);
        ivFeverBubble = (ImageView) findViewById(R.id.ivFeverBubble);
        btnChangeBubble = (Button) findViewById(R.id.btnChangeBubble);
        btnChangeBombBubble = (Button) findViewById(R.id.btnChangeBombBubble);
        btnChangeFeverBubble = (Button) findViewById(R.id.btnChangeFeverBubble);

    }

    private void getPhotoFromCamera() { // 카메라에서 이미지 가져오기
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 임시로 사용할 파일의 경로 생성
        String url = "tmp_"+String.valueOf(System.currentTimeMillis())+".png";
        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));

        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    private void getPhotoFromAlbum(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK){
            return;
        }

/*        switch (requestCode){
            case CROP_FROM_CAMERA:
                final Bubble extras = data.getExtras();

                if(extras != null){
                    Bitmap photo = extras.getPar
                }
        }*/
    }
}
