package com.ndsec.wifisec;

import com.ndsec.wifisec.Constant.ConValue;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;



@SuppressLint("NewApi")
@SuppressWarnings("deprecation")
public class MainTabActivity extends TabActivity {
	//定义TabHost对象  
	private TabHost tabHost;  
	//定义RadioGroup对象  
    private RadioGroup radioGroup; 
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
 		switch (item.getItemId()) {
 		case android.R.id.home:
 			// 当ActionBar图标被点击时调用
 			System.out.println("点击了Home按钮！");
 			break;
 		}
 		return super.onOptionsItemSelected(item);
 	}
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
    @SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
    
     ActionBar actionBar = this.getActionBar(); 
     actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP, ActionBar.DISPLAY_HOME_AS_UP);

     setContentView(R.layout.activity_maintab);//Tab页面的布局
     initView();  
     initData();
 }
 /** 
  * 初始化组件 
  */  
private void initView(){  
     //实例化TabHost，得到TabHost对象  
     tabHost = getTabHost();  
       
     //得到Activity的个数  
     int count = ConValue.mTabClassArray.length;               
               
     for(int i = 0; i < count; i++){    
         //为每一个Tab按钮设置图标、文字和内容  
         TabSpec tabSpec = tabHost.newTabSpec(ConValue.mTextviewArray[i]).setIndicator(ConValue.mTextviewArray[i]).setContent(getTabItemIntent(i));  
         //将Tab按钮添加进Tab选项卡中  
         tabHost.addTab(tabSpec);  
     }  
       
     //实例化RadioGroup  
     radioGroup = (RadioGroup) findViewById(R.id.main_radiogroup);  
 }  
  
 /** 
  * 初始化组件 
  */  
    
 private void initData() {  
     // 给radioGroup设置监听事件  
     radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
         @Override  
         public void onCheckedChanged(RadioGroup group, int checkedId) {
        	 if(checkedId==R.id.RadioButton0)
        	 {
        		 tabHost.setCurrentTabByTag(ConValue.mTextviewArray[0]);  
        	 }
        	 else if(checkedId==R.id.RadioButton1){
        		 tabHost.setCurrentTabByTag(ConValue.mTextviewArray[1]);  
        	 }
        	 else if(checkedId==R.id.RadioButton2){
        		 tabHost.setCurrentTabByTag(ConValue.mTextviewArray[2]);  
        	 }
        	 else if(checkedId==R.id.RadioButton3){
        		 tabHost.setCurrentTabByTag(ConValue.mTextviewArray[3]);  
        	 }
             /*switch (checkedId) {  
             case R.id.RadioButton0:  
                 tabHost.setCurrentTabByTag(ConValue.mTextviewArray[0]);  
                 break;  
             case R.id.RadioButton1:  
                 tabHost.setCurrentTabByTag(ConValue.mTextviewArray[1]);  
                 break;  
             case R.id.RadioButton2:  
                 tabHost.setCurrentTabByTag(ConValue.mTextviewArray[2]);  
                 break;  
             case R.id.RadioButton3:  
                 tabHost.setCurrentTabByTag(ConValue.mTextviewArray[3]);  
                 break;  
             }  */
         }  
     });  
     ((RadioButton) radioGroup.getChildAt(0)).toggle();  
 }  
    
 /** 
  * 给Tab选项卡设置内容（每个内容都是一个Activity） 
  */  
 private Intent getTabItemIntent(int index){  
     Intent intent = new Intent(this, ConValue.mTabClassArray[index]);     
     return intent;  
 }  
/*@Override
public boolean onKeyDown(int keyCode, KeyEvent event) {
   // TODO Auto-generated method stub
   if (keyCode == KeyEvent.KEYCODE_BACK) {
    long mExitTime = 0;
	if ((System.currentTimeMillis() - mExitTime) > 2000) {// 如果两次按键时间间隔大于2000毫秒，则不退出
     Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
     mExitTime = System.currentTimeMillis();// 更新mExitTime
     System.out.println("dff");
    } else {
    	System.out.println("exit");
     System.exit(0);// 否则退出程序
     
    }
    return true;
   }
   return super.onKeyDown(keyCode, event);
 }
*/
@Override
public boolean dispatchKeyEvent(KeyEvent event) {
 // TODO Auto-generated method stub
  if (event.getAction()==KeyEvent.ACTION_DOWN&&event.getKeyCode()==KeyEvent.KEYCODE_BACK) {
   new AlertDialog.Builder(this)
          .setCancelable(false)
          .setTitle("温馨提示")
          .setMessage("您确定要退出吗?")
          .setPositiveButton("确定",new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) { 
                 finish();
              }
          })
          .setNegativeButton("取消", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) { 
                
              }
          }).show();
   return true;//不知道返回true或是false有什么区别??
 }
 
 return super.dispatchKeyEvent(event);
 
}
 /*
public boolean dispatchKeyEvent(KeyEvent event)  
{  
    int keyCode=event.getKeyCode();  
    switch(keyCode)  
    {  
        case KeyEvent.KEYCODE_BACK: {  
             if(event.isLongPress())  
             {  
                 this.stopService(getIntent());  
                 System.exit(0);  
                 return true;  
             }else  
             {  
                 return false;  

             }  
        }    
    }  
    return super.dispatchKeyEvent(event);  
      
}
*/
}