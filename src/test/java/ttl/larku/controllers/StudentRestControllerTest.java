package ttl.larku.controllers;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

import java.util.Arrays;
import java.util.List;

import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import ttl.larku.domain.Student;

@RunWith(SpringJUnit4ClassRunner.class)

@ContextHierarchy({
		@ContextConfiguration(locations = { "classpath:larkUTestWebContext.xml" }),
		@ContextConfiguration(locations = { "classpath:larku-servlet.xml" }) 
})

/*
 * @ContextHierarchy({
 * 
 * @ContextConfiguration(classes = { LarkUConfig.class, LarkUDBConfig.class,
 * LarkUTestDataConfig.class }),
 * 
 * @ContextConfiguration(classes = { LarkUServletConfig.class }) })
 */
@WebAppConfiguration("WebContent")
@ActiveProfiles({ "all" })
// , "embedded" })
//@Sql(scripts = { "/ttl/larku/db/createDB.sql", "/ttl/larku/db/populateMore.sql" })
public class StudentRestControllerTest {

	// @Resource
	@Autowired
	private StudentController studentController;

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	private final int goodStudentId = 1;
	private final int badStudentId = 10000;

	@Autowired
	private DateTimeFormatter dtFormatter;

	@Before
	public void init() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	public void testGetOneStudentGoodJson() throws Exception {
		MediaType accept = MediaType.APPLICATION_JSON_UTF8;

		MockHttpServletRequestBuilder request = get("/adminrest/students/{id}", goodStudentId);

		List<ResultMatcher> matchers = Arrays.asList(status()
				.isOk(), content().contentType(accept),
				jsonPath("$.name").value("anil"));

		/*
		 * request = request.accept(accept);
		 * 
		 * ResultActions actions = mockMvc.perform(request);
		 * 
		 * actions = actions.andExpect(status().isOk());
		 * 
		 * actions = actions.andExpect(content().contentType(accept));
		 * 
		 * actions = actions.andExpect(jsonPath("$.name").value("anil"));
		 * 
		 * MvcResult result = actions.andReturn();
		 * 
		 * MockHttpServletResponse response = result.getResponse();
		 * 
		 * String jsonString = response.getContentAsString();
		 */
		String jsonString = makeCall(request, accept, null, matchers, null);
		System.out.println("resp = " + jsonString);
	}

	@Test
	public void testGetOneStudentGoodXml() throws Exception {
		MediaType accept = MediaType.APPLICATION_XML;

		MockHttpServletRequestBuilder request = get("/adminrest/students/{id}", goodStudentId);

		List<ResultMatcher> matchers = Arrays.asList(status().isOk(), content().contentType(accept),
				xpath("/student/name").string("anil"));

		String jsonString = makeCall(request, accept, null, matchers, null);
		System.out.println("resp = " + jsonString);

		/*
		 * ResultActions actions =
		 * mockMvc.perform(get("/adminrest/students/{id}",
		 * goodStudentId).accept(accept));
		 * 
		 * actions = actions.andExpect(status().isOk());
		 * 
		 * actions = actions.andExpect(content().contentType(accept));
		 * 
		 * actions = actions.andExpect(xpath("/student/name").string("anil"));
		 * 
		 * MvcResult result = actions.andReturn();
		 * 
		 * MockHttpServletResponse response = result.getResponse();
		 * 
		 * String xmlString = response.getContentAsString(); System.out.println(
		 * "resp = " + xmlString);
		 */
	}

	@Test
	public void testGetOneStudentBadId() throws Exception {

		ResultActions actions = mockMvc
				.perform(get("/adminrest/students/{id}", badStudentId).accept(MediaType.APPLICATION_JSON));

		MvcResult mvcr = actions.andReturn();
		String reo = (String)mvcr.getResponse().getContentAsString();
		assertTrue(reo.matches(".*errorMessage.*Student not found.*"));
	}

	@Test
	public void testGetAllStudentsGood() throws Exception {

		ResultActions actions = mockMvc.perform(get("/adminrest/students/").accept(MediaType.APPLICATION_JSON));

		actions = actions.andExpect(status().isOk());

		// actions = actions.andExpect(jsonPath("$.students",
		// hasSize(greaterThanOrEqualTo((3)))));

		actions = actions.andExpect(jsonPath("$", hasSize(4)));
		MvcResult result = actions.andReturn();

		MockHttpServletResponse response = result.getResponse();

		String jsonString = response.getContentAsString();
		System.out.println("resp = " + jsonString);
	}

	@Test
	@DirtiesContext
	public void testAddStudentGood() throws Exception {

		Student student = new Student("Yogita");
		student.setPhoneNumber("202 383-9393");
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(student);

		ResultActions actions = mockMvc.perform(post("/adminrest/students/").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(jsonString));

		actions = actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

		actions = actions.andExpect(status().isOk());

		actions = actions.andExpect(jsonPath("$.name").value("Yogita"));

		MvcResult result = actions.andReturn();

		MockHttpServletResponse response = result.getResponse();

		jsonString = response.getContentAsString();
		System.out.println("resp = " + jsonString);
	}

	private String makeCall(MockHttpServletRequestBuilder builder, MediaType accept, MediaType contentType,
			List<ResultMatcher> matchers, String content) throws Exception {
		if (accept != null) {
			builder = builder.accept(accept);
		}
		if (contentType != null) {
			builder = builder.contentType(contentType);
		}
		if (content != null) {
			builder = builder.content(content);
		}

		ResultActions actions = mockMvc.perform(builder);

		for (ResultMatcher m : matchers) {
			actions = actions.andExpect(m);
		}

		// Get the result and return it
		MvcResult result = actions.andReturn();

		MockHttpServletResponse response = result.getResponse();
		String jsonString = response.getContentAsString();

		return jsonString;
	}
}
