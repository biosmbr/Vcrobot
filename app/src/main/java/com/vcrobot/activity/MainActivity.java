package com.vcrobot.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vcrobot.R;
import com.vcrobot.adapter.ChatAdapter;
import com.vcrobot.adapter.EmojiAdapter;
import com.vcrobot.adapter.EmojiPagerAdapter;
import com.vcrobot.bean.ChatMessage;
import com.vcrobot.entity.RecorderMediaManager;
import com.vcrobot.utils.CommonUtil;
import com.vcrobot.utils.EmojiUtil;
import com.vcrobot.utils.LocalInfo;
import com.vcrobot.utils.NetTask;
import com.vcrobot.utils.JsonUtil;
import com.vcrobot.utils.TuringUtil;
import com.vcrobot.utils.EventUtil;
import com.vcrobot.utils.VcrobotUtil;
import com.vcrobot.view.ClipEidtText;
import com.vcrobot.view.ExpandGridView;
import com.vcrobot.view.VoiceRecognizedBtn;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, NetTask.HttpGetListenerr {
    //定义所有成员变量
    private static final String TAG = "Dolphix.J Qing";
    private RelativeLayout edittext_layout;
    private View buttonSetModeKeyboard;
    private View buttonSend;
    private View buttonPressToSpeak;
    private ImageView iv_emoticons_normal;
    private ImageView iv_emoticons_checked;
    private LinearLayout emojiIconContainer;
    private InputMethodManager manager;
    private Button buttonSetModeVoice;
    private List<String> emojiRES;
    private ClipEidtText eidtTextContent;
    private EmojiAdapter emojiAdapter;
    private ViewPager emojiViewpager;
    private VoiceRecognizedBtn voiceRecognizedBtn;
    private View animView;
    private List<ChatMessage> chatMsgList;
    private ListView chatListView;
    private ChatAdapter chatAdapter;
    private long oldTime, curTime;
    private int oldSize, curSize;
    private ImageView logo;
    private TextView email;
    private RelativeLayout netErr;
    private boolean netFlag;
    private boolean canTuring;
    private String sendText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDrawer();
        initView();
        setOnClickListener();
        sendWelcomeTips();
        netWorkConnected();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            //人脸识别
            Intent intent = new Intent(MainActivity.this,TypeFaceActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            //照片仓库
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            //语音仓库
            Intent intent = new Intent(MainActivity.this,VoiceHomeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {
            //兴趣设置
            Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            Toast.makeText(this,"正在努力开发中...", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_send) {
            Toast.makeText(this,"正在努力开发中...", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * 初始化系统侧滑盒子
     */
    private void initDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //获取头部
        View headerLayout = navigationView.getHeaderView(0);
//        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        logo = (ImageView)headerLayout.findViewById(R.id.logo);
        email = (TextView) headerLayout.findViewById(R.id.email);
    }
    //---------------------------------------------------------------------------------


    @Override
    protected void onRestart() {

        email.setText(LocalInfo.getLocalUserName(getApplicationContext()));
        super.onRestart();
    }

    @Override
    protected void onResumeFragments() {
        email.setText(LocalInfo.getLocalUserName(getApplicationContext()));
        super.onResumeFragments();
    }

    /**
     * 网络状态，子线程
     */
    private void netWorkConnected(){
        new Thread(new Runnable(){
            @Override
            public void run() {
                while (netFlag) {
                    if (!CommonUtil.isNetWorkConnected(getApplicationContext())) {
                        handler.sendEmptyMessage(EventUtil.NET_ERR);
                    } else {
                        handler.sendEmptyMessage(EventUtil.NET_OK);
                    }
                    try {
                        Thread.sleep(6000);
                    } catch (Exception e){e.printStackTrace();}
                }
            }
        }).start();

    }
    /**
     * 初始化，所有控件
     */
    private void initView() {
        edittext_layout = (RelativeLayout) findViewById(R.id.edittext_layout);
        buttonSetModeKeyboard = findViewById(R.id.btn_set_mode_keyboard);
        buttonSend = findViewById(R.id.btn_send);
        buttonPressToSpeak = findViewById(R.id.btn_press_to_speak);
        iv_emoticons_normal = (ImageView) findViewById(R.id.iv_emoticons_normal);
        iv_emoticons_checked = (ImageView) findViewById(R.id.iv_emoticons_checked);
        emojiIconContainer = (LinearLayout) findViewById(R.id.ll_face_container);
        buttonSetModeVoice = (Button) findViewById(R.id.btn_set_mode_voice);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        emojiViewpager = (ViewPager) findViewById(R.id.vPager);
        emojiRES = getEmojiRes(35);
        // 初始化表情viewpager
        List<View> views = new ArrayList<View>();
        View gv1 = getGridChildView(1);
        View gv2 = getGridChildView(2);
        views.add(gv1);
        views.add(gv2);
        emojiViewpager.setAdapter(new EmojiPagerAdapter(views));
        eidtTextContent = (ClipEidtText) findViewById(R.id.et_sendmessage);
        voiceRecognizedBtn = (VoiceRecognizedBtn) findViewById(R.id.recorder_button);
        chatMsgList = new ArrayList<ChatMessage>();
        chatListView = (ListView) findViewById(R.id.lv_chat);
        chatAdapter = new ChatAdapter(this, chatMsgList);
        chatListView.setAdapter(chatAdapter);
        oldTime = 0;
        oldSize = 0;
        netErr = (RelativeLayout) findViewById(R.id.net_err);
        netFlag = true;
        email.setText(LocalInfo.getLocalUserName(getApplicationContext()));
    }

    /**
     * 注册，所有监听事件
     */
    private void setOnClickListener() {
        buttonSetModeVoice.setOnClickListener(this);
        iv_emoticons_normal.setOnClickListener(this);
        iv_emoticons_checked.setOnClickListener(this);
        eidtTextContent.setOnFocusChangeListener(new FocusChangeListener());
        eidtTextContent.addTextChangedListener(new TextChangedListener());
        buttonSetModeKeyboard.setOnClickListener(this);
        voiceRecognizedBtn.setAudioRecorderFinishLinster(new AudioRecorderFinishLinster());
        voiceRecognizedBtn.setVoiceRecognizedFinishLinster(new VoiceRecognizedFinishLinster());
        chatListView.setOnItemClickListener(new VoiceItemClickListener());
        buttonSend.setOnClickListener(this);
        logo.setOnClickListener(this);
        netErr.setOnClickListener(this);
    }

    /**
     * 获取自定义随机欢迎语
     *
     * @return
     */
    private String getRandomWelcomeTips() {
        String[] welcome_array = this.getResources().getStringArray(R.array.welcome_tips);
        int index = (int) (Math.random() * (welcome_array.length - 1));
        return welcome_array[index];
    }

    /**
     * 发送自定义系统欢迎语
     */
    private void sendWelcomeTips() {
        ChatMessage msg = new ChatMessage();
        msg.setContent(getRandomWelcomeTips());
        msg.setFlag(EventUtil.RECEIVE);
        msg.setType(EventUtil.TYPE_TEXT);
        chatMsgList.add(msg);
        chatAdapter.notifyDataSetChanged();
    }

    /**
     * 接受消息并加添内容到List
     *
     * @param jsonStr
     */
    private void receiveMsg(String jsonStr) {
        ChatMessage msg = new ChatMessage();
        msg.setContent(JsonUtil.parseJson2KeyStr(jsonStr, "text"));
        msg.setRid(JsonUtil.parseJson2KeyStr(jsonStr,"rid"));
        msg.setTime(getCurTime());
        msg.setFlag(EventUtil.RECEIVE);
        msg.setType(EventUtil.TYPE_TEXT);
        chatMsgList.add(msg);
        chatAdapter.notifyDataSetChanged();
    }

    /**
     * 发送消息并加添内容到List
     */
    private void sendMsg() {

        boolean net = false;
        String content = eidtTextContent.getText().toString();
        if (null == content) {
            return;
        }
        if (content.isEmpty()) {
            return;
        }
        ChatMessage msg = new ChatMessage();
        msg.setContent(content);
        msg.setTime(getCurTime());
        msg.setFlag(EventUtil.SEND);
        msg.setType(EventUtil.TYPE_TEXT);

        if (!CommonUtil.isNetWorkConnected(this)){
            netErr.setVisibility(View.VISIBLE);
            msg.setNetErr(EventUtil.NET_ERR);
        }else{
            netErr.setVisibility(View.GONE);
            net = true;
        }
        chatMsgList.add(msg);
        chatAdapter.notifyDataSetChanged();
        eidtTextContent.setText("");

        sendText = getRegexText(content);
        if (net){
            canTuring = true;
            //执行发送操作,Vcrobot机器人
            new NetTask(VcrobotUtil.getRequestUrl(sendText), this).execute();

        }
        //对大量聊天信息清除处理
        removeMsgRecorder();
    }

    /**
     * 发送语音识别后的文字
     * @param srText
     */
    private void sendMsg(String srText){

        if (!CommonUtil.isNetWorkConnected(this)){
            netErr.setVisibility(View.VISIBLE);
            return;
        }else{
            netErr.setVisibility(View.GONE);
        }

        if (null == srText) {
            return;
        }
        if (srText.isEmpty()) {
            return;
        }

        sendText = getRegexText(srText);
        canTuring = true;
        new NetTask(VcrobotUtil.getRequestUrl(getRegexText(sendText)), this).execute();
        //对大量聊天信息清除处理
        removeMsgRecorder();
    }

    /**
     * 加载语音至list
     *
     * @param seconds 语音秒数
     * @param folder  语音路径
     */
    private void sendVoiceMsg(float seconds, String folder) {
        ChatMessage msg = new ChatMessage();
        msg.setSeconds(seconds);
        msg.setFolder(folder);
        msg.setTime(getCurTime());
        msg.setFlag(EventUtil.SEND);
        msg.setType(EventUtil.TYPE_VOICE);
        if (!CommonUtil.isNetWorkConnected(this)){
            netErr.setVisibility(View.VISIBLE);
            msg.setNetErr(EventUtil.NET_ERR);
        }else{
            netErr.setVisibility(View.GONE);
        }
        chatMsgList.add(msg);
        chatAdapter.notifyDataSetChanged();
        chatListView.setSelection(chatMsgList.size() - 1);
    }

    /**
     * 处理发送文本内容
     *
     * @param srcText
     * @return
     */
    private String getRegexText(String srcText) {
        if (null == srcText) {
            return null;
        }
        if (srcText.isEmpty()) {
            return "";
        }
        String regexText = null;

        // 去除开头和结尾的空白字符,转化换行为句号，空格为逗号
        regexText = srcText.trim().replaceAll("\n", ".").replaceAll(" ", ",");

        Log.i(TAG, regexText);
        return regexText;
    }

    /**
     * 移除冗余数据
     */
    private void removeMsgRecorder() {
        int count = chatMsgList.size();
        if (count < 130) {
            return;
        }
        for (int i = 0; i < 30; ++i) {
            chatMsgList.remove(i);
        }
    }

    /**
     * 3分钟后显示一次时间,或10条数据后显示
     *
     * @return
     */
    private String getCurTime() {
        curTime = System.currentTimeMillis();
        curSize = chatMsgList.size();
        oldSize = (curSize < oldSize) ? curSize : oldSize;
        if (curTime - oldTime >= 3 * 60 * 1000 || curSize - oldSize > 10) {
            oldTime = curTime;
            oldSize = curSize;
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Date curDate = new Date();
            return sdf.format(curDate);
        }
        return null;
    }

    /**
     * 内部类，录音完成通知刷新
     */
    private class AudioRecorderFinishLinster implements VoiceRecognizedBtn.AudioRecorderFinishLinster {
        @Override
        public void recorderFinish(float seconds, String folder) {

            sendVoiceMsg(seconds, folder);
        }
    }

    /**
     * 内部类，语音识别完成发送文字信息
     */
    private class VoiceRecognizedFinishLinster implements VoiceRecognizedBtn.VoiceRecognizedFinishLinster{
        @Override
        public void recognizedFinish(String srText) {
            sendMsg(srText);
        }
    }

    /**
     * 内部类，响应语音点击事件，播放语音
     */
    private class VoiceItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //评论，赞、踩
//            String c = chatMsgList.get(position).getRid();
//
//            view.findViewById(R.id.zan);
//            Log.i(TAG, "onItemClick: "+view.getId()+"----"+parent.getId()+"----"+c+"---"+position+"---"+id);
            //语音操作
            if (EventUtil.TYPE_VOICE != chatMsgList.get(position).getType()) {
                return;
            }
            if (null != animView) {
                animView.setBackgroundResource(R.mipmap.adj);
                animView = null;
            }
            //播放动画
            animView = view.findViewById(R.id.recorder_anim);
            animView.setBackgroundResource(R.drawable.play_anim);
            AnimationDrawable animDrawable = (AnimationDrawable) animView.getBackground();
            animDrawable.start();
            //播放帧频
            RecorderMediaManager.PlaySound(chatMsgList.get(position).getFolder(), new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    animView.setBackgroundResource(R.mipmap.adj);
                }
            });
        }
    }

    /**
     * 内部类，编辑框获焦事件
     */
    private class FocusChangeListener implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_active);
            } else {
                edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_normal);
            }
        }
    }

    /**
     * 内部类，编辑框状态改变事件
     */
    private class TextChangedListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!TextUtils.isEmpty(s)) {
                buttonSend.setEnabled(true);
                buttonSend.setClickable(true);
                buttonSend.setFocusable(true);
            } else {
                buttonSend.setEnabled(false);
                buttonSend.setClickable(false);
                buttonSend.setFocusable(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    /**
     * 显示语音图标按钮
     *
     * @param view
     */
    public void setModeVoice(View view) {
        hideKeyboard();
        edittext_layout.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
        buttonSetModeKeyboard.setVisibility(View.VISIBLE);
        buttonSend.setVisibility(View.GONE);
        buttonPressToSpeak.setVisibility(View.VISIBLE);
        iv_emoticons_normal.setVisibility(View.VISIBLE);
        iv_emoticons_checked.setVisibility(View.GONE);
        emojiIconContainer.setVisibility(View.GONE);
    }

    /**
     * 显示键盘图标
     *
     * @param view
     */
    public void setModeKeyboard(View view) {

        edittext_layout.setVisibility(View.VISIBLE);
        view.setVisibility(View.GONE);
        buttonSetModeVoice.setVisibility(View.VISIBLE);
        eidtTextContent.requestFocus();
        buttonPressToSpeak.setVisibility(View.GONE);
        buttonSend.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(eidtTextContent.getText())) {
            buttonSend.setEnabled(true);
            buttonSend.setClickable(true);
            buttonSend.setFocusable(true);
        } else {
            buttonSend.setEnabled(false);
            buttonSend.setClickable(false);
            buttonSend.setFocusable(false);
        }

    }

    /**
     * 显示表情图标
     */
    private void emojiIconShow() {
        iv_emoticons_normal.setVisibility(View.GONE);
        iv_emoticons_checked.setVisibility(View.VISIBLE);
        emojiIconContainer.setVisibility(View.VISIBLE);
        hideKeyboard();
    }

    /**
     * 隐藏表情栏
     */
    private void emojiIconHide() {
        iv_emoticons_normal.setVisibility(View.VISIBLE);
        iv_emoticons_checked.setVisibility(View.GONE);
        emojiIconContainer.setVisibility(View.GONE);
    }

    /**
     * 构造表情res
     *
     * @param getSum
     * @return
     */
    public List<String> getEmojiRes(int getSum) {
        List<String> emojiRES = new ArrayList<String>();
        for (int x = 1; x <= getSum; x++) {
            String rf = "ee_" + x;
            emojiRES.add(rf);
        }
        return emojiRES;
    }

    /**
     * 编辑框触发点击事件
     */
    private void editTextClicked() {
        edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_active);
        iv_emoticons_normal.setVisibility(View.VISIBLE);
        iv_emoticons_checked.setVisibility(View.INVISIBLE);
        emojiIconContainer.setVisibility(View.GONE);
    }

    /**
     * 分页，获取表情的gridview的子view
     *
     * @param i
     * @return
     */
    private View getGridChildView(int i) {
        View view = View.inflate(this, R.layout.gridview_emoji, null);
        ExpandGridView gv = (ExpandGridView) view.findViewById(R.id.gridview);
        List<String> emojiList = new ArrayList<String>();
        if (1 == i) {
            List<String> page1 = emojiRES.subList(0, 20);
            emojiList.addAll(page1);
        } else if (2 == i) {
            emojiList.addAll(emojiRES.subList(20, emojiRES.size()));
        }
        emojiList.add("delete_expression");
        emojiAdapter = new EmojiAdapter(this, 1, emojiList);
        gv.setAdapter(emojiAdapter);
        gv.setOnItemClickListener(new EmojiItemClickListener());
        return view;
    }

    /**
     * 内部类，响应Emoji点击事件
     */
    private class EmojiItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //防止NullPointExption
            String rf = (String) parent.getAdapter().getItem(position);
            try {
                // 文字输入框可见时，才可输入表情
                // 按住说话可见，不让输入表情
                if (buttonSetModeKeyboard.getVisibility() != View.VISIBLE) {

                    if (rf != "delete_expression") { // 不是删除键，显示表情
                        // 这里用的反射，所以混淆的时候不要混淆EmojiUtil这个类
                        @SuppressWarnings("rawtypes")
                        Class cls = Class.forName("com.virtualchatrobot.utils.EmojiUtil");
                        Field field = cls.getField(rf);
                        eidtTextContent.append(EmojiUtil.getEmojiText(MainActivity.this, (String) field.get(null)));
                    } else { // 删除文字或者表情
                        if (!TextUtils.isEmpty(eidtTextContent.getText())) {

                            int selectionStart = eidtTextContent.getSelectionStart();// 获取光标的位置
                            if (selectionStart > 0) {
                                String body = eidtTextContent.getText().toString();
                                String tempStr = body.substring(0, selectionStart);
                                int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
                                if (i != -1) {
                                    CharSequence cs = tempStr.substring(i, selectionStart);
                                    if (EmojiUtil.containsKey(cs.toString()))
                                        eidtTextContent.getEditableText().delete(i, selectionStart);
                                    else
                                        eidtTextContent.getEditableText().delete(selectionStart - 1, selectionStart);
                                } else {
                                    eidtTextContent.getEditableText().delete(selectionStart - 1, selectionStart);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 隐藏软键盘
     */
    private void hideKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 启动登陆界面
     */
    private void login(){
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        startActivityForResult(intent, EventUtil.MSG_LOGIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (EventUtil.MSG_LOGIN == requestCode && null != data){
            String name = data.getStringExtra("email");
            Log.i(TAG, "onActivityResult: "+name);
            email.setText(name);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 点击事件监听
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_set_mode_voice:
                setModeVoice(v);
                break;
            case R.id.btn_set_mode_keyboard:
                setModeKeyboard(v);
                break;
            case R.id.iv_emoticons_normal:
                emojiIconShow();
                break;
            case R.id.iv_emoticons_checked:
                emojiIconHide();
                break;
            case R.id.et_sendmessage:
                editTextClicked();
                break;
            case R.id.btn_send:
                sendMsg();
                break;
            case R.id.logo:
                login();
                break;
            case R.id.net_err:
                startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                break;
        }

    }

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case EventUtil.NET_ERR:
                    netErr.setVisibility(View.VISIBLE);
                    break;
                case EventUtil.NET_OK:
                    netErr.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 图灵机器人回调接口
     *
     * @param data
     */
    @Override
    public void getUrlData(String data) {
        Log.i(TAG, "getUrlData: " + data);
        canTuring = true;
        if (null != data && !data.equals("null")) {
            receiveMsg(data);
        }else if (canTuring){
            canTuring = false;
            //执行发送操作
            new NetTask(TuringUtil.getRequestUrl(sendText), this).execute();
        }
    }

    @Override
    protected void onResume() {
        RecorderMediaManager.resume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        RecorderMediaManager.pause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        RecorderMediaManager.release();
        netFlag = false;
        super.onDestroy();
    }

}
