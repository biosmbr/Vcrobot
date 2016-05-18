package com.vcrobot.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

/**
 * Created by Dolphix.J Qing on 2016/5/9.
 * 用于支持复制粘贴操作
 */
public class ClipEidtText extends EditText{

    private static final String TAG = "Dolphix.J Qing";

    public ClipEidtText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
    }

    @Override
    public boolean onTextContextMenuItem(int id) {
        Log.i(TAG,"onTextContextMenuItem");
        return super.onTextContextMenuItem(id);
    }
}
