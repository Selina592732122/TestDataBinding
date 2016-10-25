package test.com.testdatabinding.basic;

import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import test.com.testdatabinding.MainActivity;

public class Constants {
	/** 图片上传最大宽度 */
	public static final int PICTURE_MAX_X = 1000;

	private Constants() {
	}

	/**
	 * 索引图片
	 *
	 * @param index     图片索引值
	 * @param drawables 图片资源组
	 * @return 图片资源
	 */
	public static Drawable indexDrawable(int index, Drawable... drawables) {
		return drawables[index];
	}

	/**
	 * 变更文字内容颜色(变更开始位置为0)
	 *
	 * @param content 文字内容
	 * @param color   变更文字颜色(0x格式需8位)
	 * @param length  变更文字长度
	 * @return 变更后文字
	 */
	public static CharSequence foregroundColorStartWith(CharSequence content, int color, int length) {
		if (TextUtils.isEmpty(content)) {
			return content;
		}
		return foregroundColor(content, color, 0, length);
	}

	/**
	 * 变更文字内容颜色(变更开始位置为文字长度向前偏移length位)
	 *
	 * @param content 文字内容
	 * @param color   变更文字颜色(0x格式需8位)
	 * @param length  变更文字长度
	 * @return 变更后文字
	 */
	public static CharSequence foregroundColorEndWith(CharSequence content, int color, int length) {
		if (TextUtils.isEmpty(content)) {
			return content;
		}
		return foregroundColor(content, color, content.length() - length, content.length());
	}

	/**
	 * 变更文字内容颜色(变更结束位置为文字长度)
	 *
	 * @param content 文字内容
	 * @param color   变更文字颜色(0x格式需8位)
	 * @return 变更后文字
	 */
	public static CharSequence foregroundColor(CharSequence content, int color) {
		if (TextUtils.isEmpty(content)) {
			return content;
		}
		return foregroundColor(content, color, 0, content.length());
	}

	/**
	 * 变更文字内容颜色(变更结束位置为文字长度)
	 *
	 * @param content 文字内容
	 * @param color   变更文字颜色(0x格式需8位)
	 * @param start   变更开始位置
	 * @return 变更后文字
	 */
	public static CharSequence foregroundColor(CharSequence content, int color, int start) {
		if (TextUtils.isEmpty(content)) {
			return content;
		}
		return foregroundColor(content, color, start, content.length());
	}

	/**
	 * 变更文字内容颜色
	 *
	 * @param content 文字内容
	 * @param color   变更文字颜色(0x格式需8位)
	 * @param start   变更开始位置
	 * @param end     变更结束位置
	 * @return 变更后文字
	 */
	public static CharSequence foregroundColor(CharSequence content, int color, int start, int end) {
		SpannableString builder = new SpannableString(content);
		ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
		builder.setSpan(colorSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return builder;
	}

	/**
	 * 变更文字内容大小(变更开始位置为0)
	 *
	 * @param content 文字内容
	 * @param size    变更字体大小(sp)
	 * @param length  变更文字长度
	 * @return 变更后文字
	 */
	public static CharSequence absoluteSizeStartWith(CharSequence content, int size, int length) {
		if (TextUtils.isEmpty(content)) {
			return content;
		}
		return absoluteSize(content, size, 0, length);
	}

	/**
	 * 变更文字内容颜色(变更开始位置为文字长度向前偏移length位)
	 *
	 * @param content 文字内容
	 * @param size    变更字体大小(sp)
	 * @param length  变更文字长度
	 * @return 变更后文字
	 */
	public static CharSequence absoluteSizeEndWith(CharSequence content, int size, int length) {
		if (TextUtils.isEmpty(content)) {
			return content;
		}
		return absoluteSize(content, size, content.length() - length, content.length());
	}

	/**
	 * 变更文字内容大小(变更结束位置为文字长度)
	 *
	 * @param content 文字内容
	 * @param size    变更字体大小(sp)
	 * @param start   变更开始位置
	 * @return 变更后文字
	 */
	public static CharSequence absoluteSize(CharSequence content, int size, int start) {
		if (TextUtils.isEmpty(content)) {
			return content;
		}
		return absoluteSize(content, size, start, content.length());
	}

	/**
	 * 变更文字内容大小
	 *
	 * @param content 文字内容
	 * @param size    变更字体大小(sp)
	 * @param start   变更开始位置
	 * @param end     变更结束位置
	 * @return 变更后文字
	 */
	public static CharSequence absoluteSize(CharSequence content, int size, int start, int end) {
		DisplayMetrics displayMetrics = MainActivity.INSTANCE.getResources().getDisplayMetrics();
		size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size, displayMetrics);

		SpannableString builder = new SpannableString(content);
		AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(size);
		builder.setSpan(sizeSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return builder;
	}

	/**
	 * 添加删除线
	 *
	 * @param content     文字内容
	 * @param strikeColor 删除线颜色
	 * @return 变更后文字
	 */
	public static CharSequence strikeThrough(CharSequence content, final int strikeColor) {
		SpannableString builder = new SpannableString(content);
		builder.setSpan(new StrikethroughSpan() {
			@Override
			public void updateDrawState(TextPaint ds) {
				int color = ds.getColor();
				ds.setColor(strikeColor);
				super.updateDrawState(ds);
				ds.setColor(color);
			}
		}, 0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return builder;
	}

	/**
	 * 过滤0的值
	 *
	 * @param content 过滤内容
	 * @return 过滤结果
	 */
	public static CharSequence filterZero(CharSequence content) {
		if (content == null) {
			return null;
		}
		try {
			if (Double.parseDouble(content.toString()) == 0) {
				return null;
			}
		} catch (Exception ignored) {
		}
		return content;
	}

	/**
	 * 筛选null值
	 *
	 * @param contentArray 字符串数组
	 * @return 筛选结果
	 */
	public static CharSequence filterNull(String... contentArray) {
		StringBuilder builder = new StringBuilder();
		for (String contentItem : contentArray) {
			if (contentItem == null) {
				builder.append("");
			} else {
				builder.append(contentItem);
			}
		}
		return builder;
	}

	/**
	 * 调整Web内容宽度
	 *
	 * @param content Web内容
	 * @return 调整结果
	 */
	public static String wrapWebContent(String content) {
		return "<style>img{display: inline;height: auto;max-width: 100%;}</style>" + content;
	}

	/**
	 * 以友好的方式显示时间
	 *
	 * @param sDate
	 * @return
	 */
	public static String friendlyTime(String sDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
		Date time = null;
		try {
			time = dateFormat.parse(sDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (time == null) {
			return "Unknown";
		}
		String friendlyTime = "";
		Calendar cal = Calendar.getInstance();

		// 判断是否是同一天
		String curDate = dateFormat2.format(cal.getTime());
		String paramDate = dateFormat2.format(time);
		if (curDate.equals(paramDate)) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				friendlyTime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
			else
				friendlyTime = hour + "小时前";
			return friendlyTime;
		}

		long lt = time.getTime() / 86400000;
		long ct = cal.getTimeInMillis() / 86400000;
		int days = (int) (ct - lt);
		if (days == 0) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				friendlyTime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
			else
				friendlyTime = hour + "小时前";
		} else if (days == 1) {
			friendlyTime = "昨天 "+new SimpleDateFormat("HH:mm:ss").format(time);
		} else {
			friendlyTime = dateFormat2.format(time);
		}
		return friendlyTime;
	}

}
