package com.vcrobot.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vcrobot.R;

/**
 * Created by Dolphix.J Qing on 2016/5/3.
 */
public class AudioRecorderDialog {
    private Dialog dialog;
    private ImageView iv_icon;
    private ImageView iv_voice;
    private TextView tv_recorder;
    private Context context;

    public AudioRecorderDialog(Context context) {
        this.context = context;
    }

    /**
     * 构造并加载Dialog
     */
    public void loadRecordingDialog(){
        dialog = new Dialog(context, R.style.ThemeAudioRecorderDialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_recorder,null);

        dialog.setContentView(view);

        iv_icon = (ImageView) dialog.findViewById(R.id.iv_icon);
        iv_voice = (ImageView) dialog.findViewById(R.id.iv_voice);
        tv_recorder = (TextView) dialog.findViewById(R.id.tv_recorder);
        dialog.show();
    }

    /**
     * 正在录音
     */
    public void recordingDialog(){
        if (null != dialog && dialog.isShowing()){
            iv_icon.setVisibility(View.VISIBLE);
            iv_voice.setVisibility(View.VISIBLE);
            tv_recorder.setVisibility(View.VISIBLE);

            iv_icon.setImageResource(R.mipmap.recorder);
            tv_recorder.setText(R.string.str_recorder_up_cancel);
        }
    }
    /**
     * 取消发送
     */
    public void wantToCancelDialog(){
        if (null != dialog && dialog.isShowing()){
            iv_icon.setVisibility(View.VISIBLE);
            iv_voice.setVisibility(View.GONE);
            tv_recorder.setVisibility(View.VISIBLE);

            iv_icon.setImageResource(R.mipmap.cancel);
            tv_recorder.setText(R.string.str_recorder_cancel);
        }
    }

    /**
     * 录音时间过短
     */
    public void timeTooShortDialog(){
        if (null != dialog && dialog.isShowing()){
            iv_icon.setVisibility(View.VISIBLE);
            iv_voice.setVisibility(View.GONE);
            tv_recorder.setVisibility(View.VISIBLE);

            iv_icon.setImageResource(R.mipmap.voice_to_short);
            tv_recorder.setText(R.string.str_recorder_time_too_short);
        }
    }

    /**
     * 隐藏对话框
     */
    public void dimissDialog(){
        if (null != dialog && dialog.isShowing()){
            dialog.dismiss();
            dialog = null;
        }
    }

    /**
     * 更新音量等级
     */
    public void updateVoiceLevelDialog(int level){
        if (null != dialog && dialog.isShowing()){
//            iv_icon.setVisibility(View.VISIBLE);
//            iv_voice.setVisibility(View.VISIBLE);
//            tv_recorder.setVisibility(View.VISIBLE);
            //获取资源
            int resID = context.getResources().getIdentifier("v"+level,"mipmap",context.getPackageName());
            iv_voice.setImageResource(resID);
        }
    }
}
