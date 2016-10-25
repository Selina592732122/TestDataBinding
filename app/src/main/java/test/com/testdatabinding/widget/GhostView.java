package test.com.testdatabinding.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;


public class GhostView extends View {

	// View宽高
	private int mWidth, mHeight;
	private Context context;
	// 默认宽高(WRAP_CONTENT)
	private int mDefaultWidth;
	private int mDefaultHeight;
	// 画笔
	Paint mBodyPaint, mEyesPaint, mShadowPaint;
	public GhostView(Context context) {
		super(context);
		init(context);
	}

	public GhostView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public int dip2px(float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	private void init(Context context) {
		this.context = context;
		mDefaultWidth = dip2px(120);
		mDefaultHeight = dip2px(180);
		mPaddingTop = dip2px(20);

		mBodyPaint = new Paint();
		mBodyPaint.setAntiAlias(true);
		mBodyPaint.setStyle(Paint.Style.FILL);
		mBodyPaint.setColor(Color.WHITE);

		mEyesPaint = new Paint();
		mEyesPaint.setAntiAlias(true);
		mEyesPaint.setStyle(Paint.Style.FILL);
		mEyesPaint.setColor(Color.BLACK);

		mShadowPaint = new Paint();
		mShadowPaint.setAntiAlias(true);
		mShadowPaint.setStyle(Paint.Style.FILL);
		mShadowPaint.setColor(Color.argb(60, 0, 0, 0));
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
	}

	private int measureHeight(int heightMeasureSpec) {
		int mode = MeasureSpec.getMode(heightMeasureSpec);
		int size = MeasureSpec.getSize(heightMeasureSpec);
		if (mode == MeasureSpec.EXACTLY) {
			mHeight = size;
		} else if (mode == MeasureSpec.AT_MOST) {
			mHeight = mDefaultHeight;
		}
		return mHeight;
	}

	private int measureWidth(int widthMeasureSpec) {
		int mode = MeasureSpec.getMode(widthMeasureSpec);
		int size = MeasureSpec.getSize(widthMeasureSpec);
		if (mode == MeasureSpec.EXACTLY) {
			mWidth = size;
		} else if (mode == MeasureSpec.AT_MOST) {
			mWidth = mDefaultWidth;
		}
		return mWidth;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawHead(canvas);
		drawShadow(canvas);
		drawBody(canvas);
		drawEyes(canvas);
		startAnim();
	}

	// 头部的半径
	private int mHeadRadius;
	// 圆心(头部)的X坐标
	private int mHeadCentreX;
	// 圆心(头部)的Y坐标
	private int mHeadCentreY;
	// 头部最左侧的坐标
	private int mHeadLeftX;
	// 头部最右侧的坐标
	private int mHeadRightX;
	// 距离View顶部的内边距
	private int mPaddingTop;
	private void drawHead(Canvas canvas) {
		mHeadRadius = mWidth / 3;
		mHeadCentreX = mWidth / 2;
		mHeadCentreY = mWidth / 3 + mPaddingTop;
		mHeadLeftX = mHeadCentreX - mHeadRadius;
		mHeadRightX = mHeadCentreX + mHeadRadius;
		canvas.drawCircle(mHeadCentreX, mHeadCentreY, mHeadRadius, mBodyPaint);
	}
	// 影子所占区域
	private RectF mRectShadow;
	// 小鬼身体和影子之间的举例
	private int paddingShadow;

	private void drawShadow(Canvas canvas) {
		paddingShadow = mHeight / 10;
		mRectShadow = new RectF();
		mRectShadow.top = mHeight * 8 / 10;
		mRectShadow.bottom = mHeight * 9 / 10;
		mRectShadow.left = mWidth / 4;
		mRectShadow.right = mWidth * 3 / 4;
		canvas.drawArc(mRectShadow, 0, 360, false, mShadowPaint);
	}

	private Path mPath = new Path();
	// 小鬼身体胖过头部的宽度
	private int mGhostBodyWSpace;

	private void drawBody(Canvas canvas) {
		mGhostBodyWSpace = mHeadRadius * 2 / 15;
		mSkirtWidth = (mHeadRadius * 2 - mGhostBodyWSpace * 2) / mSkirtCount;
		mSkirtHeight = mHeight / 16;
		// 先画右边的身体
		mPath.moveTo(mHeadLeftX, mHeadCentreY);
		mPath.lineTo(mHeadRightX, mHeadCentreY);
		mPath.quadTo(mHeadRightX + mGhostBodyWSpace, mRectShadow.top - paddingShadow,
				mHeadRightX - mGhostBodyWSpace, mRectShadow.top - paddingShadow);


		// 从右向左画裙褶
		for (int i = 1; i <= mSkirtCount; i++) {
			if (i % 2 != 0) {
				mPath.quadTo(mHeadRightX - mGhostBodyWSpace - mSkirtWidth * i + (mSkirtWidth / 2), mRectShadow.top - paddingShadow - mSkirtHeight,
						mHeadRightX - mGhostBodyWSpace - (mSkirtWidth * i), mRectShadow.top - paddingShadow);
				canvas.drawCircle(mHeadRightX - mGhostBodyWSpace - mSkirtWidth * i + (mSkirtWidth / 2), mRectShadow.top - paddingShadow - mSkirtHeight,8,mEyesPaint);
				canvas.drawCircle(mHeadRightX - mGhostBodyWSpace - (mSkirtWidth * i), mRectShadow.top - paddingShadow,8,mEyesPaint);
			} else {
				mPath.quadTo(mHeadRightX - mGhostBodyWSpace - mSkirtWidth * i + (mSkirtWidth / 2), mRectShadow.top - paddingShadow + mSkirtHeight,
						mHeadRightX - mGhostBodyWSpace - (mSkirtWidth * i), mRectShadow.top - paddingShadow);
				canvas.drawCircle(mHeadRightX - mGhostBodyWSpace - mSkirtWidth * i + (mSkirtWidth / 2), mRectShadow.top - paddingShadow + mSkirtHeight,8,mShadowPaint);
				canvas.drawCircle(mHeadRightX - mGhostBodyWSpace - (mSkirtWidth * i), mRectShadow.top - paddingShadow,8,mShadowPaint);
			}
		}

		mPath.quadTo(mHeadLeftX - mGhostBodyWSpace, mRectShadow.top - paddingShadow, mHeadLeftX, mHeadCentreY);
		canvas.drawPath(mPath,mBodyPaint);
		canvas.drawCircle(mHeadLeftX - mGhostBodyWSpace, mRectShadow.top - paddingShadow,4,mEyesPaint);
		canvas.drawCircle(mHeadLeftX, mHeadCentreY,4,mEyesPaint);
		canvas.drawCircle(mHeadRightX + mGhostBodyWSpace, mRectShadow.top - paddingShadow,4,mEyesPaint);
		canvas.drawCircle(mHeadRightX - mGhostBodyWSpace, mRectShadow.top - paddingShadow,4,mEyesPaint);
	}

	// 单个裙褶的宽高
	private int mSkirtWidth, mSkirtHeight;
	// 裙褶的个数
	private int mSkirtCount = 7;

	private void drawEyes(Canvas canvas) {
		canvas.drawCircle(mHeadCentreX , mHeadCentreY, mHeadRadius / 6, mEyesPaint);
		canvas.drawCircle(mHeadCentreX + mHeadRadius / 2, mHeadCentreY, mHeadRadius / 6, mEyesPaint);
	}

	private void startAnim(){
		ObjectAnimator animator = ObjectAnimator.ofFloat(this,"translationX",0,500);
		animator.setRepeatMode(ObjectAnimator.RESTART);
		animator.setRepeatCount(ObjectAnimator.INFINITE);
		animator.setDuration(5000);
		animator.start();
	}
}
