package com.infy.ci.regdbamqpservice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegDBQueries {

	int projectid;
	int buildnumber;

	@Autowired
	private RegDBHelper u;

	public RegDBQueries(int projectid) {
		this.projectid = projectid;

	}

	public void setProjectid(int projectid) {
		this.projectid = projectid;
	}

	public RegDBQueries() {

	}

	private static final Logger LOGGER = LoggerFactory.getLogger(RegDBQueries.class);

	public boolean insert(int buildintoId, int total, int passed, int failed)
			throws SQLException, ClassNotFoundException {
		LOGGER.debug("unit test data inserting");
		Connection conn = null;
		PreparedStatement prepStatement = null;
		try {
			conn = u.getConnection();
			prepStatement = conn
					.prepareStatement("insert into unittest(buildinfo_id,total,pass,fail) values(?,?,?,?);");

			prepStatement.setInt(1, buildintoId);

			prepStatement.setInt(2, total);
			prepStatement.setInt(3, passed);
			prepStatement.setInt(4, failed);

			prepStatement.executeUpdate();
			LOGGER.debug("unit test insert complete");

		} finally {
			RegDBHelper.close(conn, prepStatement, null);
		}

		return true;
	}

	public List<Map<String, Object>> getUnitTestDataForLatestBuildId() throws SQLException, ClassNotFoundException {

		String sql = "select ut.total," + "ut.pass," + "ut.fail," + "bi.id," + "bi.buildnumber "
				+ "from unittest ut, buildinfo bi " + "where bi.id = ut.buildinfo_id " + "order by datetime desc "
				+ "limit 1;";
		return executeQuery(sql);
	}

	public List<Map<String, Object>> executeQuery(String sql) throws SQLException {
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try {
			conn = u.getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql);

			return u.getEntitiesFromResultSet(resultSet);
		}

		finally {
			RegDBHelper.close(conn, statement, resultSet);
		}

	}

	public List<Map<String, Object>> getUnitTestForBuildId(int buildnumber)
			throws SQLException, ClassNotFoundException {

		String sql = "select ut.total," + "ut.pass," + "ut.fail," + "bi.id," + "bi.buildnumber "
				+ "from unittest ut, buildinfo bi " + "where bi.id = ut.buildinfo_id " + "and bi.buildnumber = "
				+ buildnumber;

		return executeQuery(sql);
	}

	public List<Map<String, Object>> getUnitTestDataForNightlyBuildId(int nightlybuildnumber)
			throws SQLException, ClassNotFoundException {

		String sql = "select ut.total," + "ut.pass," + "ut.fail," + "bi.id," + "bi.buildnumber "
				+ "from unittest ut, buildinfo bi, nightlybuild nb " + "where nb.id = bi.nightlybuild_id "
				+ "and bi.id = ut.buildinfo_id " + "and nb.buildnumber = " + nightlybuildnumber;

		return executeQuery(sql);
	}

	public List<Map<String, Object>> getWeekUtAggregateDataNightlyBuild() throws SQLException, ClassNotFoundException {

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		System.out.println("current date is :" + dateFormat.format(date));
		Calendar cal = Calendar.getInstance();

		cal.add(Calendar.DATE, -7);
		System.out.println("last week dats is :" + dateFormat.format(cal.getTime()));

		// String sql = "select
		// tempnb.buildnumber,sum(pass),sum(fail),sum(total),sum(skip) from
		// buildinfo bi inner join(select * from nightlybuild where datetime >
		// '2014-02-14 01:36:03' and datetime < '2014-03-19 01:36:41' and status
		// =1) tempnb on bi.nightlybuild_id = tempnb.id inner join unittest ut
		// on ut.buildinfo_id = bi.id group by tempnb.id;";
		// String sql = "select
		// tempnb.buildnumber,sum(pass),sum(fail),sum(total),sum(skip) from
		// buildinfo bi inner join(select * from nightlybuild where datetime >"
		// + " '" + dateFormat.format(cal.getTime())+ "'" + " and datetime < '"
		// + dateFormat.format(date) + "'" + " and status =1) tempnb on
		// bi.nightlybuild_id = tempnb.id inner join unittest ut on
		// ut.buildinfo_id = bi.id where project_id = " + this.projectid + "
		// group by tempnb.id;";
		String sql = "select bi.buildnumber,ut.total,ut.pass,ut.fail,ut.skip from buildinfo bi inner join unittest ut on bi.id = ut.buildinfo_id where bi.datetime >"
				+ " '" + dateFormat.format(cal.getTime()) + "'" + " and bi.datetime < '" + dateFormat.format(date) + "'"
				+ " and bi.project_id = " + this.projectid + " and bi.nightlybuild_id is not NULL;";
		return executeQuery(sql);
	}

	public List<Map<String, Object>> getMonthUtAggregateDataNightlyBuild() throws SQLException, ClassNotFoundException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		System.out.println("current date is :" + dateFormat.format(date));
		Calendar cal = Calendar.getInstance();

		cal.add(Calendar.DATE, -30);
		System.out.println("last month dats is :" + dateFormat.format(cal.getTime()));

		// String sql = "select
		// tempnb.buildnumber,sum(pass),sum(fail),sum(total),sum(skip) from
		// buildinfo bi inner join(select * from nightlybuild where datetime >
		// '2014-02-14 01:36:03' and datetime < '2014-03-19 01:36:41' and status
		// =1) tempnb on bi.nightlybuild_id = tempnb.id inner join unittest ut
		// on ut.buildinfo_id = bi.id group by tempnb.id;";
		// String sql = "select
		// tempnb.buildnumber,sum(pass),sum(fail),sum(total),sum(skip) from
		// buildinfo bi inner join(select * from nightlybuild where datetime >"
		// + " '" + dateFormat.format(cal.getTime())+ "'" + " and datetime < '"
		// + dateFormat.format(date) + "'" + " and status =1) tempnb on
		// bi.nightlybuild_id = tempnb.id inner join unittest ut on
		// ut.buildinfo_id = bi.id where project_id = " + this.projectid + "
		// group by tempnb.id;";
		String sql = "select bi.buildnumber,ut.total,ut.pass,ut.fail,ut.skip from buildinfo bi inner join unittest ut on bi.id = ut.buildinfo_id where bi.datetime >"
				+ " '" + dateFormat.format(cal.getTime()) + "'" + " and bi.datetime < '" + dateFormat.format(date) + "'"
				+ " and bi.project_id = " + this.projectid + " and bi.nightlybuild_id is not NULL;";
		return executeQuery(sql);
	}

	public List<Map<String, Object>> getutspecificbldno(int buildnumber) throws SQLException, ClassNotFoundException {
		// String sql = "select bi.buildnumber,ut.total,ut.pass,ut.fail,ut.skip
		// from buildinfo bi inner join unittest ut on bi.id = ut.buildinfo_id
		// where bi.project_id = " + this.projectid + " and bi.buildnumber = " +
		// buildnumber + " and bi.nightlybuild_id is not NULL;";
		String sql = "select bi.buildnumber,ut.total,ut.pass,ut.fail,ut.skip from nightlybuild nt inner join buildinfo bi on nt.id = bi.nightlybuild_id and nt.buildnumber= "
				+ buildnumber + " inner join unittest ut on bi.id = ut.buildinfo_id where bi.project_id = "
				+ this.projectid + ";";
		return executeQuery(sql);
	}

	public List<Map<String, Object>> getAggregatedUnitTestDataForNightlyBuildId(int nightlybuildnumber)
			throws SQLException, ClassNotFoundException {

		LOGGER.debug("called getAggregatedUnitTestDataForNightlyBuildId");
		String sql = "select nb.id," + "sum(ut.total) total," + "sum(ut.pass) pass," + "sum(ut.fail) fail "
				+ "from unittest ut, buildinfo bi, nightlybuild nb " + "where nb.id = bi.nightlybuild_id "
				+ "and bi.id = ut.buildinfo_id " + "and nb.buildnumber = " + nightlybuildnumber + " group by nb.id;";

		return executeQuery(sql);
	}

	public List<Map<String, Object>> getAllModulesUnitTestForLatestNightlyBuild()
			throws SQLException, ClassNotFoundException {
		LOGGER.debug("called getAllModulesUnitTestForLatestNightlyBuild");

		String sql = "select suborig.modulename, suborig.datetime,suborig.id, subut.total,subut.pass,subut.fail,subut.skip from (select modulename, datetime,max(id) id from  buildinfo  subbi  where subbi.nightlybuild_id in (select id from nightlybuild where datetime in (select max(datetime) from nightlybuild where status = 1)) group by modulename) suborig LEFT JOIN unittest subut ON suborig.id = subut.buildinfo_id;";
		return executeQuery(sql);

	}

	public List<Map<String, Object>> getAggregatedUnitTestDataForLatestNightlyBuild()
			throws SQLException, ClassNotFoundException {

		LOGGER.debug("called getAggregatedUnitTestDataForNightlyBuildId");

		// String sql = "select sum(subut.total) total,sum(subut.pass)
		// pass,sum(subut.fail) fail, sum(subut.skip) skip from (select
		// modulename, datetime as dt,max(id) id from buildinfo subbi where
		// subbi.nightlybuild_id in (select id from nightlybuild where datetime
		// in (select max(datetime) from nightlybuild where status = 1)) and
		// project_id = " + this.projectid + " group by modulename) suborig LEFT
		// JOIN unittest subut ON suborig.id = subut.buildinfo_id;";
		// query to get data from so e2e snapshot

		String sql = "select * from (select max(id) id from buildinfo where project_id = " + this.projectid
				+ " and nightlybuild_id != 'NULL') tempbi inner join unittest ut on ut.buildinfo_id = tempbi.id;";
		return executeQuery(sql);
	}

	public List<Map<String, Object>> getAggregatedUnitTestDataForLatestBuild()
			throws SQLException, ClassNotFoundException {

		LOGGER.debug("called getAggregatedUnitTestDataForLatestBuild");

		String sql = "select sum(subut.total) total," + "sum(subut.pass) pass," + "sum(subut.fail) fail, "
				+ "sum(subut.skip) skip " + "from " + "(select modulename, " + "datetime as dt," + "max(id) id "
				+ "from  buildinfo  subbi  group by modulename) suborig "
				+ "LEFT JOIN unittest subut ON suborig.id = subut.buildinfo_id;";

		return executeQuery(sql);
	}

	public List<Map<String, Object>> getAllModulesUnitTestForLatestBuild() throws SQLException, ClassNotFoundException {
		LOGGER.debug("called getAllModulesUnitTestForLatestNightlyBuild");

		String sql = "select subbi.modulename," + "bi.datetime," + "subbi.id," + "subut.total," + "subut.pass,"
				+ "subut.fail, " + "subut.skip " + "from (select modulename, " + "max(id) id "
				+ "from  buildinfo  subbi  where project_id = " + this.projectid + " and nightlybuild_id is NULL"
				+ " group by modulename) subbi " + "INNER JOIN buildinfo bi " + "ON subbi.id = bi.id "
				+ "LEFT JOIN unittest subut " + "ON subbi.id = subut.buildinfo_id;";

		return executeQuery(sql);

	}

	public List<Map<String, Object>> getTrendCustomUtData(String todate, String fromdate)
			throws SQLException, ClassNotFoundException {

		String dateString1 = new String(todate);
		String dateString2 = new String(fromdate);

		String finalfromdate = null;
		String finaltodate = null;

		java.util.Date dtDate = new Date();
		// SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		SimpleDateFormat sdfAct = new SimpleDateFormat("dd/MM/yyyy");
		try {
			dtDate = sdfAct.parse(dateString1);
			System.out.println("Date After parsing in required format:" + (sdf.format(dtDate)));
			finaltodate = (sdf.format(dtDate));
		} catch (ParseException e) {
			System.out.println("Unable to parse the date string");
			e.printStackTrace();
		}

		try {
			dtDate = sdfAct.parse(dateString2);
			System.out.println("Date After parsing in required format:" + (sdf.format(dtDate)));
			finalfromdate = (sdf.format(dtDate));
		} catch (ParseException e) {
			System.out.println("Unable to parse the date string");
			e.printStackTrace();
		}
		/*
		 * 
		 * 
		 * DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		 * 
		 * Date date = new Date();
		 * 
		 * System.out.println("current date is :" + dateFormat.format(date) );
		 * Calendar cal = Calendar.getInstance();
		 * 
		 * cal.add(Calendar.DATE, -30); System.out.println("last week dats is :"
		 * + dateFormat.format(cal.getTime()));
		 */
		// String sql = "select
		// tempnb.buildnumber,sum(pass),sum(fail),sum(total),sum(skip) from
		// buildinfo bi inner join(select * from nightlybuild where datetime >
		// '2014-02-14 01:36:03' and datetime < '2014-03-19 01:36:41' and status
		// =1) tempnb on bi.nightlybuild_id = tempnb.id inner join unittest ut
		// on ut.buildinfo_id = bi.id group by tempnb.id;";
		// String sql = "select
		// tempnb.buildnumber,sum(pass),sum(fail),sum(total),sum(skip) from
		// buildinfo bi inner join(select * from nightlybuild where datetime >"
		// + " '" + finalfromdate + "'" + " and datetime < '" + finaltodate +
		// "'" + " and status =1) tempnb on bi.nightlybuild_id = tempnb.id inner
		// join unittest ut on ut.buildinfo_id = bi.id where project_id = " +
		// this.projectid + " group by tempnb.id;";

		String sql = "select bi.buildnumber,ut.total,ut.pass,ut.fail,ut.skip from buildinfo bi inner join unittest ut on bi.id = ut.buildinfo_id where bi.datetime >"
				+ " '" + finalfromdate + "'" + " and bi.datetime < '" + finaltodate + "'" + " and bi.project_id = "
				+ this.projectid + " and bi.nightlybuild_id is not NULL;";
		return executeQuery(sql);

	}

	public List<Map<String, Object>> getProjectNamesId() throws SQLException, ClassNotFoundException {

		String sql = "select id,name from projects;";

		return executeQuery(sql);
	}

	public boolean insert(int buildId, int nightlyBuild, String moduleName, String result, String reason)
			throws SQLException, ClassNotFoundException {

		Connection conn = null;
		PreparedStatement prepStatement = null;

		try {
			conn = u.getConnection();
			prepStatement = conn.prepareStatement("insert into nightlybuild(datetime) values(?);");

			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			prepStatement.setTimestamp(6, new java.sql.Timestamp(cal.getTimeInMillis()));

			prepStatement.executeUpdate();
		} finally {
			RegDBHelper.close(conn, prepStatement, null);
		}
		return true;
	}

	public int getRecordIdForBuildId(int buildId) throws SQLException, ClassNotFoundException {

		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;

		String sql = "select id  from nightlybuild order by datetime desc limit 1;";
		try {
			conn = u.getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				return resultSet.getInt("id");
			}
			return -1;
		} finally {
			RegDBHelper.close(conn, statement, resultSet);
		}
	}

	public List<Map<String, Object>> getSummaryDataLatestBuild() throws SQLException, ClassNotFoundException {

		// String sql = "select nb.buildnumber,bi.result,bi.reason from
		// nightlybuild nb LEFT JOIN buildinfo bi on nb.id = bi.nightlybuild_id
		// where nb.datetime in (select max(datetime) from nightlybuild where
		// status = 1) order by bi.datetime desc limit 1;";
		// String sql = "select
		// nb.id,nb.buildnumber,bi.result,bi.reason,sum(bi.loc) loc from
		// nightlybuild nb INNER JOIN buildinfo bi on nb.id = bi.nightlybuild_id
		// where nb.datetime in (select max(datetime) from nightlybuild where
		// status = 1) order by bi.datetime desc;";
		// String sql = "select
		// nb.id,nb.buildnumber,bi.result,bi.reason,bi.datetime datetime from
		// nightlybuild nb LEFT JOIN buildinfo bi on nb.id = bi.nightlybuild_id
		// where nb.datetime in (select max(datetime) from nightlybuild where
		// status = 1) order by bi.datetime desc limit 1;";
		String sql = "select tempBI.buildnumber,BI.result,BI.reason,tempBI.datetime,tempBI.loc  from buildinfo BI INNER JOIN (select max(bi.datetime) dt,sum(LOC) loc,nb.buildnumber,nb.datetime  from buildinfo bi,nightlybuild nb where bi.nightlybuild_id = nb.id and bi.nightlybuild_id in (select id from nightlybuild where datetime in (select max(datetime) from nightlybuild))) tempBI ON tempBI.dt = BI.datetime;";

		return executeQuery(sql);
	}

	public List<Map<String, Object>> getBuildArtifactsForLatestNightlyBuild()
			throws SQLException, ClassNotFoundException {

		// String sql = "select sum(subut.total) total,sum(subut.pass)
		// pass,sum(subut.fail) fail, sum(subut.skip) skip from (select
		// modulename, datetime as dt,max(id) id from buildinfo subbi where
		// subbi.nightlybuild_id in (select id from nightlybuild where datetime
		// in (select max(datetime) from nightlybuild where status = 1)) and
		// project_id = " + this.projectid + " group by modulename) suborig LEFT
		// JOIN unittest subut ON suborig.id = subut.buildinfo_id;";
		// query to get data from so e2e snapshot
		String sql = "select nb.buildnumber,bi.loc,bi.result,bi.reason,bi.datetime,nb.reviewidcount from nightlybuild nb inner join buildinfo bi on bi.nightlybuild_id = nb.id where project_id = "
				+ this.projectid + " order by nb.buildnumber desc limit 20;";
		return executeQuery(sql);
	}

}
