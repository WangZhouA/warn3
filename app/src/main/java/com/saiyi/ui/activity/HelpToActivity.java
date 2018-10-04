package com.saiyi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.saiyi.R;
import com.saiyi.application.MyApplication;
import com.saiyi.utils.AppManager;
import com.saiyi.utils.Constants;
import com.saiyi.utils.SPUtils;


public class HelpToActivity extends BasActivity  implements View.OnClickListener{

    ImageButton my_devices_empty_header_left;
    TextView my_devices_empty_header_text;

    ImageView iv_simplified_chinese;
    ImageView iv_japanese;
    ImageView iv_english;

    LinearLayout btn_simplified_chinese;
    LinearLayout btn_english;
    LinearLayout btn_japanese;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tohleps);

        MyApplication.getInstance().addActivity(this);

        my_devices_empty_header_left = (ImageButton) findViewById(R.id.header_left);
        my_devices_empty_header_text = (TextView) findViewById(R.id.header_text);
        my_devices_empty_header_left.setOnClickListener(this);
        my_devices_empty_header_text.setText(R.string.Language_switching);

        iv_simplified_chinese = (ImageView) findViewById(R.id.iv_simplified_chinese);
        iv_english = (ImageView) findViewById(R.id.iv_english);
        iv_japanese = (ImageView) findViewById(R.id.iv_japanese);

        btn_simplified_chinese = (LinearLayout) findViewById(R.id.btn_simplified_chinese);
        btn_english = (LinearLayout) findViewById(R.id.btn_english);
        btn_japanese = (LinearLayout) findViewById(R.id.btn_japanese);



        btn_simplified_chinese.setOnClickListener(this);
        btn_english.setOnClickListener(this);
        btn_japanese.setOnClickListener(this);

        //获取语言
        String language_code = SPUtils.get(this, SPUtils.LANGUAGE_CODE, Constants.LANGUAGE_CN).toString();
        //设置语言的选择状态,如果是简体中文
        if (Constants.LANGUAGE_CN.equals(language_code)) {
            //设置简体勾选框显示
            iv_simplified_chinese.setVisibility(View.VISIBLE);
            //日文勾选框隐藏
            iv_japanese.setVisibility(View.GONE);
            //英文勾选框隐藏
            iv_english.setVisibility(View.GONE);
        }
        //如果是日文
        else if (Constants.LANGUAGE_JP.equals(language_code)) {
            //设置简体勾选框隐藏
            iv_simplified_chinese.setVisibility(View.GONE);
            //日文勾选框显示
            iv_japanese.setVisibility(View.VISIBLE);
            //英文勾选框隐藏
            iv_english.setVisibility(View.GONE);
        }
        //如果是英文
        else if (Constants.LANGUAGE_EN.equals(language_code)) {
            //设置简体勾选框隐藏
            iv_simplified_chinese.setVisibility(View.GONE);
            //日文勾选框隐藏
            iv_japanese.setVisibility(View.GONE);
            //英文勾选框显示
            iv_english.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {

        //获取语言code
        String language_code = SPUtils.get(HelpToActivity.this, SPUtils.LANGUAGE_CODE, Constants.LANGUAGE_CN).toString();
        switch (v.getId()){


            case R.id.btn_simplified_chinese://简体中文
                //设置语言的选择状态,如果是简体中文
                if (Constants.LANGUAGE_CN.equals(language_code)) {
                    return;
                }

                //设置简体勾选框显示
                iv_simplified_chinese.setVisibility(View.VISIBLE);
                //日文勾选框隐藏
                iv_japanese.setVisibility(View.GONE);
                //英文勾选框隐藏
                iv_english.setVisibility(View.GONE);
                //记住选择简体中文
                SPUtils.put(HelpToActivity.this,SPUtils.LANGUAGE_CODE,Constants.LANGUAGE_CN);
//                setLanguage(Locale.CHINESE);
                enterMianActivity();
                break;

            case R.id.btn_english://英文
                //设置语言的选择状态,如果是简体中文
                if (Constants.LANGUAGE_EN.equals(language_code)) {
                    return;
                }

                //设置简体勾选框隐藏
                iv_simplified_chinese.setVisibility(View.GONE);
                //日文勾选框隐藏
                iv_japanese.setVisibility(View.GONE);
                //英文勾选框显示
                iv_english.setVisibility(View.VISIBLE);
                //记住选择
                SPUtils.put(HelpToActivity.this,SPUtils.LANGUAGE_CODE,Constants.LANGUAGE_EN);
//                setLanguage(Locale.ENGLISH);
                enterMianActivity();
                break;

            case R.id.btn_japanese://日语
                //设置语言的选择状态,如果是简体中文
                if (Constants.LANGUAGE_JP.equals(language_code)) {
                    return;
                }
                //设置简体勾选框隐藏
                iv_simplified_chinese.setVisibility(View.GONE);
                //日文勾选框显示
                iv_japanese.setVisibility(View.VISIBLE);
                //英文勾选框隐藏
                iv_english.setVisibility(View.GONE);
                //记住选择
                SPUtils.put(HelpToActivity.this,SPUtils.LANGUAGE_CODE,Constants.LANGUAGE_JP);
//                setLanguage(Locale.JAPANESE);
                enterMianActivity();
                break;
        }
    }

    /**
     * 切换语言,进入首页
     */
    private void enterMianActivity(){
        AppManager.getInstance().closeAllActivityExceptOne("HelpToActivity");
        Intent  intent  =new Intent(HelpToActivity.this, MyDevicesEmptyActivity.class);
//        Bundle bundle = new Bundle();
//        intent.putExtras(bundle);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra("start", "1");
        startActivity(intent);
        finish();
    }
}



