package com.example.logistics;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import com.iflytek.cloud.*;
import com.iflytek.cloud.msc.util.FileUtil;
import com.iflytek.cloud.msc.util.log.DebugLog;

import java.io.IOException;

public class Utils {
    static String currentLoginUserName;//用来记录当前登录的用户
    private static MyDBOpenHelper myDBOpenHelper;
    private static SQLiteDatabase db;

    // 语音合成对象
    private static  SpeechSynthesizer mTts;
    // 默认发音人
    private static  String voicer = "xiaoyan";
    // 引擎类型
    private static  String mEngineType = SpeechConstant.TYPE_CLOUD;
    /**
     * 初始化监听。
     */
    private static InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d("xdh0501", "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                Log.d("xdh0501", "InitListener fail " + code);
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };


    public static String answer(Context context,String recoResult){
        myDBOpenHelper = new MyDBOpenHelper(context,null,1);
        db = myDBOpenHelper.getWritableDatabase();


        String answer=null;
        if(recoResult.contains("在哪")){
            int l =  recoResult.indexOf("在哪");
            String name = recoResult.substring(0,l);
            boolean e = checkGoodsName(name);
            if(!e) {
                answer = "错误，没有找到该货品";
            }else{
                Log.d("xdh", "即将开始查询");
                Cursor cursor = db.rawQuery("select * from goods where name=?", new String[]{name});
                cursor.moveToFirst();
                int shelve = cursor.getInt(cursor.getColumnIndex("shelve"));
                int layer = cursor.getInt(cursor.getColumnIndex("layer"));
                answer = name + "在货架" + String.valueOf(shelve) + "隔层" + String.valueOf(layer);
            }
        }
        if(recoResult.contains("库存")||recoResult.contains("还有多少")||recoResult.contains("还剩多少")){
            int l = 0;
            if(recoResult.contains("库存")) {
                l = recoResult.indexOf("库存");
            }else if (recoResult.contains("还有多少")){
                l = recoResult.indexOf("还有多少");
            }else if(recoResult.contains("还剩多少")){
                l = recoResult.indexOf("还剩多少");
            }
            String name = recoResult.substring(0,l);
            boolean e = checkGoodsName(name);
            if(!e){
                answer = "错误，没有找到该货品";
            }else {
                Cursor cursor = db.rawQuery("select * from goods where name=?", new String[]{name});
                cursor.moveToFirst();
                int quantity = cursor.getInt(cursor.getColumnIndex("quantity"));
                answer = name + "库存剩余" + String.valueOf(quantity);
            }
        }
        return answer;
    }

    private static boolean checkGoodsName(String name) {
        Cursor cursor0 = db.rawQuery("select * from goods",new String[]{});
        cursor0.moveToFirst();
        boolean e = false;
        do{
            String na = cursor0.getString(cursor0.getColumnIndex("name"));
            if((na.equals(name))){
                e=true;
                Log.d("xdh0430","success");
            }
        }while (cursor0.moveToNext());
        Log.d("xdh0430","not exit");
        return  e;
    }

    //播报（语音合成）
    public static void playAnswer(Context context,String text){
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(context, mTtsInitListener);
        setParam();
        int code = mTts.startSpeaking(text, mTtsListener);
        if (code != ErrorCode.SUCCESS) {
          Log.d("xdh0501","合成和失败，错误码："+code);
        }
    }


    /**
     * 参数设置
     * @return
     */
    private static void setParam(){
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        if(mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            //支持实时音频返回，仅在synthesizeToUri条件下支持
            mTts.setParameter(SpeechConstant.TTS_DATA_NOTIFY, "1");
            //	mTts.setParameter(SpeechConstant.TTS_BUFFER_TIME,"1");

            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
            //设置合成语速
            mTts.setParameter(SpeechConstant.SPEED,"50");
            //设置合成音调
            mTts.setParameter(SpeechConstant.PITCH,"50");
            //设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME,"50");
        }else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            mTts.setParameter(SpeechConstant.VOICE_NAME, "");

        }

        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE,"3");
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "false");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "pcm");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/tts.pcm");
    }
    /**
     * 合成回调监听。
     */
    private static SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
//            showTip("开始播放");
        }

        @Override
        public void onSpeakPaused() {
//            showTip("暂停播放");
        }

        @Override
        public void onSpeakResumed() {
//            showTip("继续播放");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
//            // 合成进度
//            Log.e("MscSpeechLog_", "percent =" + percent);
//            mPercentForBuffering = percent;
//            showTip(String.format(getString(R.string.tts_toast_format),
//                    mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
//            Log.e("MscSpeechLog_", "percent =" + percent);
//            mPercentForPlaying = percent;
//            showTip(String.format(getString(R.string.tts_toast_format),
//                    mPercentForBuffering, mPercentForPlaying));
//
//            SpannableStringBuilder style=new SpannableStringBuilder(texts);
//            Log.e(TAG,"beginPos = "+beginPos +"  endPos = "+endPos);
//            style.setSpan(new BackgroundColorSpan(Color.RED),beginPos,endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            ((EditText) findViewById(R.id.tts_text)).setText(style);
        }

        @Override
        public void onCompleted(SpeechError error) {
//            System.out.println("oncompleted");
//            if (error == null) {
//                //	showTip("播放完成");
//                DebugLog.LogD("播放完成,"+container.size());
//                try {
//                    for(int i=0;i<container.size();i++) {
//                        writeToFile(container.get(i));
//                    }
//                }catch (IOException e) {
//
//                }
//                FileUtil.saveFile(memFile,mTotalSize,Environment.getExternalStorageDirectory()+"/1.pcm");
//
//
//            } else if (error != null) {
//                showTip(error.getPlainDescription(true));
//            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
//            //	 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
//            //	 若使用本地能力，会话id为null
//            if (SpeechEvent.EVENT_SESSION_ID == eventType) {
//                String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
//                Log.d(TAG, "session id =" + sid);
//            }
//
//            //当设置SpeechConstant.TTS_DATA_NOTIFY为1时，抛出buf数据
//            if (SpeechEvent.EVENT_TTS_BUFFER == eventType) {
//                byte[] buf = obj.getByteArray(SpeechEvent.KEY_EVENT_TTS_BUFFER);
//                Log.e("MscSpeechLog_", "bufis =" + buf.length);
//                container.add(buf);
//            }


        }
    };
}
