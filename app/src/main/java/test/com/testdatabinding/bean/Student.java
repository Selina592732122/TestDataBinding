package test.com.testdatabinding.bean;

import java.util.List;

public class Student {
	private String name;
	private int age;
	private List<Course> list;

	public Student(String name, int age, List<Course> list) {
		this.name = name;
		this.age = age;
		this.list = list;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public List<Course> getList() {
		return list;
	}

	public void setList(List<Course> list) {
		this.list = list;
	}
}
