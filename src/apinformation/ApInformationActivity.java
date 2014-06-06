package apinformation;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.widget.TextView;


public class ApInformationActivity extends Activity  {
	protected static final int GUIUPDATEIDENTIFIER = 0x101; 
	private final String TAG = "WifiExample";
	
	private WifiManager my_wifiManager;
	private WifiInfo wifiInfo;
	private DhcpInfo dhcpInfo;
	public TextView tvResult;
	private IntentFilter mWifiIntentFilter;
	private mWifiIntentReceiver mWifiIntentReceiver;
	
	
	
		Handler myHandler = new Handler() {
        public void handleMessage(Message msg) { 
             switch (msg.what) { 
                  case ApInformationActivity.GUIUPDATEIDENTIFIER: 
                	  //更新ui
                	  showWIFIDetail(); 
                       break; 
             } 
             super.handleMessage(msg); 
        }

		private void showWIFIDetail() {
			// TODO Auto-generated method stub
			StringBuilder sb = new StringBuilder();
			
			my_wifiManager = ((WifiManager) getSystemService("wifi"));
	  		dhcpInfo = my_wifiManager.getDhcpInfo();
	  		wifiInfo = my_wifiManager.getConnectionInfo();
	  		
			sb.append("当前WiFi热点信息：");
			sb.append("\nSSID(无线标识)：" + wifiInfo.getSSID());
			sb.append("\nBSSID(路由器MAC地址)：" + wifiInfo.getBSSID());
			sb.append("\nRssi(路由器信号值)：" + wifiInfo.getRssi());
			sb.append("\n(路由器连接速度)：" + wifiInfo.getLinkSpeed());
			sb.append("\nMAC地址：" + wifiInfo.getMacAddress());
			sb.append("\n网络id：" + wifiInfo.getNetworkId());
			sb.append("\n网络id：" + wifiInfo.getSupplicantState());
			sb.append("\n网络id：" + wifiInfo.getIpAddress());
			sb.append("\n网络id：" + wifiInfo.getHiddenSSID());
			sb.append("\n网络id：" + wifiInfo.describeContents());
			sb.append("\n");
			sb.append("Wifi信息：");
			sb.append("\nMacAddress：" + wifiInfo.getMacAddress());
			sb.append("\nBSSID：" + wifiInfo.getBSSID());
			sb.append("\nSSID：" + wifiInfo.getSSID());
			tvResult.setText(sb.toString());
		}

   };
		

		
   class myThread implements Runnable { 
        public void run() {
             while (!Thread.currentThread().isInterrupted()) {  
                   
                  Message message = new Message(); 
                  message.what = ApInformationActivity.GUIUPDATEIDENTIFIER; 
                  
                  ApInformationActivity.this.myHandler.sendMessage(message); 
                  
                  try { 
                       Thread.sleep(100);  
                  } catch (InterruptedException e) { 
                       Thread.currentThread().interrupt(); 
                  } 
             } 
        } 
   } 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.ndsec.wifisec.R.layout.apinformation_layout);
		
		tvResult = (TextView) findViewById(com.ndsec.wifisec.R.id.ResultView);
		
		
		mWifiIntentFilter = new IntentFilter(); 
        mWifiIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION); 
         
        mWifiIntentReceiver = new mWifiIntentReceiver(); 
        registerReceiver(mWifiIntentReceiver, mWifiIntentFilter); 
        
     
        
     /*    IntentFilter filter = new IntentFilter();
		 filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		 filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		 filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		 registerReceiver(new Apbroadcast(), filter);
*/
		
		//update ui
		new Thread(new myThread()).start();
		
	}

  

	/*	@Override
protected void onResume() {
		super.onResume();
		StringBuilder sb = new StringBuilder();
		
		sb.append("当前WiFi热点信息：");
		sb.append("\nSSID(无线标识)：" + wifiInfo.getSSID());
		sb.append("\nBSSID(路由器MAC地址)：" + wifiInfo.getBSSID());
		sb.append("\nRssi(路由器信号值)：" + wifiInfo.getRssi());
		sb.append("\n(路由器连接速度)：" + wifiInfo.getLinkSpeed());
		sb.append("\nMAC地址：" + wifiInfo.getMacAddress());
		sb.append("\n网络id：" + wifiInfo.getNetworkId());
		sb.append("\n网络id：" + wifiInfo.getSupplicantState());
		sb.append("\n网络id：" + wifiInfo.getIpAddress());
		sb.append("\n网络id：" + wifiInfo.getHiddenSSID());
		sb.append("\n网络id：" + wifiInfo.describeContents());
		
		sb.append("\nipAddress：" + intToIp(dhcpInfo.ipAddress));
		sb.append("\nnetmask：" + intToIp(dhcpInfo.netmask));
		sb.append("\ngateway：" + intToIp(dhcpInfo.gateway));
		sb.append("\nserverAddress：" + intToIp(dhcpInfo.serverAddress));
		sb.append("\ndns1：" + intToIp(dhcpInfo.dns1));
		sb.append("\ndns2：" + intToIp(dhcpInfo.dns2));
		sb.append("\n");
		System.out.println(dhcpInfo.leaseDuration);
		
		sb.append("Wifi信息：");
		sb.append("\nIpAddress：" + intToIp(wifiInfo.getIpAddress()));
		sb.append("\nMacAddress：" + wifiInfo.getMacAddress());
		sb.append("\nBSSID：" + wifiInfo.getBSSID());
		sb.append("\nSSID：" + wifiInfo.getSSID());
		tvResult.setText(sb.toString());
	}

	private String intToIp(int paramInt) {
		return (paramInt & 0xFF) + "." + (0xFF & paramInt >> 8) + "." + (0xFF & paramInt >> 16) + "."
				+ (0xFF & paramInt >> 24);
	}*/
	 private class mWifiIntentReceiver extends BroadcastReceiver{ 
		 
	        

			public void onReceive(Context context, Intent intent) { 
	 
	            @SuppressWarnings("unused")
				WifiInfo wifiInfo = ((WifiManager)getSystemService(WIFI_SERVICE)).getConnectionInfo(); 
	          
	         
	            /*
	            WifiManager.WIFI_STATE_DISABLING   正在停止
	            WifiManager.WIFI_STATE_DISABLED    已停止
	            WifiManager.WIFI_STATE_ENABLING    正在打开
	            WifiManager.WIFI_STATE_ENABLED     已开启
	            WifiManager.WIFI_STATE_UNKNOWN     未知
	             */ 
	             
	            switch (intent.getIntExtra("wifi_state", 0)) { 
	            case WifiManager.WIFI_STATE_DISABLING: 
	                Log.d(TAG, "WIFI STATUS : WIFI_STATE_DISABLING"); 
	                break; 
	            case WifiManager.WIFI_STATE_DISABLED: 
	                Log.d(TAG, "WIFI STATUS : WIFI_STATE_DISABLED"); 
	                break; 
	            case WifiManager.WIFI_STATE_ENABLING: 
	                Log.d(TAG, "WIFI STATUS : WIFI_STATE_ENABLING"); 
	                break; 
	            case WifiManager.WIFI_STATE_ENABLED: 
	                Log.d(TAG, "WIFI STATUS : WIFI_STATE_ENABLED"); 
	                break; 
	            case WifiManager.WIFI_STATE_UNKNOWN: 
	                Log.d(TAG, "WIFI STATUS : WIFI_STATE_UNKNOWN"); 
	                break; 
	        } 
	    } 
	    } 
	 
}
