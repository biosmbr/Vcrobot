package com.vcrobot.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vcrobot.R;

/**
 * Created by Dolphix.J Qing on 2016/5/19.
 */
public class TypeFaceActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "Dolphix.J Qing";
    private RelativeLayout loginface;
    private RelativeLayout regface;
    private TextView vcrid;
    private boolean isLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_face);

        setupActionBar();
        initView();
        setVcrid();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * 初始化所有控件
     */
    private void initView(){
        loginface = (RelativeLayout)findViewById(R.id.rl_login_face);
        regface = (RelativeLayout)findViewById(R.id.rl_reg_face);
        vcrid = (TextView)findViewById(R.id.tv_id);
        loginface.setOnClickListener(this);
        regface.setOnClickListener(this);
    }


    private void setVcrid(){
        //获得sp实例对象
        SharedPreferences sp = this.getSharedPreferences("data", Context.MODE_PRIVATE);
        //如果登陆过，直接登录(false为默认值，如果EXIST不存在，则false)
        if (sp.getBoolean("EXIST", false)) {
            String email = sp.getString("email", "");
            vcrid.setText("我的Vcr号："+email);
            isLogin = true;
        }else{
            vcrid.setText("我的Vcr号：(未登录)");
            isLogin = false;
            Toast.makeText(getApplicationContext(),"Tips:侧滑->头像->注册/登陆?",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_login_face:
                if (isLogin) {
                    Intent login = new Intent(this, VerifyActivity.class);
                    startActivity(login);
                }
                break;
            case R.id.rl_reg_face:
                if (isLogin) {
                    Intent reg = new Intent(this, RegFaceActivity.class);
                    startActivity(reg);
                }
                break;
        }
    }
}
