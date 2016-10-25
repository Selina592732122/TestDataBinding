package test.com.testdatabinding.bean;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import test.com.testdatabinding.BR;

public class Person extends BaseObservable {
	private String name;
	private int age;
	private String imgUrl = "http://www.feizl.com/upload2007/2011_05/110505164429412.jpg";

	public Person(String name, int age) {
		this.name = name;
		this.age = age;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Bindable
	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
		notifyPropertyChanged(BR.age);
	}

	@Override
	public String toString() {
		return "Person{" +
				"name='" + name + '\'' +
				", age=" + age +
				'}';
	}
}
