package com.test.bubblepang_tabs;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

/**
 * Created by K on 2015-11-14.
 */
public class Music {
    MediaPlayer mp = null;
    SoundPool sp = null;
    int soundId1;
    Music(Context co){
        mp = MediaPlayer.create(co, R.raw.main_bgm);
        //mp.setLooping(true);
    }
    Music(Context co, int musicNum){
        sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        if(musicNum == 0)
            soundId1 = sp.load(co, R.raw.bubble_bgm, 1);
    }
    void spStart(){
        sp.play(soundId1, 1, 1, 0, 0, 1);
    }
    void spStop(){
        sp.stop(soundId1);
    }
    void start(){
        mp.start();
    }
    void pause(){
        mp.pause();
    }
    void stop(){
        mp.stop();
    }
    void reset(){
        mp.reset();
    }
    void release(){
        mp.release();
    }
    boolean isPlaying(){
        return mp.isPlaying();
    }
}
