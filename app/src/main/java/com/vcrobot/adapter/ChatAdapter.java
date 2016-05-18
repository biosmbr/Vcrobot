package com.vcrobot.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vcrobot.R;
import com.vcrobot.bean.ChatMessage;
import com.vcrobot.utils.VCRConst;

import java.util.List;

/**
 * Created by Dolphix.J Qing on 2016/5/8.
 */
public class ChatAdapter extends BaseAdapter{
    private static final String TAG = "Dolphix.J Qing";
    private List<ChatMessage> cMsg;
    private RelativeLayout layout;
    private static LayoutInflater layoutInflater;
    private Context context;
    //控制语音背景图片长度
    private int minItemWidth;
    private int maxItemWidth;
//    private static View sendTextView;
//    private static View sendVoiceView;
//    private static View receiveTextView;

    public ChatAdapter(Context context, List<ChatMessage> msg) {
        this.cMsg = msg;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        calcWindowWidth();
//        initView();
    }

//    private static void initView(){
//        receiveTextView = (RelativeLayout) layoutInflater.inflate(R.layout.left_item,null);
//        sendTextView = (RelativeLayout) layoutInflater.inflate(R.layout.right_item,null);
//        sendVoiceView = (RelativeLayout) layoutInflater.inflate(R.layout.item_recorder,null);
//    }

    @Override
    public int getCount() {
        return cMsg.size();
    }

    @Override
    public Object getItem(int position) {
        return cMsg.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        ChatMessage msg = cMsg.get(position);
        if (/*null == convertView*/true){
            //1 加载布局
            if (VCRConst.RECEIVE == msg.getFlag()){
                if (VCRConst.TYPE_TEXT == msg.getType()){
                    convertView = (RelativeLayout) layoutInflater.inflate(R.layout.left_item,null);
                }
            }else if (VCRConst.SEND == msg.getFlag()){
                if (VCRConst.TYPE_TEXT == msg.getType()){
                    convertView = (RelativeLayout) layoutInflater.inflate(R.layout.right_item,null);
                }else if(VCRConst.TYPE_VOICE == msg.getType()){//加载语音布局
                    convertView = (RelativeLayout) layoutInflater.inflate(R.layout.item_recorder,null);
                }
            }
            //2 绑定控件
            viewHolder = new ViewHolder();
            //文本
            if (VCRConst.TYPE_TEXT == msg.getType()){
                viewHolder.context = (TextView)convertView.findViewById(R.id.tv_text);
            }
            //语音
            if(VCRConst.TYPE_VOICE == msg.getType()){
                viewHolder.seconds = (TextView) convertView.findViewById(R.id.recorder_time);
                viewHolder.length = (View)convertView.findViewById(R.id.recorder_length);
            }
            viewHolder.time = (TextView)convertView.findViewById(R.id.tv_time);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //3 设置文本信息
        if (VCRConst.TYPE_TEXT == msg.getType()){
            viewHolder.context.setText(msg.getContext());
        }
        //设置语音信息
        if(VCRConst.TYPE_VOICE == msg.getType()){
            viewHolder.seconds.setText(Math.round(msg.getSeconds())+"\"");
            ViewGroup.LayoutParams layoutParams = viewHolder.length.getLayoutParams();
            layoutParams.width = (int)(minItemWidth + (maxItemWidth / 60f * msg.getSeconds()));
        }

        viewHolder.time.setText(msg.getTime());

        return convertView;
    }

    private class ViewHolder{
        TextView context;
        TextView time;
        TextView seconds;
        View length;
    }

    /**
     * 计算窗口宽度
     */
    private void calcWindowWidth(){
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        maxItemWidth = (int)(displayMetrics.widthPixels * 0.7f);
        minItemWidth = (int)(displayMetrics.widthPixels * 0.2f);
    }
}



//    ViewHolder viewHolder = null;
//    ChatMessage msg = cMsg.get(position);
//if (null == convertView){
//        Log.i(TAG, "getView: come in");
//        //1 加载布局
//        if (VCRConst.RECEIVE == msg.getFlag()){
//        if (VCRConst.TYPE_TEXT == msg.getType()){
//        convertView = (RelativeLayout) layoutInflater.inflate(R.layout.left_item,null);
//        }
//        }else if (VCRConst.SEND == msg.getFlag()){
//        if (VCRConst.TYPE_TEXT == msg.getType()){
//        convertView = (RelativeLayout) layoutInflater.inflate(R.layout.right_item,null);
//        }else if(VCRConst.TYPE_VOICE == msg.getType()){//加载语音布局
//        Log.i(TAG,"Load Voice");
//        convertView = (RelativeLayout) layoutInflater.inflate(R.layout.item_recorder,null);
//        }
//        }
//        //2 绑定控件
//        viewHolder = new ViewHolder();
//        //文本
//        if (VCRConst.TYPE_TEXT == msg.getType()){
//        viewHolder.context = (TextView)convertView.findViewById(R.id.tv_text);
//        }
//        //语音
//        if(VCRConst.TYPE_VOICE == msg.getType()){
//        viewHolder.seconds = (TextView) convertView.findViewById(R.id.recorder_time);
//        viewHolder.length = (View)convertView.findViewById(R.id.recorder_length);
//        Log.i(TAG, "getView: "+viewHolder.seconds);
//        }
//        viewHolder.time = (TextView)convertView.findViewById(R.id.tv_time);
//
//        convertView.setTag(viewHolder);
//        }else{
//        viewHolder = (ViewHolder) convertView.getTag();
//        }
//        //3 设置文本信息
//        if (VCRConst.TYPE_TEXT == msg.getType()){
//        viewHolder.context.setText(msg.getContext());
//        }
//        //设置语音信息
//        if(VCRConst.TYPE_VOICE == msg.getType()){
//        Log.i(TAG, "getView: "+"viewHolder="+viewHolder+"viewHolder.seconds"+viewHolder.seconds+"msg="+msg+",seconds="+msg.getSeconds());
//        viewHolder.seconds.setText(Math.round(msg.getSeconds())+"\"");
//        ViewGroup.LayoutParams layoutParams = viewHolder.length.getLayoutParams();
//        layoutParams.width = (int)(minItemWidth + (maxItemWidth / 60f * msg.getSeconds()));
//        }
//
//        viewHolder.time.setText(msg.getTime());
//
//        return convertView;
