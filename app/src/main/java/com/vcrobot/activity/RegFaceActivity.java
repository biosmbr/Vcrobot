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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vcrobot.R;
import com.vcrobot.bean.FaceGroup;
import com.vcrobot.bean.FaceInfo;
import com.vcrobot.bean.FacePerson;
import com.vcrobot.entity.PlatformDetect;
import com.vcrobot.entity.PlatformTrain;
import com.vcrobot.utils.CommonUtil;
import com.vcrobot.utils.DrawFaceUtil;
import com.vcrobot.utils.EventUtil;
import com.vcrobot.utils.FileUtil;
import com.vcrobot.utils.LocalInfo;
import com.vcrobot.utils.NetTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by Dolphix.J Qing on 2016/5/19.
 */
public class RegFaceActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "Dolphix.J Qing";
    private String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    private String absPath = rootPath+"/vcr/tmp/";
    private String jpegName = absPath+"tmp.jpg";
    private ImageView face;
    private Bitmap b;
    private TextView info;
    private TextView result;
    private Button train;
    private JSONObject jsonObject;
    private String user;
    private String sessionID;
    private boolean canTrain;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_reg);
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
        train = (Button)findViewById(R.id.train);
        train.setOnClickListener(this);
        user = LocalInfo.getLocalUserName(getApplicationContext());
        PlatformDetect.setDetectCallback(new DetectCallback());
        PlatformTrain.setTrainCallback(new TrainCallback());
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
                        //取得face_id
                        FaceInfo.face_id = rst.getJSONArray("face").getJSONObject(0).getString("face_id");
                        if (CommonUtil.isNetWorkConnected(getApplicationContext())) {
                            //人脸识别
                            addFace();
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
     * 添加人脸
     */
    private void addFace(){

        new Thread(new Runnable(){
            @Override
            public void run() {
                requestPlatform();
            }
        }).start();
    }

    /**
     * 请求Face++平台服务
     */
    private void requestPlatform(){
        Log.i(TAG, "requestPlatform: ");
        try {
            boolean b = true;
            int t = 0;
            if (null == user){
                Log.i(TAG, "requestPlatform: null == user");
                return;
            }
            while (b && t < 3) {
                if (!user.equals("vcrobot") && CommonUtil.isNetWorkConnected(getApplicationContext())) {
                    //添加人脸
                    String result = NetTask.doGet(FacePerson.addFace(FaceInfo.face_id, user));
                    Log.i(TAG, "run: addFace "+result);
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.has("error") || !jsonObject.getBoolean("success")) {
                        //添加失败，创建用户
                        result = NetTask.doGet(FacePerson.create("vcrobot",user,"vcr"));
                        Log.i(TAG, "run: create person "+result);
                        jsonObject = new JSONObject(result);
                        if (jsonObject.has("error") || jsonObject.getInt("added_group") <= 0){
                            //group不存在，创建group
                            result = NetTask.doGet(FaceGroup.create("vcrobot","vcr"));
                            jsonObject = new JSONObject(result);
                            Log.i(TAG, "run: create group "+result);
                            if (!jsonObject.has("error")){
                                Log.i(TAG, "requestPlatform: Group create success!");
                            }
                        }else{
                            Log.i(TAG, "requestPlatform: Person create success!");
                        }
                    }else{
                        b = false;
                        handler.sendEmptyMessage(EventUtil.FACE_ADD_SUCCESS);
                        Log.i(TAG, "requestPlatform: Add face success!");
                    }
                }
                ++t;
            }
            if (b){
                handler.sendEmptyMessage(EventUtil.FACE_ADD_FAIL);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 针对verify功能对一个person进行训练。
     */
    private void trainFace(){
        new Thread(new Runnable(){
            @Override
            public void run() {
                if (CommonUtil.isNetWorkConnected(getApplicationContext())){
                    PlatformTrain.train(user);
                }
            }
        }).start();
    }


    /**
     * 人脸训练，回调
     */
    private class TrainCallback implements PlatformTrain.TrainCallback{
        @Override
        public void trainResult(JSONObject rst) {
            Log.i(TAG, "trainResult: "+rst);
            try{
                if (!rst.has("error")){
                    //相应请求的session标识符，可用于结果查询
                    sessionID = rst.getString("session_id");
                    handler.sendEmptyMessage(EventUtil.FACE_TRAIN_FINISH);
                }else{
                    //人脸训练出错
                    handler.sendEmptyMessage(EventUtil.FACE_TRAIN_ERR);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
        switch (msg.what){
            case EventUtil.FACE_DRAW_DETECT_DATA:
                DrawFaceUtil.doDraw(b,face,info,jsonObject);
                break;
            case EventUtil.NOT_EXIST_FACE:
                result.setText("不存在人脸!");
                openSysCamera();
                break;
            case EventUtil.FACE_ADD_SUCCESS:
                result.setText("提示:已将人脸添加至服务器。");
                canTrain = true;
                train.setEnabled(true);
                break;
            case EventUtil.FACE_ADD_FAIL:
                result.setText("抱歉!添加人脸失败。");
                break;
            case EventUtil.FACE_TRAIN_FINISH:
                result.setText("提示:人脸训练已完成!");
                break;
            case EventUtil.FACE_TRAIN_ERR:
                result.setText("抱歉!训练出现故障!");
                break;
        }
        super.handleMessage(msg);
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.train:
                result.setText("正在执行人脸训练...");
                canTrain = false;
                train.setEnabled(false);
                trainFace();
                break;
        }
    }
}
