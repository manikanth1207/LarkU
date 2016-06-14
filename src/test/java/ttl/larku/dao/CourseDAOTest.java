package ttl.larku.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ttl.larku.domain.Course;
import ttl.larku.domain.ScheduledClass;
import ttl.larku.domain.Student;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:larkUTestWebContext.xml" })
@DirtiesContext(classMode=ClassMode.AFTER_EACH_TEST_METHOD)
@WebAppConfiguration("WebContent")
public class CourseDAOTest {
	
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
	
	@Resource(name="courseDAO")
	private BaseDAO<Course> dao;
	
	@Before
	public void setup() {
		student1 = new Student(name1);
		student2 = new Student(name2);
		course1 = new Course(code1, title1);
		course2 = new Course(code2, title2);
		
		class1 = new ScheduledClass(course1, startDate1, endDate1);
		class2 = new ScheduledClass(course2, startDate2, endDate2);
		
		//dao = new InMemoryCourseDAO();
		dao.deleteStore();
		dao.createStore();
	}

	@Test
	public void testGetAll() {
		List<Course> courses = dao.getAll();
		assertEquals(0, courses.size());
	}

	@Test
	public void testCreate() {
		int newId = dao.create(course1).getId();
		
		Course resultCourse = dao.get(newId);
		
		assertEquals(newId, resultCourse.getId());
	}
	
	@Test(expected = NullPointerException.class)
	public void testDelete() {
		int id1 = dao.create(course1).getId();
		
		Course resultCourse = dao.get(id1);
		assertEquals(resultCourse.getId(), id1);
		
		dao.delete(resultCourse);
		
		resultCourse = dao.get(id1);
		
		assertEquals(title1, dao.get(id1).getTitle());
	}
	
	@Test
	public void testUpdate() {
		int newId = dao.create(course1).getId();
		
		Course resultCourse = dao.get(newId);
		
		assertEquals(newId, resultCourse.getId());
		
		resultCourse.setTitle(title2);
		dao.update(resultCourse);
		
		resultCourse = dao.get(resultCourse.getId());
		assertEquals(title2, resultCourse.getTitle());
	}
	

}
