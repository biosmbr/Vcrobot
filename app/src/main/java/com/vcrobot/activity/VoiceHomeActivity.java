package com.vcrobot.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.vcrobot.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dolphix.J Qing on 2016/5/19.
 */
public class VoiceHomeActivity extends AppCompatActivity implements View.OnClickListener{

    private static final File parentPath = Environment.getExternalStorageDirectory();
    private static String storagePath = parentPath.getAbsolutePath()+"/vcr/voice/";
    private ListView voiceList;
    private List<String> dataList;
    private Button delAllVoice;
    private File[] files;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recoder);

        setupActionBar();

        initView();

        loadAllVoiceFileName();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
        //添加并且显示
        voiceList.setAdapter(adapter);
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

        switch (v.getId()){
            case R.id.del_all:
                deleteAllLocalVoice();
                break;
        }
    }

    /**
     * 初始化所有控件
     */
    private void initView(){
        voiceList = (ListView)findViewById(R.id.voice_list);
        delAllVoice = (Button)findViewById(R.id.del_all);
        delAllVoice.setOnClickListener(this);
    }

    /**
     * 加载所有/vcr/语音
     */
    private void loadAllVoiceFileName(){
        dataList = new ArrayList<String>();
        File file = new File(storagePath);
        files = file.listFiles();
        if (null== files){
            return;
        }
        for (File f : files) {
//            String absPath = storagePath+f.getName();
            dataList.add(f.getName());
        }
    }

    /**
     * 删除本地所有语音
     */
    private void deleteAllLocalVoice(){
        if (null== files){
            return;
        }
        for (File f : files) {
            f.delete();
        }
        dataList.clear();
        adapter.notifyDataSetChanged();
    }


}
