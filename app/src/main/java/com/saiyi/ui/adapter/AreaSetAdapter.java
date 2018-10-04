package com.saiyi.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.saiyi.R;
import com.saiyi.http.ConstantInterface;
import com.saiyi.interfaces.AreaSetAdapterCallback;
import com.saiyi.interfaces.HttpRequestCallback;
import com.saiyi.modler.BigModle;
import com.saiyi.ui.Entity.EDerviceQueryList;
import com.saiyi.ui.activity.SetTimeBCActivity;
import com.saiyi.ui.view.CustomDatePicker;
import com.saiyi.ui.view.ForPlayDialog;
import com.saiyi.ui.view.ForPlayDialogT;
import com.saiyi.ui.view.MyDialog;
import com.saiyi.ui.view.PickerView;
import com.saiyi.ui.view.TimeUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class AreaSetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>   {
    Context mContext;
    private List<EDerviceQueryList> lists;
    AreaSetAdapterCallback callback;
    ForPlayDialog forPlayDialog;
    ForPlayDialogT forPlayDialogt;

    String [] FQSX;


    View contentViewperiod;
    //时间选择器
    private CustomDatePicker customDatePicker;

    private String SetTime;

    BigModle bigModle = new BigModle();

    public AreaSetAdapter(Context ctx, List<EDerviceQueryList> lists, AreaSetAdapterCallback callback) {
        this.mContext = ctx;
        this.lists = lists;
        this.callback = callback;


        FQSX =new String[]{mContext.getResources().getString(R.string.welcome),mContext.getResources().getString(R.string.old_man),mContext.getResources().getString(R.string.Common_defense_old),
                mContext.getResources().getString(R.string.behind),mContext.getResources().getString(R.string.smart) ,mContext.getResources().getString(R.string.Emergency_protection_zones),
                mContext.getResources().getString(R.string.Close_play),
                mContext.getResources().getString(R.string.The_doorbell_play)};


        contentViewperiod = LayoutInflater
                .from(mContext).inflate(R.layout.period_popupwindow, null);
        bigModle= new BigModle();





    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.fq_set_list_item, null, false));
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    //设置数据
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder viewHolder = (MyViewHolder) holder;
        final EDerviceQueryList eDerviceQueryList = lists.get(position);

        /**
         *  功能暂时屏蔽
         * */
//        if (eDerviceQueryList.getZoneOnOff().equals(1) || eDerviceQueryList.getZoneOnOff().equals("1")) {
//            viewHolder.actionCb.setChecked(true);
//            viewHolder.menu.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.actionCb.setChecked(false);
//            viewHolder.menu.setVisibility(View.GONE);
//        }

        if (eDerviceQueryList.getZoneType().equals("null")) {
            viewHolder.type.setText("");
        } else {
            for (int i = 0; i < FQSX.length; i++) {
                if (FQSX[i].contains(eDerviceQueryList.getZoneName())) {
                    Log.i("---->getZoneName===",eDerviceQueryList.getZoneName());
                    if (FQSX[i].contains(eDerviceQueryList.getZoneType())) {
                        Log.i("---->getZoneType===",eDerviceQueryList.getZoneType());
                        viewHolder.type.setText(FQSX[i]);
                    } else {
                        viewHolder.type.setText(eDerviceQueryList.getZoneType());
                    }
                }
            }
        }
        if (eDerviceQueryList.getZoneName().contains("防区")){

            viewHolder.areaTv.setText(mContext.getResources().getString(R.string.defence_area));
        }else {
            viewHolder.areaTv.setText(eDerviceQueryList.getZoneName());
        }
        viewHolder.number.setText(eDerviceQueryList.getZoneCode());

        viewHolder.actionCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    viewHolder.menu.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.menu.setVisibility(View.GONE);
                }
            }
        });

        //修改名字
        viewHolder.rename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forPlayDialog = new ForPlayDialog(mContext);
                forPlayDialog.setTitle(mContext.getResources().getString(R.string.redervice_name));
                forPlayDialog.setNoOnclickListener(mContext.getResources().getString(R.string.sure), new ForPlayDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        String strMessageName = forPlayDialog.setMessage();
                        callback.setEquipmentName(position, strMessageName);
                        forPlayDialog.dismiss();
                    }
                });
                forPlayDialog.setYesOnclickListener(mContext.getResources().getString(R.string.link_cancle), new ForPlayDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        forPlayDialog.dismiss();
                    }
                });
                forPlayDialog.show();
            }
        });


        viewHolder.attribute.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showMyDialog();
            }

            String strPick = "";
            String mingDi ="1";
            private void showMyDialog() {

                final View view = LayoutInflater.from(mContext).inflate(R.layout.lgddialog_item, null);
                final Button btnYes = (Button) view.findViewById(R.id.btn_yes_oo);
                final Button btlNo = (Button) view.findViewById(R.id.btn_no_oo);
                final ImageView imMingdi = (ImageView) view.findViewById(R.id.tv_mingdi);
                final List<String> list = new ArrayList<>();
                list.add(mContext.getResources().getString(R.string.welcome));
                list.add(mContext.getResources().getString(R.string.old_man));
                list.add(mContext.getResources().getString(R.string.Common_defense_old));
                list.add(mContext.getResources().getString(R.string.behind));
                list.add(mContext.getResources().getString(R.string.smart));
                list.add(mContext.getResources().getString(R.string.Emergency_protection_zones));
                list.add(mContext.getResources().getString(R.string.Close_play));
                list.add(mContext.getResources().getString(R.string.The_doorbell_play));
                final PickerView pick = (PickerView) view.findViewById(R.id.pivews);
                pick.setData(list);
                final MyDialog builder = new MyDialog(mContext, 0, 0, view, R.style.MyDialog);
                builder.setCancelable(false);
                pick.setOnSelectListener(new PickerView.onSelectListener() {
                    @Override
                    public void onSelect(String text) {
                        strPick = text;
                    }
                });

                imMingdi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                         if (mingDi.contains("2")){
                             imMingdi.setImageResource(R.drawable.ming);
                             mingDi="1";

                         }else {
                             imMingdi.setImageResource(R.drawable.noming);
                             mingDi="2";
                         }
                    }
                });

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (strPick != null) {
                            callback.setEquipmentType(position, strPick,mingDi);
                            builder.dismiss();
                        }
                    }
                });

                btlNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.dismiss();
                    }
                });
                builder.show();
            }
        });

        viewHolder.lock.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                forPlayDialogt = new ForPlayDialogT(mContext);
                forPlayDialogt.setTitle(mContext.getResources().getString(R.string.Delay_time_of_cloth_defense));
                forPlayDialogt.setYesOnclickListener(mContext.getResources().getString(R.string.link_cancle), new ForPlayDialogT.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        forPlayDialogt.dismiss();
                    }
                });
                //设置延时布放时间___指令必须三位数字
                forPlayDialogt.setNoOnclickListener(mContext.getResources().getString(R.string.sure), new ForPlayDialogT.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        String strWashCloth = forPlayDialogt.setMessage();
                        if (strWashCloth.length() < 2) {
                            strWashCloth = "00" + strWashCloth;
                        } else if (strWashCloth.length() < 3) {
                            strWashCloth = "0" + strWashCloth;
                        }
                        callback.setDelayed(position, strWashCloth);
                        forPlayDialogt.dismiss();
                    }
                });
                forPlayDialogt.show();

            }
        });

        viewHolder.actionCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.actionCb.isChecked()) {
                    callback.setSector(position, 1);

                } else {
                    callback.setSector(position, 0);
                }
            }
        });


        viewHolder.cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                forPlayDialogt = new ForPlayDialogT(mContext);
                forPlayDialogt.setTitle(mContext.getResources().getString(R.string.Delayed_alarm_time));
                forPlayDialogt.setYesOnclickListener(mContext.getResources().getString(R.string.link_cancle), new ForPlayDialogT.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        forPlayDialogt.dismiss();

                    }
                });
                forPlayDialogt.setNoOnclickListener(mContext.getResources().getString(R.string.sure), new ForPlayDialogT.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        String strCalled = forPlayDialogt.setMessage();
                        Log.i("--->strCalled", strCalled);
                        if (strCalled.length() < 2) {
                            strCalled = "00" + strCalled;
                        } else if (strCalled.length() < 3) {
                            strCalled = "0" + strCalled;
                        }
                        callback.setNewspaper(position, strCalled);

                        forPlayDialogt.dismiss();
                    }
                });
                forPlayDialogt.show();
            }
        });


        viewHolder.hostTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDatePickerUpte();
                customDatePicker.show("null");

            }
        });



        viewHolder.DingShi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, SetTimeBCActivity.class).putExtra("POS",position));
            }
        });


    }

    private void initDatePickerUpte( ) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        customDatePicker = new CustomDatePicker(mContext, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间


                if (!TimeUtil.getString(time).contains("-")) {
                    bigModle.getPutMac(mContext, ConstantInterface.MAC, ConstantInterface.MAC + "20" + TimeUtil.getString(time), 200, new HttpRequestCallback() {
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



    @Override
    public int getItemCount() {
        return lists.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView areaTv;
        CheckBox actionCb;
        View rename;
        View attribute;
        View lock;
        View cell;
        View menu;
        TextView type;
        View hostTime;
        View DingShi;
        TextView number;

        public MyViewHolder(View view) {
            super(view);
            areaTv = (TextView) view.findViewById(R.id.area_tx);
            actionCb = (CheckBox) view.findViewById(R.id.action_ck);
            menu = view.findViewById(R.id.llt_menu);
            DingShi = view.findViewById(R.id.lin_dingshi);
            rename = view.findViewById(R.id.llt_name_set);
            hostTime = view.findViewById(R.id.lin_host_time);
            attribute = view.findViewById(R.id.llt_su_set);
            lock = view.findViewById(R.id.llt_yans_set);
            cell = view.findViewById(R.id.llt_warn_set);
            type = (TextView) view.findViewById(R.id.class_tx);
            number = (TextView) view.findViewById(R.id.number);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }





}
