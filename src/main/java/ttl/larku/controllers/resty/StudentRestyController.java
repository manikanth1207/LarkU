package ttl.larku.controllers.resty;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import ttl.larku.domain.Student;
import ttl.larku.domain.StudentListHolder;
import ttl.larku.service.RegistrationService;

@Controller
@RequestMapping("/adminrest")
public class StudentRestyController {
	
	@Resource
	private RegistrationService regService;
	
	
	@RequestMapping(value="/getStudent", method=RequestMethod.GET,
			consumes={"text/html"})
	public ModelAndView getStudentWithForm(@RequestParam int id) {
		Student student = regService.getStudentService().getStudent(id);
		if(student == null) {
			return new ModelAndView("/WEB-INF/jsp/error/studentNotFound.jsp");
		}
		ModelAndView mav = new ModelAndView("/WEB-INF/jsp/showStudent.jsp", "student", student);
		return mav;
	}
	
	@RequestMapping(value="/addStudents", method=RequestMethod.GET)
	public ModelAndView addStudentsResty() {
		ModelAndView mav = new ModelAndView("/WEB-INF/jsp/addStudentsJQueryAjax.jsp",
				"statusList", Student.Status.values());
		return mav;
	}
	
	@RequestMapping(value="/students/{id}", method=RequestMethod.GET, 
			produces={"application/xml", "application/json" })
	public @ResponseBody Object getStudentXJWithPathVariable(@PathVariable("id") int id) {
		Student student = regService.getStudentService().getStudent(id);
		if(student == null) {
			return new RestErrorObject("Student not found");
		}
		return student;
	}
	
	@RequestMapping(value="/studentsParam", method=RequestMethod.GET, 
			produces={"application/xml", "application/json" })
	public @ResponseBody Object getStudentXJWithRequestParam(@RequestParam("id") int id) {
		Student student = regService.getStudentService().getStudent(id);
		if(student == null) {
			return new RestErrorObject("Student not found");
		}
		return student;
	}
	
	@RequestMapping(value="/students", method=RequestMethod.POST,
			consumes={"application/xml", "application/json" },
			produces={"application/xml", "application/json" })
	public @ResponseBody Student addStudent(@RequestBody Student student) {
		Student student2 = regService.getStudentService().createStudent(student);
		
		return student2;
	}
	
	@RequestMapping(value="/showStudents", method=RequestMethod.GET)
	public ModelAndView showStudents() {
		List<Student> students= regService.getStudentService().getAllStudents();
		return new ModelAndView("/WEB-INF/jsp/showStudentsJQuery.jsp", "students", students);
	}
	
	@RequestMapping(value="/students", method=RequestMethod.GET, 
			produces={"application/xml", "application/json" })
	public @ResponseBody List<Student> getStudentsXmlJson() {
		List<Student> students= regService.getStudentService().getAllStudents();
		return students;
	}
	
	@RequestMapping(value="/heldstudents", method=RequestMethod.GET, 
			produces={"application/xml", "application/json" })
	public @ResponseBody StudentListHolder getStudentsXmlJsonHolder() {
		List<Student> students= regService.getStudentService().getAllStudents();
		return new StudentListHolder(students);
	}
}