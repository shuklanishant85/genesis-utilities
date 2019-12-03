package com.utility.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import com.utility.constants.Constants;
import com.utility.constants.ParserProperties;

public class JsonTOXmlParser {

	public static final Log LOGGER = LogFactory.getLog(JsonTOXmlParser.class);

	public void createLookupDCRs(String inputFile, String outputFilePath) {
		Path input = Paths.get(inputFile);
		String jsonString;
		try {
			jsonString = new String(Files.readAllBytes(input));
			Map<String, JSONObject> lookupJsonMap = createLookupJson(jsonString);
			for (Entry<String, JSONObject> entry : lookupJsonMap.entrySet()) {
				Path output = Paths.get(outputFilePath + entry.getKey() + Constants.DCR_EXT);
				String xml = XML.toString(entry.getValue());
				if (StringUtils.isNotBlank(xml)) {
					xml = Constants.XML_DESCRIPTOR_TAG + xml;
					LOGGER.info("xml output generated : " + xml);
					Files.write(output, xml.getBytes(), StandardOpenOption.CREATE_NEW);
				} else {
					LOGGER.info("xml output is empty for mapping ID : " + entry.getKey());
				}
			}

		} catch (IOException e) {
			LOGGER.error("error in I/O : " + e.getMessage());
		}
	}

	private Map<String, JSONObject> createLookupJson(String jsonString) {
		Map<String, JSONObject> lookupJsonMap = new HashMap<>();
		JSONObject jsonObj = new JSONObject(jsonString);
		JSONObject lookupContentSet = jsonObj.getJSONObject(Constants.LOOKUP_CONTENT_SET);
		if (null != lookupContentSet) {
			JSONArray lookupContentGroups = lookupContentSet.getJSONArray(Constants.LOOKUP_CONTENT_GROUPS);
			for (Object object : lookupContentGroups) {
				String lookupMappingId = ((JSONObject) object).getString(Constants.MAPPING_ID);
				if (StringUtils.isNotBlank(lookupMappingId)) {
					JSONObject lookupContent = ((JSONObject) object).getJSONObject(Constants.LOOKUP_CONTENT);
					lookupContent = modifyLookupContentForDcr(lookupContent);
					lookupJsonMap.put(lookupMappingId, lookupContent);
				}
			}
		}
		return lookupJsonMap;
	}

	private JSONObject modifyLookupContentForDcr(JSONObject lookupContent) {
		JSONArray lookupJsonContentArray = new JSONArray();
		JSONObject item = new JSONObject();
		JSONObject dataContainer = new JSONObject();
		JSONObject angular = new JSONObject();
		JSONArray lookupContentItems = lookupContent.getJSONArray(Constants.LOOKUP_CONTENT_ITEMS);
		for (Object object : lookupContentItems) {
			JSONObject entryObject = new JSONObject();
			entryObject.put(Constants.VALUE, ((JSONObject) object).getString(Constants.VALUE));
			entryObject.put(Constants.KEY, ((JSONObject) object).getString(Constants.KEY));
			entryObject.put(Constants.TRANSLATION_REQUIRED, Constants.TRUE);
			lookupJsonContentArray.put(entryObject);
		}
		item.put(Constants.ITEM, lookupJsonContentArray);
		dataContainer.put(Constants.DATA_CONTAINER, item);
		angular.put(Constants.ANGULAR, dataContainer);
		return angular;
	}

	public static void convert(String inputFile, String outputFile) {
		Path input = Paths.get(inputFile);
		Path output = Paths.get(outputFile);
		String jsonString;
		try {
			jsonString = new String(Files.readAllBytes(input));
			JSONObject json = new JSONObject(jsonString);
			LOGGER.info("json input: " + jsonString);
			String xml = XML.toString(json);
			LOGGER.info("xml output: " + xml);
			Files.write(output, xml.getBytes(), StandardOpenOption.CREATE_NEW);
		} catch (IOException e) {
			LOGGER.error("error in I/O : " + e.getMessage());
		}
	}

	public static void main(String[] args) {
		
		if (args.length != 0 || StringUtils.isNotBlank(Constants.PROPERTIES_FILE_PATH)) {
			if (args.length != 0) {
				ParserProperties.setPropertyFilePath(args[0]);
			} else {
				ParserProperties.setPropertyFilePath(Constants.PROPERTIES_FILE_PATH);
			}
			PropertyConfigurator.configure(Constants.LOGGER_PROPERTIES_PATH);
			new JsonTOXmlParser().createLookupDCRs(Constants.JSON_FILE_PATH, Constants.OUTPUT_XML_FILE_PATH);
		} else {
			System.out.println("Please pass properties file path as argument...");
		}
		
	}

}
