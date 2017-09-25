package com.vcrobot.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.widget.ImageView;
import android.widget.TextView;

import com.vcrobot.bean.FaceInfo;

import org.json.JSONObject;

/**
 * Created by Dolphix.J Qing on 2016/5/23.
 */
public class DrawFaceUtil {

    /**
     * 绘制检测结果
     * @param b
     * @param face
     * @param info
     * @param rst
     */
    public static void doDraw(Bitmap b, ImageView face, TextView info, JSONObject rst){

        Bitmap tmp = Bitmap.createBitmap(b.getWidth(),b.getHeight(),b.getConfig());
        //设置红色画布
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        //设置空心
        paint.setStyle(Paint.Style.STROKE);
        //线条宽度
        paint.setStrokeWidth(Math.max(b.getWidth(), b.getHeight()) / 200f);
        Canvas canvas = new Canvas(tmp);
        canvas.drawBitmap(b, new Matrix(), null);

        try{
            //解析Json数据，获得人脸数据
            JSONObject jsonAttr = rst.getJSONArray("face").getJSONObject(0).getJSONObject("attribute");
            JSONObject jsonPos = rst.getJSONArray("face").getJSONObject(0).getJSONObject("position");
            FaceInfo.face_id = rst.getJSONArray("face").getJSONObject(0).getString("face_id");
            //---------------
            FaceInfo.age_value = (int)jsonAttr.getJSONObject("age").getInt("value");
            FaceInfo.gender_value = (String)jsonAttr.getJSONObject("gender").getString("value");
            FaceInfo.glass_value = (String)jsonAttr.getJSONObject("glass").getString("value");
            FaceInfo.race_value = (String)jsonAttr.getJSONObject("race").getString("value");
            FaceInfo.center_x = (float)jsonPos.getJSONObject("center").getDouble("x");
            FaceInfo.center_y = (float)jsonPos.getJSONObject("center").getDouble("y");
            FaceInfo.eye_left_x = (float)jsonPos.getJSONObject("eye_left").getDouble("x");
            FaceInfo.eye_left_y = (float)jsonPos.getJSONObject("eye_left").getDouble("y");
            FaceInfo.eye_right_x = (float)jsonPos.getJSONObject("eye_right").getDouble("x");
            FaceInfo.eye_right_y = (float)jsonPos.getJSONObject("eye_right").getDouble("y");
            FaceInfo.face_height = (float)jsonPos.getDouble("height");
            FaceInfo.face_width = (float)jsonPos.getDouble("width");
            FaceInfo.mouth_left_x = (float)jsonPos.getJSONObject("mouth_left").getDouble("x");
            FaceInfo.mouth_left_y = (float)jsonPos.getJSONObject("mouth_left").getDouble("y");
            FaceInfo.mouth_right_x = (float)jsonPos.getJSONObject("mouth_right").getDouble("x");
            FaceInfo.mouth_right_y = (float)jsonPos.getJSONObject("mouth_right").getDouble("y");
            FaceInfo.nose_x = (float)jsonPos.getJSONObject("nose").getDouble("x");
            FaceInfo.nose_y = (float)jsonPos.getJSONObject("nose").getDouble("y");
        }catch (Exception e){
            e.printStackTrace();
        }

        FaceInfo.changePercentToValue(b.getWidth(),b.getHeight());
        float w = FaceInfo.face_width;
        float h = FaceInfo.face_height;

        //绘制人脸框
        canvas.drawRect((FaceInfo.center_x-FaceInfo.face_width/2f),(FaceInfo.center_y-FaceInfo.face_height/2f),
                (FaceInfo.center_x+FaceInfo.face_width/2f),(FaceInfo.center_y+FaceInfo.face_height/2f),paint);
        paint.setColor(Color.GREEN);
        //绘制左眼
        RectF oval = new RectF(FaceInfo.eye_left_x-w/8f,FaceInfo.eye_left_y-h/20f,FaceInfo.eye_left_x+w/8f,FaceInfo.eye_left_y+h/20f);
        canvas.drawOval(oval,paint);
        //绘制右眼
        oval = new RectF(FaceInfo.eye_right_x-w/8f,FaceInfo.eye_right_y-h/20f,FaceInfo.eye_right_x+w/8f,FaceInfo.eye_right_y+h/20f);
        canvas.drawOval(oval,paint);
        paint.setColor(Color.BLUE);
        //绘制鼻子
        oval = new RectF(FaceInfo.nose_x-w/6f,FaceInfo.nose_y-h/6f,FaceInfo.nose_x+w/6f,FaceInfo.nose_y+h/8f);
        canvas.drawOval(oval,paint);
        paint.setColor(Color.YELLOW);
        //绘制嘴巴
//        oval = new RectF(FaceInfo.mouth_left_x,FaceInfo.mouth_left_y+30,FaceInfo.mouth_right_x,FaceInfo.mouth_left_y-30);
//        canvas.drawOval(oval,paint);
        canvas.drawLine(FaceInfo.mouth_left_x,FaceInfo.mouth_left_y,FaceInfo.mouth_right_x,FaceInfo.mouth_right_y,paint);
        b = tmp;
        //获取附加信息
        String data = "性别:"+FaceInfo.gender_value+", 年龄"+FaceInfo.age_value+", 眼镜:"+FaceInfo.glass_value+", 种族:"+FaceInfo.race_value;
        face.setImageBitmap(tmp);
        info.setText(data);
        //绘制文字
//        paint = new Paint( Paint.ANTI_ALIAS_FLAG);
//        paint.setColor(Color.WHITE);
//        paint.setTextSize(35);
//        String text = "性别:"+FaceInfo.gender_value+",年龄"+FaceInfo.age_value+",眼镜:"+FaceInfo.glass_value+"种族:"+FaceInfo.race_value;
//        float baseX = 30;
//        float baseY = curBitmap.getHeight()-150;
//        // 绘制文本
//        canvas.drawText( text, baseX, baseY, paint);

    }
}
