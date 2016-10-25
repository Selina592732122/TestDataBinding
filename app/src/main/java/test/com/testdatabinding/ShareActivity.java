package test.com.testdatabinding;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.yuyh.library.imgsel.ImageLoader;
import com.yuyh.library.imgsel.ImgSelActivity;
import com.yuyh.library.imgsel.ImgSelConfig;

import java.util.List;

import test.com.testdatabinding.widget.StarView;

public class ShareActivity extends AppCompatActivity {

	private static final int REQUEST_CODE_PICTURE_PICKER = 11;
	// 自定义图片加载器
	private ImageLoader loader = new ImageLoader() {
		@Override
		public void displayImage(Context context, String path, ImageView imageView) {
			// TODO 在这边可以自定义图片加载库来加载ImageView，例如Glide、Picasso、ImageLoader等
			Glide.with(context).load(path).into(imageView);
		}
	};
	private TextView tvResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);

		findViewById(R.id.btnShare).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//可以将一下代码加到你的MainActivity中，或者在任意一个需要调用分享功能的activity当中
				//				String[] mPermissionList = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS};
				//				ActivityCompat.requestPermissions(ShareActivity.this, mPermissionList, 100);


				final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
						{
								SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
								SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
						};
				UMImage image = new UMImage(ShareActivity.this, "http://www.umeng.com/images/pic/social/integrated_3.png");
				UMShareListener umShareListener = new UMShareListener() {
					@Override
					public void onResult(SHARE_MEDIA platform) {
						Toast.makeText(ShareActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onError(SHARE_MEDIA platform, Throwable throwable) {
						Toast.makeText(ShareActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onCancel(SHARE_MEDIA platform) {
						Toast.makeText(ShareActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
					}
				};

				//				new ShareAction(ShareActivity.this).setDisplayList(displaylist)
				//						.withTitle("title")
				//						.withText("——来自友盟分享面板")
				//						.withMedia(image)
				//						.withTargetUrl("https://wsq.umeng.com/")
				//						.setCallback(umShareListener)
				//						.open();
				new ShareAction(ShareActivity.this)
						.setPlatform(SHARE_MEDIA.QQ)
						.setCallback(umShareListener)
						.withText("hello umeng video")
						.withTargetUrl("http://www.baidu.com")
						.withMedia(image)
						.share();
				//				new ShareAction(ShareActivity.this).setDisplayList(displaylist)
				//						.withText("呵呵")
				//						.withTitle("title")
				//						.withTargetUrl("http://www.baidu.com")
				//						.withMedia(image)
				//						.setListenerList(umShareListener)
				//						.open();
			}
		});

		//白天模式 http://blog.csdn.net/yanzhenjie1003/article/details/52464363
		findViewById(R.id.btnDay).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
				recreate();
			}
		});
		// 夜间模式
		findViewById(R.id.btnNight).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
				recreate();
			}
		});

		tvResult = (TextView) findViewById(R.id.btnPicker);
		tvResult.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				picturePicker();
			}
		});

		StarView starView = (StarView) findViewById(R.id.starView);
		starView.setmScore(5);
	}

	private void picturePicker() {
		// 自由配置选项
		ImgSelConfig config = new ImgSelConfig.Builder(loader)
				// 是否多选
				.multiSelect(true)
				// “确定”按钮背景色
				.btnBgColor(Color.GRAY)
				// “确定”按钮文字颜色
				.btnTextColor(Color.BLUE)
				// 使用沉浸式状态栏
				.statusBarColor(Color.parseColor("#3F51B5"))
				// 返回图标ResId
				.backResId(R.mipmap.ic_clean_edit)
				// 标题
				.title("图片")
				// 标题文字颜色
				.titleColor(Color.WHITE)
				// TitleBar背景色
				.titleBgColor(Color.parseColor("#3F51B5"))
				// 裁剪大小。needCrop为true的时候配置
				.cropSize(1, 1, 200, 200)
				.needCrop(true)
				// 第一个是否显示相机
				.needCamera(false)
				// 最大选择图片数量
				.maxNum(9)
				.build();

		// 跳转到图片选择器
		ImgSelActivity.startActivity(this, config, REQUEST_CODE_PICTURE_PICKER);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//		UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

		// 图片选择结果回调
		if (requestCode == REQUEST_CODE_PICTURE_PICKER && resultCode == RESULT_OK && data != null) {
			List<String> pathList = data.getStringArrayListExtra(ImgSelActivity.INTENT_RESULT);
			for (String path : pathList) {
				tvResult.append(path + "\n");
			}
		}
	}

}
