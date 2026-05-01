package com.openclassroom.devops.orion.microcrm;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class MicroCRMApplicationTests {

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	void contextLoads() {
		assertNotNull(applicationContext);
	}

	@Test
	void applicationShouldBootstrapWithoutStartingWebServer() {
		assertDoesNotThrow(() -> {
			try (ConfigurableApplicationContext ignored = new SpringApplicationBuilder(MicroCRMApplication.class)
					.web(WebApplicationType.NONE)
					.run("--spring.main.banner-mode=off")) {
				// Context starts and closes explicitly to avoid port conflicts with local runs.
			}
		});
	}

}
