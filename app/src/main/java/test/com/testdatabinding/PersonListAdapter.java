package test.com.testdatabinding;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import test.com.testdatabinding.bean.Person;
import test.com.testdatabinding.databinding.ItemPersonInfoListBinding;

public class PersonListAdapter extends RecyclerView.Adapter {
	private Context mContext;
	private List<Person> mList;
	private OnRecyclerViewClickListener onRecyclerViewClickListener;
	public PersonListAdapter(Context context, List<Person> list) {
		mContext = context;
		mList = list;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		return new PersonViewHolder(DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_person_info_list, viewGroup, false));
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
		PersonViewHolder holder = (PersonViewHolder) viewHolder;
		holder.mDataBinding.setPerson(mList.get(i));
		holder.mDataBinding.ll.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				return onRecyclerViewClickListener.onItemLongClick(i);
			}
		});
		holder.mDataBinding.ll.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onRecyclerViewClickListener.onItemClick(i);
			}
		});
	}

	@Override
	public int getItemCount() {
		return mList != null ? mList.size() : 0;
	}

	public interface OnRecyclerViewClickListener {
		void onItemClick(int position);
		boolean onItemLongClick(int position);
	}

	public void setOnRecyclerViewClickListener(OnRecyclerViewClickListener onRecyclerViewClickListener){
		this.onRecyclerViewClickListener = onRecyclerViewClickListener;
	}

	class PersonViewHolder extends RecyclerView.ViewHolder {

		private final ItemPersonInfoListBinding mDataBinding;

		public PersonViewHolder(ViewDataBinding dataBinding) {
			super(dataBinding.getRoot());
			mDataBinding = (ItemPersonInfoListBinding) dataBinding;
		}
	}
}
