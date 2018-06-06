package com.why.project.spinnerviewpopdemo.views.spinner.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

import com.why.project.spinnerviewpopdemo.R;
import com.why.project.spinnerviewpopdemo.bean.SpinnearBean;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by HaiyuKing
 * Used 多选对话框的适配器
 */

public class MySpinnerPopMultListArrayAdapter  extends ArrayAdapter<SpinnearBean> {

	private Context mcontext;
	private int listitemResourceid;//列表项的布局文件ID
	private ArrayList<Boolean> selecteIndex;//列表项的下标值集合，用于存储选中状态
	/**
	 * 重写构造函数，获取列表项布局文件ID*/
	public MySpinnerPopMultListArrayAdapter(Context context, int resource,
											List<SpinnearBean> objects) {
		super(context, resource, objects);
		listitemResourceid = resource;
		mcontext = context;

		selecteIndex = new ArrayList<Boolean>();
		if(objects.size() > 0){
			for(int i=0;i<objects.size();i++){
				selecteIndex.add(i,objects.get(i).isSelectedState());
			}
		}
	}

	/**
	 * 重写getView*/
	public View getView(int position, View convertView, ViewGroup parent) {

		final SpinnearBean listitem = getItem(position);
		final int index = position;

		final ViewHolder holder;
		View view;
		if(convertView == null){
			view = LayoutInflater.from(mcontext).inflate(listitemResourceid, parent, false);
			holder = new ViewHolder();
			holder.id_checkedTextView = (CheckedTextView) view.findViewById(R.id.id_checkedTextView);
			view.setTag(holder);
		}else{
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}

		holder.id_checkedTextView.setText((CharSequence) listitem.getParaName());

		//如果选中的下标值等于当前下标值，则修改文本颜色为选中样式
		if(listitem.isSelectedState()){
			setSelectedState(holder.id_checkedTextView,true);
		}else{
			setSelectedState(holder.id_checkedTextView,false);
		}

		//列表项的点击事件
		view.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				if(holder.id_checkedTextView.isSelected()){
					//如果点击之前处于选中状态，则点击后变成默认状态
					setSelectedState(holder.id_checkedTextView,false);
					selecteIndex.set(index,false);
				}else{
					setSelectedState(holder.id_checkedTextView,true);
					selecteIndex.set(index,true);
				}
			}
		});

		return view;
	}
	//添加static 解决Type safety: Unchecked cast from Object to MySpinnerListArrayAdapter<String>.ViewHolder
	static class ViewHolder{

		CheckedTextView id_checkedTextView;
	}

	/**更换选中状态的图标*/
	private void setSelectedState(CheckedTextView view, boolean selected){
		view.setSelected(selected);//关键

		if(selected){
			//设置CheckedTextView控件的android:drawableTop属性值
			Drawable drawable = ContextCompat.getDrawable(mcontext, R.drawable.spinnerview_pop_icon_ck_selected);
			//setCompoundDrawables 画的drawable的宽高是按drawable.setBound()设置的宽高
			//而setCompoundDrawablesWithIntrinsicBounds是画的drawable的宽高是按drawable固定的宽高，即通过getIntrinsicWidth()与getIntrinsicHeight()自动获得
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			view.setCompoundDrawables(null, null, drawable, null);
		}else{
			//设置CheckedTextView控件的android:drawableTop属性值
			Drawable drawable = ContextCompat.getDrawable(mcontext, R.drawable.spinnerview_pop_icon_ck_normal);
			//setCompoundDrawables 画的drawable的宽高是按drawable.setBound()设置的宽高
			//而setCompoundDrawablesWithIntrinsicBounds是画的drawable的宽高是按drawable固定的宽高，即通过getIntrinsicWidth()与getIntrinsicHeight()自动获得
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			view.setCompoundDrawables(null, null, drawable, null);
		}
	}

	public ArrayList<Boolean> getSelecteIndex() {
		return selecteIndex;
	}

	//列表项的单击事件监听接口
	public interface OnMyMultItemClickListener{
		void OnMyMultItemClick(ArrayList<Boolean> selecteIndexList);
	}

	public void setOnMyItemClickListener(OnMyMultItemClickListener onMyMultItemClickListener){
		this.onMyMultItemClickListener = onMyMultItemClickListener;
	}

	private OnMyMultItemClickListener onMyMultItemClickListener;
}
