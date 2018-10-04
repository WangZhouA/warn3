package com.saiyi.ui.fragment.child;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.saiyi.R;
import com.saiyi.http.ConstantInterface;
import com.saiyi.interfaces.HttpRequestCallback;
import com.saiyi.modler.BigModle;
import com.saiyi.ui.activity.SetTimeBCActivity;
import com.saiyi.ui.adapter.MyDevicesActivityAdapter;
import com.saiyi.ui.view.CustomDatePicker;
import com.saiyi.ui.view.ForPlayDialog;
import com.saiyi.ui.view.ForPlayDialogT;
import com.saiyi.ui.view.TimeUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by 陈姣姣 on 2018/6/27.
 */

public class CfChildSetTimFragment extends Fragment implements View.OnClickListener{


    View baseView;
    private String IntentDerviceId;

    BigModle bigModle;

    ForPlayDialog forPlayDialog;
    ForPlayDialogT forPlayDialogt;


    //时间选择器
    private CustomDatePicker customDatePicker;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (baseView == null) {
            baseView = inflater.inflate(R.layout.cf_chid_settime_fragment_item , null);
        }
        baseView.findViewById(R.id.outTime_protection).setOnClickListener(this);
        baseView.findViewById(R.id.Delayed_alarm).setOnClickListener(this);
        baseView.findViewById(R.id.host_time).setOnClickListener(this);
        baseView.findViewById(R.id.Fixed_protection_time).setOnClickListener(this);

        if (MyDevicesActivityAdapter.getIntentDerviceId() != null) {
            IntentDerviceId = MyDevicesActivityAdapter.getIntentDerviceId();
            Log.i("--->id_dervice", IntentDerviceId);
        }


        bigModle =new BigModle();
        initDatePickerUpte();
        return baseView;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.outTime_protection:

                forPlayDialogt = new ForPlayDialogT(getActivity());
                forPlayDialogt.setTitle(getActivity().getResources().getString(R.string.Delay_time_of_cloth_defense));
                forPlayDialogt.setYesOnclickListener(getActivity().getResources().getString(R.string.link_cancle), new ForPlayDialogT.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        forPlayDialogt.dismiss();
                    }
                });
                //设置延时布放时间___指令必须三位数字
                forPlayDialogt.setNoOnclickListener(getActivity().getResources().getString(R.string.sure), new ForPlayDialogT.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        String strWashCloth = forPlayDialogt.setMessage();
                        if (strWashCloth.length() < 2) {
                            strWashCloth = "00" + strWashCloth;
                        } else if (strWashCloth.length() < 3) {
                            strWashCloth = "0" + strWashCloth;
                        }
                        Log.e("----->布防", strWashCloth);

                        bigModle.getPutMac(getActivity(), ConstantInterface.MAC, ConstantInterface.MAC + "17" + "2" + strWashCloth, 0x01, new HttpRequestCallback() {
                            @Override
                            public void onResponse(String sequest, int type) {
                                Log.e("---->成功","接警");
                            }

                            @Override
                            public void onFailure(String exp) {
                                Log.e("---->失败","接警");
                            }
                        });

                        forPlayDialogt.dismiss();
                    }
                });
                forPlayDialogt.show();

                break;
            case R.id.Delayed_alarm:

                forPlayDialogt = new ForPlayDialogT(getActivity());
                forPlayDialogt.setTitle(getActivity().getResources().getString(R.string.Delayed_alarm_time));
                forPlayDialogt.setYesOnclickListener(getActivity().getResources().getString(R.string.link_cancle), new ForPlayDialogT.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        forPlayDialogt.dismiss();

                    }
                });
                forPlayDialogt.setNoOnclickListener(getActivity().getResources().getString(R.string.sure), new ForPlayDialogT.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        String strCalled = forPlayDialogt.setMessage();
                        Log.i("--->strCalled", strCalled);
                        if (strCalled.length() < 2) {
                            strCalled = "00" + strCalled;
                        } else if (strCalled.length() < 3) {
                            strCalled = "0" + strCalled;
                        }
                        Log.e("----->报警", strCalled);

                        bigModle.getPutMac(getActivity(), ConstantInterface.MAC, ConstantInterface.MAC + "17" + "1" + strCalled, 0x02, new HttpRequestCallback() {
                            @Override
                            public void onResponse(String sequest, int type) {
                                Log.e("---->成功","接警");
                            }

                            @Override
                            public void onFailure(String exp) {
                                Log.e("---->失败","接警");
                            }
                        });

                        forPlayDialogt.dismiss();
                    }
                });

                forPlayDialogt.show();

                break;
            case R.id.host_time:
                customDatePicker.show("null");
                break;
            case R.id.Fixed_protection_time:
                getActivity().startActivity(new Intent( getActivity(), SetTimeBCActivity.class));
                break;

        }
    }

    private void initDatePickerUpte( ) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        customDatePicker = new CustomDatePicker(getActivity(), new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间


                if (!TimeUtil.getString(time).contains("-")) {
                    bigModle.getPutMac(getActivity(), ConstantInterface.MAC, ConstantInterface.MAC + "20" + TimeUtil.getString(time), 200, new HttpRequestCallback() {
                        @Override
                        public void onResponse(String sequest, int type) {
                        }

                        @Override
                        public void onFailure(String exp) {

                        }
                    });

                }else {
                }

            }
        }, "2010-01-01 08:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(true); // 不显示时和分
        customDatePicker.setIsLoop(false); // 不允许循环滚动
    }

}
