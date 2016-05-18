package com.vcrobot.entity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

/**
 * Created by Dolphix.J Qing on 2016/5/3.
 */
public class RecorderMediaManager {
    private static MediaPlayer mediaPlayer;
    private static boolean isPause;

    public static void PlaySound(String folder, OnCompletionListener onCompletionListener){

        if (null == mediaPlayer){
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    mediaPlayer.reset();
                    return false;
                }
            });
        }else{
            mediaPlayer.reset();
        }
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnCompletionListener(onCompletionListener);
        try{
            mediaPlayer.setDataSource(folder);
            mediaPlayer.prepare();
        }catch (Exception e){e.printStackTrace();}

        mediaPlayer.start();
    }

    public static void pause(){
        if (null != mediaPlayer && mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            isPause = true;
        }
    }

    public static void resume(){
        if (null != mediaPlayer && isPause){
            mediaPlayer.start();
            isPause = false;
        }
    }

    public static void release(){
        if (null != mediaPlayer){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
