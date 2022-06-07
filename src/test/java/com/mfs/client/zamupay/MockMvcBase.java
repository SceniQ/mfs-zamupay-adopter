package com.mfs.client.zamupay;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
public abstract class MockMvcBase {

	@Autowired
	protected WebApplicationContext context;

	@Autowired
	protected ObjectMapper objectMapper;
	
	protected MockMvc mockMvc;
	
	/**
	 * This method sets up the application context
	 */
	@Before
	public void setUp() {
		
		mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .alwaysDo(print())
                .build();
		
	}

}