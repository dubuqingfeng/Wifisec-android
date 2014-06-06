package com.ndsec.wifisec;

import com.ndsec.wifisec.Params;
import wificrack.WiFiCrackActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class ListViewActivity extends Activity {
	private ListView mylistview;
	private List<Params> list = makeList();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_listview);
		mylistview = (ListView) findViewById(R.id.listview_main);
		ArrayList<HashMap<String, String>> myArrayList = new ArrayList<HashMap<String, String>>();
	
	for (Params p : list) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id",p.getId());
		map.put("name", p.getName());
		map.put("note", p.getNote());
		myArrayList.add(map);
	}
	SimpleAdapter mySimpleAdapter = new SimpleAdapter(this, myArrayList,// 数据源
			R.layout.list_items,// ListView内部数据展示形式的布局文件listitem.xml
			new String[] { "id", "name" ,"note"},// HashMap中的两个key值
														// itemTitle和itemContent
			new int[] { R.id.itemID, R.id.itemName,R.id.itemNote });/*
															 * 布局文件listitem.
															 * xml中组件的id
															 * 布局文件的各组件分别映射到HashMap的各元素上
															 * ，完成适配
															 */
	mylistview.setAdapter(mySimpleAdapter);

	/*
	 * mylistview.setOnTouchListener(new OnTouchListener(){
	 * 
	 * @Override public boolean onTouch(View v, MotionEvent event) { // TODO
	 * Auto-generated method stub if(event.getAction() ==
	 * MotionEvent.ACTION_DOWN) { mylistview.setBackgroundColor(Color.BLUE);
	 * } return false; }
	 * 
	 * });
	 */

	mylistview.setOnItemClickListener(new OnItemClickListener() {
/**
* http://www.oschina.net/question/263483_46547
*/
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			 switch (arg2){
			 //无线弱口令密码检测
				 case 0:
			  Intent intent = new Intent(ListViewActivity.this,WiFiCrackActivity.class);
              //intent.setClass(ListViewActivity.this, ListActivity.class);
			  intent.putExtra("params", list.get(arg2));  
              startActivity(intent);
              System.out.println("aaa");
              break;
              //无线路由器管理地址检测
				 case 1:
					  Intent intent1 = new Intent(ListViewActivity.this,apmanageaddress.ManageAddressActivity.class);
		              //intent.setClass(ListViewActivity.this, ListActivity.class);
					  intent1.putExtra("params", list.get(arg2));  
		              startActivity(intent1);
		              System.out.println("aaa");
		              break;
		      //路由器管理密码的检测
				 case 2:
					  Intent intent2 = new Intent(ListViewActivity.this,managepassword.ManagePasswordActivity.class);
		              //intent.setClass(ListViewActivity.this, ListActivity.class);
					  intent2.putExtra("params", list.get(arg2));  
		              startActivity(intent2);
		              System.out.println("aaa");
		              break;
		       //DNS服务器地址管理       
				 case 3:
					  Intent intent3 = new Intent(ListViewActivity.this,SwitchActivity.class);
		              //intent.setClass(ListViewActivity.this, ListActivity.class);
					  intent3.putExtra("params", list.get(arg2));  
		              startActivity(intent3);
		              System.out.println("aaa");
		              break;
		       //更改管理地址和端口
				 case 4:
					  Intent intent4 = new Intent(ListViewActivity.this,SwitchActivity.class);
		              //intent.setClass(ListViewActivity.this, ListActivity.class);
					  intent4.putExtra("params", list.get(arg2));  
		              startActivity(intent4);
		              System.out.println("aaa");
		              break;
		       //ARP地址管理
				 case 5:
					  Intent intent5 = new Intent(ListViewActivity.this,SwitchActivity.class);
		              //intent.setClass(ListViewActivity.this, ListActivity.class);
					  intent5.putExtra("params", list.get(arg2));  
		              startActivity(intent5);
		              System.out.println("aaa");
		              break;
		       //root权限WiFi密码查看
				 case 6:
					  Intent intent6 = new Intent(ListViewActivity.this,wifipassword.WifiPasswordsActivity.class);
		              //intent.setClass(ListViewActivity.this, ListActivity.class);
					  intent6.putExtra("params", list.get(arg2));  
		              startActivity(intent6);
		              System.out.println("aaa");
		              break;
		       //常见网络工具测试
				 case 7:
					  Intent intent7 = new Intent(ListViewActivity.this,nettest.TestAActivity.class);
		              //intent.setClass(ListViewActivity.this, ListActivity.class);
					  intent7.putExtra("params", list.get(arg2));  
		              startActivity(intent7);
		              System.out.println("aaa");
		              break;
		        //无线ap定位
				 case 8:
					  Intent intent8 = new Intent(ListViewActivity.this,nettest.NetTestActivity.class);
		              //intent.setClass(ListViewActivity.this, ListActivity.class);
					  intent8.putExtra("params", list.get(arg2));  
		              startActivity(intent8);
		              System.out.println("aaa");
		              break;
				 case 9:
					  Intent intent9 = new Intent(ListViewActivity.this,SwitchActivity.class);
		              //intent.setClass(ListViewActivity.this, ListActivity.class);
					  intent9.putExtra("params", list.get(arg2));  
		              startActivity(intent9);
		              System.out.println("aaa");
		              break;
		         //无线路由器信息查看
				 case 10:
					  Intent intent10 = new Intent(ListViewActivity.this,apinformation.ApInformationActivity.class);
		              //intent.setClass(ListViewActivity.this, ListActivity.class);
					  intent10.putExtra("params", list.get(arg2));  
		              startActivity(intent10);
		              
		              break;
			 }
		}

	});
}

private List<Params> makeList() {
	List<Params> l = new ArrayList<Params>();
	int i = 0;
		Params p1 = new Params();
		p1.setId("id" + i);
		i = i + 1;
		p1.setName("无线弱口令密码检测");
		p1.setNote("利用常见的弱口令检测路由器无线密码");
		l.add(p1);
		
		Params p2 = new Params();
		p2.setId("id" + i);
		i = i + 1;
		p2.setName("无线路由器管理地址检测");
		//p2.setNote("note2");
		l.add(p2);
		
		Params p3 = new Params();
		p3.setId("id" + i);
		i = i + 1;
		p3.setName("路由器管理密码检测");
		//p2.setNote("note2");
		l.add(p3);
		
		Params p4 = new Params();
		p4.setId("id" + i);
		i = i + 1;
		p4.setName("DNS服务器地址管理");
		//p2.setNote("note2");
		l.add(p4);
		
		Params p5 = new Params();
		p5.setId("id" + i);
		i = i + 1;
		p5.setName("更改管理地址和端口");
		//p2.setNote("note2");
		l.add(p5);
		
		Params p6 = new Params();
		p6.setId("id" + i);
		i = i + 1;
		p6.setName("ARP地址管理");
		//p2.setNote("note2");
		l.add(p6);
		
		Params p7 = new Params();
		p7.setId("id" + i);
		i = i + 1;
		p7.setName("root权限WiFi密码查看");
		//p2.setNote("note2");
		l.add(p7);
		
		Params p8 = new Params();
		p8.setId("id" + i);
		i = i + 1;
		p8.setName("常见网络工具测试");
		//p2.setNote("note2");
		l.add(p8);
		
		Params p9 = new Params();
		p9.setId("id" + i);
		i = i + 1;
		p9.setName("无线路由器定位");
		//p2.setNote("note2");
		l.add(p9);
		
		Params p10 = new Params();
		p10.setId("id" + i);
		i = i + 1;
		p10.setName("网络带宽测试");
		//p2.setNote("note2");
		l.add(p10);
		
		Params p11 = new Params();
		p11.setId("id" + i);
		i = i + 1;
		p11.setName("无线路由器信息查看");
		//p2.setNote("note2");
		l.add(p11);
	return l;
}
}