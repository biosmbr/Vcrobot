package com.vcrobot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.vcrobot.activity.MainActivity;
import com.vcrobot.utils.VCRConst;

/**
 * Created by Dolphix.J Qing on 2016/5/16.
 */
public class SplashActivity extends Activity{
    private static final int sleepTime = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {

        handler.sendEmptyMessageDelayed(VCRConst.LOAD_FINISH,sleepTime);
        super.onStart();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case VCRConst.LOAD_FINISH:
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
