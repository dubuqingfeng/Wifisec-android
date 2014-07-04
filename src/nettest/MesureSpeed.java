package nettest;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MesureSpeed extends Activity {

		     String addrs= "www.baidu.com";
			private TextView tv_result;
		        
		     public Double Ping(String str) {
		 		StringBuffer buf = new StringBuffer();
		 		String s = "";
		 		Process process;
		 		String teep = null;
		 		String teep2 = null;
		 		String Average = null;
		 		try {
		 			String pingUrl = "ping -s 1000 -c 4 " + str;
		 			process = Runtime.getRuntime().exec(pingUrl);
		 			BufferedReader br = new BufferedReader(new InputStreamReader(
		 					process.getInputStream()));

		 			while ((s = br.readLine()) != null) {
		 				buf.append(s + "");
		 			}

		 			process.waitFor();
		 			Log.e("ds", buf.toString());
		 			if (buf != null && !buf.equals("") && buf.length() > 1) {
		 				if (buf != null && buf.length() > 0) {
		 					teep = buf.substring(buf.lastIndexOf("=") + 1).trim();
		 				}
		 				if (teep != null && teep.length() > 0) {
		 					teep2 = teep.substring(teep.indexOf("/") + 1).toString();
		 				}
		 				if (teep2 != null && teep2.length() > 0) {
		 					Average = teep2.substring(0, teep2.indexOf("/")).toString();
		 				}
		 			}
		 			// Toast.makeText(this, Average, 1).show();
		 		} catch (Exception ex) {
		 			ex.printStackTrace();
		 		}
		 		if (Average != null && !Average.equals("")) {
		 			return Double.valueOf(Average);
		 		} else {
		 			return 0.0;
		 		}

		 	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(com.ndsec.wifisec.R.layout.activity_mesure_speed);
        tv_result = (TextView)findViewById(com.ndsec.wifisec.R.id.tv_testresult);     
             
           //  try
          //   {
                Double valus = Ping(addrs);
                System.out.println(addrs);	
				System.out.println("your speed is:"+(1000.0/valus)+"KB");  
				tv_result.setText(""+1000.0/valus);
          //   }

          //   catch(Exception ex)

           //  {

            //     System.out.println(ex.getMessage());

            // }

           }

         
		
	}



