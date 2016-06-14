package ttl.larku.service;

import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class StudentServiceTest {

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
	
	private Course course1;
	private Course course2;
	private ScheduledClass class1;
	private ScheduledClass class2;
	private Student student1;
	private Student student2;
	
	@Resource
	private StudentService studentService;
	
	@Before
	public void setup() {
		student1 = new Student(name1);
		student2 = new Student(name2);
		course1 = new Course(code1, title1);
		course2 = new Course(code2, title2);
		
		class1 = new ScheduledClass(course1, startDate1, endDate1);
		class2 = new ScheduledClass(course2, startDate2, endDate2);
		
		//studentService = new StudentService();
		studentService.clear();
	}
	
	@Test
	public void testCreateStudent() {
		Student newStudent = studentService.createStudent(name1);
		
		Student result = studentService.getStudent(newStudent.getId());
		
		assertEquals(name1, result.getName());
		assertEquals(1, studentService.getAllStudents().size());
	}
	
	@Test
	public void testDeleteStudent() {
		Student student1 = studentService.createStudent(name1);
		Student student2 = studentService.createStudent(name2);
		
		assertEquals(2, studentService.getAllStudents().size());
		
		studentService.deleteStudent(student1.getId());
		
		assertEquals(1, studentService.getAllStudents().size());
		assertEquals(name2, studentService.getAllStudents().get(0).getName());
	}

	@Test
	public void testDeleteNonExistentStudent() {
		Student student1 = studentService.createStudent(name1);
		Student student2 = studentService.createStudent(name2);
		
		assertEquals(2, studentService.getAllStudents().size());
		
		//Non existent Id
		studentService.deleteStudent(9999);
		
		assertEquals(2, studentService.getAllStudents().size());
	}
	
	@Test
	public void testUpdateStudent() {
		Student student1 = studentService.createStudent(name1);
		
		assertEquals(1, studentService.getAllStudents().size());
		
		student1.setName(name2);
		studentService.updateStudent(student1);
		
		assertEquals(1, studentService.getAllStudents().size());
		assertEquals(name2, studentService.getAllStudents().get(0).getName());
	}

}
