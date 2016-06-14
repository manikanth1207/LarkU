package ttl.larku.jconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import ttl.larku.dao.BaseDAO;
import ttl.larku.domain.Course;
import ttl.larku.domain.ScheduledClass;
import ttl.larku.domain.Student;

@Configuration
@EnableWebMvc
@Import({LarkUConfig.class, LarkUTestDataConfig.class})
@Profile("test")
public class LarkUTestWebConfig extends WebMvcConfigurerAdapter {
	
	@Autowired
	private LarkUTestDataConfig testData;
	
	@Bean
	public BaseDAO<Student> studentDAO() {
		return testData.studentDAOWithInitData();
	}
	
	@Bean
	public BaseDAO<ScheduledClass> classDAO() {
		return testData.classDAOWithInitData();
	}
	
	@Bean
	public BaseDAO<Course> courseDAO() {
		return testData.courseDAOWithInitData();
	}
}
