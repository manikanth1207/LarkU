package ttl.larku.controllers;

import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.ModelAndView;

import ttl.larku.domain.ScheduledClass;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:larkUTestWebContext.xml" })
public class RegistrationControllerTest {

	@Resource(name="courseController")
	private CourseController courseController;
	
	@Resource(name="registrationController")
	private RegistrationController regController;

	@Resource
	private ApplicationContext appContext;

	@Test
	public void testGetAllClasses() {
		ModelAndView result = regController.getAllClasses();
		assertEquals("/WEB-INF/jsp/showClasses.jsp", result.getViewName());
	}

	@Test
	public void testShowAddClassForm() {
		ModelAndView result = regController.showAddClassForm(new ScheduledClass());
		assertEquals("/WEB-INF/jsp/addClass.jsp", result.getViewName());
	}

	@Test
	public void testAddClass() {
		ModelAndView result = regController.addClass("MATH-101", "10/10/2016", "10/10/2017");
		assertEquals("redirect:getAllClasses", result.getViewName());
	}

	
	@Test
	public void testShowRegisterStudentForm() {
		ModelAndView result = regController.showRegisterStudentForm();
		assertEquals("/WEB-INF/jsp/addRegistration.jsp", result.getViewName());
	}

	@Test
	public void testRegisterStudent() {
		ModelAndView result = regController.registerStudent(1, 1);
		assertEquals("/WEB-INF/jsp/showStudent.jsp", result.getViewName());
	}

}
