package test.com.testdatabinding.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import test.com.testdatabinding.R;

//http://geek.csdn.net/news/detail/99435    刮奖
public class ScratchView extends View {
	private final static String tag = "ScratchView";

	private OnEraseListener onEraseListener;
	private int mMaskColor;
	private Paint mMaskPaint;
	private Bitmap mMaskBitmap;
	private Canvas mMaskCanvas;
	private Rect mRect;
	private float mEraseSize;
	private Paint mErasePaint;
	private Path mErasePath;
	private int mTouchSlop;
	private float mStartX;
	private float mStartY;
	private int mWatermarkId;
	private BitmapDrawable mWatermark;
	private int mMaxPercent = 75;
	private boolean mIsComplete;

	public ScratchView(Context context) {
		super(context);
		init(context);
	}

	public ScratchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		TypedArray typedArray = context.obtainStyledAttributes(R.styleable.ScratchView);
		mMaskColor = typedArray.getColor(R.styleable.ScratchView_maskColor, 0xffcdcdcd);
		mEraseSize = typedArray.getFloat(R.styleable.ScratchView_eraseSize, 60);
		mWatermarkId = typedArray.getResourceId(R.styleable.ScratchView_watermark, R.mipmap.ic_launcher);
		typedArray.recycle();
		setWatermark(mWatermarkId);

		//蒙层相关
		mMaskPaint = new Paint();
		mMaskPaint.setDither(true);//防抖动
		mMaskPaint.setAntiAlias(true);//抗锯齿
		setMaskColor(mMaskColor);
		//擦除相关
		mErasePaint = new Paint();
		mErasePaint.setDither(true);
		mErasePaint.setAntiAlias(true);
		mErasePaint.setStyle(Paint.Style.STROKE);
		mErasePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		mErasePaint.setStrokeCap(Paint.Cap.ROUND);
		setEraseSize(mEraseSize);
		mErasePath = new Path();

		ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
		mTouchSlop = viewConfiguration.getScaledTouchSlop();
		Log.d(tag, "mTouchSlop =" + mTouchSlop);
	}

	/**
	 * 设置水印
	 *
	 * @param resId
	 */
	private void setWatermark(int resId) {
		Log.d("setWatermark", "watermarkResId = " + resId);
		if (resId == -1) {
			return;
		}

		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
		mWatermark = new BitmapDrawable(getResources(), bitmap);
		mWatermark.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
	}

	/**
	 * 设置擦子大小
	 *
	 * @param mEraseSize
	 */
	public void setEraseSize(float mEraseSize) {
		mErasePaint.setStrokeWidth(mEraseSize);
	}

	/**
	 * 设置蒙层颜色
	 *
	 * @param mMaskColor
	 */
	public void setMaskColor(int mMaskColor) {
		mMaskPaint.setColor(mMaskColor);
		postInvalidate();
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Log.d(tag, "onDraw");


		canvas.drawBitmap(mMaskBitmap, 0, 0, mMaskPaint);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		Log.d(tag, "w=" + w + ",h=" + h);
		createMasker(w, h);
	}

	private void createMasker(int w, int h) {
		mMaskBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		mMaskCanvas = new Canvas(mMaskBitmap);
		mRect = new Rect(0, 0, w, h);
		mMaskCanvas.drawRect(mRect, mMaskPaint);

		if (mWatermark != null) {
			Rect bounds = new Rect(mRect);
			mWatermark.setBounds(bounds);
			mWatermark.draw(mMaskCanvas);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				startErase(event.getX(), event.getY());
				invalidate();
				return true;
			case MotionEvent.ACTION_MOVE:
				erase(event.getX(), event.getY());
				invalidate();
				return true;
			case MotionEvent.ACTION_UP:
				stopErase(event.getX(), event.getY());
				invalidate();
				return true;
		}
		return super.onTouchEvent(event);
	}

	private void stopErase(float x, float y) {
		mErasePath.reset();
		mStartX = 0;
		mStartY = 0;
	}

	private void erase(float x, float y) {
		int dx = (int) Math.abs(x - mStartX);
		int dy = (int) Math.abs(y - mStartY);
		if (dx >= mTouchSlop || dy >= mTouchSlop) {
			mStartX = x;
			mStartY = y;

			mErasePath.lineTo(x, y);
			mMaskCanvas.drawPath(mErasePath, mErasePaint);

			onErase();

			mErasePath.reset();
			mErasePath.moveTo(x, y);
		}
	}


	private void startErase(float x, float y) {
		mErasePath.reset();
		mErasePath.moveTo(x, y);

		mStartX = x;
		mStartY = y;
	}


	private void onErase() {
		int width = getWidth();
		int height = getHeight();
		new AsyncTask<Integer, Integer, Boolean>() {
			@Override
			protected Boolean doInBackground(Integer... integers) {
				int width = integers[0];
				int height = integers[1];
				int[] totalPix = new int[width * height];
				mMaskBitmap.getPixels(totalPix, 0, width, 0, 0, width, height);

				int transCount = 0;
				for (int i = 0; i < totalPix.length; i++) {
					if (totalPix[i] == 0) {
						transCount++;
					}
				}

				int percent = transCount * 100 / (width * height);
				publishProgress(percent);

				return percent >= mMaxPercent;
			}

			@Override
			protected void onProgressUpdate(Integer... values) {
				super.onProgressUpdate(values);
				onEraseListener.onProgress(values[0]);
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				if (result && !mIsComplete) {
					mIsComplete = true;
					onEraseListener.onComplete(ScratchView.this);
				}
			}
		}.execute(width, height);
	}

	public void setonEraseListener(OnEraseListener onEraseListener) {
		this.onEraseListener = onEraseListener;
	}

	public interface OnEraseListener {
		void onProgress(int progress);

		void onComplete(View v);
	}
}
