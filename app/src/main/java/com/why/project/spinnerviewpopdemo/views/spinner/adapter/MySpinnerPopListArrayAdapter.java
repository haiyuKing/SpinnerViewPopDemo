package com.why.project.spinnerviewpopdemo.views.spinner.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.why.project.spinnerviewpopdemo.R;
import com.why.project.spinnerviewpopdemo.bean.SpinnearBean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static android.content.ContentValues.TAG;


public class MySpinnerPopListArrayAdapter extends ArrayAdapter<SpinnearBean>{
	
	private Context mcontext;
	private int listitemResourceid;//列表项的布局文件ID
	private int selecteIndex;//选中列表项的下标值
	/**
	 * 重写构造函数，获取列表项布局文件ID*/
	public MySpinnerPopListArrayAdapter(Context context, int resource,
										List<SpinnearBean> objects, int selecteItem) {
		super(context, resource, objects);
		listitemResourceid = resource;
		mcontext = context;
		selecteIndex = selecteItem;
	}
	
	/**
	 * 重写getView*/
	public View getView(int position, View convertView, ViewGroup parent) {
		
		SpinnearBean listitem = getItem(position);
		final int index = position;
		final ViewHolder holder;
		View view;
		if(convertView == null){
			view = LayoutInflater.from(mcontext).inflate(listitemResourceid, parent, false);
			holder = new ViewHolder();
			holder.listitemText = (TextView) view.findViewById(R.id.listitemText);
			view.setTag(holder);
		}else{
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		
		holder.listitemText.setText((CharSequence) listitem.getParaName());
		
		//列表项的点击事件
		view.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if(onMyItemClickListener != null){
					onMyItemClickListener.OnMyItemClick(index);//执行Activity界面中的方法
				}
			}
		});
		
		//如果选中的下标值等于当前下标值，则修改文本颜色为选中样式
		if(selecteIndex == position){
			holder.listitemText.setTextColor(getContext().getResources().getColor(R.color.spinnerpop_selected_text_color));
		}else{
			holder.listitemText.setTextColor(getContext().getResources().getColor(R.color.spinnerpop_normal_text_color));
		}
		
		if(! listitem.getCheckColor().equals("noData")){
			//如果checkColor有赋值后的数据，则执行下面的代码
			String checkColor = listitem.getCheckColor();

			//获取xml里设置的statelistdrawable内的各个状态对应的drawable http://blog.csdn.net/ni357103403/article/details/50402253
			StateListDrawable mySelectorGrad = (StateListDrawable)view.getBackground();

			try {
				Class slDraClass = StateListDrawable.class;
				Method getStateCountMethod = slDraClass.getDeclaredMethod("getStateCount", new Class[0]);
				Method getStateSetMethod = slDraClass.getDeclaredMethod("getStateSet", int.class);
				Method getDrawableMethod = slDraClass.getDeclaredMethod("getStateDrawable", int.class);
				int count = (Integer) getStateCountMethod.invoke(mySelectorGrad, new Object[]{});//对应item标签
				Log.d(TAG, "state count ="+count);
				for(int i=0;i < count;i++) {
					int[] stateSet = (int[]) getStateSetMethod.invoke(mySelectorGrad, i);//对应item标签中的 android:state_xxxx
					if (stateSet == null || stateSet.length == 0) {
						Log.d(TAG, "state is null");
						GradientDrawable drawable = (GradientDrawable) getDrawableMethod.invoke(mySelectorGrad, i);//这就是你要获得的Enabled为false时候的drawable
						drawable.setColor(Color.parseColor(checkColor));
					} else {
						for (int j = 0; j < stateSet.length; j++) {
							Log.d(TAG, "state =" + stateSet[j]);
							Drawable drawable = (Drawable) getDrawableMethod.invoke(mySelectorGrad, i);//这就是你要获得的Enabled为false时候的drawable
						}
					}
				}
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		return view;
	}
	//添加static 解决Type safety: Unchecked cast from Object to MySpinnerListArrayAdapter<String>.ViewHolder
	static class ViewHolder{
		
		TextView listitemText;
	}
	
	//列表项的单击事件监听接口
	public interface OnMyItemClickListener{
		void OnMyItemClick(int position);
	}
	
	public void setOnMyItemClickListener(OnMyItemClickListener onMyItemClickListener){
		this.onMyItemClickListener = onMyItemClickListener;
	}
	
	private OnMyItemClickListener onMyItemClickListener;
}
