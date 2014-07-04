package aboutme;

import aboutme.UpdateService;
import aboutme.Config;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class UpdateActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(com.ndsec.wifisec.R.layout.aboutme_list);
		checkVersion();
	}
	public void checkVersion() {

		if (Config.localVersion < Config.serverVersion) {
			Log.i("shibin", "==============================");
			// 发现新版本，提示用户更新
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("软件升级")
			.setMessage("发现新版本,建议立即更新使用.")
			.setPositiveButton("更新",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// 开启更新服务UpdateService
									// 这里为了把update更好模块化，可以传一些updateService依赖的值
									// 如布局ID，资源ID，动态获取的标题,这里以app_name为例
									
									Intent updateIntent = new Intent(
											UpdateActivity.this,
											UpdateService.class);
									updateIntent.putExtra("titleId",
											com.ndsec.wifisec.R.string.app_name);
									startService(updateIntent);
									
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});
			alert.create().show();
		} else {
			// 清理工作，略去
			// cheanUpdateFile(),文章后面我会附上代码
		}
	}
}