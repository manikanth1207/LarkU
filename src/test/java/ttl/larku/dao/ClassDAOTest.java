package ttl.larku.dao;

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
public class ClassDAOTest {

	private String name1 = "Bloke";
	private String name2 = "Blokess";
	private String newName = "Different Bloke";
	
	private String code1 = "BOT-101";
	private String code2 = "BOT-202";
	private String title1 = "Intro To Botany";
	private String title2 = "Advanced Basket Weaving";
	
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
	
	@Resource(name="classDAO")
	private BaseDAO<ScheduledClass> dao;
	
	@Resource(name="courseDAO")
	private BaseDAO<Course> courseDAO;
	
	@Before
	public void setup() {
		dao.deleteStore();
		dao.createStore();
		
		student1 = new Student(name1);
		student2 = new Student(name2);
		
		
		course1 = new Course(code1, title1);
		course1 = courseDAO.create(course1);
		//course1.setId(1);
		course2 = new Course(code2, title2);
		course2 = courseDAO.create(course2);
		//course2.setId(3);
		
		class1 = new ScheduledClass(course1, startDate1, endDate1);
		class2 = new ScheduledClass(course2, startDate2, endDate2);
		
		
	}
	@Test
	public void testCreate() {
		
		int newId = dao.create(class1).getId();
		
		ScheduledClass result = dao.get(newId);
		
		assertEquals(newId, result.getId());
	}
	
	@Test
	public void testUpdate() {
		int newId = dao.create(class1).getId();
		
		ScheduledClass result = dao.get(newId);
		
		assertEquals(newId, result.getId());
		
		result.setCourse(course2);
		dao.update(result);
		
		result = dao.get(newId);
		assertEquals(title2, result.getCourse().getTitle());
	}
	
	@Test
	public void testDelete() {
		int id1 = dao.create(class1).getId();
		
		ScheduledClass resultClass = dao.get(id1);
		assertEquals(resultClass.getId(), id1);
		
		dao.delete(resultClass);
		
		resultClass = dao.get(id1);
		
		assertEquals(null, resultClass);
	}

}
