package test.com.testdatabinding.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import test.com.testdatabinding.R;


public class StarView extends LinearLayout {

	private int mScore;
	private Rect[] mRect = new Rect[5];
	private Drawable mStarEmptyDrawable;
	private Drawable mStarFullDrawable;
	private Drawable starHalfDrawable;
	private Drawable mStarHalfDrawable;

	public StarView(Context context) {
		super(context);
	}

	public StarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		setOrientation(LinearLayout.HORIZONTAL);
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StarView);
		mScore = typedArray.getInteger(R.styleable.StarView_score, 0);
		typedArray.recycle();
		Toast.makeText(context, "" + mScore, Toast.LENGTH_SHORT).show();

		mStarEmptyDrawable = context.getResources().getDrawable(R.mipmap.ic_star_empty);
		mStarFullDrawable = context.getResources().getDrawable(R.mipmap.ic_star_full);
		mStarHalfDrawable = context.getResources().getDrawable(R.mipmap.ic_star_half);

		for (int i = 0; i < 5; i++) {
			mRect[i] = new Rect();
			ImageView imageView = new ImageView(context);
			imageView.setPadding(15, 15, 15, 15);
			if (mScore > i) {
				imageView.setImageDrawable(mStarFullDrawable);
			} else {
				imageView.setImageDrawable(mStarEmptyDrawable);
			}
			addView(imageView, new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		}

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		for (int i = 0; i < 5; i++) {
			ImageView imageView = (ImageView) getChildAt(i);
			mRect[i].left = imageView.getLeft();
			mRect[i].right = imageView.getRight();
			mRect[i].top = imageView.getTop();
			mRect[i].bottom = imageView.getBottom();
		}
	}

	public void setmScore(int score) {
		this.mScore = score;
		for (int i = 0; i < 5; i++) {
			ImageView imageView = (ImageView) getChildAt(i);
			if (mScore > i) {
				imageView.setImageDrawable(mStarFullDrawable);
			} else {
				imageView.setImageDrawable(mStarEmptyDrawable);
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		for (int i = 0; i < 5; i++) {
			if (mRect[i].contains((int) event.getX(), (int) event.getY())) {
				mScore = i + 1;
				setmScore(mScore);
			}
		}
		return super.onTouchEvent(event);
	}
}
