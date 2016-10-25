package test.com.testdatabinding;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class GlideActivity extends Activity {

	private static final int PHOTO_PICKED_WITH_DATA = 22;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_glide);
		ImageView imageView = (ImageView) findViewById(R.id.imageView);
		Glide.with(this)
				.load("http://img.my.csdn.net/uploads/201508/05/1438760757_3588.jpg")
				.skipMemoryCache(true)//禁止内存缓存
				.diskCacheStrategy(DiskCacheStrategy.NONE)//禁止磁盘缓存

				.into(imageView);

		// 必须在UI线程中调用
		//		Glide.get(context).clearMemory();//清除内存缓存
		// 必须在后台线程中调用，建议同时clearMemory()
		//		Glide.get(applicationContext).clearDiskCache();//清除磁盘缓存

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		ListView listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(new MyAdapter());
		//		setFull();
		findViewById(R.id.btnOpen).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				photoPicker();
			}
		});
	}

	private void setFull() {
		Activity at = (Activity) this;
		WindowManager.LayoutParams lp = at.getWindow().getAttributes();
		lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
		at.getWindow().setAttributes(lp);
		at.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
	}

	/**
	 * 图片选择
	 */
	private void photoPicker(){
		Intent localIntent3 = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(localIntent3, PHOTO_PICKED_WITH_DATA);
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return 8;
		}

		@Override
		public Object getItem(int i) {
			return null;
		}

		@Override
		public long getItemId(int i) {
			return 0;
		}

		@Override
		public View getView(int i, View view, ViewGroup viewGroup) {
			ViewHolder viewHolder;
			if (view == null) {
				viewHolder = new ViewHolder();
				view = getLayoutInflater().inflate(R.layout.item_edit_list, null);
				viewHolder.editText = (EditText) view.findViewById(R.id.et);
				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}

			return view;
		}

		class ViewHolder {
			EditText editText;
		}
	}
}
