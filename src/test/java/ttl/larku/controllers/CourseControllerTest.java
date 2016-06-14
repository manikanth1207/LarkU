package ttl.larku.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.servlet.ModelAndView;

import ttl.larku.domain.Course;
import ttl.larku.domain.ScheduledClass;
import ttl.larku.domain.Student;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:larkUTestWebContext.xml" })
public class CourseControllerTest {

	@Resource(name="courseController")
	private CourseController courseController;
	
	@Resource(name="studentController")
	private StudentController studentController;

	@Resource
	private ApplicationContext appContext;

	private String name1 = "Bloke";
	private String name2 = "Blokess";
	private String newName = "Different Bloke";
	
	private String code1 = "BOT-101";
	private String code2 = "BOT-202";
	private String title1 = "Intro To Botany";
	private String title2 = "Outtro To Botany";
	
	private String startDate1 = "10/10/2012";
	private String startDate2 = "10/10/2013";
	private String endDate1 = "05/10/2013";
	private String endDate2 = "05/10/2014";
	
	private int id1, id2;
	
	private Course course1;
	private Course course2;
	private ScheduledClass class1;
	private ScheduledClass class2;
	private Student student1;
	private Student student2;

	private String postedName = "postedName";

	@Before
	public void init() {
	}

	@Test
	public void testGetCourses() {
		
		ModelAndView result = courseController.getCourses();
		
		List<Course> courses = (List)result.getModel().get("courses");

		assertTrue(courses.size() >= 3);
		assertEquals("/WEB-INF/jsp/showCourses.jsp", result.getViewName());
	}

	@Test
	public void testGetCourseById() {
		
		ModelAndView result = courseController.getCourse(1);
		
		assertEquals("BKTW-101", ((Course)(result.getModel().get("course"))).getCode());
		assertEquals("/WEB-INF/jsp/showCourse.jsp", result.getViewName());
	}

	@Test
	public void testShowAddCourseForm() {
		
		String result = courseController.showAddCourseForm(new Course());
		
		assertEquals("/WEB-INF/jsp/addCourse.jsp", result);
	}

	@Test
	public void testAddCourse() {

		MockHttpServletRequest request = new MockHttpServletRequest("POST",
				"/registration/addCourse");
		request.setParameter("title", title1);
		request.setParameter("code", code1);

		Course course = new Course();

		//give it to the binder
		WebDataBinder binder = new WebDataBinder(course);
		
		//And have the binder set the values on the object based on the 
		//parameter of the "Post"
		binder.bind(new MutablePropertyValues(request.getParameterMap()));

		ModelAndView result = courseController.addCourse(
				(Course) binder.getTarget(), binder.getBindingResult());

		assertEquals("redirect:getCourses", result.getViewName());
	}
}	
