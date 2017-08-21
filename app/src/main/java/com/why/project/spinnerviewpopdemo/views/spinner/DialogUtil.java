package com.why.project.spinnerviewpopdemo.views.spinner;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.why.project.spinnerviewpopdemo.R;
import com.why.project.spinnerviewpopdemo.model.SpinnearModel;

import java.util.ArrayList;

public class DialogUtil {

    /*=============列表对话框=====================*/
	private static Dialog dialog = null;
	private static View view;

    /*=============列表对话框：样式一：单选（无radio样式）=====================*/
    /**
     *@param itemClickListener - 列表项的点击事件监听：执行调用该方式的父类的自定义监听事件 */
    public static void showListDialog(Context context, ArrayList<SpinnearModel> mArrayList, final MySpinnerPopListArrayAdapter.OnMyItemClickListener itemClickListener, final int selecteItem) {
    	//引用进度列表对话框布局文件
    	view = View.inflate(context, R.layout.spinnerview_pop_list_layout, null);

		//列表
		ListView mListView = (ListView) view.findViewById(R.id.list);
		MySpinnerPopListArrayAdapter mArrayAdapter = new MySpinnerPopListArrayAdapter(context, R.layout.spinnerview_pop_list_item, mArrayList,selecteItem);
		mListView.setAdapter(mArrayAdapter);
		mArrayAdapter.setOnMyItemClickListener(itemClickListener);//此处必须是自定义的adapter设置监听接口

		//设置选中的列表项的焦点
		mListView.setSelectionFromTop(selecteItem, 0);

		dialog = new Dialog(context, R.style.dialogutil_list_style);
		//设置为false，按对话框以外的地方不起作用
		dialog.setCanceledOnTouchOutside(false);
		//设置为false，按返回键不能退出
		dialog.setCancelable(true);
		dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				if(itemClickListener != null){
					itemClickListener.OnMyItemClick(-1);
				}
			}
		});
		dialog.setContentView(view);
		dialog.show();
	}

	/*=============列表对话框：样式二：多选（radio样式）=====================*/
	/**
	 *@param itemClickListener - 列表项的点击事件监听：执行调用该方式的父类的自定义监听事件 */
	public static void showListMultDialog(Context context, ArrayList<SpinnearModel> mArrayList, final MySpinnerPopMultListArrayAdapter.OnMyMultItemClickListener itemClickListener) {
		//引用进度列表对话框布局文件
		view = View.inflate(context, R.layout.spinnerview_pop_list_layout_mult, null);

		//列表
		ListView mListView = (ListView) view.findViewById(R.id.list);
		final MySpinnerPopMultListArrayAdapter mArrayAdapter = new MySpinnerPopMultListArrayAdapter(context, R.layout.spinnerview_pop_list_item_mult, mArrayList);
		mListView.setAdapter(mArrayAdapter);
		mArrayAdapter.setOnMyItemClickListener(itemClickListener);//此处必须是自定义的adapter设置监听接口

		//确定
		TextView confirmTv = (TextView) view.findViewById(R.id.tv_confirm);
		confirmTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(itemClickListener != null){
					//局部变量监听接口，用于获取多选的选中状态列表集合，然后用于确定文本的点击事件
					ArrayList<Boolean> selecteIndexList = new ArrayList<Boolean>();
					selecteIndexList = mArrayAdapter.getSelecteIndex();
					itemClickListener.OnMyMultItemClick(selecteIndexList);//选中状态集合
				}
			}
		});
		//取消
		TextView cancelTv = (TextView) view.findViewById(R.id.tv_cancel);
		cancelTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				closeDialog();//关闭对话框，自动执行onDismiss中的方法
			}
		});

		dialog = new Dialog(context, R.style.dialogutil_list_style);
		//设置为false，按对话框以外的地方不起作用
		dialog.setCanceledOnTouchOutside(false);
		//设置为false，按返回键不能退出
		dialog.setCancelable(true);
		dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				if(itemClickListener != null){
					itemClickListener.OnMyMultItemClick(new ArrayList<Boolean>());//空的选中状态集合
				}
			}
		});
		dialog.setContentView(view);
		dialog.show();
	}
    
    /**
     * 关闭正在显示的对话框*/
	public static void closeDialog() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
			dialog = null;
		}
	}
}