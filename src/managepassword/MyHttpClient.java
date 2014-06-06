package managepassword;

import javax.net.ssl.SSLSocketFactory;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

public class MyHttpClient {  
    private static final String CHARSET = HTTP.UTF_8;  
    private static HttpClient mClient ;  
       
    private MyHttpClient(){   
    }  
    public static synchronized HttpClient getInstance(){  
        if(null == mClient){  
            HttpParams params = new BasicHttpParams();  
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);  
            HttpProtocolParams.setContentCharset(params, CHARSET) ;  
            HttpProtocolParams.setUseExpectContinue(params, true);  
            HttpProtocolParams.setUserAgent(params, "Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) " 
                        +"AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");  
            ConnManagerParams.setTimeout(params, 1000);//从连接池中取连接超时的超时时间  
            HttpConnectionParams.setConnectionTimeout(params, 2000);// http连接超时  
            HttpConnectionParams.setSoTimeout(params, 4000);// 请求超时时间  
            SchemeRegistry schReg = new SchemeRegistry();  
            
            // 使用线程安全来连接管理创建HttpClient对象  
            ClientConnectionManager conManager = new ThreadSafeClientConnManager(params,schReg);  
               
            mClient = new DefaultHttpClient(conManager,params);  
        }  
        return mClient ;  
    }  
       
    public static synchronized void closeHttpClient() {  
        if(mClient != null) {  
            mClient.getConnectionManager().shutdown();  
            mClient = null;  
        }  
    }  
}