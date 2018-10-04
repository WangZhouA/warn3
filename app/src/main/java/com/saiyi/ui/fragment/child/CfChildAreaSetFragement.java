package com.saiyi.ui.fragment.child;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.saiyi.R;
import com.saiyi.http.ConstantInterface;
import com.saiyi.interfaces.AreaSetAdapterCallback;
import com.saiyi.interfaces.HttpRequestCallback;
import com.saiyi.modler.BigModle;
import com.saiyi.ui.Entity.EDerviceQueryList;
import com.saiyi.ui.adapter.AreaSetAdapter;
import com.saiyi.ui.adapter.MyDevicesActivityAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CfChildAreaSetFragement extends Fragment implements HttpRequestCallback, AreaSetAdapterCallback {

    //	ExpandableLayoutListView fqSetView;
    View view;
    private String intentDerviceId;
    private int number = 1;
    private RecyclerView mRecyclerView;
    private RefreshLayout mRefreshLayout;
    LinearLayoutManager layoutManager;
    private ClassicsHeader mClassicsHeader;
    private Drawable mDrawableProgress;
    List<EDerviceQueryList> lists = new ArrayList<>();
    private final int GETLIST = 100;//获取列表的接口标识
    private final int EDITNAME = 200;//修改防区名称
    private final int SETSECTOR = 300;//防区开关
    private final int SETEQUIPMENTTYPE = 400;//设置防区类型
    private final int SETDELAYED = 500;//延时布防
    AreaSetAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fq_setting, null);
            if (MyDevicesActivityAdapter.getIntentDerviceId() != null) {
                intentDerviceId = MyDevicesActivityAdapter.getIntentDerviceId();
            }
        }


        IntentFilter intentFilter =new IntentFilter("UPDATE");
        intentFilter.addAction(ConstantInterface.FANGQU_ITEM_UPDATE);
        getActivity().registerReceiver(myReceiver,intentFilter);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRefreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        //触发自动刷新
        mRefreshLayout.autoRefresh();
        mRefreshLayout.setEnableFooterTranslationContent(true);
        // mRefreshLayout.setEnableHeaderTranslationContent(true);
        mRefreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                okHttpQueryDerviceList();
            }
        });

        //下拉
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                number = 11;
                okHttpQueryDerviceList();
            }
        });
        //上拉
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                number = number + 11;
                okHttpQueryDerviceList();
            }
        });
        return view;
    }

    BigModle bigModle = new BigModle();

    //查询防区信息
    private void okHttpQueryDerviceList() {
        final String strurl = ConstantInterface.HTTP_SERVICE_ADDRESS_QUERY_DERVICE_LIST;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("pageNo", 1);
            //  jsonObject.put("pageSize", (number + 15));
            jsonObject.put("pageSize", number );
            jsonObject.put("deviceId", intentDerviceId);
            bigModle.getData(getActivity(), jsonObject, GETLIST, strurl, this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void toToask(String content) {
        Toast.makeText(getActivity(), content, Toast.LENGTH_LONG).show();

    }

    //获取数据成功的回调
    @Override
    public void onResponse(String sequest, int type) {
        /**
         *  private final int GETLIST = 100;//获取列表的接口标识
         private final int EDITNAME = 200;//修改防区名称
         private final int SETSECTOR = 300;//防区开关
         private final int SETEQUIPMENTTYPE = 400;//设置防区类型
         private final int SETDELAYED = 500;//延时布防
         */
        switch (type) {
            case EDITNAME:
//                toToask(getResources().getString(R.string.edit_ok));
                okHttpQueryDerviceList();
                break;
            case SETSECTOR:
//                toToask(getResources().getString(R.string.edit_ok));
                okHttpQueryDerviceList();
                break;
            case SETEQUIPMENTTYPE:
//                toToask(getResources().getString(R.string.ok));
                okHttpQueryDerviceList();
                break;
            case SETDELAYED:
//                toToask(getResources().getString(R.string.ok));
                okHttpQueryDerviceList();
                break;
            case GETLIST:
                lists.clear();
                Log.e("saiyi", sequest);
                //避免刷新失败_拿到数据之后在相加
                number = number + 11;
                //停止刷新
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadmore();
                if (number > 99) {//加载完成
                    mRefreshLayout.setLoadmoreFinished(true);//全部加载完成__true 全部
                } else {
                    mRefreshLayout.setLoadmoreFinished(false);//全部加载完成__true 全部
                }
                try {
                    JSONObject jsonObject = new JSONObject(sequest);
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
                    if (adapter == null) {
                        adapter = new AreaSetAdapter(getActivity(), lists, this);
                        mRecyclerView.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            default:

                break;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        getActivity().unregisterReceiver(myReceiver);

    }

    //获取数据失败,或数据异常的回调
    @Override
    public void onFailure(String exp) {
        toToask(getResources().getString(R.string.no));
        //停止刷新
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadmore();
    }

    //防区开关设置
    @Override
    public void setSector(int position, int type) {

        Log.e("_",""+position);
        if (type==1){
            if (position<=8) {
                bigModle.getPutMac(getActivity(), ConstantInterface.MAC, ConstantInterface.MAC + "01" +"0"+ (position + 1), 0, this);
            }else {
                bigModle.getPutMac(getActivity(), ConstantInterface.MAC, ConstantInterface.MAC + "01" + (position + 1), 0, this);
            }
        }else {

            if (position<=8) {
                bigModle.getPutMac(getActivity(), ConstantInterface.MAC, ConstantInterface.MAC + "00" + "0"+(position + 1), 0, this);
            }else {
                bigModle.getPutMac(getActivity(), ConstantInterface.MAC, ConstantInterface.MAC + "00" + (position + 1), 0, this);
            }
        }


    }

    //修改设备名称
    @Override
    public void setEquipmentName(int position, String name) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("zoneId", lists.get(position).getZoneId());
            jsonObject.put("zoneName", name);
            bigModle.getData(getActivity(), jsonObject, EDITNAME, ConstantInterface.HTTP_SERVICE_ADDRESS_MODIFY_THE_PROPERTIES, this);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setEquipmentType(int position, String content,String mingdi) {
        //防区属性
        String numbert;
        if (position == 0) {
            numbert = "01";
        } else {
            numbert = (position + 1) + "";
            if (numbert.length() < 2) {
                numbert = "0" + numbert;
            }
        }


        if (mingdi.contains("1")){

            mingdi="2";
        }else if (mingdi.contains("2")){
            mingdi="1";
        }

        //提交指令
        bigModle.getPutMac(getActivity(), ConstantInterface.MAC, ConstantInterface.MAC + "12" + numbert +mingdi + getInstructions(content) + "1" + "1", SETEQUIPMENTTYPE, this);
    }

    //延时布防
    @Override
    public void setDelayed(int position, String number) {
        bigModle.getPutMac(getActivity(), ConstantInterface.MAC, ConstantInterface.MAC + "17" + "2" + number, SETDELAYED, this);
    }

    @Override
    public void setNewspaper(int position, String number) {
        //提交指令
        bigModle.getPutMac(getActivity(), ConstantInterface.MAC, ConstantInterface.MAC + "17" + "1" + number, SETDELAYED, this);
    }


    private String getInstructions(String data) {
        final String[] contextList = {getResources().getString(R.string.welcome), getResources().getString(R.string.old_man),
                getResources().getString(R.string.Common_defense_old),
                getResources().getString(R.string.behind), getResources().getString(R.string.smart),
                getResources().getString(R.string.Emergency_protection_zones),
                getResources().getString(R.string.Close_play),
                getResources().getString(R.string.The_doorbell_play)
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
        }else if (contextList[5].equals(data)){
            return "4";
        }else if (contextList[6].equals(data)){
            return "5";
        }else if (contextList[7].equals(data)){
            return "6";
        }
        return "";
    }


    private BroadcastReceiver myReceiver =new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action =intent.getAction();
            if (action.contains("UPDATE")){

                okHttpQueryDerviceList();

            }else if (action.contains(ConstantInterface.FANGQU_ITEM_UPDATE)){

                okHttpQueryDerviceList();

            }

        }
    };
}
