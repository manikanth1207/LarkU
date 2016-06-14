package ttl.larku.jconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ttl.larku.dao.BaseDAO;
import ttl.larku.domain.Course;
import ttl.larku.domain.ScheduledClass;
import ttl.larku.domain.Student;

@Configuration
@Import({LarkUConfig.class, LarkUDBConfig.class})
public class LarkU_SE_DBConfig { 
	
	@Autowired
	private LarkUDBConfig testData;
	
	@Bean
	public BaseDAO<Student> studentDAO() {
		//return testData.jpaStudentDAO();
		return testData.jdbcTemplateStudentDAO();
	}
	
	@Bean
	public BaseDAO<ScheduledClass> classDAO() {
		//return testData.jpaClassDAO();
		return testData.jdbcTemplateClassDAO();
	}
	
	@Bean
	public BaseDAO<Course> courseDAO() {
		//return testData.jpaCourseDAO();
		return testData.jdbcTemplateCourseDAO();
	}
}
