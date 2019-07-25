package com.infy.ci.regdbamqpservice;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;

@Component
public class RegDB implements CIData {

	@Autowired
	RegDBQueries ut;

	public RegDB() {
		// TODO Auto-generated constructor stub
	}

	public RegDB(int projectid) {
		ut = new RegDBQueries(projectid);
		// TODO Auto-generated constructor stub
	}

	public void setProjectid(int projectid) {

		ut.setProjectid(projectid);
	}

	@Override
	public String getAggregatedDataForBuild(int buildno) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAggregatedDataForLatestBuild() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAllModuleDataForBuild(int buildno) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getModuleDataForLatestBuild() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getModuleDataForBuild(int buildno) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAllModuleDataForLatestNightlyBuild() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAllModuleDataForNightlyBuild(int buildno) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getModuleDataForLatestNightlyBuild() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getModuleDataForNightlyBuild(int buildno) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAllModuleDataForLatestBuild() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLatestNightlyaggregate() throws IOException {
		List<Map<String, Object>> data;
		ChartData d = new ChartData();
		Gson gson = new Gson();
		List<String> arrayList = new ArrayList<String>();
		arrayList.add("Pass");
		arrayList.add("Fail");
		arrayList.add("Skip");

		int pas = 0;
		int fail = 0;
		int skip = 0;
		String json;

		ArrayList<Integer> singleList = new ArrayList<Integer>();

		try {
			data = ut.getAggregatedUnitTestDataForLatestNightlyBuild();
			if (data.size() != 0) {
				for (Map<String, Object> data1 : data) {
					for (Map.Entry<String, Object> entry : data1.entrySet()) {
						System.out.println(entry.getKey() + ": " + entry.getValue());

						if (entry.getKey().equals("pass")) {
							pas = Integer.parseInt(entry.getValue().toString());
						} else if (entry.getKey().equals("fail")) {
							fail = Integer.parseInt(entry.getValue().toString());
						} else if (entry.getKey().equals("skip")) {
							skip = Integer.parseInt(entry.getValue().toString());
						}

					}

					singleList.add(pas);
					singleList.add(fail);
					singleList.add(skip);

					Map<String, Object> map = new HashMap<>();
					map.put("Data", singleList);

					ArrayList<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
					dataList.add(map);

					d.setCategories(arrayList);
					d.setData(dataList);

					json = gson.toJson(d);
					return json;

				}
			} else {
				return null;
			}
		} catch (SQLException | ClassNotFoundException e) {
			throw new IOException("Failed to fetch data for unit test", e);
		}
		return null;
	}

	@Override
	public String getAggregatedDataForNightlyBuild(int buildno) throws IOException {

		List<Map<String, Object>> data;
		ChartData d = new ChartData();
		Gson gson = new Gson();
		List<String> arrayList = new ArrayList<String>();
		arrayList.add("Pass");
		arrayList.add("Fail");
		arrayList.add("Skip");

		int pas = 0;
		int fail = 0;
		int skip = 0;
		String json;

		ArrayList<Integer> singleList = new ArrayList<Integer>();
		try {
			data = ut.getutspecificbldno(buildno);
			if (data.size() != 0) {
				for (Map<String, Object> data1 : data) {
					for (Map.Entry<String, Object> entry : data1.entrySet()) {
						System.out.println(entry.getKey() + ": " + entry.getValue());

						if (entry.getKey().equals("pass")) {
							pas = Integer.parseInt(entry.getValue().toString());
						} else if (entry.getKey().equals("fail")) {
							fail = Integer.parseInt(entry.getValue().toString());
						} else if (entry.getKey().equals("skip")) {
							skip = Integer.parseInt(entry.getValue().toString());
						}

					}

					singleList.add(pas);
					singleList.add(fail);
					singleList.add(skip);

					Map<String, Object> map = new HashMap<>();
					map.put("Data", singleList);

					ArrayList<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
					dataList.add(map);

					d.setCategories(arrayList);
					d.setData(dataList);

					json = gson.toJson(d);
					return json;

				}
			} else {
				return null;
			}
		} catch (SQLException | ClassNotFoundException e) {
			throw new IOException("Failed to fetch data for unit test", e);
		}
		return null;

	}

	@Override
	public String getAggregatedDataForLatestNightlyBuild() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAllModulesAggregatedDataForLatestBuild() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAllModulesAggregatedDataForLatestNightlyBuild() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLatestCiModulewise() throws IOException {
		List<Map<String, Object>> data;
		try {
			data = ut.getAllModulesUnitTestForLatestBuild();
		} catch (SQLException | ClassNotFoundException e) {
			throw new IOException("Failed to fetch data for unit test", e);
		}

		return getJSONDataForColumnwise(data);
	}

	private String getJSONDataForColumnwise(List<Map<String, Object>> data)
			throws JsonProcessingException, IOException {
		if (null != data) {
			Map<String, String> selectDataList = new HashMap<String, String>();
			selectDataList.put("fail", "Fail");
			selectDataList.put("pass", "Pass");
			selectDataList.put("skip", "Skip");
			return RegHelper.getInstance().getJSONDataForChartColumnWise(data, "modulename", selectDataList);
		} else {
			throw new IOException("Build data for specified build id not found");
		}
	}

	@Override
	public String getTrendWeekData() throws IOException {

		ChartData d = new ChartData();
		Gson gson = new Gson();

		ObjectClass testResultPass = new ObjectClass("Pass");
		ObjectClass testResultFail = new ObjectClass("Fail");
		ObjectClass testResultSkip = new ObjectClass("Skip");
		ObjectClass testResultTotal = new ObjectClass("Total");

		List<ObjectClass> result = new ArrayList<ObjectClass>();
		String json;

		List<Integer> arrayList = new ArrayList<Integer>();

		List<Map<String, Object>> data;
		Map<String, Object> map1;
		Map<String, Object> map2;

		try {
			data = ut.getWeekUtAggregateDataNightlyBuild();
		} catch (SQLException | ClassNotFoundException e) {
			throw new IOException("Failed to fetch data for unit test", e);
		}

		for (Map<String, Object> data1 : data) {
			for (Map.Entry<String, Object> entry : data1.entrySet()) {
				System.out.println(entry.getKey() + ": " + entry.getValue());

				if (entry.getKey().equals("buildnumber")) {
					arrayList.add(Integer.parseInt(entry.getValue().toString()));
				}

				else if (entry.getKey().equals("pass")) {
					testResultPass.data.add(Integer.parseInt(entry.getValue().toString()));
				}

				else if (entry.getKey().equals("fail")) {
					testResultFail.data.add(Integer.parseInt(entry.getValue().toString()));
				} else if (entry.getKey().equals("total")) {
					testResultTotal.data.add(Integer.parseInt(entry.getValue().toString()));
				}

				else if (entry.getKey().equals("skip")) {
					testResultSkip.data.add(Integer.parseInt(entry.getValue().toString()));
				}

			}

		}
		result.add(testResultPass);
		result.add(testResultFail);
		result.add(testResultTotal);
		result.add(testResultSkip);
		d.setCategories(arrayList);
		d.setData(result);
		json = gson.toJson(d);
		return json;

	}

	@Override
	public String getTrendMonthData() throws IOException {
		ChartData d = new ChartData();
		Gson gson = new Gson();

		ObjectClass testResultPass = new ObjectClass("Pass");
		ObjectClass testResultFail = new ObjectClass("Fail");
		ObjectClass testResultSkip = new ObjectClass("Skip");
		ObjectClass testResultTotal = new ObjectClass("Total");

		List<ObjectClass> result = new ArrayList<ObjectClass>();
		String json;

		List<Integer> arrayList = new ArrayList<Integer>();

		List<Map<String, Object>> data;
		Map<String, Object> map1;
		Map<String, Object> map2;

		try {
			data = ut.getMonthUtAggregateDataNightlyBuild();
		} catch (SQLException | ClassNotFoundException e) {
			throw new IOException("Failed to fetch data for unit test", e);
		}

		for (Map<String, Object> data1 : data) {
			for (Map.Entry<String, Object> entry : data1.entrySet()) {
				System.out.println(entry.getKey() + ": " + entry.getValue());

				if (entry.getKey().equals("buildnumber")) {
					arrayList.add(Integer.parseInt(entry.getValue().toString()));
				}

				else if (entry.getKey().equals("pass")) {
					testResultPass.data.add(Integer.parseInt(entry.getValue().toString()));
				}

				else if (entry.getKey().equals("fail")) {
					testResultFail.data.add(Integer.parseInt(entry.getValue().toString()));
				} else if (entry.getKey().equals("total")) {
					testResultTotal.data.add(Integer.parseInt(entry.getValue().toString()));
				}

				else if (entry.getKey().equals("skip")) {
					testResultSkip.data.add(Integer.parseInt(entry.getValue().toString()));
				}

			}

		}
		result.add(testResultPass);
		result.add(testResultFail);
		result.add(testResultTotal);
		result.add(testResultSkip);
		d.setCategories(arrayList);
		d.setData(result);
		json = gson.toJson(d);
		return json;

	}

	@Override
	public String getTrendCustomData(String todate, String fromdate) throws IOException {

		ChartData d = new ChartData();
		Gson gson = new Gson();

		ObjectClass testResultPass = new ObjectClass("Pass");
		ObjectClass testResultFail = new ObjectClass("Fail");
		ObjectClass testResultSkip = new ObjectClass("Skip");
		ObjectClass testResultTotal = new ObjectClass("Total");

		List<ObjectClass> result = new ArrayList<ObjectClass>();
		String json;

		List<Integer> arrayList = new ArrayList<Integer>();

		List<Map<String, Object>> data;
		Map<String, Object> map1;
		Map<String, Object> map2;

		try {
			data = ut.getTrendCustomUtData(todate, fromdate);
		} catch (SQLException | ClassNotFoundException e) {
			throw new IOException("Failed to fetch data for unit test for custom date range", e);
		}

		for (Map<String, Object> data1 : data) {
			for (Map.Entry<String, Object> entry : data1.entrySet()) {
				System.out.println(entry.getKey() + ": " + entry.getValue());

				if (entry.getKey().equals("buildnumber")) {
					arrayList.add(Integer.parseInt(entry.getValue().toString()));
				}

				else if (entry.getKey().equals("pass")) {
					testResultPass.data.add(Integer.parseInt(entry.getValue().toString()));
				}

				else if (entry.getKey().equals("fail")) {
					testResultFail.data.add(Integer.parseInt(entry.getValue().toString()));
				} else if (entry.getKey().equals("total")) {
					testResultTotal.data.add(Integer.parseInt(entry.getValue().toString()));
				}

				else if (entry.getKey().equals("skip")) {
					testResultSkip.data.add(Integer.parseInt(entry.getValue().toString()));
				}

			}

		}
		result.add(testResultPass);
		result.add(testResultFail);
		result.add(testResultTotal);
		result.add(testResultSkip);
		d.setCategories(arrayList);
		d.setData(result);
		json = gson.toJson(d);
		return json;
	}

	@Override
	public void setBuildNumber(int buildnumber) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getbuildwiseinfo(int projectid, int buildnumber) throws IOException {

		ChartData d = new ChartData();
		Gson gson = new Gson();

		ObjectClass testResultPass = new ObjectClass("Pass");
		ObjectClass testResultFail = new ObjectClass("Fail");
		ObjectClass testResultSkip = new ObjectClass("Skip");
		ObjectClass testResultTotal = new ObjectClass("Total");

		List<ObjectClass> result = new ArrayList<ObjectClass>();
		String json;

		List<Integer> arrayList = new ArrayList<Integer>();

		List<Map<String, Object>> data;
		Map<String, Object> map1;
		Map<String, Object> map2;

		try {
			data = ut.getutspecificbldno(buildnumber);
		} catch (SQLException | ClassNotFoundException e) {
			throw new IOException("Failed to fetch data for unit test", e);
		}

		for (Map<String, Object> data1 : data) {
			for (Map.Entry<String, Object> entry : data1.entrySet()) {
				System.out.println(entry.getKey() + ": " + entry.getValue());

				if (entry.getKey().equals("buildnumber")) {
					arrayList.add(Integer.parseInt(entry.getValue().toString()));
				}

				else if (entry.getKey().equals("pass")) {
					testResultPass.data.add(Integer.parseInt(entry.getValue().toString()));
				}

				else if (entry.getKey().equals("fail")) {
					testResultFail.data.add(Integer.parseInt(entry.getValue().toString()));
				} else if (entry.getKey().equals("total")) {
					testResultTotal.data.add(Integer.parseInt(entry.getValue().toString()));
				}

				else if (entry.getKey().equals("skip")) {
					testResultSkip.data.add(Integer.parseInt(entry.getValue().toString()));
				}

			}

		}
		result.add(testResultPass);
		result.add(testResultFail);
		result.add(testResultTotal);
		result.add(testResultSkip);
		d.setCategories(arrayList);
		d.setData(result);
		json = gson.toJson(d);
		return json;
	}

	@Override
	public String getProjectNames() throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		List<Map<String, Object>> data;
		data = ut.getProjectNamesId();
		List<ProductDesc> result = new ArrayList<ProductDesc>();

		Gson gson = new Gson();
		String json;
		for (Map<String, Object> data1 : data) {
			ProductDesc p = new ProductDesc();
			for (Map.Entry<String, Object> entry : data1.entrySet()) {
				System.out.println(entry.getKey() + ": " + entry.getValue());

				if (entry.getKey().equals("id")) {
					p.setId(Integer.parseInt(entry.getValue().toString()));
				}

				else if (entry.getKey().equals("name")) {
					p.setDn(entry.getValue().toString());
				}
			}
			result.add(p);

		}

		json = gson.toJson(result);
		return json;
	}

	@Override
	public String getLatestNightlybuilds() throws IOException {

		List<Map<String, Object>> data;
		Gson gson = new Gson();
		List<Object> arrayList = new ArrayList<Object>();

		String json;

		try {

			data = ut.getBuildArtifactsForLatestNightlyBuild();
			if (data.size() != 0) {
				for (Map<String, Object> data1 : data) {
					NightArtifacts na = new NightArtifacts();
					for (Map.Entry<String, Object> entry : data1.entrySet()) {
						System.out.println(entry.getKey() + ": " + entry.getValue());

						if (entry.getKey().equals("buildnumber")) {
							// pas =
							// Integer.parseInt(entry.getValue().toString());
							// na.setId(Integer.parseInt(entry.getValue().toString()));
							na.setBuildnumber(Integer.parseInt(entry.getValue().toString()));
						} else if (entry.getKey().equals("loc")) {
							na.setLoc(Integer.parseInt(entry.getValue().toString()));
						} else if (entry.getKey().equals("result")) {
							na.setResult(entry.getValue().toString());
						}

						else if (entry.getKey().equals("reason")) {
							na.setReason(entry.getValue().toString());
						}

						else if (entry.getKey().equals("datetime")) {
							na.setDatetime(entry.getValue().toString());
						}

						else if (entry.getKey().equals("reviewidcount")) {
							na.setReviewidcount(Integer.parseInt(entry.getValue().toString()));
						}

					}

					arrayList.add(na);

				}

				json = gson.toJson(arrayList);
				return json;
			} else {
				return null;
			}

		} catch (SQLException | ClassNotFoundException e) {
			throw new IOException("Failed to fetch data for latest nightly build artifact", e);
		}

	}

}
