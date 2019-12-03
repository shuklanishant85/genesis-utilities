package com.genesis.parser.perf.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import com.genesis.rules.script.WorkbookCreator;
import com.genesis.script.perf.run.ReportGenerator;

@Configuration
@PropertySource("script.properties")
public class AppConfig {

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public ReportGenerator reportGenerator() {
		return new ReportGenerator();
	}

	@Bean
	public WorkbookCreator workbookCreator() {
		return new WorkbookCreator();
	}

	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		WorkbookCreator workbookCreator = (WorkbookCreator) context.getBean("workbookCreator");
		workbookCreator.createExcelReport();
		((AnnotationConfigApplicationContext) context).close();
	}

}
