package com.why.project.spinnerviewpopdemo.views.spinner;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import com.why.project.spinnerviewpopdemo.R;
import com.why.project.spinnerviewpopdemo.model.SpinnearModel;
import java.util.ArrayList;

/**
 * @Created HaiyuKing
 * @Used PopWindow样式的下拉菜单
 */
public class PopWindowUtil {

	/**下拉菜单的弹出窗口*/
	private static PopupWindow popupWindow = null;
	
	/**显示popupWindow弹出框*/
	public static void showPopupWindows(Context context, final View spinnerview, ArrayList<SpinnearModel> mArrayList, final MySpinnerPopListArrayAdapter.OnMyItemClickListener itemClickListener, int selecteItem){
		
		if(popupWindow != null){
			if(popupWindow.isShowing()){
				popupWindow.dismiss();
				popupWindow = null;
			}
		}
		//一个自定义的布局，作为显示的内容
		View popupWindowView = LayoutInflater.from(context).inflate(R.layout.spinnerview_pop_list_layout, null);
        
        /**在初始化contentView的时候，强制绘制contentView，并且马上初始化contentView的尺寸。
         * 另外一个点需要注意：popwindow_layout.xml的根Layout必须为LinearLayout；如果为RelativeLayout的话，会导致程序崩溃。*/
        popupWindowView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        
        //用于获取单个列表项的高度
        View listitemView = LayoutInflater.from(context).inflate(R.layout.spinnerview_pop_list_item, null);
        listitemView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        
        //列表
        ListView mListView = (ListView) popupWindowView.findViewById(R.id.list);
        MySpinnerPopListArrayAdapter mArrayAdapter = new MySpinnerPopListArrayAdapter(context, R.layout.spinnerview_pop_list_item, mArrayList,selecteItem);
		mListView.setAdapter(mArrayAdapter);
		mArrayAdapter.setOnMyItemClickListener(itemClickListener);//此处必须是自定义的adapter设置监听接口
		
		//设置选中的列表项的焦点
		mListView.setSelectionFromTop(selecteItem, 0);
		
		//实例化PopupWindow【宽度为屏幕宽度，高度为自身高度】
		//popupWindow = new PopupWindow(popupWindowView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		//【宽度为依赖控件的宽度，高度为4个列表项的高度】【添加了判断，如果小于4个，则wrap_content】
		if(mArrayList.size() <= 8){
			popupWindow = new PopupWindow(popupWindowView, spinnerview.getMeasuredWidth(), LayoutParams.WRAP_CONTENT);
		}else{
			popupWindow = new PopupWindow(popupWindowView, spinnerview.getMeasuredWidth(), listitemView.getMeasuredHeight() * 8);
		}
		
		popupWindow.setTouchable(true);//设置可以触摸
		popupWindow.setFocusable(true);//代表可以允许获取焦点的，如果有输入框的话，可以聚焦
		
		//监听popWindow的隐藏时执行的操作--这个不错
        popupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				//执行还原原始状态的操作，比如选中状态颜色高亮显示[去除],不能使用notifyDataSetInvalidated()，否则会出现popwindow显示错位的情况
				if(itemClickListener != null){
					itemClickListener.OnMyItemClick(-1);
				}
			}
		});
		
		//下面两个参数是实现点击点击外面隐藏popupwindow的
		//这里设置显示PopuWindow之后在外面点击是否有效。如果为false的话，那么点击PopuWindow外面并不会关闭PopuWindow。当然这里很明显只能在Touchable下才能使用。不设置此项则下面的捕获window外touch事件就无法触发。
		popupWindow.setOutsideTouchable(true);
		
		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
		//方式一
		ColorDrawable dw = new ColorDrawable(0x00000000);
        popupWindow.setBackgroundDrawable(dw);
        
         //int xPos = - popupWindow.getWidth() / 2 + view.getWidth() / 2;//X轴的偏移值:xoff表示x轴的偏移，正值表示向右，负值表示向左；
        int xPos = 0;//X轴的偏移值:xoff表示x轴的偏移，正值表示向左，负值表示向右；
        int yPos = 0;//Y轴的偏移值相对某个控件的位置，有偏移;yoff表示相对y轴的偏移，正值是向下，负值是向上；
        
        //=======展现在控件的下方
    	//相对于当前view进行位置设置
		popupWindow.showAsDropDown(spinnerview, xPos, yPos);
	}
	
	/**关闭列表弹出框*/
	public static void closePopupWindows(){
		if(popupWindow != null){
			if(popupWindow.isShowing()){
				popupWindow.dismiss();
				popupWindow = null;
			}
		}
	}
	
}
