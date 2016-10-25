package test.com.testdatabinding.basic;

import android.databinding.BindingAdapter;
import android.net.Uri;

import com.facebook.drawee.view.SimpleDraweeView;

public class Utils {
	@BindingAdapter({"imageUrl"})
	public static void imageLoader(SimpleDraweeView draweeView, String url) {
		Uri uri = Uri.parse(url);
		draweeView.setImageURI(uri);
	}
}  