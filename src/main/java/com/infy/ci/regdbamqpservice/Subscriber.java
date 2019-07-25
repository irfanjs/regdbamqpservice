package com.infy.ci.regdbamqpservice;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@Component
@SpringBootApplication
public class Subscriber {
	
	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		SpringApplication.run(Subscriber.class, args);
	}
	
	private final static Logger logger = LoggerFactory.getLogger(Subscriber.class);
	
	@Component
	public static class RpcListener {

		@Autowired
		RegDB db;

		 @RabbitListener(queues="${jsa.rabbitmq.queue}")
		public String reply(String request) throws IOException, TimeoutException, ClassNotFoundException, SQLException {
			logger.info("Sent Message was: " + request);
			String response;
			String[] output = request.split("-");
			String projectid = output[1];

			db.setProjectid(Integer.parseInt(projectid));

			if (output[0].equals("aggregate")) {
				String build = output[2];
				String buildtype = output[3];

				if (build.toLowerCase().equals("latest") && buildtype.equals("nightly")) {
					response = db.getLatestNightlyaggregate();
				} else {
					response = db.getAggregatedDataForNightlyBuild(Integer.parseInt(build));
				}
			} else if (output[0].equals("modulewise")) {
				String build = output[2];
				String buildtype = output[3];
				if (build.toLowerCase().equals("latest") && buildtype.equals("ci")) {
					response = db.getLatestCiModulewise();
				} else {

					response = db.getAggregatedDataForNightlyBuild(Integer.parseInt(build));
				}
			}

			else if (output[0].equals("week")) {
				response = db.getTrendWeekData();
			} else if (output[0].equals("month")) {
				response = db.getTrendMonthData();
			} else if (output[0].equals("custom")) {
				String todate = output[2];
				String fromdate = output[3];
				response = db.getTrendCustomData(todate, fromdate);
			} else if (output[0].equals("projects")) {
				response = db.getProjectNames();
			} else if (output[0].equals("latestnightlybuilds")) {
				response = db.getLatestNightlybuilds();
			} else if (output[0].equals("daterange")) {
				DateRange dr = new DateRange();
				response = dr.getdaterange();
			} else if (output[0].equals("buildnumber")) {
				String buildnumber = output[2];
				response = db.getAggregatedDataForNightlyBuild(Integer.parseInt(buildnumber));
			}

			else {
				throw new RuntimeException("failed to return a response. please check the message sent");
			}
			return response;

		}

	}
	

}
