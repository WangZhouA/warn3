package com.saiyi.ui.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyi.R;
import com.saiyi.http.ConstantInterface;
import com.saiyi.interfaces.HttpRequestCallback;
import com.saiyi.modler.BigModle;
import com.saiyi.recevice.JpushReceiver;
import com.saiyi.ui.view.MyDialog;
import com.saiyi.ui.view.PercentageRing;
import com.saiyi.ui.view.TimeUtil;


public class BaojingActvivity extends BasActivity implements View.OnTouchListener {

    ImageView imBaojing;

    AnimationDrawable animaition;

    private PercentageRing mPercentageRing1;

    ImageView image;

    MediaPlayer mediaPlayer;

    BigModle bigModle;


    TextView tv_MSGA;
    TextView tvTime;


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    if (mAddProgressAnima != null) {
                        if (mAddProgressAnima.isRunning()) {
                            mAddProgressAnima.cancel();
                        }
                        mAddProgressAnima = null;
                    }
                    if (mAddProgressAnima == null) {
                        mAddProgressAnima = createAddProgressAnim();
                        mAddProgressAnima.start();


                    }
                    break;

                case 1:
                    if (mAddProgressAnima != null && mAddProgressAnima.isRunning()) {
                        mAddProgressAnima.cancel();
                        mAddProgressAnima = null;
                    }
                    mPercentageRing1.setTargetPercent(hasFillProgressed ? 100 : 0);
                    break;

            }

        }
    };

    private ValueAnimator mAddProgressAnima;
    private boolean hasFillProgressed;
    private boolean isCancledAnim;

    int progress;

    private ValueAnimator createAddProgressAnim() {
        ValueAnimator addProgressAnima = ValueAnimator.ofInt(100);
        addProgressAnima.setDuration(2000);
        addProgressAnima.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progress = (int) animation.getAnimatedValue();
                mPercentageRing1.setTargetPercent(progress);
            }
        });
        addProgressAnima.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                hasFillProgressed = false;
                isCancledAnim = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.d("TAG", "onAnimationEnd===========");
                if (!isCancledAnim) {
                    hasFillProgressed = true;
                    image.setEnabled(false);
                    showDialog();
                }
            }


            @Override
            public void onAnimationCancel(Animator animation) {
                Log.d("TAG", "onAnimationCancel===========");
                isCancledAnim = true;
                hasFillProgressed = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        return addProgressAnima;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baojing_actvivity);
        imBaojing = (ImageView) findViewById(R.id.im_baojing);
        imBaojing.setBackgroundResource(R.drawable.animation_list);//其中R.drawable.animation_list就是上一步准备的动画描述文件的资源名
        animaition = (AnimationDrawable) imBaojing.getBackground();   //不一定是设置背景，也可以作为src图片设置
        animaition.setOneShot(false);
        tv_MSGA = (TextView) findViewById(R.id.tv_MSG);
        mPercentageRing1 = (PercentageRing) findViewById(R.id.Circle3);
        //设置目标百分比为30
        image = (ImageView) findViewById(R.id.image);

        tvTime = (TextView) findViewById(R.id.tv_time);
        image.setOnTouchListener(this);

        bigModle =new BigModle();

        tvTime.setText(TimeUtil.getCurrentAllTime());

        IntentFilter intentFilter =new IntentFilter(ConstantInterface.UPDATE_CEFANG);
        registerReceiver(myBroadcastReceiver,intentFilter);



        if (mediaPlayer==null) {
            mediaPlayer = MediaPlayer.create(BaojingActvivity.this, R.raw.jingche);
            //监听音频播放完的代码，实现音频的自动循环播放
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer arg0) {
                    mediaPlayer.start();
                    mediaPlayer.setLooping(true);

                }
            });
            mediaPlayer.start();
        }else {

        }

        tv_MSGA.setText(JpushReceiver.getMsg());

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        String  msg =intent.getStringExtra("msg");
        Log.e(TAG, "payload->messageAAA"+msg);
        if (!TextUtils.isEmpty(msg)){
            String [] str =msg.split(",");
            tv_MSGA.setText("("+msg.substring(0,12)+")"+" "+msg.substring(14,16)+" "+str[1]+" "+getResources().getString(R.string.Someone_broke_into));
        }


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        animaition.start();
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            Log.e("----->按下", "按下");

            handler.sendEmptyMessage(0);


        } else if (event.getAction() == MotionEvent.ACTION_UP) {

            Log.e("----->放开", "放开");
            handler.sendEmptyMessage(1);
        }

        return true;
    }
     MyDialog dialog;
    private void  showDialog(){
        LayoutInflater inflate =LayoutInflater.from(this);
        View  view=inflate.inflate(R.layout.qiangzhi_xiaxian_layout,null);
        dialog =new MyDialog(this,1,1,view,R.style.MyDialog);
        Button yes = (Button) view.findViewById(R.id.btn_no_xiaxian_ss);
        Button no = (Button) view.findViewById(R.id.btn_yes_xian_ss);
        TextView tvTitle = (TextView) view.findViewById(R.id.for_tv_titles);
        TextView tvMsg = (TextView) view.findViewById(R.id.tv_msg);
        tvTitle.setText(getResources().getString(R.string.jiechutixing));
        tvMsg.setText(getResources().getString(R.string.jiechutixing_MSG));
        yes.setText(getResources().getString(R.string.jiechu));
        no.setText(getResources().getString(R.string.HULVE));
        tvTitle.setTextSize(20);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (mediaPlayer!=null) {
                    mediaPlayer.stop();
                    mediaPlayer=null;
                }else {

                    }

                bigModle.getPutMac(BaojingActvivity.this, ConstantInterface.MAC, ConstantInterface.MAC + "0000", 0, new HttpRequestCallback() {
                    @Override
                    public void onResponse(String sequest, int type) {

                        Toast.makeText(BaojingActvivity.this, getResources().getString(R.string.ok), Toast.LENGTH_LONG).show();
                        Intent intent =new Intent(BaojingActvivity.this,MyDevicesEmptyActivity.class);
                        startActivity(intent);
                        finish();

                    }

                    @Override
                    public void onFailure(String exp) {
                        Toast.makeText(BaojingActvivity.this, getResources().getString(R.string.no), Toast.LENGTH_LONG).show();
                        Intent intent =new Intent(BaojingActvivity.this,MyDevicesEmptyActivity.class);
                        startActivity(intent);
                        finish();

                    }
                });
//
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialog.dismiss();
                if (mediaPlayer!=null) {
                    mediaPlayer.stop();
                    mediaPlayer=null;
                }else {

                }
                Intent intent =new Intent(BaojingActvivity.this,MyDevicesEmptyActivity.class);
                startActivity(intent);
                finish();


            }
        });

        dialog.show();

    }
//
//    /**
//     * 按两次退出
//     */
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
////
//            dialog.dismiss();
//            dialog.dismiss();
//            if (mediaPlayer!=null) {
//                mediaPlayer.stop();
//                mediaPlayer=null;
//            }else {
//
//            }
//            Intent intent =new Intent(BaojingActvivity.this,MyDevicesEmptyActivity.class);
//            startActivity(intent);
//            finish();
//
//
//        }
//        return  false;
//
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
         unregisterReceiver(myBroadcastReceiver);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            System.out.println("按下了back键   onKeyDown()");

            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }


    private BroadcastReceiver myBroadcastReceiver =new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if (action.contains(ConstantInterface.UPDATE_CEFANG)) {


                if (mediaPlayer!=null) {
                    mediaPlayer.stop();
                    mediaPlayer=null;
                }else {

                }
                intent =new Intent(BaojingActvivity.this,MyDevicesEmptyActivity.class);
                startActivity(intent);
                finish();


            }
        }
    };

}
