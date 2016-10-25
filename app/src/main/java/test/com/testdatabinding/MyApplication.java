package test.com.testdatabinding;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.umeng.socialize.PlatformConfig;

public class MyApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		Fresco.initialize(this);

		//微信 wx12342956d1cab4f9,a5ae111de7d9ea137e88a5e02c07c94d
		PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
		//豆瓣RENREN平台目前只能在服务器端配置
		PlatformConfig.setQQZone("1105603507", "R5EmukCq5uGLBEFd");
	}
}