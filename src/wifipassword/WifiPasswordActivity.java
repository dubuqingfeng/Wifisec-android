package wifipassword;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.ndsec.wifisec.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class WifiPasswordActivity extends Activity {
	
	private final static int kSystemRootStateUnknow=-1;
    private final static int kSystemRootStateDisable=0;
    private final static int kSystemRootStateEnable=1;
    private static int systemRootState=kSystemRootStateUnknow;
	public static final String TAG = "test";
	TextView tv_show;
	Context m_context;
	List<String> m_pwd;
	private SimpleAdapter simpleAdapter;
	private List<Map<String, String>> list;
	private ListView datalist;
	int m_size;
	List<String> m_ssid;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(com.ndsec.wifisec.R.layout.wifipassword_layout);
		
		m_context = this;
		MyApplication.getInstance().addActivity(this);
		this.m_ssid = new ArrayList();
		this.m_pwd = new ArrayList();
		this.datalist = ((ListView)findViewById(com.ndsec.wifisec.R.id.datalist));
		this.tv_show = (TextView) findViewById(com.ndsec.wifisec.R.id.wifiInfo);
		LinearLayout localLinearLayout = new LinearLayout(this);
	    RelativeLayout localRelativeLayout = (RelativeLayout)findViewById(com.ndsec.wifisec.R.id.showrelati);
		
	    
	    if (!isRootSystem())
	    {
	      this.tv_show.setText("该手机没有root，请root后使用!");
	      this.tv_show.setGravity(17);
	      Toast.makeText(this.m_context, String.valueOf("该手机没有root，请root后使用!"), 1).show();
	      return;
	    }else {
			readPrivate();
			if (m_ssid.size() == 0) {
				this.datalist.setVisibility(AccessibilityNodeInfoCompat.MOVEMENT_GRANULARITY_PARAGRAPH);
				this.tv_show.setVisibility(0);
				this.tv_show.setText("没有WiFi信息");
			} else {
				this.tv_show.setVisibility(AccessibilityNodeInfoCompat.MOVEMENT_GRANULARITY_PARAGRAPH);
				this.datalist.setVisibility(0);
				for (int i = 0; ; i++)
			    {
			      if (i >= this.m_ssid.size())
			      {
			        this.simpleAdapter = new SimpleAdapter(this, this.list, com.ndsec.wifisec.R.layout.data_list, new String[] { "_id", "name" }, new int[] { com.ndsec.wifisec.R.id._id, com.ndsec.wifisec.R.id.name });
			        this.datalist.setAdapter(this.simpleAdapter);
			        this.datalist.setOnItemClickListener(new AdapterView.OnItemClickListener()
			        {
			          
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						if (!((String)WifiPasswordActivity.this.m_pwd.get(arg2)).equals(""))
			            {
							ClipboardManager clip = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
							clip.setPrimaryClip(ClipData.newPlainText(null, ""+WifiPasswordActivity.this.m_pwd.get(arg2)));  
							if (clip.hasPrimaryClip()){  
							    clip.getPrimaryClip().getItemAt(0).getText();  
							}  
							// ((ClipboardManager)WifiPasswordActivity.this.m_context.getSystemService("clipboard")).setText((CharSequence)WifiPasswordActivity.this.m_pwd.get(arg2));
			              Toast.makeText(WifiPasswordActivity.this.m_context, String.valueOf("密码已复制到剪切板!"), 0).show();
			              return;
			            }
			            Toast.makeText(WifiPasswordActivity.this.m_context, String.valueOf("密码为空!"), 0).show();
					}
			        });
			        return;
			      }
			      HashMap localHashMap = new HashMap();
			      Log.d("test", "x:" + i);
			      Log.d("test", "m_ssid:" + (String)this.m_ssid.get(i));
			      Log.d("test", "m_pwd:" + (String)this.m_pwd.get(i));
			      localHashMap.put("_id", "SSID:  " + (String)this.m_ssid.get(i));
			      localHashMap.put("name", "密码:  " + (String)this.m_pwd.get(i));
			      this.list.add(localHashMap);
			    }
			  }

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
	
	
	
	public void readPrivate() {
		DataOutputStream os;
		BufferedReader br;
		int m;
		Process process = null;
		os = null;
		try {
			process = Runtime.getRuntime().exec("su");
			String command = "cp /data/misc/wifi/wpa_supplicant.conf /mnt/sdcard/";
			
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(new StringBuilder(String.valueOf(command)).append("\n").toString());
			//os.writeBytes("exit\n");
			os.flush();
			process.waitFor();
			if (os != null) {
				os.close();
			}
			process.destroy();
			Log.d(TAG, "22223");
			Log.d(TAG, "22224");
			br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("/data/misc/wifi/wpa_supplicant.conf"))));
			m = -1;
			while (true) {
				String result = br.readLine();
				if (result == null) {
					br.close();
					return;
				} else {
					Log.d(TAG, result);
					if (result.contains("network={")) {
						m++;
						m_ssid.add(m, "");
						m_pwd.add(m, "");
					} else if (result.contains("ssid=")) {
						result = result.trim();
						Log.d(TAG, new StringBuilder("ssssssssssssssssssssss:").append(result).toString());
						if (result.startsWith("ssid=")) {
							Log.d(TAG, new StringBuilder("eeeeeeeeeeeeeee:").append(result).toString());
							String[] one_ssid = result.split("\"");
							Log.d(TAG, new StringBuilder("m:").append(m).append("  ssid:").append(one_ssid[1]).toString());
							m_ssid.set(m, one_ssid[1]);
						}
					} else if (result.contains("psk=")) {
						result = result.trim();
						if (result.contains("\"")) {
							m_pwd.set(m, result.split("\"")[1]);
						} else {
							m_pwd.set(m, result.substring(4, result.length()));
						}
					} else if (result.contains("wep_key0=")) {
						result = result.trim();
						if (result.contains("\"")) {
							m_pwd.set(m, result.split("\"")[1]);
						} else {
							m_pwd.set(m, result.substring(MotionEventCompat.ACTION_HOVER_ENTER, result.length()));
						}
					}
					Log.d(TAG, new StringBuilder("m_ssid size:").append(m_ssid.size()).toString());
					Log.d(TAG, new StringBuilder("m_pwd size:").append(m_pwd.size()).toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			m_ssid.clear();
			m_pwd.clear();
			return;
		}
	}

}
