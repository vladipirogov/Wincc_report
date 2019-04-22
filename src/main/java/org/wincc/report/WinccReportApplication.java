package org.wincc.report;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class WinccReportApplication {

	public static void main(String[] args) {
		//SpringApplication.run(WinccReportApplication.class, args);
		SpringApplicationBuilder builder = new SpringApplicationBuilder(WinccReportApplication.class);
		builder.headless(false);
		ConfigurableApplicationContext context = builder.run(args);
	}

}
