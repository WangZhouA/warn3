package com.saiyi.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyi.R;
import com.saiyi.http.ConstantInterface;
import com.saiyi.interfaces.HttpRequestCallback;
import com.saiyi.modler.BigModle;
import com.saiyi.ui.Entity.EDerviceQueryList;
import com.saiyi.ui.view.ForPlayDialog;
import com.saiyi.ui.view.ForPlayDialogT;
import com.saiyi.ui.view.MyDialog;
import com.saiyi.ui.view.PickerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FqSettingAdapter extends BaseAdapter implements HttpRequestCallback {

    Context mContext;

    ForPlayDialog forPlayDialog;
    ForPlayDialogT forPlayDialogt;
    private List<EDerviceQueryList> lists = new ArrayList<>();
    private String intentDerviceId;


    private List<String> liststr = new ArrayList<>();
    private String[] strArray;

    private boolean offOrOpen;

    private int flag;

    private String strMessageName;
    private String strWashCloth;
    private String strCalled;
    private String strPick;

    private int flagName = 0;


    public FqSettingAdapter(Context context, List<EDerviceQueryList> lists) {
        mContext = context;
        this.lists = lists;
        //获取derviceId
        if (MyDevicesActivityAdapter.getIntentDerviceId() != null) {
            intentDerviceId = MyDevicesActivityAdapter.getIntentDerviceId();
            notifyDataSetChanged();
        }


    }

    @Override
    public int getCount() {
        Log.i("--->lists.size()", "" + lists.size());

        return lists.size();

    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.i("--->1", "1");
        final ViewHolder viewHolder;
        final EDerviceQueryList eDerviceQueryList = lists.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.fq_set_list_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        if (eDerviceQueryList.getZoneOnOff().equals(1) || eDerviceQueryList.getZoneOnOff().equals("1")) {

            viewHolder.actionCb.setChecked(true);
            viewHolder.menu.setVisibility(View.VISIBLE);

        } else {
            viewHolder.actionCb.setChecked(false);
            viewHolder.menu.setVisibility(View.GONE);
        }

        if (eDerviceQueryList.getZoneType().equals("null")) {

            viewHolder.type.setText("");
        } else {
            viewHolder.type.setText(eDerviceQueryList.getZoneType());
        }

        viewHolder.areaTv.setText(eDerviceQueryList.getZoneName());


        viewHolder.areaTv.setText(eDerviceQueryList.getZoneName());
        viewHolder.actionCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    viewHolder.menu.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.menu.setVisibility(View.GONE);
                }
            }
        });


        viewHolder.rename.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                flag = 1;
                forPlayDialog = new ForPlayDialog(mContext);
                forPlayDialog.setTitle(mContext.getResources().getString(R.string.redervice_name));

                forPlayDialog.setNoOnclickListener(mContext.getResources().getString(R.string.sure), new ForPlayDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        flagName = 1;
                        strMessageName = forPlayDialog.setMessage();
                        Log.i("--->strMessageName", strMessageName);
                        viewHolder.areaTv.setText(strMessageName);
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

            private void showMyDialog() {
                final View view = LayoutInflater.from(mContext).inflate(R.layout.lgddialog_item, null);
                final Button btnYes = (Button) view.findViewById(R.id.btn_yes_oo);
                final Button btlNo = (Button) view.findViewById(R.id.btn_no_oo);
                final List<String> list = new ArrayList<>();
                list.add(mContext.getResources().getString(R.string.welcome));
                list.add(mContext.getResources().getString(R.string.old_man));
                list.add(mContext.getResources().getString(R.string.Common_defense_old));
                list.add(mContext.getResources().getString(R.string.behind));
                list.add(mContext.getResources().getString(R.string.smart));
                final PickerView pick = (PickerView) view.findViewById(R.id.pivews);
                pick.setData(list);
                final MyDialog builder = new MyDialog(mContext, 0, 0, view, R.style.MyDialog);
                builder.setCancelable(false);
                pick.setOnSelectListener(new PickerView.onSelectListener() {
                    @Override
                    public void onSelect(String text) {
                        Log.i("--->选择了 ", text);
                        strPick = text;
                    }
                });

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        flag = 2;
                        if (strPick != null) {

                            Log.i("--->strpick", strPick);
                            viewHolder.type.setText(strPick);
                            okHttpModifyTheProperties(position, getInstructions(strPick));
                            builder.dismiss();
                        } else {

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
                forPlayDialogt.setNoOnclickListener(mContext.getResources().getString(R.string.sure), new ForPlayDialogT.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        flag = 3;
                        strWashCloth = forPlayDialogt.setMessage();
                        if (strWashCloth.length() < 2) {
                            strWashCloth = "00" + strWashCloth;
                        } else if (strWashCloth.length() < 3) {
                            strWashCloth = "0" + strWashCloth;
                        }
                        Log.i("--->strWashCloth", strWashCloth);
                        okHttpModifyTheProperties(position, strWashCloth);
                        forPlayDialogt.dismiss();
                    }
                });
                forPlayDialogt.show();

            }
        });


        viewHolder.actionCb.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                if (viewHolder.actionCb.isChecked()) {
                    Log.i("--->actionCb", "开");
                    offOrOpen = true;
                    okHttpSetDerviceState(position);


                } else {
                    Log.i("--->actionCb", "关");
                    offOrOpen = false;
                    okHttpSetDerviceState(position);

                }
            }
        });


        viewHolder.cell.setOnClickListener(new View.OnClickListener()

        {

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
                        flag = 4;
                        strCalled = forPlayDialogt.setMessage();
                        Log.i("--->strCalled", strCalled);
                        if (strCalled.length() < 2) {
                            strCalled = "00" + strCalled;
                        } else if (strCalled.length() < 3) {
                            strCalled = "0" + strCalled;
                        }
                        okHttpModifyTheProperties(position, strCalled);
                        forPlayDialogt.dismiss();
                    }
                });
                forPlayDialogt.show();
            }
        });
        return convertView;
    }

    class ViewHolder {
        View rootView;
        TextView areaTv;
        CheckBox actionCb;
        View rename;
        View attribute;
        View lock;
        View cell;
        View menu;
        TextView type;


        public ViewHolder(View view) {
            rootView = view;
            areaTv = (TextView) view.findViewById(R.id.area_tx);
            actionCb = (CheckBox) view.findViewById(R.id.action_ck);
            menu = view.findViewById(R.id.llt_menu);
            rename = view.findViewById(R.id.llt_name_set);
            attribute = view.findViewById(R.id.llt_su_set);
            lock = view.findViewById(R.id.llt_yans_set);
            cell = view.findViewById(R.id.llt_warn_set);
            type = (TextView) view.findViewById(R.id.class_tx);
        }


    }


    private void okHttpSetDerviceState(final int position) {
        EDerviceQueryList eDerviceQueryList = lists.get(position);

        final String strurl = ConstantInterface.HTTP_SERVICE_ADDRESS_OFF_OPEN;
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        JSONObject jsonObject = new JSONObject();
        try {
            if (offOrOpen == true) {
                Log.i("--->zoneId", eDerviceQueryList.getZoneId());
                jsonObject.put("zoneId", eDerviceQueryList.getZoneId());
                jsonObject.put("zoneOnOff", 1);
            } else {
                jsonObject.put("zoneId", eDerviceQueryList.getZoneId());
                jsonObject.put("zoneOnOff", 0);
            }
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
                    String result = response.body().string();
                    Log.i("--->", "正确的查询信息" + result.toString());

                } else {
                    Log.i("--->", "错误的信息" + response.body().string());
                }
            }
        });

    }

    BigModle bigModle = new BigModle();

    //修改防区属性相关类型
    //位置  属性指令
    private void okHttpModifyTheProperties(final int position, final String pos) {
        EDerviceQueryList eDerviceQueryList = lists.get(position);
        final String strurl = ConstantInterface.HTTP_SERVICE_ADDRESS_MODIFY_THE_PROPERTIES;
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        JSONObject jsonObject = new JSONObject();
        try {
            if (flag == 1) {
                jsonObject.put("zoneId", eDerviceQueryList.getZoneId());
                jsonObject.put("zoneName", strMessageName);
            } else if (flag == 2) {

                jsonObject.put("zoneId", eDerviceQueryList.getZoneId());
                jsonObject.put("zoneType", strPick);

            } else if (flag == 3) {

                jsonObject.put("zoneId", eDerviceQueryList.getZoneId());
                jsonObject.put("zoneDelayOpen", strWashCloth);

            } else if (flag == 4) {

                jsonObject.put("zoneId", eDerviceQueryList.getZoneId());
                jsonObject.put("zoneDelayWarn", strCalled);

            }
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
                    String result = response.body().string();
                    Log.i("--->", "正确的修改信息" + result.toString());
                    //防区设置__提交防区指令
                    if (flag == 2) {
                        String numbert;
                        if (position == 0) {
                            numbert = "01";
                        } else {
                            numbert = (position + 1) + "";
                            if (numbert.length() < 2) {
                                numbert = "0" + numbert;
                            }
                        }
                        bigModle.getPutMac(mContext, ConstantInterface.MAC, ConstantInterface.MAC + "12" + numbert + "0" + pos + "0" + "0", 0, FqSettingAdapter.this);

                        //设置延时时间
                    } else if (flag == 3) {
                        bigModle.getPutMac(mContext, ConstantInterface.MAC, ConstantInterface.MAC + "17" + "2" + pos, 0, FqSettingAdapter.this);
                    } else if (flag == 4) {
                        bigModle.getPutMac(mContext, ConstantInterface.MAC, ConstantInterface.MAC + "17" + "1" + pos, 0, FqSettingAdapter.this);
                    }
                } else {
                    Log.i("--->", "错误的信息" + response.body().string());
                }
            }
        });

    }


    @Override
    public void onResponse(String sequest, int type) {
        Toast.makeText(mContext, mContext.getResources().getString(R.string.ok), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailure(String exp) {
        Toast.makeText(mContext, mContext.getResources().getString(R.string.no), Toast.LENGTH_LONG).show();
    }

    //查询防区信息
    private void okHttpQueryDerviceList() {
        final String strurl = ConstantInterface.HTTP_SERVICE_ADDRESS_QUERY_DERVICE_LIST;
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("pageNo", 1);
            jsonObject.put("pageSize", 10);
            jsonObject.put("deviceId", intentDerviceId);
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
                    String result = response.body().string();
                    Log.i("--->", "正确的查询信息" + result.toString());
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject item = jsonArray.getJSONObject(i);
                            String zoneId = item.getString("zoneId");
                            String zoneName = item.getString("zoneName");
                            String zoneCode = item.getString("zoneCode");
                            String zoneType = item.getString("zoneType");
                            String zoneDelayOpen = item.getString("zoneDelayOpen");
                            String zoneDelayWarn = item.getString("zoneDelayWarn");
                            String zoneOnOff = item.getString("zoneOnOff");
                            String zoneStatus = item.getString("zoneStatus");
                            String deviceId = item.getString("deviceId");

                            EDerviceQueryList eDerviceQueryList = new EDerviceQueryList();
                            eDerviceQueryList.setZoneId(zoneId);
                            eDerviceQueryList.setZoneName(zoneName);
                            eDerviceQueryList.setZoneCode(zoneCode);
                            eDerviceQueryList.setZoneType(zoneType);
                            eDerviceQueryList.setZoneDelayOpen(zoneDelayOpen);
                            eDerviceQueryList.setZoneDelayWarn(zoneDelayWarn);
                            eDerviceQueryList.setZoneOnOff(zoneOnOff);
                            eDerviceQueryList.setZoneStatus(zoneStatus);
                            eDerviceQueryList.setDeviceId(deviceId);
                            lists.add(eDerviceQueryList);
                            //liststr.add(zoneId);
                        }
//						strArray=liststr.toArray(new String[liststr.size()]);
//						Log.i("--->strArray", Arrays.toString(strArray));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.i("--->", "错误的信息" + response.body().string());
                }
            }
        });
    }


    private String getInstructions(String data) {
        final String[] contextList = {mContext.getResources().getString(R.string.welcome), mContext.getResources().getString(R.string.old_man),
                mContext.getResources().getString(R.string.Common_defense_old),
                mContext.getResources().getString(R.string.behind), mContext.getResources().getString(R.string.smart)
        };
        if (contextList[0].equals(data)) {
            return "7";
        } else if (contextList[1].equals(data)) {
            return "8";
        } else if (contextList[2].equals(data)) {
            return "1";
        } else if (contextList[3].equals(data)) {
            return "2";
        } else if (contextList[4].equals(data)) {
            return "3";
        }
        return "";
    }
}
