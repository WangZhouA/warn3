package com.saiyi.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.saiyi.R;
import com.saiyi.application.MyApplication;
import com.saiyi.http.ConstantInterface;
import com.saiyi.ui.fragment.FragmentLoginActivity;
import com.saiyi.ui.view.CircleImageView;
import com.saiyi.ui.view.MyDialog;
import com.saiyi.utils.UserInfo;
import com.saiyi.utils.UserInfos;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/3/24.
 */

public class PersonalActivity extends BasActivity implements View.OnClickListener{

    private Button personal_back;
    private CircleImageView personal_head;
    private TextView personal_name;
    private LinearLayout personal_btn1;
    private LinearLayout personal_btn2;
    private LinearLayout personal_btn3;
    private Button personal_quit;
//  ic_head_1


    UserInfos us= new UserInfos(PersonalActivity.this);
    private String  user_number;

    private String  jsonName;
    private String  jsonPictureName;



    //    personal_head_portrait
    private Handler handler=new Handler(){

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case 1:
                    if (jsonName!=null){
                        personal_name.setText(jsonName);
                    }
                    if (jsonPictureName!=null){
                        String  strurl=ConstantInterface.HTTP_SERVICE_HOST+jsonPictureName;
//                        Glide.with(PersonalActivity.this).load(strurl).into(personal_head);
                        ConstantInterface.showImage(PersonalActivity.this,strurl,R.drawable.personal_head_portrait,R.drawable.personal_head_portrait,personal_head);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        MyApplication.getInstance().addActivity(this);
        personal_back = (Button)findViewById(R.id.personal_back);
        personal_head = (CircleImageView) findViewById(R.id.personal_head);
        personal_name = (TextView)findViewById(R.id.personal_name);
        personal_btn1 = (LinearLayout) findViewById(R.id.ReNameLinout);
        personal_btn2 = (LinearLayout) findViewById(R.id.helpLinout);
        personal_btn3 = (LinearLayout) findViewById(R.id.aboutLinout);
        personal_quit = (Button)findViewById(R.id.personal_quit);

        personal_back.setOnClickListener(this);
        personal_head.setOnClickListener(this);
        personal_btn1.setOnClickListener(this);
        personal_btn2.setOnClickListener(this);
        personal_btn3.setOnClickListener(this);
        personal_quit.setOnClickListener(this);

        personal_quit.setBackgroundResource(R.drawable.def_login_btn1);



        //拿到登录的账号用来去获取useId
        if (FragmentLoginActivity.getUser_number()!=null){
            user_number=FragmentLoginActivity.getUser_number();
            Log.i("--->user_number",""+user_number);
        }


        getBitmapFromSharedPreferences();

    }

    @Override
    protected void onResume() {
        if (us.getStringInfo("ReName")!=null){
            personal_name.setText(us.getStringInfo("ReName"));
        }
        getBitmapFromSharedPreferences();


        okHttpQueryUserMessage();


        super.onResume();
    }

    private void okHttpQueryUserMessage() {

        final  String strurl=ConstantInterface.HTTP_SERVICE_ADDRESS_QUERY_USER_MESSAGE+user_number;

        //创建网络处理的对象
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        Request request = new Request.Builder().url(strurl).build();
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
                    JSONObject jsonObject =new JSONObject(result);
                    JSONObject json=jsonObject.getJSONObject("data");

                    jsonName= json.getString("name");
                    Log.i("--->jsonName",jsonName);

                    jsonPictureName=json.getString("headimg");
                    Log.i("--->jsonPictureName",jsonPictureName);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Message mesg =new  Message();
                mesg.what=1;
                handler.sendMessage(mesg);


                Log.i("--->",result);

            }
        });



    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.personal_back:
                startActivity(new Intent(PersonalActivity.this,MyDevicesEmptyActivity.class));
                break;
            case R.id.personal_head:
                startActivity(new Intent(PersonalActivity.this,PersonalSetActivity.class));
                break;
            case R.id.ReNameLinout:
                startActivity(new Intent(PersonalActivity.this,ForgetPasswordActivity.class));
                break;
            case R.id.helpLinout:
                startActivity(new Intent(PersonalActivity.this,HelpToActivity.class));
                break;
            case R.id.aboutLinout:
                startActivity(new Intent(PersonalActivity.this,About.class));

                break;
            case R.id.personal_quit:




                View view = LayoutInflater.from(PersonalActivity.this).inflate(R.layout.qiangzhi_xiaxian_layout,null);
                final MyDialog builder = new MyDialog(PersonalActivity.this, 0, 0, view, R.style.MyDialog);
                builder.setCancelable(false);
                Button btn_no_xian_ss= (Button) view.findViewById(R.id.btn_yes_xian_ss);
                Button  btn_yes_xiaxian_ss= (Button) view.findViewById(R.id.btn_no_xiaxian_ss);
                TextView tvTitle = (TextView) view.findViewById(R.id.for_tv_titles);
                TextView tvMsg = (TextView) view.findViewById(R.id.tv_msg);

                tvTitle.setText(getResources().getString(R.string.exit_login));
                tvMsg.setText(getResources().getString(R.string.exit_login_msg));
                tvMsg.setGravity(Gravity.CENTER);

                //取消按钮
                btn_no_xian_ss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.dismiss();
                    }
                });
                //确认按钮
                btn_yes_xiaxian_ss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        UserInfo userInfo =new UserInfo(PersonalActivity.this);
                        userInfo.clear();
                        Intent  intent  =new Intent(PersonalActivity.this, HomeActivity.class);
                        Bundle bundle = new Bundle();
                        intent.putExtras(bundle);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("start", "1");
                        startActivity(intent);
                        finish();
                    }
                });

                builder.show();

                break;
        }
    }

    //从SharedPreferences获取图片
    private void getBitmapFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("testSP", Context.MODE_PRIVATE);
        //第一步:取出字符串形式的Bitmap
        String imageString = sharedPreferences.getString("image", "");
        //第二步:利用Base64将字符串转换为ByteArrayInputStream
        byte[] byteArray = Base64.decode(imageString, Base64.DEFAULT);
        if (byteArray.length == 0) {
            personal_head.setImageResource(R.drawable.personal_head_portrait);
        } else {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
            //第三步:利用ByteArrayInputStream生成Bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(byteArrayInputStream);
            personal_head.setImageBitmap(bitmap);
        }
    }
}
