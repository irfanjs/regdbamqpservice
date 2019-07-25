package com.infy.ci.regdbamqpservice;

import java.io.IOException;
import java.sql.SQLException;

public interface CIData {
	public void setBuildNumber(int buildnumber);

	public String getAggregatedDataForBuild(int buildno) throws IOException;

	public String getAggregatedDataForLatestBuild() throws IOException;

	public String getAggregatedDataForNightlyBuild(int buildno) throws IOException;

	public String getAggregatedDataForLatestNightlyBuild() throws IOException;

	public String getAllModulesAggregatedDataForLatestBuild() throws IOException;

	public String getAllModulesAggregatedDataForLatestNightlyBuild() throws IOException;

	public String getAllModuleDataForBuild(int buildno) throws IOException;

	public String getModuleDataForLatestBuild() throws IOException;

	public String getModuleDataForBuild(int buildno) throws IOException;

	public String getAllModuleDataForLatestNightlyBuild() throws IOException;

	public String getAllModuleDataForNightlyBuild(int buildno) throws IOException;

	public String getModuleDataForLatestNightlyBuild() throws IOException;

	public String getModuleDataForNightlyBuild(int buildno) throws IOException;

	public String getAllModuleDataForLatestBuild() throws IOException;

	public String getLatestNightlyaggregate() throws IOException;

	public String getbuildwiseinfo(int projectid, int buildnumber) throws IOException;

	public String getLatestCiModulewise() throws IOException;

	public String getTrendWeekData() throws IOException;

	public String getTrendMonthData() throws IOException;

	public String getTrendCustomData(String todate, String fromdate) throws IOException, ClassNotFoundException;

	public String getProjectNames() throws ClassNotFoundException, SQLException;

	public String getLatestNightlybuilds() throws IOException;
}
