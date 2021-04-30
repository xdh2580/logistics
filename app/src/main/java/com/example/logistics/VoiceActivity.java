package com.example.logistics;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.iflytek.cloud.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class VoiceActivity extends AppCompatActivity {

    private static String TAG = "xdh";
    private SpeechRecognizer mIat;
    private String language = "zh_cn";
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;

    private String resultType = "plain";
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    private Toast mToast;
    private StringBuffer buffer = new StringBuffer();

    EditText mResultText;
    Button button;
    TextView text_show_result;

    String resultOfString;//识别结果（字符串）

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);
        SpeechUtility b = SpeechUtility.createUtility(this, "appid=47149c79");
        Log.d("xdh","utility创建完了");
        if(b==null){
            Log.d("xdh","是空的");
        }else{
            Log.d("xdh","没毛病");
        }
        mIat = SpeechRecognizer.createRecognizer(this, mInitListener);
        Log.d("xdh", "创建完了");
        if (mIat == null) {
            Log.d("xdh", "是空的");
        } else {
            Log.d("xdh", "没毛病");
        }
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        showTip("小语在！");
        mResultText = findViewById(R.id.text_result);
        text_show_result = findViewById(R.id.text_voice_show_result);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 设置参数
                setParam();
                int ret = 0;
                ret = mIat.startListening(mRecognizerListener);
                if (ret != ErrorCode.SUCCESS) {
                    Log.d("xdh", "听写失败,错误码：" + ret + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
                } else {
                    Log.d("xdh","showaa");
                            showTip("开始说话");

                }
            }
        });


    }
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d("xdh", "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
//                showTip("初始化失败，错误码：" + code+",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
                Log.d("xdh", "SpeechRecognizer fail");
            }
        }
    };
    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            showTip("开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。

            showTip(error.getPlainDescription(true));

        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            showTip("结束说话");
            Toast.makeText(VoiceActivity.this, "识别结束", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.d(TAG, results.getResultString());
//            System.out.println(flg++);
            if (resultType.equals("json")) {
                resultOfString = printAndReturnResult(results);

            } else if (resultType.equals("plain")) {
                buffer.append(results.getResultString());
                mResultText.setText(buffer.toString());
                showResultText(buffer.toString());//显示识别到的语音的相应回答
                mResultText.setSelection(mResultText.length());
            }

//            if (isLast & cyclic) {
//                // TODO 最后的结果
//                Message message = Message.obtain();
//                message.what = 0x001;
//                han.sendMessageDelayed(message,100);
//            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            showTip("当前正在说话，音量大小：" + volume);
            Log.d(TAG, "返回音频数据：" + data.length);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    private void showTip(String str) {
//        mToast = Toast.makeText(this,"", Toast.LENGTH_SHORT);
//        mToast.cancel();
        mToast.setText(str);
        Log.d("xdh0430","toshow"+str);
        mToast.show();
    }

    public void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, resultType);


        if (language.equals("zh_cn")) {
            String lag = "mandarin";
            Log.e("xdh", "language:" + language);// 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, lag);
        } else {

            mIat.setParameter(SpeechConstant.LANGUAGE, language);
        }
        Log.e("xdh", "last language:" + mIat.getParameter(SpeechConstant.LANGUAGE));

        //此处用于设置dialog中不显示错误码信息
        //mIat.setParameter("view_tips_plain","false");

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "1");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
    }

    private String printAndReturnResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

        mResultText.setText(resultBuffer.toString());
        mResultText.setSelection(mResultText.length());
        return resultBuffer.toString();
    }

    private void showResultText(String recoResult) {
//        String answer = Utils.answer(VoiceActivity.this,"牛奶在哪");
        Log.d("xdh","已经到showResult函数");

        String answer = Utils.answer(VoiceActivity.this,recoResult);
        text_show_result.setText(answer);
    }


}