package test.com.testdatabinding.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.EditText;

import test.com.testdatabinding.R;

public class PasswordInputView2 extends EditText {
	private static int[] mFocusState = new int[6];
	//	private static int[] mNormalState = new int[]{android.R.attr.state_empty};
	private Drawable mBackgroundDrawable;
	private int textLength;
	private float passwordWidth = 20;
	private Paint passwordPaint;

	public PasswordInputView2(Context context) {
		super(context);
	}

	public PasswordInputView2(Context context, AttributeSet attrs) {
		super(context, attrs);
		mBackgroundDrawable = ContextCompat.getDrawable(context, R.drawable.bg_password_input);
		passwordPaint = new Paint();
		passwordPaint.setColor(Color.BLACK);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int width = getWidth();
		int height = getHeight();
		int childWidth = width / 6;

		int passwordLength = 6;
		for (int i = 0; i < passwordLength; i++) {
			int left = i * childWidth;
			mFocusState[i] = android.R.attr.state_empty;
			mBackgroundDrawable.setBounds(left, 0, left + childWidth, getHeight());
			mBackgroundDrawable.draw(canvas);
		}

		// 密码
		float cx, cy = height / 2;
		float half = width / passwordLength / 2;
		for (int i = 0; i < textLength; i++) {
			cx = width * i / passwordLength + half;
			canvas.drawCircle(cx, cy, passwordWidth, passwordPaint);
			if (i == textLength - 1) {
				mFocusState[i] = android.R.attr.state_focused;
				int left = i * childWidth - 15;
				mBackgroundDrawable.setBounds(left, 0, left + childWidth, getHeight());
				mBackgroundDrawable.draw(canvas);
			}

		}

	}

	@Override
	protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
		super.onTextChanged(text, start, lengthBefore, lengthAfter);
		textLength = text.length();
		invalidate();
	}
}
