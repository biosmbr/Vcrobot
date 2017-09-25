package com.vcrobot.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vcrobot.R;
import com.vcrobot.utils.LocalInfo;

/**
 * 设置界面
 * Created by Dolphix.J Qing on 2016/5/18.
 */
public class SettingsActivity extends AppCompatActivity implements OnClickListener {

    private RelativeLayout rl_switch_one;
    private RelativeLayout rl_switch_two;
    private RelativeLayout rl_switch_three;
    private RelativeLayout rl_switch_four;
    private ImageView iv_switch_open_one;
    private ImageView iv_switch_close_one;
    private ImageView iv_switch_open_two;
    private ImageView iv_switch_close_two;
    private ImageView iv_switch_open_three;
    private ImageView iv_switch_close_three;
    private ImageView iv_switch_open_four;
    private ImageView iv_switch_close_four;
    private TextView tv_swicth;

    private String user;

    private Button logoutBtn;
    private boolean one,two,three,four;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setupActionBar();

        initView();
        setSwitch();
    }

    /**
     * 初始化所有控件
     */
    private void initView(){
        rl_switch_one = (RelativeLayout) this.findViewById(R.id.rl_switch_one);
        rl_switch_two = (RelativeLayout) this.findViewById(R.id.rl_switch_two);
        rl_switch_three = (RelativeLayout) this.findViewById(R.id.rl_switch_three);
        rl_switch_four = (RelativeLayout) this.findViewById(R.id.rl_switch_four);

        iv_switch_open_one = (ImageView) this.findViewById(R.id.iv_switch_open_one);
        iv_switch_close_one = (ImageView) this.findViewById(R.id.iv_switch_close_one);
        iv_switch_open_two = (ImageView) this.findViewById(R.id.iv_switch_open_two);
        iv_switch_close_two = (ImageView) this.findViewById(R.id.iv_switch_close_two);
        iv_switch_open_three = (ImageView) this.findViewById(R.id.iv_switch_open_three);
        iv_switch_close_three = (ImageView) this.findViewById(R.id.iv_switch_close_three);
        iv_switch_open_four = (ImageView) this.findViewById(R.id.iv_switch_open_four);
        iv_switch_close_four = (ImageView) this.findViewById(R.id.iv_switch_close_four);

        tv_swicth = (TextView)findViewById(R.id.tv_swicth);
        logoutBtn = (Button) this.findViewById(R.id.btn_logout);
        rl_switch_one.setOnClickListener(this);
        rl_switch_two.setOnClickListener(this);
        rl_switch_three.setOnClickListener(this);
        rl_switch_four.setOnClickListener(this);

        user = LocalInfo.getLocalUserName(getApplicationContext());
        logoutBtn.setOnClickListener(this);
    }

    /**
     * 恢复switch状态
     */
    private void setSwitch(){

        if (user.equals("vcrobot")){
            logoutBtn.setText("注册&登陆");
        }else{
            logoutBtn.setText("退出登陆");
        }
        //获得sp实例对象
        SharedPreferences sp = this.getSharedPreferences("settings", Context.MODE_PRIVATE);
        //如果登陆过，直接登录(false为默认值，如果EXIST不存在，则false)
        if (sp.getBoolean("EXIST", false)) {
            Log.i("TAG","exist");
            if (sp.getBoolean("four",false)){
                iv_switch_open_four.setVisibility(View.VISIBLE);
                iv_switch_close_four.setVisibility(View.INVISIBLE);
                rl_switch_one.setVisibility(View.VISIBLE);
                rl_switch_two.setVisibility(View.VISIBLE);
                rl_switch_three.setVisibility(View.VISIBLE);
                tv_swicth.setText("已开启所有选项");
            }else{
                iv_switch_open_four.setVisibility(View.INVISIBLE);
                iv_switch_close_four.setVisibility(View.VISIBLE);
                rl_switch_one.setVisibility(View.GONE);
                rl_switch_two.setVisibility(View.GONE);
                rl_switch_three.setVisibility(View.GONE);
                tv_swicth.setText("已关闭所有选项");
                return;
            }

            if (sp.getBoolean("one",false)){
                iv_switch_open_one.setVisibility(View.VISIBLE);
                iv_switch_close_one.setVisibility(View.INVISIBLE);
            }else{
                iv_switch_open_one.setVisibility(View.INVISIBLE);
                iv_switch_close_one.setVisibility(View.VISIBLE);
            }
            if (sp.getBoolean("two",false)){
                iv_switch_open_two.setVisibility(View.VISIBLE);
                iv_switch_close_two.setVisibility(View.INVISIBLE);
            }else{
                iv_switch_open_two.setVisibility(View.INVISIBLE);
                iv_switch_close_two.setVisibility(View.VISIBLE);
            }
            if (sp.getBoolean("three",false)){
                iv_switch_open_three.setVisibility(View.VISIBLE);
                iv_switch_close_three.setVisibility(View.INVISIBLE);
            }else{
                iv_switch_open_three.setVisibility(View.INVISIBLE);
                iv_switch_close_three.setVisibility(View.VISIBLE);
            }
        }
    }
    /**
     * 保存设置信息
     */
    private void saveLoginInfo() {
        Log.i("TAG", "save:"+one+"|"+two+"|"+three+"|"+four);
        SharedPreferences.Editor editor = getSharedPreferences("settings", MODE_PRIVATE).edit();
        editor.putBoolean("EXIST", true);
        if (one){
            editor.putBoolean("one", true);
        }else{
            editor.putBoolean("one", false);
        }
        if (two){
            editor.putBoolean("two", true);
        }else{
            editor.putBoolean("two", false);
        }
        if (three){
            editor.putBoolean("three", true);
        }else{
            editor.putBoolean("three", false);
        }
        if (four){
            editor.putBoolean("four", true);
        }else{
            editor.putBoolean("four", false);
        }
        editor.commit();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_switch_one:
                if (iv_switch_open_one.getVisibility() == View.VISIBLE) {
                    iv_switch_open_one.setVisibility(View.INVISIBLE);
                    iv_switch_close_one.setVisibility(View.VISIBLE);
                    one = false;
                    saveLoginInfo();
                } else {
                    iv_switch_open_one.setVisibility(View.VISIBLE);
                    iv_switch_close_one.setVisibility(View.INVISIBLE);
                    one = true;
                    saveLoginInfo();
                }
                break;
            case R.id.rl_switch_two:
                if (iv_switch_open_two.getVisibility() == View.VISIBLE) {
                    iv_switch_open_two.setVisibility(View.INVISIBLE);
                    iv_switch_close_two.setVisibility(View.VISIBLE);
                    two = false;
                    saveLoginInfo();
                } else {
                    iv_switch_open_two.setVisibility(View.VISIBLE);
                    iv_switch_close_two.setVisibility(View.INVISIBLE);
                    two = true;
                    saveLoginInfo();
                }
                break;
            case R.id.rl_switch_three:
                if (iv_switch_open_three.getVisibility() == View.VISIBLE) {
                    iv_switch_open_three.setVisibility(View.INVISIBLE);
                    iv_switch_close_three.setVisibility(View.VISIBLE);
                    three = false;
                    saveLoginInfo();
                } else {
                    iv_switch_open_three.setVisibility(View.VISIBLE);
                    iv_switch_close_three.setVisibility(View.INVISIBLE);
                    three = true;
                    saveLoginInfo();
                }
                break;
            case R.id.rl_switch_four:
                if (iv_switch_open_four.getVisibility() == View.VISIBLE) {
                    iv_switch_open_four.setVisibility(View.INVISIBLE);
                    iv_switch_close_four.setVisibility(View.VISIBLE);
                    //------
                    rl_switch_one.setVisibility(View.GONE);
                    rl_switch_two.setVisibility(View.GONE);
                    rl_switch_three.setVisibility(View.GONE);
                    one = false;
                    two = false;
                    three = false;
                    four = false;
                    tv_swicth.setText("已关闭所有选项");
                    saveLoginInfo();
                } else {
                    iv_switch_open_four.setVisibility(View.VISIBLE);
                    iv_switch_close_four.setVisibility(View.INVISIBLE);
                    //------
                    rl_switch_one.setVisibility(View.VISIBLE);
                    rl_switch_two.setVisibility(View.VISIBLE);
                    rl_switch_three.setVisibility(View.VISIBLE);
                    //------
                    iv_switch_open_one.setVisibility(View.VISIBLE);
                    iv_switch_close_one.setVisibility(View.INVISIBLE);
                    iv_switch_open_two.setVisibility(View.VISIBLE);
                    iv_switch_close_two.setVisibility(View.INVISIBLE);
                    iv_switch_open_three.setVisibility(View.VISIBLE);
                    iv_switch_close_three.setVisibility(View.INVISIBLE);
                    one = true;
                    two = true;
                    three = true;
                    four = true;
                    tv_swicth.setText("已开启所有选项");
                    saveLoginInfo();
                }
                break;
            case R.id.btn_logout: //退出登陆
                logout();
                break;

            default:
                break;
        }

    }

    /**
     * 退出登陆
     */
    private void logout() {
        if (user.equals("vcrobot")){
            finish();
            Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
            startActivity(intent);
        }else{
            LocalInfo.clearLocalData(getApplicationContext());
            logoutBtn.setText("注册&登陆");
        }
    }
}
