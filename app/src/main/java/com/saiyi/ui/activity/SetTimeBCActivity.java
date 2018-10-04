package com.saiyi.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.saiyi.R;
import com.saiyi.http.ConstantInterface;
import com.saiyi.interfaces.HttpRequestCallback;
import com.saiyi.modler.BigModle;
import com.saiyi.ui.view.CheckTextView;
import com.saiyi.ui.view.CustomDatePickerCopy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class SetTimeBCActivity extends BasActivity implements View.OnClickListener,HttpRequestCallback{


    ImageButton imRight,imLeft;
    TextView tvBF,tvCF;
    CheckTextView  tvOne,tvTwo,tvThree,tvFour,tvFive,tvSix,tvSeven;
    TextView [] textViewsArray;


    int postion;
    //时间选择器
    private CustomDatePickerCopy customDatePickerSleep;
    private CustomDatePickerCopy customDatePickerUpte;
    BigModle BigModle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_time_bc);

        Intent intent =getIntent();
        postion = intent.getIntExtra("POS",110);

        imRight= (ImageButton) findViewById(R.id.header_right);
        imLeft= (ImageButton) findViewById(R.id.header_left);
        tvBF= (TextView) findViewById(R.id.tv_bf_time);
        tvCF= (TextView) findViewById(R.id.tv_cf_time);
        tvOne= (CheckTextView) findViewById(R.id.tv_week_one);
        tvTwo= (CheckTextView) findViewById(R.id.tv_week_two);
        tvThree= (CheckTextView) findViewById(R.id.tv_week_three);
        tvFour= (CheckTextView) findViewById(R.id.tv_week_four);
        tvFive= (CheckTextView) findViewById(R.id.tv_week_five);
        tvSix= (CheckTextView) findViewById(R.id.tv_week_six);
        tvSeven= (CheckTextView) findViewById(R.id.tv_week_seven);

        imRight.setOnClickListener(this);
        imLeft.setOnClickListener(this);
        tvBF.setOnClickListener(this);
        tvCF.setOnClickListener(this);
        tvOne.setOnClickListener(this);
        tvTwo.setOnClickListener(this);
        tvThree.setOnClickListener(this);
        tvFour.setOnClickListener(this);
        tvFive.setOnClickListener(this);
        tvSix.setOnClickListener(this);
        tvSeven.setOnClickListener(this);

        textViewsArray = new TextView[]{tvOne,tvTwo,tvThree,tvFour,tvFive,tvSix,tvSeven};
        initDatePickerSleep();
        initDatePickerUpte();

        BigModle =new BigModle();
    }



    private boolean flagONE;
    private boolean flagTWO;
    private boolean flagTHREE;
    private boolean flagFOUR;
    private boolean flagFIVE;
    private boolean flagSIX;
    private boolean flagSEVEN;


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.header_left:

                finish();
                break;
            case R.id.header_right:

                /**
                 * 保存
                 * **/

                String [] bf =tvBF.getText().toString().split(":");
                String [] cf =tvCF.getText().toString().split(":");
                boolean [] Str = {flagONE,flagTWO,flagTHREE,flagFOUR,flagFIVE,flagSIX,flagSEVEN};


//                    if (postion<=8) {
//                        BigModle.getPutMac(SetTimeBCActivity.this, ConstantInterface.MAC, ConstantInterface.MAC + "18"+"0"+(postion+1)+bf[0] + bf[1] + cf[0] + cf[1] + getArray(Str)+"#",100,this);
//                    }else {
//                        BigModle.getPutMac(SetTimeBCActivity.this, ConstantInterface.MAC, ConstantInterface.MAC +"18"+ (postion+1)+bf[0] + bf[1] + cf[0] + cf[1] + getArray(Str)+"#",100,this);
//                    }


                BigModle.getPutMac(SetTimeBCActivity.this, ConstantInterface.MAC, ConstantInterface.MAC +"18"+ "01" +bf[0] + bf[1] + cf[0] + cf[1] + getArray(Str)+"#",100,this);

                    finish();
                break;
            case R.id.tv_bf_time:
                customDatePickerSleep.show("2010-01-01"+" "+tvBF.getText().toString());

                break;
            case R.id.tv_cf_time:
                customDatePickerUpte.show("2010-01-01"+" "+tvCF.getText().toString());

                break;

            case  R.id.tv_week_one:
                if (flagONE==false){
                    tvOne.setChecked(true);
                    tvOne.setTextColor(Color.parseColor("#FFFFFF"));
                    flagONE=true;
                }else {
                    tvOne.setChecked(false);
                    tvOne.setTextColor(Color.parseColor("#CDCDCD"));
                    flagONE=false;

                }

                break;


            case  R.id.tv_week_two:

                if (flagTWO==false){
                    tvTwo.setChecked(true);
                    tvTwo.setTextColor(Color.parseColor("#FFFFFF"));
                    flagTWO=true;
                }else {
                    tvTwo.setChecked(false);
                    tvTwo.setTextColor(Color.parseColor("#CDCDCD"));
                    flagTWO=false;
                }

                break;
            case  R.id.tv_week_three:

                if (flagTHREE==false){
                    tvThree.setChecked(true);
                    tvThree.setTextColor(Color.parseColor("#FFFFFF"));
                    flagTHREE=true;
                }else {
                    tvThree.setChecked(false);
                    tvThree.setTextColor(Color.parseColor("#CDCDCD"));
                    flagTHREE=false;
                }


                break;


            case  R.id.tv_week_four:

                if (flagFOUR==false){
                    tvFour.setChecked(true);
                    tvFour.setTextColor(Color.parseColor("#FFFFFF"));
                    flagFOUR=true;
                }else {
                    tvFour.setChecked(false);
                    tvFour.setTextColor(Color.parseColor("#CDCDCD"));
                    flagFOUR=false;
                }

                break;


            case  R.id.tv_week_five:
                if (flagFIVE==false){
                    tvFive.setChecked(true);
                    tvFive.setTextColor(Color.parseColor("#FFFFFF"));
                    flagFIVE=true;
                }else {
                    tvFive.setChecked(false);
                    tvFive.setTextColor(Color.parseColor("#CDCDCD"));
                    flagFIVE=false;
                }
                break;


            case  R.id.tv_week_six:

                if (flagSIX==false){
                    tvSix.setChecked(true);
                    tvSix.setTextColor(Color.parseColor("#FFFFFF"));
                    flagSIX=true;
                }else {
                    tvSix.setChecked(false);
                    tvSix.setTextColor(Color.parseColor("#CDCDCD"));
                    flagSIX=false;
                }


                break;


            case  R.id.tv_week_seven:

                if (flagSEVEN==false){
                    tvSeven.setChecked(true);
                    tvSeven.setTextColor(Color.parseColor("#FFFFFF"));
                    flagSEVEN=true;
                }else {
                    tvSeven.setChecked(false);
                    tvSeven.setTextColor(Color.parseColor("#CDCDCD"));
                    flagSEVEN=false;
                }

                break;

        }

    }

    private void initDatePickerSleep() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        tvBF.setText(now.split(" ")[1]);
        customDatePickerSleep = new CustomDatePickerCopy(this, new CustomDatePickerCopy.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间

                tvBF.setText(time.split(" ")[1]);


            }
        }, "2010-01-01 01:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePickerSleep.showSpecificTime(true); // 不显示时和分
        customDatePickerSleep.setIsLoop(false); // 不允许循环滚动

    }
    private void initDatePickerUpte() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        tvCF.setText(now.split(" ")[1]);
        customDatePickerUpte = new CustomDatePickerCopy(this, new CustomDatePickerCopy.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间

                tvCF.setText(time.split(" ")[1]);


            }
        }, "2010-01-01 01:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePickerUpte.showSpecificTime(true); // 不显示时和分
        customDatePickerUpte.setIsLoop(false); // 不允许循环滚动


    }


    public  static String getArray( boolean [] Str){

        String  returnA="";
        for (int i = 0; i < Str.length; i++) {
            if (Str[i]==true) {

                returnA+=i+1;
            }
        }
        return returnA;
    }

    @Override
    public void onResponse(String sequest, int type) {

    }

    @Override
    public void onFailure(String exp) {

    }
}
