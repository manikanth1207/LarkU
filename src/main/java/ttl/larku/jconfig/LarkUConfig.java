package ttl.larku.jconfig;

import javax.annotation.Resource;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;

import ttl.larku.dao.BaseDAO;
import ttl.larku.domain.Course;
import ttl.larku.domain.ScheduledClass;
import ttl.larku.domain.Student;
import ttl.larku.service.ClassService;
import ttl.larku.service.CourseService;
import ttl.larku.service.RegistrationService;
import ttl.larku.service.StudentService;

/**
 * Configures the business Services.  
 * IMPORTANT: You *HAVE* to provide bean definitions for
 * "classDAO", "courseDAO", and "studentDAO" in some
 * other config location. 
 * @author whynot
 *
 */
@Configuration
@ComponentScan(basePackages={"ttl.larku.service"}) 
public class LarkUConfig {
	
	
	//These *HAVE* to be set up in some other config file,
	//else the injections will fail 
	@Resource
	private BaseDAO<Student> studentDAO;

	@Resource
	private BaseDAO<ScheduledClass> classDAO;

	@Resource
	private BaseDAO<Course> courseDAO;
	
	@Bean
	public CourseService courseService() {
		CourseService service = new CourseService();
		//service.setCourseDAO(dbConfig.courseDAO());
		service.setCourseDAO(courseDAO);
		
		return service;
	}
	
	@Bean
	public ClassService classService() {
		ClassService service = new ClassService();
		//service.setClassDAO(dbConfig.classDAO());
		service.setClassDAO(classDAO);
		service.setCourseService(courseService());
		return service;
	}
	
	@Bean
	public StudentService studentService() {
		StudentService service = new StudentService();
		//service.setStudentDAO(dbConfig.studentDAO());
		service.setStudentDAO(studentDAO);
		
		return service;
	}
	
	@Bean(name="registrationService")
	public RegistrationService regService() {
		RegistrationService service = new RegistrationService();
		service.setClassService(classService());
		service.setCourseService(courseService());
		service.setStudentService(studentService());
		
		return service;
	}
	
	@Bean(name="conversionService")
	public FormattingConversionServiceFactoryBean conversionService() {
		FormattingConversionServiceFactoryBean fcsfb = new  FormattingConversionServiceFactoryBean();

		return fcsfb;
	}
	
	
	@Bean
	public DateTimeFormatter localDateFormatter() {
		
		DateTimeFormatter dtFormatter = DateTimeFormat.forPattern("MM/dd/yyyy");
		return dtFormatter;
	}
}
