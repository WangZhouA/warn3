package com.saiyi.ui.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputType;
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
import com.saiyi.ui.activity.ForgetPasswordActivity;
import com.saiyi.ui.activity.HomeActivity;
import com.saiyi.ui.activity.MyDevicesEmptyActivity;
import com.saiyi.utils.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.TELEPHONY_SERVICE;

public class FragmentLoginActivity extends Fragment implements View.OnClickListener, TextWatcher {

    private EditText edit_login_num;
    private EditText edit_login_pas;

    private ImageButton login_eyes;

    private Button login_fro_btn;
    private Button fra_login;

    private int flag = 0;
    String user_num;
    String user_pas;



    private static String user_number;


    private UserInfo userInfo;
    private static final String USER_NAME = "user_name";
    private static final String PASSWORD = "password";
    private static final String ISSAVEPASS = "savePassWord";
    private static final String AUTOLOGIN = "autoLogin";


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case 1:

                    Toast.makeText(getActivity(), getResources().getString(R.string.Please_check_whether_the_account_password_is_correct), Toast.LENGTH_SHORT).show();

                    break;

                case 0:




                    break;
            }
        }
    };


    public FragmentLoginActivity() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View baseView = inflater.inflate(R.layout.fragment_login, container, false);

        edit_login_num = (EditText) baseView.findViewById(R.id.fra_login_num);
        edit_login_pas = (EditText) baseView.findViewById(R.id.fra_login_pas);
        login_eyes = (ImageButton) baseView.findViewById(R.id.frag_login_eyes);
        login_fro_btn = (Button) baseView.findViewById(R.id.fra_fro_btn);
        fra_login = (Button) baseView.findViewById(R.id.login_btn);


        login_eyes.setOnClickListener(this);
        login_fro_btn.setOnClickListener(this);
        fra_login.setOnClickListener(this);
        edit_login_num.addTextChangedListener(this);
        edit_login_pas.addTextChangedListener(this);
        edit_login_num.setInputType(InputType.TYPE_CLASS_PHONE);// 限制输入的类型为电话

        //密文显示
        if (flag == 0) {
            edit_login_pas.setTransformationMethod(PasswordTransformationMethod.getInstance());
            flag = 1;
        }
        userInfo = new UserInfo(getActivity());


        if (userInfo.getBooleanInfo(ISSAVEPASS)) {
            edit_login_num.setText(userInfo.getStringInfo(USER_NAME));
            edit_login_pas.setText(userInfo.getStringInfo(PASSWORD));
        }

//        // 默认是要自动登陆的
//
//        //默认是自动登录的
//        if (HomeActivity.getflag()==null) {
//            if (userInfo.getBooleanInfo(AUTOLOGIN)) {
//                startActivity(new Intent(getActivity(), MyDevicesEmptyActivity.class));
//                getActivity().finish();
//            }
//
//        }else {
//            //这个是账号登不同设备了,需要重登
//
//        }
        if (HomeActivity.getUserNumToRegister() != null && HomeActivity.getUserPasToRegister() != null) {
            edit_login_num.setText(HomeActivity.getUserNumToRegister());
            edit_login_pas.setText(HomeActivity.getUserPasToRegister());
        }


        return baseView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.frag_login_eyes:
                if (flag == 1) {
                    edit_login_pas.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    flag = 0;
                } else {
                    edit_login_pas.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    flag = 1;
                }
                break;

            case R.id.fra_fro_btn:
                startActivity(new Intent(getActivity(), ForgetPasswordActivity.class));
//                startActivity(new Intent(getActivity(), BaojingActvivity.class));

                break;


            case R.id.login_btn:
                //先判断有没有网络
                if (TureOrFalseNetwork() == true) {
                    final String strurl = ConstantInterface.HTTP_SERVICE_ADDRESS_LOGIN;
                    OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("phone", user_num);
                        jsonObject.put("pwd", user_pas);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    RequestBody body = RequestBody.create(JSON, jsonObject.toString());

                    final Request request = new Request.Builder()
                            .url(strurl)
                            .post(body)
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.i("--->", "" + e.toString());
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
//                                    Log.i("--->","正确的登录信息"+response.body().string());
                                String strResult = response.body().string();
                                try {
                                    JSONObject jsonObjs = new JSONObject(strResult);
                                    String s = jsonObjs.getString("result");
                                    if (s.equals(1) || s.equals("1")) {
                                        userInfo.setUserInfo(USER_NAME, user_num);
                                        userInfo.setUserInfo(PASSWORD, user_pas);
                                        userInfo.setUserInfo(ISSAVEPASS, true);
                                        userInfo.setUserInfo(AUTOLOGIN, true);
                                        okHttpGet_ViewingUserInformation(user_num);

                                        JPushInterface.setAlias(getActivity(),user_num ,new TagAliasCallback() {
                                            @Override
                                            public void gotResult(int requestCode, String s, Set<String> set) {
                                                int  requestCodeA= requestCode;
                                                Log.e("requestCodeA---------->", "====" + requestCodeA);
                                                switch (requestCode) {
                                                    case 0:
                                                        Log.e("别名设置成功---------------->", "====" + s);
                                                        break;
                                                    case 6002:
                                                        Log.e("别名设置失败---------------->", "====" + s);

                                                        break;
                                                }
                                            }
                                        });

//                                        JPushManager.getInstance().setAlias();

                                        startActivity(new Intent(getActivity(), MyDevicesEmptyActivity.class));
                                        getActivity().finish();

                                    } else {
                                        Message msg = handler.obtainMessage();
                                        msg.what = 1;
                                        handler.sendMessage(msg);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            } else {
                                Log.i("--->", "错误的登录信息" + response.body().string());
                            }
                        }
                    });
                } else {

                    Toast.makeText(getActivity(), getResources().getString(R.string.No_Internet_connection), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    private void okHttpGet_ViewingUserInformation(String user_number ) throws IOException {
        final String strurl = ConstantInterface.HTTP_SERVICE_ADDRESS_VIEWING_USER_INFORMATION + user_number;

        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        Request request = new Request.Builder().url(strurl).build();
        //call就是我们可以执行的请求类
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //失败
                Log.e(ConstantInterface.TAG, "post--->" + Thread.currentThread().getName() + "结果  " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                       Log.e(ConstantInterface.TAG,"get--->"+Thread.currentThread().getName() + "结果  " + response.body().string());
                String result = response.body().string();
                try {
                    JSONObject object = new JSONObject(result);
                    JSONObject object1 = object.getJSONObject("data");
                    userInfo.setUserInfo("userId", object1.getString("id"));
                    Log.e("----->userId",object1.getString("id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }





    @Override
    public void onHiddenChanged(boolean hidden) {

        if (hidden) {

        } else {

        }
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
        if (edit_login_num.length() == 11 && edit_login_pas.length() >= 6) {
            Log.e("---edit_login_pas--->", "" + edit_login_pas.getText().toString());
            user_num = edit_login_num.getText().toString().trim();
            user_number = user_num;
            user_pas = edit_login_pas.getText().toString();
            if (isMobile(user_num) == true) {
                fra_login.setEnabled(true);
                fra_login.setBackgroundResource(R.drawable.def_login_btn1);
            } else {
                fra_login.setEnabled(false);
                fra_login.setBackgroundResource(R.drawable.def_login_btn);
            }
        } else {
            fra_login.setEnabled(false);
            fra_login.setBackgroundResource(R.drawable.def_login_btn);
        }
    }


    public static String getUser_number() {
        return user_number;
    }


    //判断是否有网络连接
    private boolean TureOrFalseNetwork() {

        ConnectivityManager mConnectivity = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mTelephony = (TelephonyManager) getActivity().getSystemService(TELEPHONY_SERVICE);
        //检查网络连接
        NetworkInfo info = mConnectivity.getActiveNetworkInfo();
        if (info == null || !mConnectivity.getBackgroundDataSetting()) {
            return false;
        }
        return true;
    }



}


