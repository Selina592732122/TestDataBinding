package test.com.testdatabinding;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.demievil.library.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import test.com.testdatabinding.bean.Person;
import test.com.testdatabinding.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
	private static final int onLoading = 0;
	private static final int onRefresh = 1;

	public static MainActivity INSTANCE;
	private ActivityMainBinding mDataBinding;
	private List<Person> mList = new ArrayList<>();
	private int i;
	private PersonListAdapter mAdapter;
	private int lastVisibleItem;
	private LinearLayoutManager mLayoutManager;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == onRefresh) {
				mList.clear();
			}
			observeData();
			mAdapter.notifyDataSetChanged();
		}
	};
	private RefreshLayout mRefreshLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		INSTANCE = this;
		mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

		observeData();
		observeList();
		observeClickListener();
	}

	/**
	 * 获取数据
	 */
	private void observeData() {
		mList.add(new Person("小明", 23));
		mList.add(new Person("小红", 24));
		mList.add(new Person("小兰", 25));
		mList.add(new Person("小花", 26));
		mList.add(new Person("小小", 27));
		mDataBinding.swipeRefreshLayout.setRefreshing(false);//刷新完成
	}

	/**
	 * 列表初始化
	 */
	private void observeList() {
		mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
		mDataBinding.recyclerView.setLayoutManager(mLayoutManager);
		mAdapter = new PersonListAdapter(this, mList);
		mDataBinding.recyclerView.setAdapter(mAdapter);
		mDataBinding.recyclerView.addItemDecoration(new PersonListSpacingItemDecoration(this));
		mDataBinding.recyclerView.setItemAnimator(new DefaultItemAnimator());
		observeRefresh();
	}

	/**
	 * swipeRefreshLayout的使用
	 */
	private void observeRefresh() {
		mDataBinding.swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent),
				getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorPrimaryDark));
		mDataBinding.swipeRefreshLayout.setOnRefreshListener(this);

		// 这句话是为了，第一次进入页面的时候显示加载进度条
		mDataBinding.swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
				.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
						.getDisplayMetrics()));

		mDataBinding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
				if (newState == RecyclerView.SCROLL_STATE_IDLE
						&& lastVisibleItem + 1 == mAdapter.getItemCount()) {
					mDataBinding.swipeRefreshLayout.setRefreshing(true);
					// 此处在现实项目中，请换成网络请求数据代码，sendRequest .....
					handler.sendEmptyMessageDelayed(onLoading, 2000);
				}
				Log.d("onScrollStateChanged", "lastVisibleItem =" + lastVisibleItem + ",newState=" + newState + ",count=" + mAdapter.getItemCount());
			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
				Log.d("onScrolled", "lastVisibleItem =" + lastVisibleItem);
			}
		});
	}

	/**
	 * 点击事件
	 */
	private void observeClickListener() {
		mDataBinding.setAddDataClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mList.add(new Person("JieBaoBao" + i++, 25));
				mAdapter.notifyDataSetChanged();
			}
		});
		mAdapter.setOnRecyclerViewClickListener(new PersonListAdapter.OnRecyclerViewClickListener() {
			@Override
			public void onItemClick(int position) {
				int age = mList.get(position).getAge() + 1;
				mList.get(position).setAge(age);
				//				mAdapter.notifyItemChanged(position);//若不写适配器刷新，则需要Person继承BaseObservable,@Bindable,notifyPropertyChanged(BR.age);
				Log.d("onItemClick", mList.toString());
			}

			@Override
			public boolean onItemLongClick(final int position) {
				new AlertDialog.Builder(MainActivity.this).setTitle("提示").setMessage("是否删除该行数据？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						mList.remove(position);
						mAdapter.notifyDataSetChanged();
					}
				}).setNegativeButton("取消", null).show();
				return true;
			}
		});
	}

	@Override
	public void onRefresh() {
		//下拉刷新
		handler.sendEmptyMessageDelayed(onRefresh, 2000);
	}
}
