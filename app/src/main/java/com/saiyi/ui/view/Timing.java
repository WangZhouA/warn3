package com.saiyi.ui.view;

public class Timing {

	public int id;
	/*
	 * 时间字符
	 */
	public String tvtimingtime;
	/*
	 * 是否打开
	 */
	public int alarmclockswitch;
	/*
	 * 存放标签
	 */
	public int lable = 0;
	/*
	 * 存放选中日期,7个数字表示7天，0为关闭，1为打开
	 */
	public int[] weektime = { 0, 0, 0, 0, 0, 0, 0 };

	public String weekStr;

	public int getId() {
		return id;
	}

	public Timing(int id, String tvtimingtime, int alarmclockswitch, int lable, int[] weektime, String weekStr) {
		this.id = id;
		this.tvtimingtime = tvtimingtime;
		this.alarmclockswitch = alarmclockswitch;
		this.lable = lable;
		this.weektime = weektime;
		this.weekStr = weekStr;
	}

	public Timing() {
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWeekStr() {
		return weekStr;
	}

	public void setWeekStr(String weekStr) {
		this.weekStr = weekStr;
	}

	public int getAlarmclockswitch() {
		return alarmclockswitch;
	}

	public String getTvtimingtime() {
		return tvtimingtime;
	}

	public void setTvtimingtime(String tvtimingtime) {
		this.tvtimingtime = tvtimingtime;
	}

	public int isAlarmclockswitch() {
		return alarmclockswitch;
	}

	public void setAlarmclockswitch(int alarmclockswitch) {
		this.alarmclockswitch = alarmclockswitch;
	}

	public int getLable() {
		return lable;
	}

	public void setLable(int lable) {
		this.lable = lable;
	}

	public int[] getWeektime() {
		return weektime;
	}

	public void setWeektime(int[] weektime) {
		this.weektime = weektime;
	}

}
