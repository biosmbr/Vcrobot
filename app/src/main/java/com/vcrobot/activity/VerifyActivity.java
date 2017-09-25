package com.vcrobot.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vcrobot.R;
import com.vcrobot.bean.FaceInfo;
import com.vcrobot.entity.PlatformDetect;
import com.vcrobot.entity.PlatformVerify;
import com.vcrobot.utils.CommonUtil;
import com.vcrobot.utils.DrawFaceUtil;
import com.vcrobot.utils.EventUtil;
import com.vcrobot.utils.FileUtil;
import com.vcrobot.utils.LocalInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by Dolphix.J Qing on 2016/5/19.
 */
public class VerifyActivity extends AppCompatActivity {

    private static final String TAG = "Dolphix.J Qing";
    private String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    private String absPath = rootPath+"/vcr/tmp/";
    private String jpegName = absPath+"tmp.jpg";
    private ImageView face;
    private Bitmap b;
    private TextView info;
    private TextView result;
    private JSONObject jsonObject;
    private String user;
    private float confidence;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_verify);
        setupActionBar();
        initView();
        openSysCamera();
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
     * 检查存储状况，并创建相应文件夹
     * @return
     */
    private boolean savePrepare(){
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Log.i(TAG,"No Sdcard");
            Toast.makeText(this,"No Sdcard!",Toast.LENGTH_SHORT).show();
            return false;
        }else{
            FileUtil.createPath(absPath);
        }
        return true;
    }

    /**
     * 开启系统相机
     */
    private void openSysCamera(){
        if (savePrepare()){
            //开启系统相机
            Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            camera.putExtra("camerasensortype", 2); // 调用前置摄像头
            camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(absPath,"tmp.jpg")));
            startActivityForResult(camera, EventUtil.SYS_CAMERA);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EventUtil.SYS_CAMERA && resultCode == Activity.RESULT_OK){
            Log.i(TAG,"Camera finish!");
            //保持原图比例压缩，并保存为tmp2.jpg
            b = FileUtil.compressBySize(jpegName,500,500);
            face.setImageBitmap(b);
            FileUtil.saveBitmap(b,absPath+"tmp2.jpg");
            if (CommonUtil.isNetWorkConnected(getApplicationContext())){
                //人脸检测
                detectFace();
            }else{
                Toast.makeText(getApplicationContext(),"无法检测，网络可能存在问题!",Toast.LENGTH_SHORT).show();
            }
        }else if (requestCode == EventUtil.SYS_CAMERA && resultCode == Activity.RESULT_CANCELED){
            finish();
        }
    }
    /**
     * 初始化所有控件
     */
    private void initView(){
        face = (ImageView)findViewById(R.id.face);
        info = (TextView)findViewById(R.id.info);
        result = (TextView)findViewById(R.id.result);
        user = LocalInfo.getLocalUserName(getApplicationContext());
        PlatformDetect.setDetectCallback(new DetectCallback());
        PlatformVerify.setVerifyCallback(new VerifyCallback());
    }

    /**
     * 检测人脸
     */
    private void detectFace(){

        new Thread(new Runnable(){
            @Override
            public void run() {
                PlatformDetect.detect(new File(absPath+"tmp2.jpg"));
            }
        }).start();
    }

    /**
     * 人脸检测，回调
     */
    private class DetectCallback implements PlatformDetect.DetectCallback{

        @Override
        public void detectResult(JSONObject rst) {
            if (null == rst){
                return;
            }
            Log.i(TAG, "detectResult: "+rst.toString());
            try {
                if (!rst.has("error")){
                    //找出所有人脸
                    final int count = rst.getJSONArray("face").length();
                    if (count > 0){
                        Log.i(TAG, "detectResult: 存在人脸");
                        jsonObject = rst;
                        if (CommonUtil.isNetWorkConnected(getApplicationContext())) {
                            //人脸识别
                            verifyFace();
                        }
                        //开始根据数据绘制位置、坐标
                        handler.sendEmptyMessage(EventUtil.FACE_DRAW_DETECT_DATA);
                        return;
                    }
                }
                //不存在人脸
                handler.sendEmptyMessage(EventUtil.NOT_EXIST_FACE);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 人脸识别
     */
    private void verifyFace(){

        new Thread(new Runnable(){
            @Override
            public void run() {
                //face_id，用户名
                PlatformVerify.verify(FaceInfo.face_id, user);
            }
        }).start();
    }

    /**
     * 人脸识别，回调
     */
    private class VerifyCallback implements PlatformVerify.VerifyCallback{

        @Override
        public void verifyResult(JSONObject rst) {

            Log.i(TAG, "verifyResult: "+rst.toString());
            try {
                if (!rst.has("error")){
                    //置信度
                    confidence = (float) rst.getDouble("confidence");
                    if (rst.getBoolean("is_same_person")){
                        //识别成功
                        handler.sendEmptyMessage(EventUtil.FACE_VERIFY_SUCCESS);
                    }
                    else{
                        handler.sendEmptyMessage(EventUtil.FACE_VERIFY_FAIL);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK){
            finish();
        }
        return false;
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case EventUtil.FACE_DRAW_DETECT_DATA:
                    DrawFaceUtil.doDraw(b,face,info,jsonObject);
                    break;
                case EventUtil.NOT_EXIST_FACE:
                    result.setText("识别结果:不存在人脸!");
                    openSysCamera();
                    break;
                case EventUtil.FACE_VERIFY_SUCCESS:
                    result.setText("认证通过!判断置信度"+confidence+"%");
                    break;
                case EventUtil.FACE_VERIFY_FAIL:
                    result.setText("抱歉!判断置信度"+confidence+"%");
                    break;
            }
            super.handleMessage(msg);
        }
    };

}
