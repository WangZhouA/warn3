package com.saiyi.ui.fragment;

import android.app.Fragment;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.saiyi.R;
import com.saiyi.http.ConstantInterface;
import com.saiyi.ui.activity.HomeActivity;
import com.saiyi.ui.activity.MyDevicesEmptyActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.saiyi.R.id.frag_reg_get;

public class FragmentRegisterActivity extends Fragment implements View.OnClickListener ,TextWatcher{

    private EditText edit_reg_num;
    private EditText edit_reg_pas;
    private EditText edit_reg_get;

    private Button frag_res;
    private Button res_get;
    private ImageButton reg_eyes;
    private  int flag = 0;
    String user_num;

    //定时器
    private CountDownTimer timer;

    Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    String text= (String) msg.obj;
                    startActivity(new Intent(getActivity(), MyDevicesEmptyActivity.class));
                    break;
                case 2:

                    break;
                case 3:
                    Toast.makeText(getActivity(), getResources().getString(R.string.Registration_failed),Toast.LENGTH_SHORT).show();
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View baseView = inflater.inflate(R.layout.fragment_register, container, false);

        edit_reg_num = (EditText) baseView.findViewById(R.id.fra_reg_num);
        edit_reg_get = (EditText) baseView.findViewById(R.id.fra_reg_get);
        edit_reg_pas = (EditText) baseView.findViewById(R.id.fra_reg_pas);
        frag_res = (Button) baseView.findViewById(R.id.reg_btn);
        res_get = (Button) baseView.findViewById(frag_reg_get);
        reg_eyes = (ImageButton) baseView.findViewById(R.id.frag_reg_eyes);

        frag_res.setOnClickListener(this);
        res_get.setOnClickListener(this);

        frag_res.setEnabled(false); //设置按钮不可按
        reg_eyes.setOnClickListener(this);

        //密文显示
        if (flag==0) {
            edit_reg_pas.setTransformationMethod(PasswordTransformationMethod.getInstance());
            flag=1;
        }
        edit_reg_num.addTextChangedListener(this);
        edit_reg_get.addTextChangedListener(this);
        edit_reg_pas.addTextChangedListener(this);



        timer=new CountDownTimer(60*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                res_get.setText(millisUntilFinished/1000+"s");
            }
            @Override
            public void onFinish() {
                res_get.setText(R.string.key_get);
                res_get.setEnabled(true);
            }
        };




        return baseView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case frag_reg_get:
                user_num=edit_reg_num.getText().toString();
                if (isMobile(user_num) == true) {
                    res_get.setEnabled(false);
                    timer.start();
                    okHttpGetCode();
                }
                break;

            case R.id.reg_btn:
                httpPost();

                break;

            case R.id.frag_reg_eyes:

                if (flag==1) {
                    edit_reg_pas.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    flag=0;
                }else {
                    edit_reg_pas.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    flag=1;
                }

                break;

        }
    }

    private void httpPost() {
//        {"phone":"18875902412","name":"张","pwd":"123456","code":"474557","email":"1048095346@qq.com","height":"168","weight":"55.55"}

        final String code = edit_reg_get.getText().toString();
        final String pwd = edit_reg_pas.getText().toString();
        final String phone = user_num;
        final String strurl = ConstantInterface.HTTP_SERVICE_ADDRESS_REGISTER;
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
//                             Log.i("--->","正确的注册信息"+response.body().string());
//                             startActivity(new Intent(getActivity(), MyDevicesEmptyActivity.class));
//                             getActivity().finish();
                            String strResult = response.body().string();
                            try {
                                JSONObject jsonObjs =  new JSONObject(strResult);
                                String s =jsonObjs.getString("result");
                                if (s.equals(1)||s.equals("1")){
                                    startActivity(new Intent(getActivity(), HomeActivity.class)
                                    .putExtra("userNum",phone).putExtra("pwd",pwd));

                                }else {

                                     Message msg =handler.obtainMessage();
                                     msg.what=3;
                                     handler.sendMessage(msg);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {
                            Log.i("--->","错误的注册信息"+response.body().string());
                        }
                    }
                });
            }
        }.start();
    }
//    private void httpGet() {
//        final String strurl= ConstantInterface.HTTP_SERVICE_ADDRESS+user_num;
//        new Thread (){
//            @Override
//            public void run() {
//                try {
//                    HttpUtils.okhttpGet(strurl);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//    }

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
        String num = "[1][3578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
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
        if (edit_reg_num.length()==11 && edit_reg_pas.length()>=6 ) {
            user_num =edit_reg_num.getText().toString().trim();
            if (isMobile(user_num) == true) {
                res_get.setEnabled(true);
                frag_res.setEnabled(true);
                frag_res.setBackgroundResource(R.drawable.def_login_btn1);
            }else {
                frag_res.setEnabled(false);
                frag_res.setBackgroundResource(R.drawable.def_login_btn);
            }
        }else {
            frag_res.setEnabled(false);
            frag_res.setBackgroundResource(R.drawable.def_login_btn);
        }
    }



    class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
            Log.d(ConstantInterface.TAG,String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            Log.d(ConstantInterface.TAG, String.format("Received response for %s in %.1fms%n%sconnection=%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers(), chain.connection()));

            return response;
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