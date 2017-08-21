package com.why.project.spinnerviewpopdemo.views.spinner;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.why.project.spinnerviewpopdemo.R;
import com.why.project.spinnerviewpopdemo.model.SpinnearModel;

import java.util.ArrayList;

/**
 * Created by HaiyuKing
 * Used 下拉菜单区域：自定义——继承RelativeLayout【多选对话框】
 */

public class SpinnerViewMultiDialog extends RelativeLayout {

	private static final String SEPARATOR = ";";//多选文本拼接的分隔符

	/**下拉菜单文本区域*/
	private TextView titleTextView;
	/**接收传递过来的列表项文本集合*/
	private ArrayList<SpinnearModel> mTitleTextList = null;//原始类型为String

	/**下拉菜单区域的点击事件：用于显示下拉菜单对话框*/
	private OnSpinnerClickListener listener = null;
	/**列表对话框的确定文本的点击事件：用于将选中的列表项赋值给下拉菜单区域*/
	private OnSpinnerConfirmClickListener itemListener = null;

	/**对话框是否隐藏的状态值*/
	private boolean handedPop = false;
	/**上下文，用于展现对话框的载体*/
	private Context mContext;
	private ArrayList<Boolean> selecteIndexList;

	/**下拉菜单是否可编辑*/
	private boolean canEditable = true;
	/**文本的颜色：默认黑色*/
	private int textDefaultColor = 0;

	/**
	 * 这里构造方法也很重要，不加这个很多属性不能再XML里面定义*/
	public SpinnerViewMultiDialog(Context context, AttributeSet attrs) {
		super(context, attrs);

		mContext = context;
		//引用布局：一个文本和右侧的图标
		LayoutInflater.from(context).inflate(R.layout.spinnerview_pop_view, this, true);

		//获取textview对象，并设置点击的监听事件——判断
		titleTextView = (TextView) findViewById(R.id.titleTextView);
		textDefaultColor = getResources().getColor(R.color.spinnerpop_normal_text_color);
		//默认文本颜色
		titleTextView.setTextColor(textDefaultColor);
		titleTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//弹出对话框
				if(canEditable){
					if (handedPop) {
						listener.OnFinished();
					} else {
						PopupListDialog();
					}
				}
			}
		});
	}

	/**
	 * 弹出列表对话框*/
	public void PopupListDialog() {

		showSelectedState(true);//设置下拉菜单文本框为选中/默认样式

		if (null == mTitleTextList) {
			mTitleTextList = new ArrayList<SpinnearModel>();
		}

		MySpinnerPopMultListArrayAdapter.OnMyMultItemClickListener multItemClickListener = new MySpinnerPopMultListArrayAdapter.OnMyMultItemClickListener() {
			@Override
			public void OnMyMultItemClick(ArrayList<Boolean> selecteIndexList) {
				//如果position == -1，标明是点击弹出框外面的区域
				if(selecteIndexList.size() > 0){
					DialogUtil.closeDialog();//关闭列表对话框
					//将选中状态的列表项的选中状态赋值到model类中
					setSelectedIndexAndText(selecteIndexList);

					if (null != itemListener) {
						itemListener.OnConfirmed(selecteIndexList);
					}
				}
				showSelectedState(false);//设置下拉菜单文本框为选中/默认样式
			}
		};

		//原始类型为String
		ArrayList<String> itemTextList = new ArrayList<String>();
		for(int i=0;i<mTitleTextList.size();i++){
			itemTextList.add(mTitleTextList.get(i).getParaName());
		}
		DialogUtil.showListMultDialog(mContext, mTitleTextList, multItemClickListener);
	}

	//设置列表项文本集合——常用
	public void setData(ArrayList<SpinnearModel> mArrayList) {
		this.mTitleTextList = mArrayList;
	}

	/**设置选中的下标值以及文本以及SpinnearModel中的选中状态值*/
	public void setSelectedIndexAndText(ArrayList<Boolean> selecteIndexList){

		StringBuffer selectedNames = new StringBuffer();
		for(int i=0;i<selecteIndexList.size();i++){
			mTitleTextList.get(i).setSelectedState(selecteIndexList.get(i));
			if(selecteIndexList.get(i)){//如果为true,则执行下面的代码
				selectedNames.append(mTitleTextList.get(i).getParaName() + SEPARATOR);
			}
		}
		titleTextView.setText(selectedNames);
		this.selecteIndexList = selecteIndexList;
	}

	//设置对话框的隐藏状态值
	public void setHandedPopup(boolean hand) {
		handedPop = hand;
	}

	//设置下拉菜单区域的点击事件监听
	public void setOnSpinnerClickListener(OnSpinnerClickListener listener) {
		this.listener = listener;
	}
	//设置列表对话框的确定按钮的点击事件监听
	public void setOnSpinnerConfirmClickListener(OnSpinnerConfirmClickListener itemListener) {
		this.itemListener = itemListener;
	}

	//获得文本
	public String getText() {
		return titleTextView.getText().toString();
	}

	//获得选中的列表项的下标值
	public ArrayList<Boolean> getSelecteIndexList() {
		return selecteIndexList;
	}

	//设置文本颜色
	public void setTextColor(int color){
		textDefaultColor = color;
		titleTextView.setTextColor(textDefaultColor);
	}
	//设置提示语
	public void setHint(String hint) {
		titleTextView.setHint(hint);
	}

	//设置右侧的图标
	private void setDrawableRight(Drawable rightIcon) {
		rightIcon.setBounds(0, 0, rightIcon.getMinimumWidth(), rightIcon.getMinimumHeight());
		titleTextView.setCompoundDrawables(null, null, rightIcon, null);
	}

	/*===========================是否展现选中状态===========================*/
	private void showSelectedState(boolean isSelected){
		//选中状态
		if(isSelected){
			//修改箭头图标
			setDrawableRight(ContextCompat.getDrawable(mContext,R.drawable.spinnerview_pop_icon_shang));
			//修改文本颜色
			titleTextView.setTextColor(getResources().getColor(R.color.spinnerpop_selected_text_color));
		}else{
			//修改箭头图标
			setDrawableRight(ContextCompat.getDrawable(mContext,R.drawable.spinnerview_pop_icon_xia));
			//修改文本颜色
			titleTextView.setTextColor(textDefaultColor);
		}
	}

	/**
	 * 设置是否可编辑*/
	public void setEditable(boolean canEdit){
		canEditable = canEdit;
		if(canEditable){
			//修改背景颜色--白色
			//在使用shape的同时，用代码修改shape的颜色属性http://blog.csdn.net/wangdong20/article/details/37966333
			GradientDrawable myGrad = (GradientDrawable)getBackground();
			myGrad.setColor(ContextCompat.getColor(mContext,R.color.spinnerpop_canedit_bg_color));
		}else{
			//修改背景颜色--灰色
			GradientDrawable myGrad = (GradientDrawable)getBackground();
			myGrad.setColor(ContextCompat.getColor(mContext,R.color.spinnerpop_notedit_bg_color));
		}
	}

}
