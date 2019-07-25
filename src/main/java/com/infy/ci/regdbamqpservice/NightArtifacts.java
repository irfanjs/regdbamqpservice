package com.infy.ci.regdbamqpservice;

public class NightArtifacts {
	
	int id;
	int loc;
	String result;
	String reason;
	String datetime;
	int buildnumber;
	public int getBuildnumber() {
		return buildnumber;
	}
	public void setBuildnumber(int buildnumber) {
		this.buildnumber = buildnumber;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLoc() {
		return loc;
	}
	public void setLoc(int loc) {
		this.loc = loc;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	public int getReviewidcount() {
		return reviewidcount;
	}
	public void setReviewidcount(int reviewidcount) {
		this.reviewidcount = reviewidcount;
	}
	int reviewidcount;
	


}
