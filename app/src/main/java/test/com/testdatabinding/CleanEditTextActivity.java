package test.com.testdatabinding;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import test.com.testdatabinding.bean.Course;
import test.com.testdatabinding.bean.Student;


public class CleanEditTextActivity extends AppCompatActivity {
	private static final String tag = "RxJava";
	private ImageView mIv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clean_edit_text);
		mIv = (ImageView) findViewById(R.id.iv);
		//		testRxJava();
		testMap();


	}

	private void testMap() {
		//		Observable.just("0.00", "1.00", "2.00").subscribe(new Action1<String>() {
		//			@Override
		//			public void call(String s) {
		//				Log.d(tag, "字符串" + s);
		//			}
		//		});
		//
		//		Observable.just("0", "1", "2")
		//				.map(new Func1<String, Double>() {
		//					@Override
		//					public Double call(String s) {
		//						return Double.valueOf(s);
		//					}
		//				})
		//				.subscribe(new Action1<Double>() {
		//					@Override
		//					public void call(Double d) {
		//						Log.d(tag, "转换结果：" + d);
		//					}
		//				});

		//		Student[] stu = {new Student("chen", 1), new Student("qing", 2), new Student("jie", 3)};
		ArrayList<Student> stu = new ArrayList<>();
//		stu.add(new Student("chen", 10, null));
//		stu.add(new Student("qing", 11, null));
//		stu.add(new Student("jie", 12, null));
//		Observable.from(stu).map(new Func1<Student, String>() {
//			@Override
//			public String call(Student student) {
//				return student.getName();
//			}
//		}).subscribe(new Action1<String>() {
//			@Override
//			public void call(String s) {
//				Log.d(tag, "学生名字：" + s);
//			}
//		});

		List<Course> list = new ArrayList<>();
		list.add(new Course(1, "语文"));
		list.add(new Course(2, "数学"));
		list.add(new Course(3, "英语"));
		stu.add(new Student("shen", 13, list));
		List<Course> list2 = new ArrayList<>();
		list2.add(new Course(1, "语文"));
		list2.add(new Course(2, "数学"));
		list2.add(new Course(3, "英语"));
		list2.add(new Course(4, "生物"));
		list2.add(new Course(5, "体育"));
		stu.add(new Student("ling", 14, list2));

		Observable.from(stu).flatMap(new Func1<Student, Observable<Course>>() {
			@Override
			public Observable<Course> call(Student student) {
				return Observable.from(student.getList());
			}
		}).subscribe(new Action1<Course>() {
			@Override
			public void call(Course course) {
				Log.d(tag, "课程名称：" + course.getCourseName());
			}
		});


	}
	
	private void testRxJava() {
		Subscriber<String> subscriber = new Subscriber<String>() {
			@Override
			public void onCompleted() {
				Log.d(tag, "onCompleted");
			}

			@Override
			public void onError(Throwable e) {
				Log.d(tag, "onError");
			}

			@Override
			public void onNext(String s) {
				Log.d(tag, "item = " + s);
			}
		};

		//		Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
		//			@Override
		//			public void call(Subscriber<? super String> subscriber) {
		//				subscriber.onNext("3");
		//				subscriber.onNext("2");
		//				subscriber.onNext("1");
		//				subscriber.onCompleted();
		//			}
		//		});

		//		String[] str = {"a","b","c"};
		//		Observable<String> observable = Observable.from(str);

		Observable<String> observable = Observable.just("1", "2", "3");
		//		observable.subscribe(subscriber);

		Action1<String> onNext = new Action1<String>() {
			@Override
			public void call(String s) {
				Log.d(tag, "call,item = " + s);
			}
		};

		Action1<Throwable> onError = new Action1<Throwable>() {
			@Override
			public void call(Throwable throwable) {
				Log.d(tag, "onError");
			}
		};

		Action0 onComplete = new Action0() {
			@Override
			public void call() {
				Log.d(tag, "onComplete");
			}
		};

		//		observable.subscribe(onNext, onError, onComplete);


		String[] str = {"abc", "def", "ghi"};
		Observable.from(str).subscribe(new Action1<String>() {
			@Override
			public void call(String s) {
				Log.d(tag, "打印字符串：" + s);
			}
		});


		Observable.create(new Observable.OnSubscribe<Drawable>() {
			@Override
			public void call(Subscriber<? super Drawable> subscriber) {
				subscriber.onNext(ContextCompat.getDrawable(CleanEditTextActivity.this, R.mipmap.bg_password_input_focus));
				subscriber.onNext(ContextCompat.getDrawable(CleanEditTextActivity.this, R.mipmap.bg_password_input_normal));
				subscriber.onNext(ContextCompat.getDrawable(CleanEditTextActivity.this, R.mipmap.ic_clean_edit));
				subscriber.onCompleted();
			}
		})
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Drawable>() {
					@Override
					public void onCompleted() {
						Log.d(tag, "取得图片并显示-->onComplete");
					}

					@Override
					public void onError(Throwable e) {
						Log.d(tag, "onError");
					}

					@Override
					public void onNext(Drawable drawable) {
						mIv.setImageDrawable(drawable);
					}
				});

	}
}
