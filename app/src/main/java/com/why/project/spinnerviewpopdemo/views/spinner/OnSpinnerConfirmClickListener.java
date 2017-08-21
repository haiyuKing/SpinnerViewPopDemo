package com.why.project.spinnerviewpopdemo.views.spinner;

import java.util.ArrayList;

/**
 * Created by HaiyuKing
 * Used 多选对话框的确定按钮的点击事件监听
 */

public interface OnSpinnerConfirmClickListener {
	public void OnConfirmed(ArrayList<Boolean> selecteIndexList);
}
