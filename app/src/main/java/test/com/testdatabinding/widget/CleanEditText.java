package test.com.testdatabinding.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;

import test.com.testdatabinding.R;

public class CleanEditText extends EditText {

	private Drawable mCleanDrawable;
	private Rect mCleanRect;

	public CleanEditText(Context context) {
		this(context, null);
	}

	public CleanEditText(Context context, AttributeSet attrs) {
		super(context, attrs);

		mCleanRect = new Rect();
		mCleanDrawable = ContextCompat.getDrawable(context, R.mipmap.ic_clean_edit);

	}

	public CleanEditText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//		if (event.getAction() == MotionEvent.ACTION_UP) {
		Rect rect = mCleanDrawable.getBounds();
		int x = (int) event.getX();
		int y = (int) event.getY();
		Log.d("onTouchEvent", "x=" + x + ",y=" + y + ",rawX=" + event.getRawX() + ",rawY=" + event.getRawY());
		Log.d("onTouchEvent", "left=" + rect.left + ",right=" + rect.right + ",top" + rect.top + ",bottom" + rect.bottom);
		mCleanRect.left = getWidth() - getPaddingRight() - rect.right;
		mCleanRect.right = mCleanRect.left + rect.right;
		mCleanRect.top = getPaddingTop();
		mCleanRect.bottom = getHeight() - getPaddingBottom();

		if (mCleanRect.contains(x, y)) {
			setText("");
			toggleDrawable("");
			event.setAction(MotionEvent.ACTION_CANCEL);
		}
		//		}
		return super.onTouchEvent(event);
	}

	private void toggleDrawable(CharSequence text) {
		if (text.length() != 0) {
			setCompoundDrawablesWithIntrinsicBounds(null, null, mCleanDrawable, null);
		} else {
			setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
		}
		//		postInvalidate();
	}

	@Override
	protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
		super.onTextChanged(text, start, lengthBefore, lengthAfter);
		toggleDrawable(text);
	}

}
