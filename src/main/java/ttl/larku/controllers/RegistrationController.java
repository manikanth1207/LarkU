package ttl.larku.controllers;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ttl.larku.domain.Course;
import ttl.larku.domain.ScheduledClass;
import ttl.larku.domain.Student;
import ttl.larku.service.RegistrationService;

@Controller
@RequestMapping("/registration")
public class RegistrationController {
	
	@Resource
	private RegistrationService regService;
	
	@RequestMapping(value="/getAllClasses", method=RequestMethod.GET)
	public ModelAndView getAllClasses() {
		List<ScheduledClass> classes = regService.getClassService().getAllScheduledClasses();
		ModelAndView mav = new ModelAndView("/WEB-INF/jsp/showClasses.jsp", "classes", classes);
		return mav;
	}
	
	
	@RequestMapping(value="/addClass", method=RequestMethod.GET) 
	public ModelAndView showAddClassForm(ScheduledClass sClass) {
		List<Course> courses = regService.getCourseService().getAllCourses();
		
		return new ModelAndView("/WEB-INF/jsp/addClass.jsp", "courses", courses);
	}
	
	@RequestMapping(value="/addClass", method=RequestMethod.POST)
	public ModelAndView addClass(@RequestParam String courseCode,
			@RequestParam String startDate, @RequestParam String endDate) {
		
		regService.addNewClassToSchedule(courseCode, startDate, endDate);
				
		List<ScheduledClass> classes = regService.getClassService().getAllScheduledClasses();
		ModelAndView mav = new ModelAndView("redirect:getAllClasses", "classes", classes);
		return mav;
	}
	
	@RequestMapping(value="/registerStudentForClass", method=RequestMethod.GET)
	public ModelAndView showRegisterStudentForm() {
		List<Student> students = regService.getStudentService().getAllStudents();
		List<ScheduledClass> classes = regService.getClassService().getAllScheduledClasses();
		
		ModelAndView mav = new ModelAndView("/WEB-INF/jsp/addRegistration.jsp");
		mav.addObject("students", students);
		mav.addObject("classes", classes);
		
		return mav;
	}
	
	@RequestMapping(value="/registerStudentForClass", method=RequestMethod.POST)
	public ModelAndView registerStudent(@RequestParam int studentId, @RequestParam int classId) {
		
		ScheduledClass sClass = regService.getClassService().getScheduledClass(classId);
		regService.registerStudentForClass(studentId, sClass.getCourse().getCode(), sClass.getStartDate());
		
		Student student = regService.getStudentService().getStudent(studentId);
		
		return new ModelAndView("/WEB-INF/jsp/showStudent.jsp", "student", student);
	}
}
