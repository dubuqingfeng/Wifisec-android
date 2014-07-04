package com.ndsec.wifisec;

/**
 * @author yangyu
 *	功能描述：常量工具类
 */
public class Constant {

	
	public static final class ConValue{
		
		/**
		 * Tab选项卡的图标
		 */
		public static int   mImageViewArray[] = {R.drawable.tab_icon_test,
											     R.drawable.tab_icon1,
											     R.drawable.tab_icon4,
											     R.drawable.tab_icon5};
		/**
		 * Tab选项卡的文字
		 */
		public static String mTextviewArray[] = {"基础检测", "局域网管理", "安全资讯", "关于我们"};
		
		
		/**
		 * 每一个Tab界面
		 */
		public static Class mTabClassArray[]= {ListViewActivity.class,
											   apinformation.ApInformationActivity.class,
											   apinformation.ApInformationActivity.class,
											   aboutme.ListViewActivity.class};
	}
}
