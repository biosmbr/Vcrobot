package com.vcrobot.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.vcrobot.R;

import java.util.List;

/**
 * Created by Dolphix.J Qing on 2016/5/9.
 */
public class EmojiAdapter extends ArrayAdapter<String>{

    public EmojiAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = View.inflate(getContext(), R.layout.row_emoji, null);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_emoji);

        String rf = getItem(position);
        int resId = getContext().getResources().getIdentifier(rf, "mipmap", getContext().getPackageName());
        imageView.setImageResource(resId);

        return convertView;
    }
}
