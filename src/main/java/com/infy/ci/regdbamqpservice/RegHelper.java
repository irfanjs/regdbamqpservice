package com.infy.ci.regdbamqpservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RegHelper {
	 static Logger log = Logger.getLogger(RegHelper.class.getName());
	
	static private RegHelper cihelper;
	private final static Object cihelperLock = new Object();
	private RegHelper(){
		
		
	}
	
	public static RegHelper getInstance(){
		synchronized (cihelperLock) {
			if(null == cihelper){
				cihelper = new RegHelper();
			}
		}
		return cihelper;
	}
	
	public String getJSONDataForChart(List<Map<String, Object>> data,Map<String,String> selectDataList) throws JsonProcessingException{
		List<ChartData> returnData = new ArrayList<ChartData>();
		ObjectMapper mapper = new ObjectMapper();
		Set<String> selectDataKeys = selectDataList.keySet();
		for(int idx = 0;idx < data.size(); idx++){
			ChartData chartData = new ChartData();
			chartData.setCategories(new ArrayList<String>());
			chartData.setData(new ArrayList<Object>());
			Map<String, Object> map =  data.get(idx);
			for (String key : map.keySet()){
				if(selectDataKeys.contains(key)){
					((List<Object>)chartData.getCategories()).add(selectDataList.get(key));
					((List<Object>)chartData.getData()).add(map.get(key));
				}
			}				
			if(data.size() == 1){
				return mapper.writeValueAsString(chartData);
			}
			returnData.add(chartData);
		}
		
        return mapper.writeValueAsString(returnData);
	}
	
	@SuppressWarnings("unchecked")
	public String getJSONDataForChartPivot(List<Map<String, Object>> data) throws JsonProcessingException{		
		ObjectMapper mapper = new ObjectMapper();
		ChartData chartData = new ChartData();
		chartData.setCategories(new ArrayList<String>());
		chartData.setData(new ArrayList<Object>());
		for(int idx = 0;idx < data.size(); idx++){			
			Map<String, Object> map =  data.get(idx);
			log.debug("the" + idx + "value is" + data.get(idx));
			Object[] values = map.values().toArray();
			log.debug("the 0th value of values is " + values[0]);
			log.debug("the first value of values is " + values[1]);
		//	((List<String>)chartData.getCategories()).add((String)values[0]); this is commented to fix the bug. double to string casting exception
			((List<Object>)chartData.getData()).add(values[0]);
			((List<Object>)chartData.getCategories()).add(values[1]);
		}
		return mapper.writeValueAsString(chartData);
	}
	
	public String getJSONDataForChartColumnWise(List<Map<String, Object>> data,String column1, Map<String,String> selectDataList) throws JsonProcessingException{
		ChartData chartData = new ChartData();
		ObjectMapper mapper = new ObjectMapper();
		chartData.setCategories(new ArrayList<String>());
		//String[] colorCode = new String[]{"#218833","#AA8833","#992233","#99AA11"};
		List<ChartNameData> valueList = new ArrayList<ChartNameData>();
		Object[] selectDataKeys = selectDataList.keySet().toArray();
		for(int valColNamesIdx = 0;valColNamesIdx < selectDataList.size(); valColNamesIdx++){
			ChartNameData nameData = new ChartNameData();
			nameData.setName(selectDataList.get(selectDataKeys[valColNamesIdx]));
			//nameData.setColor(colorCode[valColNamesIdx]);
			nameData.setData(new ArrayList<Object>());
			valueList.add(nameData);
		}
		chartData.setData(valueList);
		for(int idx = 0;idx < data.size(); idx++){	
			Map<String, Object> map =  data.get(idx);
			String categoryName = map.get(column1).toString();
			if(!categoryName.isEmpty()){
				((List<Object>)chartData.getCategories()).add(categoryName);
				for(int valIdx = 0; valIdx < selectDataList.size(); valIdx++){
					ChartNameData nameData = valueList.get(valIdx);
					((List<Object>)nameData.getData()).add(map.get(selectDataKeys[valIdx]));
				}
			}
		}
		return mapper.writeValueAsString(chartData);
        //return mapper.writeValueAsString(returnData);
	}
	
	public String getJSONDataForChart(List<Map<String, Object>> data,Map<String,String> selectDataList, String remainingPercentageLable) throws JsonProcessingException{
		List<Object> resultData = new ArrayList<Object>();
		Set<String> selectDataKeys = selectDataList.keySet();
		double totalPercentValue = 0;
		for(int idx = 0;idx < data.size(); idx++){	
			Map<String, Object> map =  data.get(idx);
			for (String key : map.keySet()){
				if(selectDataKeys.contains(key)){
					List<Object> valueData = new ArrayList<Object>();
					valueData.add(selectDataList.get(key));
					double percentValue = (double)map.get(key);
					valueData.add(percentValue);
					resultData.add(valueData);
					totalPercentValue += percentValue;
				}
			}
		}
		if(100 > totalPercentValue){
			List<Object> valueData = new ArrayList<Object>();
			valueData.add(remainingPercentageLable);
			valueData.add(100 - totalPercentValue);
			resultData.add(valueData);
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(resultData);
	}
}
