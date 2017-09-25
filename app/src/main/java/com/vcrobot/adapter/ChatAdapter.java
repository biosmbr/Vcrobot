package com.vcrobot.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vcrobot.R;
import com.vcrobot.bean.ChatMessage;
import com.vcrobot.utils.EventUtil;
import com.vcrobot.utils.NetTask;
import com.vcrobot.utils.VcrobotUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Dolphix.J Qing on 2016/5/8.
 */
public class ChatAdapter extends BaseAdapter {
    private static final String TAG = "Dolphix.J Qing";
    private List<ChatMessage> cMsg;
    private RelativeLayout layout;
    private static LayoutInflater layoutInflater;
    private Context context;
    //控制语音背景图片长度
    private int minItemWidth;
    private int maxItemWidth;
    private boolean canPush = true;
    private HashSet<Integer> pos;
//    private static View sendTextView;
//    private static View sendVoiceView;
//    private static View receiveTextView;

    public ChatAdapter(Context context, List<ChatMessage> msg) {
        this.cMsg = msg;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        calcWindowWidth();
        pos = new HashSet<Integer>();
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        final ChatMessage msg =  cMsg.get(position);
        if (/*null == convertView*/true){
            //1 加载布局
            if (EventUtil.RECEIVE == msg.getFlag()){
                if (EventUtil.TYPE_TEXT == msg.getType()){
                    convertView = (RelativeLayout) layoutInflater.inflate(R.layout.left_item,null);
                }
            }else if (EventUtil.SEND == msg.getFlag()){
                if (EventUtil.TYPE_TEXT == msg.getType()){
                    convertView = (RelativeLayout) layoutInflater.inflate(R.layout.right_item,null);
                }else if(EventUtil.TYPE_VOICE == msg.getType()){//加载语音布局
                    convertView = (RelativeLayout) layoutInflater.inflate(R.layout.item_recorder,null);
                }
            }
            //2 绑定控件
            viewHolder = new ViewHolder();

            //文本
            if (EventUtil.TYPE_TEXT == msg.getType()){
                viewHolder.context = (TextView)convertView.findViewById(R.id.tv_text);
            }
            //语音
            if(EventUtil.TYPE_VOICE == msg.getType()){
                viewHolder.seconds = (TextView) convertView.findViewById(R.id.recorder_time);
                viewHolder.length = (View)convertView.findViewById(R.id.recorder_length);
            }
            viewHolder.time = (TextView)convertView.findViewById(R.id.tv_time);
            //网络
            if (EventUtil.NET_ERR == msg.getNetErr()){
                viewHolder.warning = (ImageView)convertView.findViewById(R.id.warning);
            }
            final ViewHolder vh = viewHolder;
            canPush = true;
            //评论，赞、踩
            if (EventUtil.RECEIVE == msg.getFlag()){
                viewHolder.eval = (TextView)convertView.findViewById(R.id.eval);
                viewHolder.zan = (ImageView) convertView.findViewById(R.id.zan);
                viewHolder.cai = (ImageView)convertView.findViewById(R.id.cai);
                if (pos.contains(position)){
                    viewHolder.eval.setText("您已评价!");
                    viewHolder.zan.setEnabled(false);
                    viewHolder.cai.setEnabled(false);
                }else{
                    viewHolder.zan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pos.add(position);
                            if (canPush) {
                                canPush = false;
                                final String rid = cMsg.get(position).getRid();
                                if (rid == null || rid.equals("10001")){
                                    vh.eval.setText("该条记录，无法评价.");
                                    return;
                                }
                                vh.eval.setText("已赞! 感谢您的评价.");

                                if (rid != null){
                                    new NetTask(VcrobotUtil.getQAUrl(rid,1), (NetTask.HttpGetListenerr) context).execute();
                                }
                                Log.i(TAG, "onClick: " + msg.getRid() + "-----" + msg.getContext());
                            }
                        }
                    });
                    viewHolder.cai.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pos.add(position);
                            if (canPush) {
                                canPush = false;
                                final String rid = cMsg.get(position).getRid();
                                if (rid == null || rid.equals("10001")){
                                    vh.eval.setText("该条记录，无法评价.");
                                    return;
                                }
                                vh.eval.setText("已踩~ 感谢您的评价.");
                                if (rid != null){
                                    new NetTask(VcrobotUtil.getQAUrl(rid,0), (NetTask.HttpGetListenerr) context).execute();
                                }
                                Log.i(TAG, "onClick: " + msg.getRid() + "-----" + msg.getContext());
                            }
                        }
                    });
                }
            }


            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //3 设置文本信息
        if (EventUtil.TYPE_TEXT == msg.getType()){
            viewHolder.context.setText(msg.getContext());
        }
        //设置语音信息
        if(EventUtil.TYPE_VOICE == msg.getType()){
            viewHolder.seconds.setText(Math.round(msg.getSeconds())+"\"");
            ViewGroup.LayoutParams layoutParams = viewHolder.length.getLayoutParams();
            layoutParams.width = (int)(minItemWidth + (maxItemWidth / 60f * msg.getSeconds()));
        }

        //设置网络
        if (EventUtil.NET_ERR == msg.getNetErr()){
            viewHolder.warning.setVisibility(View.VISIBLE);
        }
        viewHolder.time.setText(msg.getTime());

        return convertView;
    }

    private class ViewHolder{
        TextView context;
        TextView time;
        TextView seconds;
        View length;
        ImageView warning;
        TextView eval;
        ImageView zan;
        ImageView cai;
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

    /**
     * 发送评价
     * @param rid
     * @param eval
     */
    private void sendEval(final String rid,final int eval){
        Log.i(TAG, "run1: "+rid+"---"+eval);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "run2: "+rid+"---"+eval);
                if (rid == null){
                    return;
                }

                new NetTask(VcrobotUtil.getQAUrl(rid,eval), (NetTask.HttpGetListenerr) context).execute();
            }
        });
    }


//    public interface EvalClickCallback{
//
//        void onEvalClick();
//    }

}
