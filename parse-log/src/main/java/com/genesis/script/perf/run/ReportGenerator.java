package com.genesis.script.perf.run;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import com.genesis.script.perf.model.ResultSet;

@PropertySource("script.properties")
public class ReportGenerator {
	@Value("${root.path}")
	private String root;

	@Value("${input.file}")
	private String inputFile;

	public List<ResultSet> generateReportMap() {
		List<ResultSet> resultList = new ArrayList<>();
		try (RandomAccessFile file = new RandomAccessFile(root + inputFile, "r")) {
			String line = file.readLine();
			while (null != line) {
				System.out.println(line);
				ResultSet resultSet = createResultSet(line);
				if (resultSet != null) {
					resultList.add(resultSet);
				}
				line = file.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return resultList;
	}

	private ResultSet createResultSet(String line) {
		String[] fields = StringUtils.split(line, " ");
		ResultSet resultSet = null;
		if (fields != null && fields.length == 22) {
			resultSet = new ResultSet();
			resultSet.setThreadCount(Long.parseLong(fields[2]));
			resultSet.setTime(fields[4]);
			resultSet.setThreadPerSec(fields[6]);
			resultSet.setAverageTimeInSec(fields[8]);
			resultSet.setMinTime(fields[10]);
			resultSet.setMaxTime(fields[12]);
			resultSet.setErrorCount(fields[14]);
			resultSet.setActiveThreads(fields[17]);
			resultSet.setStartedThreads(fields[19]);
			resultSet.setFinishedThreads(fields[21]);
		}
		return resultSet;
	}
}
