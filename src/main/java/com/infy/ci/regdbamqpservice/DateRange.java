package com.infy.ci.regdbamqpservice;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;

public class DateRange {

	String startweek;
	String endweek;
	String startmonth;
	String endmonth;
	String start;
	String end;
	String start1;
	String end1;

	List<String> dates = new ArrayList<String>();
	List<String> datesmnth = new ArrayList<String>();

	List<String> dates1 = new ArrayList<String>();
	List<String> dates2 = new ArrayList<String>();

	public String getdaterange() {
		Gson gson = new Gson();
		ChartData cd = new ChartData();

		String json;
		HashMap<String, String> month = new HashMap();
		HashMap<String, String> week = new HashMap();

		DateRange dr = new DateRange();
		dates1 = dr.getweekdates();
		endweek = dates1.get(0);
		startweek = dates1.get(1);

		week.put("start", startweek);
		week.put("end", endweek);

		cd.setWeek(week);

		dates2 = dr.getmonthdates();

		endmonth = dates2.get(0);
		startmonth = dates2.get(1);

		month.put("start", startmonth);
		month.put("end", endmonth);

		cd.setMonth(month);

		json = gson.toJson(cd);
		return json;

	}

	public List<String> getweekdates() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		System.out.println("current date is :" + dateFormat.format(date));
		end = dateFormat.format(date);
		Calendar cal = Calendar.getInstance();

		cal.add(Calendar.DATE, -7);
		System.out.println("last week dats is :" + dateFormat.format(cal.getTime()));
		start = dateFormat.format(cal.getTime());

		dates.add(end);
		dates.add(start);
		return dates;

	}

	public List<String> getmonthdates() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		System.out.println("current date is :" + dateFormat.format(date));
		end1 = dateFormat.format(date);
		Calendar cal = Calendar.getInstance();

		cal.add(Calendar.DATE, -30);
		System.out.println("last month dats is :" + dateFormat.format(cal.getTime()));
		start1 = dateFormat.format(cal.getTime());

		datesmnth.add(end1);
		datesmnth.add(start1);
		return datesmnth;
	}

}
