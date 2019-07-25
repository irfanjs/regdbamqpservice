package com.infy.ci.regdbamqpservice;

import java.util.HashMap;

public class ChartData {
	Object Categories;
	Object Data;
	Object month;
	Object week;

	public Object getWeek() {

		return week;
	}

	public void setWeek(HashMap<String, String> week) {
		this.week = week;
	}

	public Object getMonth() {
		return month;
	}

	public void setMonth(HashMap<String, String> month) {
		this.month = month;
	}

	public Object getCategories() {
		return Categories;
	}

	public void setCategories(Object Categories) {
		this.Categories = Categories;
	}

	public Object getData() {
		return Data;
	}

	public void setData(Object Data) {
		this.Data = Data;

	}

}
