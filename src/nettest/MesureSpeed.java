package nettest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MesureSpeed extends Activity implements OnClickListener {

	private static final int LOADING = 0x111;
	private static final int STOP = 0x112;
	private ProgressBar mBar;
	private int mProgressState;
	private TextView mSpeed;
	private Button mMeasureSpeed;
	private PatchItem mBack;
	private float mSpeedContent;
	private String mAddr = "http://cdn.market.hiapk.com/data/upload/2012/12_09/22/cn.lgx.phoneexpert_221804.apk";
	private String mAddr2 = "http://gdown.baidu.com/data/wisegame/6f9153d4a8d1f7d8/QQ.apk";
	private String mAddr3 = "http://gdown.baidu.com/data/wisegame/baidusearch_Android_10189_1399k.apk";
	private Handler mHandler = new Handler(Util.sTaskRunner.getLooper());
	private int testCount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(com.ndsec.wifisec.R.layout.activity_mesure_speed);
		
		mSpeed = (TextView) findViewById(com.ndsec.wifisec.R.id.speed_content);
		mMeasureSpeed = (Button) findViewById(com.ndsec.wifisec.R.id.mesure_speed);
		mBar = (ProgressBar) findViewById(com.ndsec.wifisec.R.id.bar);
		mMeasureSpeed.setOnClickListener(this);
		
		testCount = 0;
	}

	@Override
	public void onClick(View v) {
		if (mBack.isMyChild(v)) {
			Util.finish(this);
		} else if (v == mMeasureSpeed) {
			mMeasureSpeed.setEnabled(false);
			mBar.setVisibility(View.VISIBLE);
			mProgressState = 0;
			testCount = 0;
			mBar.setProgress(mProgressState);
			mHandler.removeCallbacks(null);
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					measureSpeed(mAddr);
				}
			}, 0);
		}
	}

	private Handler mProgressHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LOADING:
				mBar.setProgress(mProgressState);
				break;
			case STOP:
				mBar.setVisibility(View.GONE);
				setSpeed();
				mMeasureSpeed.setEnabled(true);
				break;
			default:
				break;
			}
		}

	};

	private void setSpeed() {
		if (mSpeedContent >= 1024) {
			mSpeedContent = (float) ((mSpeedContent) / (1024 + 0.0));
			mSpeedContent = (float) (((int) (mSpeedContent * 10) % 10 + 0.0) / 10 + (int) mSpeedContent);
			mSpeed.setText(mSpeedContent + getString(R.string.m));
		} else {
			mSpeed.setText((int) mSpeedContent + getString(R.string.kb));
		}
	}

	private void measureSpeed(String httpUrl) {
		if (!NetUtil.isWifiConnected(this) && !NetUtil.isWireConnected(this)) {
			Toast.makeText(this, getString(R.string.no_net), Toast.LENGTH_SHORT)
					.show();
			mProgressHandler.sendEmptyMessage(STOP);
			return;
		}
		int fileLen = 0;
		long startTime = 0;
		long endTime = 0;
		final String fileName = "tmp.apk";
		HttpURLConnection conn = null;
		InputStream is = null;
		FileOutputStream fos = null;
		File tmpFile = new File("/sdcard/temp");
		if (!tmpFile.exists()) {
			tmpFile.mkdir();
		}
		final File file = new File("/sdcard/temp/" + fileName);
		try {
			URL url = new URL(httpUrl);
			try {
				conn = (HttpURLConnection) url.openConnection();
				LogUtil.d("lening");
				fileLen = conn.getContentLength();
				LogUtil.d("len=" + fileLen);
				if (fileLen <= 0) {
					mSpeedContent = 0;
					mProgressHandler.sendEmptyMessage(STOP);
					Toast.makeText(this, getString(R.string.conn_fail),
							Toast.LENGTH_SHORT).show();

					return;
				}
				startTime = System.currentTimeMillis();
				is = conn.getInputStream();
				fos = new FileOutputStream(file);
				byte[] buf = new byte[256];
				conn.connect();
				if (conn.getResponseCode() >= 400) {
					Toast.makeText(this, getString(R.string.no_time),
							Toast.LENGTH_SHORT).show();
					mProgressHandler.sendEmptyMessage(STOP);
					return;
				} else {
					while (true) {
						if (is != null) {
							int numRead = is.read(buf);
							if (numRead <= 0) {
								break;
							} else {
								fos.write(buf, 0, numRead);
							}
							mProgressState += (int) (((numRead + 0.0) / (fileLen + 0.0)) * 1000000);
							mProgressHandler.sendEmptyMessage(LOADING);
							// LogUtil.d("numRead=" + numRead + "  fileLen="
							// + fileLen);
						} else {
							break;
						}
					}
				}
				endTime = System.currentTimeMillis();
			} catch (IOException e) {
				e.printStackTrace();
				Toast.makeText(this, getString(R.string.no_permission),
						Toast.LENGTH_SHORT).show();
			} finally {
				if (conn != null) {
					conn.disconnect();
				}
				try {
					if (fos != null) {
						fos.close();
					}
					if (is != null) {
						is.close();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		mSpeedContent = fileLen / (endTime - startTime);
		mProgressHandler.sendEmptyMessage(STOP);
	}
}