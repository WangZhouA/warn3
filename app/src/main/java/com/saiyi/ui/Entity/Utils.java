package com.saiyi.ui.Entity;

public class Utils {
	public static int getWidth(int height) {
		int temp = height / 9;

		return 1;
	}
//
//	public static int getHeight(int width) {
//		int temp = width / 16;
//
//		return temp * 9;
//	}

	/**
	 * 宽高比例
	 *
	 * @param widthProportion
	 * @param heightProportion
	 * @param width
	 * @return
	 */
	public static int getHeight(int widthProportion, int heightProportion, int width) {
		int temp = width / widthProportion;

		return temp * heightProportion;
	}
}