package com.saiyi.interfaces;


/**
 * Created by Administrator on 2017/6/23.
 */

public interface AreaSetAdapterCallback {
    //防区设置
    void setSector(int position, int type);

    //修改防区名称
    void setEquipmentName(int position, String name);

    //设置防区类型
    void setEquipmentType(int position, String content,String mingdi);

    //设置延时布放时间
    void setDelayed(int position, String number);
//设置延时报警
    void setNewspaper(int position, String number);
}
