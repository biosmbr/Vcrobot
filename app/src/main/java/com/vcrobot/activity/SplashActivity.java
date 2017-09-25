package com.vcrobot.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.vcrobot.R;
import com.vcrobot.utils.EventUtil;

/**
 * Created by Dolphix.J Qing on 2016/5/16.
 */
public class SplashActivity extends Activity{
    private static final int sleepTime = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {

        handler.sendEmptyMessageDelayed(EventUtil.LOAD_FINISH,sleepTime);
        super.onStart();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case EventUtil.LOAD_FINISH:
                    comeInMainActivity();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 开启MainActivity
     */
    private void comeInMainActivity(){
        finish();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
