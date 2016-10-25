package test.com.testdatabinding;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

public class PersonListSpacingItemDecoration extends RecyclerView.ItemDecoration {
	private int mSpaceSize;

	private Paint mDividerPaint;
	private int mDividerSize;

	public PersonListSpacingItemDecoration(Context context) {
		DisplayMetrics mDisplayMetrics = context.getResources().getDisplayMetrics();
		mSpaceSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, mDisplayMetrics);
		mDividerSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, mDisplayMetrics);
		mDividerPaint = new Paint();
		mDividerPaint.setColor(0xFFCDCDCD);
	}

	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
		if (view.getVisibility() == View.GONE) {
			return;
		}
		int position = parent.getChildAdapterPosition(view);
		if (position == 0) {
			outRect.top = mSpaceSize;
		}
	}

	@Override
	public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
		final int left = parent.getPaddingLeft();
		final int right = parent.getWidth() - parent.getPaddingRight();

		int childCount = parent.getChildCount();
		for (int index = 0; index < childCount; index++) {
			View child = parent.getChildAt(index);
			if (child.getVisibility() == View.GONE) {
				continue;
			}
//			if (parent.getChildAdapterPosition(child) == 0) {
//				continue;
//			}

			int top = child.getBottom() - mDividerSize;
			int bottom = top + mDividerSize;
			canvas.drawRect(left, top, right, bottom, mDividerPaint);
		}
	}
}
