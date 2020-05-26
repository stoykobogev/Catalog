package com.catalog.controllers;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.catalog.CatalogApplication;
import com.catalog.constants.Roles;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CatalogApplication.class })
@WebAppConfiguration
@ActiveProfiles("test")
public abstract class AbstractControllerTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private WebApplicationContext wac;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private MockMvc mockMvc;
	
	
	@Before
	public void init() throws Exception {
	    this.mockMvc = MockMvcBuilders
	    		.webAppContextSetup(this.wac)
	    		.apply(springSecurity())
	    		.build();
	}

	protected <T> T parseJsonToObject(String json, Class<T> clazz) throws JsonMappingException, JsonProcessingException {
		T object = this.objectMapper.readValue(json, clazz);
		
		return object;
	}
	
	protected <T> List<T> parseJsonToList(String json, Class<T> clazz) throws JsonMappingException, JsonProcessingException {
		List<T> list = this.objectMapper.readValue(json, this.objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
		
		return list;
	}
	
	protected String parseObjectToJson(Object object) throws JsonProcessingException {
		return this.objectMapper.writeValueAsString(object);
	}
	
	protected ResultActions perform(MockHttpServletRequestBuilder requestBuilder) throws Exception {
		return this.mockMvc.perform(requestBuilder.with(csrf()).with(user("admin").roles(Roles.ADMIN)));
	}
	
	protected ResultActions performWithBody(MockHttpServletRequestBuilder requestBuilder, Object object) throws Exception {
		return this.mockMvc.perform(requestBuilder.with(csrf()).with(user("admin").roles(Roles.ADMIN))
				.content(parseObjectToJson(object)).contentType(MediaType.APPLICATION_JSON));
	}
	
	protected void testAuthorities(MockHttpServletRequestBuilder validRequest, Collection<String> allowedRoles)
			throws Exception {
		
		Set<String> otherRolesSet = Roles.ALL.stream()
				.filter((role) -> !allowedRoles.contains(role))
				.collect(Collectors.toSet());
		
		for (String role : allowedRoles) {
			int status = this.mockMvc.perform(validRequest.with(csrf()).with(user("user").roles(role))).andReturn().getResponse().getStatus();
			assertNotEquals(HttpStatus.FORBIDDEN.value(), status);
		}
		
		for (String role : otherRolesSet) {
			this.mockMvc.perform(validRequest.with(csrf()).with(user("user").roles(role))).andExpect(status().isForbidden());
		}
	}
}
