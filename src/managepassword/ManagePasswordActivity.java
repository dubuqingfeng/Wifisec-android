package managepassword;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.os.Bundle;

public class ManagePasswordActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		HttpGetData();
	}
	public void HttpGetData() {
			    try {
			        HttpClient httpclient = new DefaultHttpClient();
			        String uri = "http://192.168.1.1";
			        HttpGet get = new HttpGet(uri);
			        //添加http头信息 
			        get.addHeader("Authorization", "Basic YWRtaW46MjU2Mjc3OTU=");
			        
			        
			        HttpResponse response;
			        System.out.println("Post请求faspmg！");
			        response = httpclient.execute(get);
			        System.out.println("Post请求ing！");
			        int code = response.getStatusLine().getStatusCode();
			        //检验状态码，如果成功接收数据
			        if (code == 200) {
			                   
			            String rev = EntityUtils.toString(response.getEntity());       
			            System.out.println("Post请求成功！");
			        }
		    } catch (Exception e) {    
		    	System.out.println("Post请求error！");
			    }
		}
}
