package com.ndsec.wifisec;



import com.ndsec.switchLayout.OnViewChangeListener;
import com.ndsec.switchLayout.SwitchLayout;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SwitchActivity extends Activity{
    /** Called when the activity is first created. */
    SwitchLayout switchLayout;//自定义的控件
	LinearLayout linearLayout;
	int mViewCount;//自定义控件中子控件的个数
	ImageView mImageView[];//底部的imageView
	int mCurSel;//当前选中的imageView
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mainswitch);
       
        init();
    }

	private void init() {
		switchLayout = (SwitchLayout) findViewById(R.id.switchLayoutID);
		linearLayout = (LinearLayout) findViewById(R.id.linerLayoutID);
		
		//得到子控件的个数
		mViewCount = switchLayout.getChildCount();
		mImageView = new ImageView[mViewCount];
		//设置imageView
		for(int i = 0;i < mViewCount;i++){
			//得到LinearLayout中的子控件
			mImageView[i] = (ImageView) linearLayout.getChildAt(i);
			mImageView[i].setEnabled(true);//控件激活
			mImageView[i].setOnClickListener(new MOnClickListener());
			mImageView[i].setTag(i);//设置与view相关的标签
		}
		//设置第一个imageView不被激活
		mCurSel = 0;
		mImageView[mCurSel].setEnabled(false);
		switchLayout.setOnViewChangeListener(new MOnViewChangeListener());
		
	}
	
	//点击事件的监听器
	private class MOnClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			int pos = (Integer) v.getTag();
			System.out.println("pos:--" + pos);
			//设置当前显示的ImageView
			setCurPoint(pos);
			//设置自定义控件中的哪个子控件展示在当前屏幕中
			switchLayout.snapToScreen(pos);
		}
	}
	

	/**
	 * 设置当前显示的ImageView
	 * @param pos
	 */
	private void setCurPoint(int pos) {
		if(pos < 0 || pos > mViewCount -1 || mCurSel == pos)
			return;
		//当前的imgaeView将可以被激活
		mImageView[mCurSel].setEnabled(true);
		//将要跳转过去的那个imageView变成不可激活
		mImageView[pos].setEnabled(false);
		mCurSel = pos;
	}
	
	//自定义控件中View改变的事件监听
	private class MOnViewChangeListener implements OnViewChangeListener{
		@Override
		public void onViewChange(int view) {
			System.out.println("view:--" + view);
			if(view < 0 || mCurSel == view){
				return ;
			}else if(view > mViewCount - 1){
				//当滚动到第五个的时候activity会被关闭
				System.out.println("finish activity");
				finish();
			}
			setCurPoint(view);
		}
		
	}
	
}