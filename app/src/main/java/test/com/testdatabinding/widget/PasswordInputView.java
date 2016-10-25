package test.com.testdatabinding.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.EditText;

public class PasswordInputView extends EditText {
	private Paint borderPaint;
	private int borderColor;
	private float borderRadius = 10;
	private float defaultContMargin = 4;
	private float defaultSplitLineWidth = 2;
	private int textLength;
	private float passwordWidth = 5;
	private Paint passwordPaint;

	public PasswordInputView(Context context) {
		this(context, null);
	}

	public PasswordInputView(Context context, AttributeSet attrs) {
		super(context, attrs);
		borderPaint = new Paint();
		passwordPaint = new Paint();
		passwordPaint.setColor(Color.BLACK);
		borderColor = 0xFFE0E0E0;

	}

	protected void onDraw(Canvas canvas) {
		int width = getWidth();
		int height = getHeight();

		// 外边框
		RectF rect = new RectF(0, 0, width, height);
		borderPaint.setColor(borderColor);
		canvas.drawRoundRect(rect, borderRadius, borderRadius, borderPaint);

		// 内容区
		RectF rectIn = new RectF(rect.left + defaultContMargin, rect.top + defaultContMargin,
				rect.right - defaultContMargin, rect.bottom - defaultContMargin);
		borderPaint.setColor(Color.WHITE);
		canvas.drawRoundRect(rectIn, borderRadius, borderRadius, borderPaint);

		// 分割线
		borderPaint.setColor(borderColor);
		borderPaint.setStrokeWidth(defaultSplitLineWidth);
		int passwordLength = 6;
		for (int i = 1; i < passwordLength; i++) {
			float x = width * i / passwordLength;
			canvas.drawLine(x, 0, x, height, borderPaint);
		}

		// 密码
		float cx, cy = height / 2;
		float half = width / passwordLength / 2;
		for (int i = 0; i < textLength; i++) {
			cx = width * i / passwordLength + half;
			canvas.drawCircle(cx, cy, passwordWidth, passwordPaint);
		}
	}

	@Override
	protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
		super.onTextChanged(text, start, lengthBefore, lengthAfter);
		textLength = text.length();
	}
}
