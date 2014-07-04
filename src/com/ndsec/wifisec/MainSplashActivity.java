package com.ndsec.wifisec;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;



public class MainSplashActivity extends Activity {
	
	
	SharedPreferences sp;
	boolean isFirst;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		final View view = View.inflate(this, R.layout.splash, null);
		
		setContentView(view);
		
		SharedPreferences sharedPreferences = getSharedPreferences("share", MODE_PRIVATE);
		final boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
		final Editor editor = sharedPreferences.edit();
		
		AlphaAnimation animation = new AlphaAnimation(0.0f,1.0f);
		animation.setDuration(2000);
		view.startAnimation(animation);
		animation.setAnimationListener(new AnimationListener(){

			
			@Override
			public void onAnimationEnd(Animation arg0) {
				//跳转activity，判断是否是第一次
				// TODO Auto-generated method stub
				
				if (isFirstRun)
				{
				//Log.d("debug", "第一次运行");
					Intent intent = new Intent(MainSplashActivity.this,SwitchActivity.class);
					startActivity(intent);
					finish();
					editor.putBoolean("isFirstRun", false);
					editor.commit();
				} else
				{
					Intent intent = new Intent(MainSplashActivity.this,MainTabActivity.class);
					startActivity(intent);
					finish();
				} 
			  
				
			}

			
			private SharedPreferences getSharedPreferences(String string,
					int modeWorldReadable) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
			
		
		//AnimationSet set = new AnimationSet(false);  
		//Animation animation = new AlphaAnimation(0,1);   //AlphaAnimation 控制渐变透明的动画效果   
		//animation.setDuration(1000);     //动画时间毫秒数   
		//set.addAnimation(animation);    //加入动画集合    
	}
}
