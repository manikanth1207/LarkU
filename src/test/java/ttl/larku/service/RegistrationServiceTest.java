package ttl.larku.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ttl.larku.domain.Course;
import ttl.larku.domain.ScheduledClass;
import ttl.larku.domain.Student;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:larkUTestWebContext.xml" })
@DirtiesContext
@WebAppConfiguration("WebContent")
public class RegistrationServiceTest {

	private String name1 = "Bloke";
	private String name2 = "Blokess";
	private String newName = "Different Bloke";
	
	private String code1 = "BOT-101";
	private String code2 = "BOT-202";
	private String title1 = "Intro To Botany";
	private String title2 = "Outtro To Botany";
	
	private String startDate1 = "10/10/2012";
	private String startDate2 = "10/10/2013";
	private String badStartDate = "12/12/2099";
	private String endDate1 = "05/10/2013";
	private String endDate2 = "05/10/2014";
	
	private Course course1;
	private Course course2;
	private ScheduledClass class1;
	private ScheduledClass class2;
	private Student student1;
	private Student student2;
	
	/*
	private ClassService classService;
	private CourseService courseService;
	private StudentService studentService;
	*/
	
	@Resource
	private RegistrationService regService;
	
	@Resource
	private ApplicationContext appContext;
	
	@Before
	public void setup() {
		student1 = new Student(name1);
		student2 = new Student(name2);
		course1 = new Course(code1, title1);
		course2 = new Course(code2, title2);
		
		class1 = new ScheduledClass(course1, startDate1, endDate1);
		class2 = new ScheduledClass(course2, startDate2, endDate2);
		
		/*
		ApplicationContext appContext = 
				new ClassPathXmlApplicationContext("larkUContext.xml");
		*/
		
		//regService = appContext.getBean("regService", RegistrationService.class);
		ClassService classService = appContext.getBean("classService", ClassService.class);
		CourseService courseService = appContext.getBean("courseService", CourseService.class); 
		StudentService studentService = appContext.getBean("studentService", StudentService.class);
		//regService = new RegistrationService();
		//ClassService classService = new ClassService();
		//CourseService courseService = new CourseService();
		//StudentService studentService = new StudentService();
		//Empty out our databases
		studentService.clear();
		courseService.clear();
		classService.clear();
		
		//Initialize courseService
		course1 = courseService.createCourse(code1, title1);
		course2 = courseService.createCourse(code2, title2);
		
		student1 = studentService.createStudent(name1);
		student2 = studentService.createStudent(name2);
		
	}
	
	@Test
	public void testAddClassAndStudent() {
		ScheduledClass sClass = regService.addNewClassToSchedule(code1, startDate1, endDate1);
		
		regService.registerStudentForClass(student1.getId(), code1, startDate1);
		
		List<Student> students = regService.getStudentsForClass(code1, startDate1);
		
		assertEquals(1, students.size());
		assertEquals(name1, students.get(0).getName());
		
		assertEquals(1, students.get(0).getClasses().size());
		assertEquals(code1, students.get(0).getClasses().get(0).getCourse().getCode());
	}
	
	@Test
	public void testDropStudent() {
		ScheduledClass sClass = regService.addNewClassToSchedule(code1, startDate1, endDate1);
		
		regService.registerStudentForClass(student1.getId(), code1, startDate1);
		regService.registerStudentForClass(student2.getId(), code1, startDate1);
		
		List<Student> students = regService.getStudentsForClass(code1, startDate1);
		
		assertEquals(2, students.size());
		assertEquals(name1, students.get(0).getName());
		
		regService.dropStudentFromClass(student1.getId(), code1, startDate1);
		
		students = regService.getStudentsForClass(code1, startDate1);
		
		assertEquals(1, students.size());
		assertEquals(name2, students.get(0).getName());
		
		assertEquals(1, students.get(0).getClasses().size());
		assertEquals(code1, students.get(0).getClasses().get(0).getCourse().getCode());
	}

	@Test
	public void testRegisterForNonExistentClass() {
		ScheduledClass sClass = regService.addNewClassToSchedule(code1, startDate1, endDate1);
		
		regService.registerStudentForClass(student1.getId(), code1, badStartDate);
		
		List<Student> students = regService.getStudentsForClass(code1, startDate1);
		
		assertEquals(0, students.size());
	}

	@Test
	public void testDropStudentFromNonExistentClass() {
		ScheduledClass sClass = regService.addNewClassToSchedule(code1, startDate1, endDate1);
		
		regService.registerStudentForClass(student1.getId(), code1, startDate1);
		regService.registerStudentForClass(student2.getId(), code1, startDate1);
		
		List<Student> students = regService.getStudentsForClass(code1, startDate1);
		
		assertEquals(2, students.size());
		assertEquals(name1, students.get(0).getName());
		
		regService.dropStudentFromClass(student1.getId(), code1, badStartDate);
		
		students = regService.getStudentsForClass(code1, startDate1);
		
		assertEquals(2, students.size());
	}

	@Test
	public void testGetStudentsFromNonExistentClass() {
		ScheduledClass sClass = regService.addNewClassToSchedule(code1, startDate1, endDate1);
		
		regService.registerStudentForClass(student1.getId(), code1, startDate1);
		regService.registerStudentForClass(student2.getId(), code1, startDate1);
		
		List<Student> students = regService.getStudentsForClass(code1, badStartDate);
		
		assertEquals(0, students.size());
	}

	@Test
	public void testGetAllScheduledClasses() {
		ScheduledClass sClass = regService.addNewClassToSchedule(code1, startDate1, endDate1);
		
		List<ScheduledClass> classes = regService.getScheduledClasses();
		
		assertEquals(1, classes.size());
		
		assertEquals(sClass.getStartDate(), classes.get(0).getStartDate());
	}



}
