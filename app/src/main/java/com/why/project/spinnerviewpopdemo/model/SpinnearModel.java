package com.why.project.spinnerviewpopdemo.model;

/**
 * Used 下拉菜单的类
 */
public class SpinnearModel {

	/**下拉菜单项的文字*/
	private String paraName;
	/**下拉菜单项的value值*/
	private String paraValue;
	/**下拉菜单项的是否选中状态标记值*/
	private boolean selectedState;
	
	/**下拉菜单项的背景颜色值:例如：#C5C5C5*/
	private String checkColor = "noData";//默认值为noData，用来判断是否赋值过这个字段
	
	public SpinnearModel(){
		selectedState = false;
		checkColor = "noData";
	}
	
	public String getParaName() {
		return paraName;
	}
	public void setParaName(String paraName) {
		this.paraName = paraName;
	}
	public String getParaValue() {
		return paraValue;
	}
	public void setParaValue(String paraValue) {
		this.paraValue = paraValue;
	}
	public boolean isSelectedState() {
		return selectedState;
	}
	public void setSelectedState(boolean selectedState) {
		this.selectedState = selectedState;
	}

	public String getCheckColor() {
		return checkColor;
	}

	public void setCheckColor(String checkColor) {
		this.checkColor = checkColor;
	}
}
