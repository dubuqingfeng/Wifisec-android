package nettest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ping 多个Ip拿出响应最快的那个Ip
 * 
 * @author GTF 2012-8-9
 * 
 */
public class TestAActivity extends Activity {
	private static final String TAG = "TestAActivity";
	/** Called when the activity is first created. */

	static Context ctx;

	TextView tv;
	static String[] str = { "www.hao123.com", "www.baidu.com",
			"www.google.com", "www.tudou.com", "www.youku.com" };
	static double[] ints = new double[str.length];
	static HashMap<Double, String> hashmap = new HashMap<Double, String>();
	Handler handler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (hashmap.size() == str.length) {

					if (hashmap != null && hashmap.size() > 0) {
						// 是以响应的速度，进行冒泡排序
						for (int i = 0; i < ints.length; ++i) {
							for (int j = ints.length - 1; j > i; --j) {
								if (ints[j] < ints[j - 1]) {
									double temp = ints[j];
									ints[j] = ints[j - 1];
									ints[j - 1] = temp;
								}
							}
							if (i == 0) {
								// 打印响应最快的网址
								System.err.println("响应最开的网址:"
										+ hashmap.get(ints[i]).toString());
							}
						}
					}
				}
				break;

			default:
				break;
			}
			super.dispatchMessage(msg);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		tv = new TextView(this);

		for (int i = 0; i < str.length; i++) {
			new SpeedThread(str[i], i);
		}
		setContentView(com.ndsec.wifisec.R.layout.mainswitch);
	}

	/**
	 * @param str 
	 * @return avg 的速度
	 */
	public Double Ping(String str) {
		StringBuffer buf = new StringBuffer();
		String s = "";
		Process process;
		String teep = null;
		String teep2 = null;
		String Average = null;
		try {
			String pingUrl = "ping -c 2 -w 100 " + str;
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

	/**
	 * 同步测试是响应速度
	 * 
	 * @author Administrator
	 * 
	 */
	public class SpeedThread extends Thread {
		String strping;
		int intIndex;

		public SpeedThread(String str, int i) {
			strping = str;
			intIndex = i;
			this.start();
		}

		@Override
		public void run() {
			Double ms = Ping(strping);
			Log.e(TAG, "网址：" + strping + " 响应豪秒：" + ms);
			ints[intIndex] = ms;
			hashmap.put(ms, strping);
			handler.sendEmptyMessage(1);

		}

	}
/*--------------------------------------以下是给文本框的链接加上超链接（一个及多个都可以）--------------------------------------------------------------------*/
	private void GetHl(TextView tv, String htmlLinkText) {
		String str = IsHyperLinks(htmlLinkText);
		tv.setText(Html.fromHtml(str));
		tv.setMovementMethod(LinkMovementMethod.getInstance());
		CharSequence text = tv.getText();
		if (text instanceof Spannable) {
			int end = text.length();
			Spannable sp = (Spannable) tv.getText();
			URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
			SpannableStringBuilder style = new SpannableStringBuilder(text);
			style.clearSpans();// should clear old spans
			for (URLSpan url : urls) {
				MyURLSpan myURLSpan = new MyURLSpan(url.getURL());
				style.setSpan(myURLSpan, sp.getSpanStart(url),
						sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			}
			tv.setText(style);
		}
	}

	private static class MyURLSpan extends ClickableSpan {

		private String mUrl;

		MyURLSpan(String url) {
			mUrl = url;
		}

		@Override
		public void onClick(View widget) {
			Intent intent = new Intent();
			intent.setAction("android.intent.action.VIEW");
			Uri content_url = Uri.parse(mUrl);
			intent.setData(content_url);
			ctx.startActivity(intent);

			Toast.makeText(ctx, mUrl, Toast.LENGTH_LONG).show();
			widget.setBackgroundColor(Color.parseColor("#00000000"));
		}
	}

	/**
	 * @param Content
	 * @return 超链接url
	 * @author gtf 2012-7-28
	 */
	public static String IsHyperLinks(String Content) {
		Pattern pattern = Pattern
				.compile(
						"(https?://|www).[-A-Z0-9+&@#/%?=~_|!:,.;]*[-A-Z0-9+&@#/%=~_|]",
						Pattern.CASE_INSENSITIVE);
		// Pattern pattern = Pattern.compile(regex);

		Matcher matcher = pattern.matcher(Content);
		String mache = null;
		String str = "";
		int num = 0;
		while (matcher.find()) {
			int start = matcher.start();
			int end = matcher.end();
			mache = Content.substring(start, end);
			System.out.println(mache);
			num++;
			if (num == 1) {
				if (getLookWWWStart(mache)) {
					str = Content.replace(mache,
							"<a style=\"color:red;\" href=http://" + mache
									+ "\"> " + mache + "</a>");
				} else {
					str = Content.replace(mache,
							"<a style=\"color:red;\" href=" + mache + "\"> "
									+ mache + "</a>");
				}

			} else {
				if (getLookWWWStart(mache)) {
					str = str.replace(mache,
							"<a style=\"color:red;\" href=http://" + mache
									+ "\"> " + mache + "</a>");
				} else {
					str = str.replace(mache, "<a style=\"color:red;\" href="
							+ mache + "\"> " + mache + "</a>");
				}
			}

		}

		return str;
	}

	/**
	 * 检测给www开头的url
	 * 
	 * @param str
	 * @return
	 */
	private static boolean getLookWWWStart(String str) {
		String regex = "www.\\S{0,}";
		return str.matches(regex);
	}
}