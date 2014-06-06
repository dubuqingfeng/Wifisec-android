package apmanageaddress;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Enumeration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.DhcpInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ManageAddressActivity extends Activity {
	
	private final String TAG = "PortsScan";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(com.ndsec.wifisec.R.layout.manageaddressactivity);
	  
	    Button startScan = (Button)findViewById(com.ndsec.wifisec.R.id.ib_portscan);  
	    //button
	    startScan.setOnClickListener(new OnClickListener() {  
	           
	         

			@Override  
	         public void onClick(View arg0) {  
	             
				// TODO Auto-generated method stub  
				 TextView tv_portscan1 = (TextView) findViewById(com.ndsec.wifisec.R.id.tv_ports);
	        	 tv_portscan1.setText("");

	        	 Thread thread = new Thread(new ScanPorts(1,1000));  
	             thread.start();  
	             Toast.makeText(ManageAddressActivity.this, "开始扫描", Toast.LENGTH_LONG).show();  
	         }  
	     });    
		
		final TextView tv_ipaddress = (TextView)findViewById(com.ndsec.wifisec.R.id.tv_ipaddress);
		String ip = getLocalWifiIpAddress();
		tv_ipaddress.setText(""+ip);
		
		
		final TextView tv_gatewayaddress = (TextView)findViewById(com.ndsec.wifisec.R.id.tv_gatewayaddress);
		final String gateway = getGatewayIPAddress(this);
		tv_gatewayaddress.setText(""+gateway);
		
		//打开网关地址的按钮监听事件
	    ImageButton myImageButton = (ImageButton) findViewById(com.ndsec.wifisec.R.id.ib_openaddress);
	    myImageButton.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
	    	Uri uri = Uri.parse("http://"+gateway);  
	        startActivity(new Intent(Intent.ACTION_VIEW,uri));  
	    }
	    });
	    //添加个管理密码检测的按钮
	    //修改地址的监听事件
	    Button ib_editaddress = (Button)findViewById(com.ndsec.wifisec.R.id.ib_editaddress);
	    ib_editaddress.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tv_ipaddress.setText("hello,250");
			}
		});
	    /*
	    Button ib_portscan = (Button)findViewById(com.ndsec.wifisec.R.id.ib_portscan);
	    ib_portscan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tv_ipaddress.setText("hello,portscan");
			}
		});
		*/
	    
	}
	public String getLocalIpAddress() {  
        String ipaddress="";
       
    try {  
        for (Enumeration<NetworkInterface> en = NetworkInterface  
                .getNetworkInterfaces(); en.hasMoreElements();) {  
            NetworkInterface intf = en.nextElement();  
            for (Enumeration<InetAddress> enumIpAddr = intf  
                    .getInetAddresses(); enumIpAddr.hasMoreElements();) {  
                InetAddress inetAddress = enumIpAddr.nextElement();  
                if (!inetAddress.isLoopbackAddress()) {  
                        ipaddress=ipaddress+";"+ inetAddress.getHostAddress().toString();  
                }  
            }  
        }  
    } catch (SocketException ex) {  
        Log.e("WifiPreference IpAddress", ex.toString());  
    }  
    return ipaddress;
    } 
	
	private String getLocalWifiIpAddress() {
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        // 获取32位整型IP地址
        int ipAddress = wifiInfo.getIpAddress();
        
        //返回整型地址转换成“*.*.*.*”地址
        return String.format("%d.%d.%d.%d",
                (ipAddress & 0xff), (ipAddress >> 8 & 0xff),
                (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
    }
	public String getGatewayIPAddress(Context ctx){  
        WifiManager wifi_service = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);  
        DhcpInfo dhcpInfo = wifi_service.getDhcpInfo();  
        WifiInfo wifiinfo = wifi_service.getConnectionInfo();  
        System.out.println("Wifi info----->"+wifiinfo.getIpAddress());  
        System.out.println("DHCP info gateway----->"+Formatter.formatIpAddress(dhcpInfo.gateway));  
        System.out.println("DHCP info netmask----->"+Formatter.formatIpAddress(dhcpInfo.netmask));  
        //DhcpInfo中的ipAddress是一个int型的变量，通过Formatter将其转化为字符串IP地址  
        return Formatter.formatIpAddress(dhcpInfo.gateway);  
    }  
	 

 class ScanPorts extends Thread{  
     private int minPort, maxPort;  

     public ScanPorts(int minPort, int maxPort) {  
         this.minPort = minPort;  
         this.maxPort = maxPort;  
     }  

     @Override  
     public void run() {  
         // TODO Auto-generated method stub  
         for(int i=minPort; i<=maxPort; i++){  
             try{  
                 Socket socket = new Socket();  
                 SocketAddress socketAddress = new InetSocketAddress("192.168.1.1", i);  
                 socket.connect(socketAddress,50);  
                 handler.sendEmptyMessage(i);  
                 socket.close();  
             }catch(Exception e){  
                 Log.e(TAG, e.getMessage());  
             }  
         }  
         handler.post(new Runnable() {  
               
             @Override  
             public void run() {  
                 // TODO Auto-generated method stub  
                 Toast.makeText(ManageAddressActivity.this, "扫描完成", Toast.LENGTH_LONG).show();  
             }  
         });  
         super.run();  
     }  
       
 } 
 private Handler handler = new Handler(){ 
	 

@Override  
 public void handleMessage(Message msg) {  
	TextView tv_portscan = (TextView) findViewById(com.ndsec.wifisec.R.id.tv_ports); 
	// TODO Auto-generated method stub  
     //将已经打开的端口号显示在TextView控件上  
	 tv_portscan.append(String.valueOf(msg.what) + ":ok\n");  
     Log.i(TAG, String.valueOf(msg.what));  
     super.handleMessage(msg);  
 }  
   
}; 

}
