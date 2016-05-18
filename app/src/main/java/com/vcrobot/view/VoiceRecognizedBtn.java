package com.vcrobot.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.vcrobot.R;
import com.vcrobot.utils.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;

/**
 * Created by Dolphix.J Qing on 2016/5/14.
 */
public class VoiceRecognizedBtn extends Button implements RecognizerListener {

    private static final String TAG = "Dolphix.J Qing";
    // 语音听写对象
    private SpeechRecognizer sr;
    // 用HashMap存储听写结果
    private HashMap<String, String> srResults = new LinkedHashMap<String, String>();

    private String srText;

    private String curFilePath;
    private static final int DISTANCE_Y_CANCEL = 50;
    private static final int STATE_NORMAL = 1;
    private static final int STATE_RECORDING = 2;
    private static final int STATE_CANCEL = 3;

    private int curState = STATE_NORMAL;
    private boolean isRecording = false;
    private float totalAudioTime = 0f;
    //是否触发Long onClick
    private boolean longClick = false;
    private long curTime = 0L;

    private AudioRecorderDialog audioRecorderDialog;

    public VoiceRecognizedBtn(Context context) {
        super(context,null);
    }

    public VoiceRecognizedBtn(Context context, AttributeSet attrs) {
        super(context, attrs);

        audioRecorderDialog = new AudioRecorderDialog(getContext());
        initView();
        //null,表示使用云识别
        sr = SpeechRecognizer.createRecognizer(context, null);
    }

    private void initView(){
        setOnLongClickListener(new LongClickListener());
    }

    /**
     * 返回录音文件名
     * @return
     */
    private String getVoiceFileName(){
        return (curFilePath = Environment.getExternalStorageDirectory()+"/vcr/"+randomFileName()+".wav");
    }


    /**
     * 录音完成后的回调
     */
    public interface AudioRecorderFinishLinster{
        void recorderFinish(float seconds, String folder);
    }

    private AudioRecorderFinishLinster audioRecorderFinishLinster;

    public void setAudioRecorderFinishLinster(AudioRecorderFinishLinster audioRecorderFinishLinster){
        this.audioRecorderFinishLinster = audioRecorderFinishLinster;
    }

    /**
     * 语音识别完成后回调
     */
    public interface VoiceRecognizedFinishLinster {
        void recognizedFinish(String srText);
    }

    private VoiceRecognizedFinishLinster voiceRecognizedFinishLinster;

    public void setVoiceRecognizedFinishLinster(VoiceRecognizedFinishLinster voiceRecognizedFinishLinster){
        this.voiceRecognizedFinishLinster = voiceRecognizedFinishLinster;
    }

    private static final int MSG_RECOGNIZED = 0;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case MSG_RECOGNIZED:
                    if (null != voiceRecognizedFinishLinster){
                        Log.i(TAG, "handleMessage: "+srText);
                        voiceRecognizedFinishLinster.recognizedFinish(srText);
                        sr.cancel();
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };
    /**
     * 改变状态
     * @param curState
     */
    private void changeState(int curState){
        if (curState != this.curState){
            this.curState = curState;
            switch (curState){
                case STATE_NORMAL:
                    setBackgroundResource(R.drawable.btn_recorder_normal);
                    setText(R.string.str_recorder_normal);
                    break;
                case STATE_RECORDING:
                    setBackgroundResource(R.drawable.btn_recorder_recording);
                    setText(R.string.str_recorder_recording);
                    if (isRecording){
                        audioRecorderDialog.recordingDialog();
                    }
                    break;
                case STATE_CANCEL:
                    setBackgroundResource(R.drawable.btn_recorder_recording);
                    setText(R.string.str_recorder_cancel);
                    audioRecorderDialog.wantToCancelDialog();
                    break;
            }
        }
    }

    /**
     * 重置状态
     */
    private void reSetState(){
        isRecording = false;
        longClick = false;
        changeState(STATE_NORMAL);
        totalAudioTime = 0f;
        curTime = 0L;
    }

    /**
     * 判断手势是否取消
     * @param x
     * @param y
     * @return
     */
    private boolean wantToCancel(int x, int y){
        if (x < 0 || x > getWidth()){
            return true;
        }
        if (y < (- DISTANCE_Y_CANCEL) || y > (getHeight() + DISTANCE_Y_CANCEL)){
            return true;
        }
        return false;
    }

    private String getCurFilePath(){
        return curFilePath;
    }

    /**
     * 监听长按事件
     */
    private class LongClickListener implements OnLongClickListener{
        @Override
        public boolean onLongClick(View v) {
            longClick = true;
            //清空结果集
            srResults.clear();
            setParam();
            //开始监听语音
            int ret = sr.startListening(VoiceRecognizedBtn.this);
            Log.i(TAG, "onLongClick: "+ret);
            return false;
        }
    }
    /**
     * 发音音量
     * @param i 0~30
     * @param bytes
     */
    @Override
    public void onVolumeChanged(int i, byte[] bytes) {
        audioRecorderDialog.updateVoiceLevelDialog(i/5+1);
//        Log.i(TAG, "onVolumeChanged: "+i);
    }

    /**
     * 已经准备好，可以开始录音
     */
    @Override
    public void onBeginOfSpeech() {
        Log.i(TAG, "onBeginOfSpeech: ");
        //发送可以开始录音消息
        audioRecorderDialog.loadRecordingDialog();
        isRecording = true;
        curTime = System.currentTimeMillis();
    }

    /**
     * 录音结束，不再接受语音
     */
    @Override
    public void onEndOfSpeech() {
//        audioRecorderDialog.dimissDialog();
        Log.i(TAG, "onEndOfSpeech: ");
    }

    /**
     * 语音检测结果
     * @param recognizerResult
     * @param b
     */
    @Override
    public void onResult(RecognizerResult recognizerResult, boolean b) {

        String voiceText = getVoiceResult(recognizerResult);

        if (b) {
            srText = voiceText;
            handler.sendEmptyMessageDelayed(MSG_RECOGNIZED,0);
        }
    }

    /**
     * 录音出现错误
     * @param speechError
     */
    @Override
    public void onError(SpeechError speechError) {
        Log.i(TAG, "onError: "+speechError.toString());
    }

    /**
     * 开发者，扩展函数，用于提交信息
     * @param i
     * @param i1
     * @param i2
     * @param bundle
     */
    @Override
    public void onEvent(int i, int i1, int i2, Bundle bundle) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int)event.getX();
        int y = (int)event.getY();

        switch (action){
            case MotionEvent.ACTION_DOWN:
                changeState(STATE_RECORDING);
                break;
            case MotionEvent.ACTION_MOVE:
                if (isRecording){
                    if (wantToCancel(x,y)){
                        changeState(STATE_CANCEL);
                        //取消语音识别
                        sr.cancel();
                    }else{
                        changeState(STATE_RECORDING);
                    }
                }

                break;
            case MotionEvent.ACTION_UP:

                //停止录音
                sr.stopListening();
                //计算录制时长
                totalAudioTime = (System.currentTimeMillis() - curTime)*1.0f / 1000f;
                Log.i(TAG, "onTouchEvent: "+totalAudioTime);

                if (!longClick){
                    reSetState();
                    //取消语音识别
                    sr.cancel();
                    super.onTouchEvent(event);
                }
                if (!isRecording || totalAudioTime < 0.6f){
                    Log.i(TAG, "onTouchEvent: "+totalAudioTime);
                    audioRecorderDialog.timeTooShortDialog();
                    try {
                        Thread.sleep(2500);
                    }catch (Exception e) {e.printStackTrace();}
                    audioRecorderDialog.dimissDialog();
                    //取消语音识别
                    sr.cancel();
                } else if (STATE_RECORDING == curState){//正常录制结束
                    audioRecorderDialog.dimissDialog();
//                    //预留等待时间
//                    while ((times--) >= 0 && (null == srText)) {
//                        try {
//                            Thread.sleep(10);
//                        }catch (Exception e) {e.printStackTrace();}
//                    }
//
//                    Log.i(TAG, "语音识别结果"+srText);
                    if (null != audioRecorderFinishLinster){
                        audioRecorderFinishLinster.recorderFinish(totalAudioTime, getCurFilePath());
                    }
                    reSetState();
                    return super.onTouchEvent(event);
                } else if (STATE_CANCEL == curState){
                    audioRecorderDialog.dimissDialog();
                    //取消语音识别
                    sr.cancel();
                }
                reSetState();
                break;
        }
        return super.onTouchEvent(event);
    }

    private String getVoiceResult(RecognizerResult results) {
        String text = JsonUtil.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        srResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : srResults.keySet()) {
            resultBuffer.append(srResults.get(key));
        }

        return resultBuffer.toString();
    }
    /**
     * 获取一个随机文件名
     * @return
     */
    private String randomFileName(){
        return UUID.randomUUID().toString();
    }

    /**
     * 参数设置
     */
    public void setParam() {
        // 清空参数
        sr.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎-云端
        sr.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 设置返回结果格式
        sr.setParameter(SpeechConstant.RESULT_TYPE, "json");

        // 设置语言
        sr.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 设置语言区域
        sr.setParameter(SpeechConstant.ACCENT, "mandarin");

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        sr.setParameter(SpeechConstant.VAD_BOS, "99999999");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        sr.setParameter(SpeechConstant.VAD_EOS, "99999999");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        sr.setParameter(SpeechConstant.ASR_PTT, "1");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        sr.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
        sr.setParameter(SpeechConstant.ASR_AUDIO_PATH, getVoiceFileName());
    }

}
