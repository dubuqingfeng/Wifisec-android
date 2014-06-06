package com.ndsec.wifisec;


import wificrack.WiFiCrackActivity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

 

@SuppressWarnings("deprecation")
public class MainTabActivity extends TabActivity {

 

 @Override

 public void onCreate(Bundle savedInstanceState) {

     super.onCreate(savedInstanceState);

     setContentView(R.layout.activity_maintabactivity);//Tab页面的布局

     Resources res = getResources(); 

     TabHost tabHost = getTabHost();  // The activity TabHost

//     TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);

     TabSpec spec;

     Intent intent;  // Reusable Intent for each tab

 

   //第一个TAB

     intent = new Intent(this,ListViewActivity.class);//新建一个Intent用作Tab1显示的内容（这里ShowActivity自定义）
     spec = tabHost.newTabSpec("tab1")//新建一个 Tab
     .setIndicator("基础检测", res.getDrawable(android.R.drawable.ic_media_play))//设置名称以及图标
     .setContent(intent);//设置显示的intentx
     tabHost.addTab(spec);//添加进去

 

     //第二个TAB

     intent = new Intent(this,WiFiCrackActivity.class);//第二个Intent用作Tab1显示的内容
     spec = tabHost.newTabSpec("tab2")//新建一个 Tab
     .setIndicator("局域网管理", res.getDrawable(android.R.drawable.ic_menu_camera))//设置名称以及图标
     .setContent(intent);
     tabHost.addTab(spec);

     
     intent = new Intent(this,WiFiCrackActivity.class);//第二个Intent用作Tab1显示的内容
     spec = tabHost.newTabSpec("tab3")//新建一个 Tab
     .setIndicator("安全资讯", res.getDrawable(android.R.drawable.ic_menu_camera))//设置名称以及图标
     .setContent(intent);
     tabHost.addTab(spec);
     
     intent = new Intent(this,aboutme.ListViewActivity.class);//第二个Intent用作Tab1显示的内容
     spec = tabHost.newTabSpec("tab4")//新建一个 Tab
     .setIndicator("关于我们", res.getDrawable(android.R.drawable.ic_menu_camera))//设置名称以及图标
     .setContent(intent);
     tabHost.addTab(spec);
     

     tabHost.setCurrentTab(0);//设置默认选中项 

 }
}