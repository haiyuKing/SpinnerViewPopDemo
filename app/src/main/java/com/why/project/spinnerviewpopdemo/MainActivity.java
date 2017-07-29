package com.why.project.spinnerviewpopdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.why.project.spinnerviewpopdemo.model.SpinnearModel;
import com.why.project.spinnerviewpopdemo.views.spinner.OnSpinnerClickListener;
import com.why.project.spinnerviewpopdemo.views.spinner.OnSpinnerItemClickListener;
import com.why.project.spinnerviewpopdemo.views.spinner.SpinnerViewPop;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

	private SpinnerViewPop spinnerView_pop0;

	private SpinnerViewPop spinnerView_pop1;
	/**下拉菜单列表集合*/
	private ArrayList<SpinnearModel> mSpinner1List;

	private SpinnerViewPop spinnerView_pop2;
	/**下拉菜单列表集合*/
	private ArrayList<SpinnearModel> mSpinner2List;

	private SpinnerViewPop spinnerView_pop3;

	private TextView tv_show;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initViews();
		initDatas();
		initEvents();
	}

	private void initViews() {

		spinnerView_pop0 = (SpinnerViewPop) findViewById(R.id.spinnerView_pop0);
		spinnerView_pop0.setEditable(false);//禁用下拉菜单区域

		spinnerView_pop1 = (SpinnerViewPop) findViewById(R.id.spinnerView_pop1);
		spinnerView_pop1.setHandedPopup(true);//实现点击下拉菜单区域触发事件，一般用来隐藏软键盘，或者网络请求，最后手动弹出下拉菜单

		spinnerView_pop2 = (SpinnerViewPop) findViewById(R.id.spinnerView_pop2);
		spinnerView_pop2.setHandedPopup(false);//默认为false，点击下拉菜单区域直接弹出下拉菜单

		spinnerView_pop3 = (SpinnerViewPop) findViewById(R.id.spinnerView_pop3);
		spinnerView_pop3.setSpinnerType(SpinnerViewPop.TYPE_DIALOG);//设置对话框样式，默认为popwindow样式
		spinnerView_pop3.setHandedPopup(false);//默认为false，点击下拉菜单区域直接弹出下拉菜单

		tv_show = (TextView) findViewById(R.id.tv_show);

	}

	private void initDatas() {
		mSpinner1List = new ArrayList<SpinnearModel>();
		//模拟获取数据集合
		try{
			mSpinner1List = parseJsonArray("spinners.txt");
		}catch (Exception e) {
			e.printStackTrace();
		}
		//设置下拉菜单显示的列表项文本
		if (mSpinner1List != null && mSpinner1List.size() > 0){
			spinnerView_pop1.setData(mSpinner1List);//设置下拉菜单列表集合源
			spinnerView_pop1.setSelectedIndexAndText(0);//更改下拉菜单选中的列表项下标值
		}

		mSpinner2List = new ArrayList<SpinnearModel>();
		//模拟获取数据集合
		try{
			mSpinner2List = parseJsonArray("spinners2.txt");
		}catch (Exception e) {
			e.printStackTrace();
		}
		//设置下拉菜单显示的列表项文本
		if (mSpinner2List != null && mSpinner2List.size() > 0){
			spinnerView_pop2.setData(mSpinner2List);//设置下拉菜单列表集合源
			spinnerView_pop2.setSelectedIndexAndText(0);//更改下拉菜单选中的列表项下标值
		}

		//设置下拉菜单显示的列表项文本
		if (mSpinner1List != null && mSpinner1List.size() > 0){
			spinnerView_pop3.setData(mSpinner1List);//设置下拉菜单列表集合源
			spinnerView_pop3.setSelectedIndexAndText(0);//更改下拉菜单选中的列表项下标值
		}

	}

	private void initEvents() {

		//下拉菜单区域的点击事件监听
		spinnerView_pop1.setOnSpinnerClickListener(new OnSpinnerClickListener() {
			@Override
			public void OnFinished() {
				//KeyboardUtil.hideKeyboard(MainActivity.this);//隐藏软键盘
				spinnerView_pop1.PopupListDialog();
			}
		});

		//下拉菜单列表的列表项的点击事件监听
		spinnerView_pop1.setOnSpinnerItemClickListener(new OnSpinnerItemClickListener() {
			@Override
			public void OnFinished(int position) {
				tv_show.setText(mSpinner1List.get(position).getParaName() + ":" + mSpinner1List.get(position).getParaValue());
			}
		});

		//下拉菜单列表的列表项的点击事件监听
		spinnerView_pop2.setOnSpinnerItemClickListener(new OnSpinnerItemClickListener() {
			@Override
			public void OnFinished(int position) {
				tv_show.setText(mSpinner2List.get(position).getParaName() + ":" + mSpinner2List.get(position).getParaValue());
			}
		});

		//下拉菜单列表的列表项的点击事件监听
		spinnerView_pop3.setOnSpinnerItemClickListener(new OnSpinnerItemClickListener() {
			@Override
			public void OnFinished(int position) {
				tv_show.setText(mSpinner1List.get(position).getParaName() + ":" + mSpinner1List.get(position).getParaValue());
			}
		});
	}



	/*===========读取assets目录下的js字符串文件（js数组和js对象），然后生成List集合===========*/
	public static final String LISTROOTNODE = "spinnerList";
	public static final String KEY_LISTITEM_NAME = "paraName";
	public static final String KEY_LISTITEM_VALUE = "paraValue";
	public static final String KEY_LISTITEM_CHECKCOLOR = "checkColor";

	/**
	 * 解析JSON文件的简单数组
	 */
	private ArrayList<SpinnearModel> parseJsonArray(String fileName) throws Exception{

		ArrayList<SpinnearModel> itemsList = new ArrayList<SpinnearModel>();

		String jsonStr = getStringFromAssert(MainActivity.this, fileName);
		if(jsonStr.equals("")){
			return null;
		}
		JSONObject allData = new JSONObject(jsonStr);  //全部内容变为一个项
		JSONArray jsonArr = allData.getJSONArray(LISTROOTNODE); //取出数组
		for(int x = 0;x<jsonArr.length();x++){
			SpinnearModel model = new SpinnearModel();
			JSONObject jsonobj = jsonArr.getJSONObject(x);
			model.setParaName(jsonobj.getString(KEY_LISTITEM_NAME));
			model.setParaValue(jsonobj.getString(KEY_LISTITEM_VALUE));
			if(jsonobj.has(KEY_LISTITEM_CHECKCOLOR)){
				model.setCheckColor(jsonobj.getString(KEY_LISTITEM_CHECKCOLOR));
			}
			model.setSelectedState(false);
			itemsList.add(model);
			model = null;
		}
		return itemsList;
	}

	/**
	 * 访问assets目录下的资源文件，获取文件中的字符串
	 * @param filePath - 文件的相对路径，例如："listdata.txt"或者"/www/listdata.txt"
	 * @return 内容字符串
	 * */
	public String getStringFromAssert(Context mContext, String filePath) {

		String content = ""; // 结果字符串
		try {
			InputStream is = mContext.getResources().getAssets().open(filePath);// 打开文件
			int ch = 0;
			ByteArrayOutputStream out = new ByteArrayOutputStream(); // 实现了一个输出流
			while ((ch = is.read()) != -1) {
				out.write(ch); // 将指定的字节写入此 byte 数组输出流
			}
			byte[] buff = out.toByteArray();// 以 byte 数组的形式返回此输出流的当前内容
			out.close(); // 关闭流
			is.close(); // 关闭流
			content = new String(buff, "UTF-8"); // 设置字符串编码
		} catch (Exception e) {
			Toast.makeText(mContext, "对不起，没有找到指定文件！", Toast.LENGTH_SHORT)
					.show();
		}
		return content;
	}
}
