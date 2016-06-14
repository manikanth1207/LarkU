package ttl.larku.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ttl.larku.domain.Student;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:larkUTestWebContext.xml" })
@DirtiesContext
@WebAppConfiguration("WebContent")
public class StudentDAOTest {

	private String name1 = "Bloke";
	private String name2 = "Blokess";
	private String newName = "Different Bloke";
	private Student student1;
	private Student student2;
	
	@Resource(name="studentDAO")
	private BaseDAO<Student> dao;
	
	@Before
	public void setup() {
		student1 = new Student(name1);
		student2 = new Student(name2);
		
		dao.deleteStore();
		dao.createStore();
	}
	
	
	@Test
	public void testGetAll() {
		List<Student> students = dao.getAll();
		assertEquals(0, students.size());
	}
	
	@Test
	public void testCreate() {
		
		int newId = dao.create(student1).getId();
		
		Student resultstudent = dao.get(newId);
		
		assertEquals(newId, resultstudent.getId());
	}
	
	@Test
	public void testUpdate() {
		int newId = dao.create(student1).getId();
		
		Student resultStudent = dao.get(newId);
		
		assertEquals(newId, resultStudent.getId());
		
		resultStudent.setName(newName);
		dao.update(resultStudent);
		
		resultStudent = dao.get(resultStudent.getId());
		assertEquals(newName, resultStudent.getName());
	}
	
	@Test
	public void testDelete() {
		int id1 = dao.create(student1).getId();
		
		Student resultStudent = dao.get(id1);
		assertEquals(resultStudent.getId(), id1);
		
		dao.delete(resultStudent);
		
		resultStudent = dao.get(id1);
		
		assertEquals(null, resultStudent);
		
	}

}
