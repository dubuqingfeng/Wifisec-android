package wifipassword;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class WifiPasswordsActivity extends Activity {
	
	private final static int kSystemRootStateUnknow=-1;
    private final static int kSystemRootStateDisable=0;
    private final static int kSystemRootStateEnable=1;
    private static int systemRootState=kSystemRootStateUnknow;
	public static final String TAG = "test";
	TextView tv_show;
	Context m_context;
	int m_size;
	private MyHandler myHandler;
	


	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(com.ndsec.wifisec.R.layout.wifipassword_layout);
		
	//MyApplication.getInstance().addActivity(this);
		this.tv_show = (TextView) findViewById(com.ndsec.wifisec.R.id.textView1);
		myHandler = new MyHandler();
	    if (!isRootSystem())
	    {
	      this.tv_show.setText("该手机没有root，请root后使用!");
	      this.tv_show.setGravity(17);
	      Toast.makeText(this.m_context, String.valueOf("该手机没有root，请root后使用!"), 1).show();
	      return;
	    }else {
	    	MyThread m = new MyThread();
            new Thread(m).start();

	    }
	}
	
	public static boolean isRootSystem()
    {
    if(systemRootState==kSystemRootStateEnable)
    {
    return true;
    }
    else if(systemRootState==kSystemRootStateDisable)
    {
    return false;
    }
File f=null;
final String kSuSearchPaths[]={"/system/bin/","/system/xbin/","/system/sbin/","/sbin/","/vendor/bin/"};
try{
for(int i=0;i<kSuSearchPaths.length;i++)
{
f=new File(kSuSearchPaths[i]+"su");
if(f!=null&&f.exists())
{
systemRootState=kSystemRootStateEnable;
return true;
}
}
}catch(Exception e)
{
}
systemRootState=kSystemRootStateDisable;
return false;
    }
	

	 @SuppressLint("HandlerLeak")
	class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Bundle b = msg.getData();
           String info = b.getString("info");
           WifiPasswordsActivity.this.tv_show.setText(info);
           
		}
		 
	 }
	
	
	class MyThread implements Runnable {
       public void run() {
           Process process;
           StringBuilder content = new StringBuilder();
			try {
				process = Runtime.getRuntime().exec("su");			
				String cmd = "cat /data/misc/wifi/wpa_supplicant.conf";
				//String cmd = "id";
				DataOutputStream dataOutputStream = new DataOutputStream(process.getOutputStream());
				DataInputStream dataIntputStream = new DataInputStream(process.getInputStream());
				DataInputStream dataErrorStream = new DataInputStream(process.getErrorStream());
				dataOutputStream.writeBytes(cmd + "\n");			
				dataOutputStream.flush();
				Thread.sleep(2000);
				
				String line = "";
				if (dataIntputStream.available() > 0)
				{
					String error = "";
					
					int total = dataIntputStream.available();
					Log.e("TotalCount", Integer.toString(total));
					int i = 0;
					while(i < total)
					{	
						
						line = dataIntputStream.readLine();
						if(line.trim().startsWith("ssid=") || line.trim().startsWith("psk=")||line.trim().startsWith("wep_key0="))
						{
							content.append(line + "\n");
						}
						
						i += line.length() + 1;
					}
					
					dataOutputStream.close();
					dataErrorStream.close();
					dataErrorStream.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("Exception1", e.toString());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("Exception2", e.toString());
			}
                     
           Message msg = new Message();
           Bundle b = new Bundle();// 存放数据
           b.putString("info", content.toString());
           Log.e("info", content.toString());
           msg.setData(b);
           WifiPasswordsActivity.this.myHandler.sendMessage(msg); // 向Handler发送消息,更新UI
       }
   }



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(com.ndsec.wifisec.R.menu.main, menu);
		return true;
	}

}


