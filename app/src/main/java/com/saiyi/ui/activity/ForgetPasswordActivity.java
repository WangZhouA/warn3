package com.saiyi.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.saiyi.R;
import com.saiyi.application.MyApplication;
import com.saiyi.http.ConstantInterface;
import com.saiyi.utils.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ForgetPasswordActivity extends BasActivity implements View.OnClickListener,TextWatcher {


    private EditText edit_forget_num;
    private EditText edit_forget_pas;
    private EditText edit_forget_get;

    private Button forget_sure;
    private Button forget_get;

    private ImageButton forget_eyes;
    private ImageButton header_left;
    private ImageButton header_right;

    private TextView header_text;
    String user_num;

    private  int flag=0;

    private UserInfo userInfo =new UserInfo(ForgetPasswordActivity.this);
    //定时器
    private CountDownTimer timer;

    Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:

                    break;
                case 2:
                    forget_get.setText(R.string.have_send);
                    break;
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pas);
        MyApplication.getInstance().addActivity(this);
        edit_forget_num = (EditText)findViewById(R.id.forget_pas_num);
        edit_forget_get = (EditText)findViewById(R.id.forget_pas_key_get);
        edit_forget_pas = (EditText)findViewById(R.id.forget_pas_pas);
        forget_sure = (Button)findViewById(R.id.forget_pas_sure);
        forget_get = (Button)findViewById(R.id.forget_pas_get);
        forget_eyes = (ImageButton)findViewById(R.id.forget_pas_eyes);
        header_left = (ImageButton)findViewById(R.id.header_left);
        header_right = (ImageButton)findViewById(R.id.header_right);
        header_text = (TextView)findViewById(R.id.header_text);

        forget_get.setOnClickListener(this);
        forget_eyes.setOnClickListener(this);
        forget_sure.setOnClickListener(this);
        header_left.setOnClickListener(this);

        header_text.setText(R.string.to_re);
        header_right.setVisibility(View.GONE);
        header_left.setImageResource(R.drawable.ic_back);
        forget_sure.setEnabled(false); //设置按钮不可按


        edit_forget_num.addTextChangedListener(this);
        edit_forget_get.addTextChangedListener(this);
        edit_forget_pas.addTextChangedListener(this);


        if (flag==0) {
            edit_forget_pas.setTransformationMethod(PasswordTransformationMethod.getInstance());
            flag=1;
        }


        timer=new CountDownTimer(60*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                forget_get.setText(millisUntilFinished/1000+"s");
            }
            @Override
            public void onFinish() {
                forget_get.setText(R.string.key_get);
                forget_get.setEnabled(true);
            }
        };



    }


    @Override
    protected void onResume() {
        if (userInfo.getStringInfo("user_name")!=null) {
            edit_forget_num.setText(userInfo.getStringInfo("user_name"));
        }
        super.onResume();
    }

    @Override
    public void onClick(View v){

        switch (v.getId())
        {
            case R.id.forget_pas_get:
                user_num=edit_forget_num.getText().toString();
                if (isMobile(user_num) == true) {
                    forget_get.setEnabled(true);
                    timer.start();
                    okHttpGetCode();
                }

                break;

            case R.id.forget_pas_eyes:

                if (flag==1) {
                    edit_forget_pas.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    flag=0;
                }else {
                    edit_forget_pas.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    flag=1;
                }

                break;

            case R.id.forget_pas_sure:
                httpPost();

//                startActivity(new Intent(ForgetPasswordActivity.this, MyDevicesEmptyActivity.class));
                break;

            case R.id.header_left:
                //  startActivity(new Intent(ForgetPasswordActivity.this, FragmentLoginActivity.class));
                finish();
                break;
        }
    }

    private void httpPost() {

        final String code = edit_forget_get.getText().toString();
        final String pwd = edit_forget_pas.getText().toString();
        final String phone = user_num;
        final String strurl = ConstantInterface.HTTP_SERVICE_ADDRESS_FORGOT_PASSWORD;
        new Thread() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();

                JSONObject jsonObject =new JSONObject();
                try {
                    jsonObject.put("phone",phone);
                    jsonObject.put("pwd",pwd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                RequestBody body =RequestBody.create(JSON,jsonObject.toString());

                final Request request =new Request.Builder()
                        .url(strurl)
                        .post(body)
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i("--->",""+e.toString());
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()){
//                            Log.i("--->","正确的找回信息"+response.body().string());
//                            startActivity(new Intent(ForgetPasswordActivity.this, MyDevicesEmptyActivity.class));
//                            finish();

                            String strResult = response.body().string();
                            Log.i("--->","正确的登录信息"+strResult);
                            try {
                                JSONObject jsonObjs =  new JSONObject(strResult);
                                String s =jsonObjs.getString("result");
                                if (s.equals(1)||s.equals("1")){
                                    Intent intent = new Intent(ForgetPasswordActivity.this, HomeActivity.class);
                                    Bundle bundle = new Bundle();
                                    intent.putExtras(bundle);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("start", "1");
                                    intent.putExtra("userNum",phone).putExtra("pwd",pwd);
                                    startActivity(intent);
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {
                            Log.i("--->","错误的找回信息"+response.body().string());
                        }
                    }
                });
            }
        }.start();
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobile(String number) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String num = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(number)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return number.matches(num);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        //1.同时监听俩个输入框，监听长度
        if (edit_forget_num.length()==11 && edit_forget_pas.length()>=6 ) {
            user_num =edit_forget_num.getText().toString().trim();
            if  (isMobile(user_num) == true) {
                forget_sure.setEnabled(true);
                forget_get.setEnabled(true);
                forget_sure.setBackgroundResource(R.drawable.def_login_btn1);
            }else {
                forget_sure.setEnabled(false);
                forget_sure.setBackgroundResource(R.drawable.def_login_btn);
            }
        }else {
            forget_sure.setEnabled(false);
            forget_sure.setBackgroundResource(R.drawable.def_login_btn);
        }
    }



    private void okHttpGetCode(){

        final  String  strurl=ConstantInterface.HTTP_SERVICE_ADDRESS+user_num;
        //创建网络处理的对象
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        final Request request = new Request.Builder().url(strurl).build();
        //call就是我们可以执行的请求类
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //失败
                Log.e(ConstantInterface.TAG,"get--->"+Thread.currentThread().getName() + "结果  " + e.toString());

            }
            @Override
            public void onResponse(Call call, Response response   ) throws IOException {
                String  result =response.body().string();
                try {
                    JSONObject  object = new JSONObject(result);
                    String flag = object.getString("result");
                    if (flag.equals(1) || flag.equals("1")) {
                        Message message =handler.obtainMessage();
                        message.what=2;
                        handler.sendMessage(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}

