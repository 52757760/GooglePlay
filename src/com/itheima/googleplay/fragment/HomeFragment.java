package com.itheima.googleplay.fragment;

import java.util.List;
import java.util.Random;

import com.itheima.googleplay.R;
import com.itheima.googleplay.domain.AppInfo;
import com.itheima.googleplay.http.HttpHelper;
import com.itheima.googleplay.protocol.HomeProtocol;
import com.itheima.googleplay.tools.BitmapHelper;
import com.itheima.googleplay.tools.UiUtils;
import com.itheima.googleplay.tools.ViewUtils;
import com.itheima.googleplay.view.BaseListView;
import com.itheima.googleplay.view.LoadingPage.LoadResult;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.PauseOnScrollListener;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

public class HomeFragment extends BaseFragment {
	private List<AppInfo> datas;
	
	// 当Fragment挂载的activity创建的时候调用
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		show();
	}

	public View createSuccessView() {
		BaseListView listView=new BaseListView(UiUtils.getContext());

		listView.setAdapter(new HomeAdapter());
	
		// 第二个参数 慢慢滑动的时候是否加载图片 false  加载   true 不加载
		//  第三个参数  飞速滑动的时候是否加载图片  true 不加载 
		listView.setOnScrollListener(new PauseOnScrollListener(bitmapUtils, false, true));
		bitmapUtils.configDefaultLoadingImage(R.drawable.ic_default);  // 设置如果图片加载中显示的图片
        bitmapUtils.configDefaultLoadFailedImage(R.drawable.ic_default);// 加载失败显示的图片

		return listView;
	}
	
	private class HomeAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return datas.size();
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			ViewHolder holder;
			if(convertView==null){
				view=View.inflate(UiUtils.getContext(), R.layout.item_app, null);
				holder=new ViewHolder();
				holder.item_icon=(ImageView) view.findViewById(R.id.item_icon);
				holder.item_title=(TextView) view.findViewById(R.id.item_title);
				holder.item_size=(TextView) view.findViewById(R.id.item_size);
				holder.item_bottom=(TextView) view.findViewById(R.id.item_bottom);
				holder.item_rating=(RatingBar) view.findViewById(R.id.item_rating);
				view.setTag(holder);
			}else{
				view=convertView;
				holder=(ViewHolder) view.getTag();
			}
			AppInfo appInfo=datas.get(position);
			holder.item_title.setText(appInfo.getName());// 设置应用程序的名字
			String size=Formatter.formatFileSize(UiUtils.getContext(), appInfo.getSize());
			holder.item_size.setText(size);
			holder.item_bottom.setText(appInfo.getDes());
			float stars = appInfo.getStars();
			holder.item_rating.setRating(stars); // 设置ratingBar的值
			String iconUrl = appInfo.getIconUrl();  //http://127.0.0.1:8090/image?name=app/com.youyuan.yyhl/icon.jpg
			
			// 显示图片的控件
			bitmapUtils.display(holder.item_icon, HttpHelper.URL+"image?name="+iconUrl);
			return view;
		}
		@Override
		public Object getItem(int position) {
			return datas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
	}
	static class ViewHolder{
		ImageView item_icon;
		TextView item_title,item_size,item_bottom;
		RatingBar item_rating;
	}
	
	
	
	public LoadResult load() {
		HomeProtocol protocol=new HomeProtocol();
		datas = protocol.load(0);
		return checkData(datas);
	}
	


}
