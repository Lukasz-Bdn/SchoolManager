package pl.schoolmanager.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.schoolmanager.entity.School;
import pl.schoolmanager.entity.Student;
import pl.schoolmanager.entity.Subject;
import pl.schoolmanager.entity.Teacher;
import pl.schoolmanager.entity.User;
import pl.schoolmanager.entity.UserRole;
import pl.schoolmanager.repository.SchoolRepository;
import pl.schoolmanager.repository.SubjectRepository;
import pl.schoolmanager.repository.TeacherRepository;
import pl.schoolmanager.repository.UserRepository;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private SubjectRepository subjectRepository;

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private SchoolRepository schoolRepo;

	@GetMapping("/all")
	public String all(Model m) {
		return "teacher/all_teachers";
	}

	// CREATE
	@GetMapping("/create")
	public String createTeacher(Model m) {
		m.addAttribute("teacher", new Teacher());
		return "teacher/new_teacher";
	}

	@PostMapping("/create")
	public String createTeacherPost(@Valid @ModelAttribute Teacher teacher, BindingResult bindingResult, Model m) {
		if (bindingResult.hasErrors()) {
			return "teacher/new_teacher";
		}
		this.teacherRepository.save(teacher);
		return "index";
	}

	// Make new teacher automatically creating new user role
	@GetMapping("/user_teacher")
	public String newTeacherFromUser(Model m) {
		m.addAttribute("teacher", new Teacher());
		return "teacher/user_teacher";
	}

	@PostMapping("/user_teacher")
	public String newTeacherFromUserPost(@Valid @ModelAttribute Teacher teacher, BindingResult bindingResult, Model m) {
		if (bindingResult.hasErrors()) {
			return "teacher/user_teacher";
		}
		User user = getLoggedUser();
		UserRole userRole = new UserRole();
		userRole.setUsername(user.getUsername());
		userRole.setUserRole("ROLE_TEACHER");
		userRole.setSchool(teacher.getSchool());
		userRole.setUser(user);
		teacher.setUserRole(userRole);
		this.teacherRepository.save(teacher);
		return "redirect:/teacher/all";
	}

	// READ
	@GetMapping("/view/{teacherId}")
	public String viewTeacher(Model m, @PathVariable long teacherId) {
		Teacher teacher = this.teacherRepository.findOne(teacherId);
		m.addAttribute("teacher", teacher);
		return "teacher/show_teacher";
	}

	// UPDATE
	@GetMapping("/update/{teacherId}")
	public String updateTeacher(Model m, @PathVariable long teacherId) {
		Teacher teacher = this.teacherRepository.findOne(teacherId);
		m.addAttribute("teacher", teacher);
		return "teacher/edit_teacher";
	}

	@PostMapping("/update/{teacherId}")
	public String updateTeacherPost(@Valid @ModelAttribute Teacher teacher, BindingResult bindingResult,
			@PathVariable long teacherId) {
		if (bindingResult.hasErrors()) {
			return "teacher/edit_teacher";
		}
		teacher.setId(teacherId);
		this.teacherRepository.save(teacher);
		return "index";
	}

	// DELETE
	@GetMapping("/delete/{teacherId}")
	public String deleteTeacher(@PathVariable long teacherId) {
		this.teacherRepository.delete(teacherId);
		return "index";
	}

	// SHOW ALL
	@ModelAttribute("availableTeachers")
	public List<Teacher> getTeachers() {
		return this.teacherRepository.findAll();
	}

	// ADD SUBJECT TO TEACHER
	@GetMapping("/addSubject/{teacherId}")
	public String addSubject(Model m, @PathVariable long teacherId) {
		Teacher teacher = this.teacherRepository.findOne(teacherId);
		List<Subject> subjects = this.subjectRepository.findAllByTeacherId(teacherId);
		List<Subject> subjectsNotTaught = this.subjectRepository.findAllByTeacherIdIsNullOrTeacherIdIsNot(teacherId);
		m.addAttribute("teacher", teacher);
		m.addAttribute("subjects", subjects);
		m.addAttribute("subjectsNotTaught", subjectsNotTaught);
		return "teacher/addSubject_teacher";
	}

	@GetMapping("addSubject/{teacherId}/{subjectId}")
	public String addSubject(@PathVariable long teacherId, @PathVariable long subjectId) {
		Teacher teacher = this.teacherRepository.findOne(teacherId);
		Subject subject = this.subjectRepository.findOne(subjectId);
		subject.getTeacher().add(teacher);
		this.subjectRepository.save(subject);
		return "redirect:/teacher/addSubject/{teacherId}";
	}

	// Additional methods
	private User getLoggedUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = ((org.springframework.security.core.userdetails.User) principal).getUsername();
		return this.userRepo.findOneByUsername(username);
	}

	@ModelAttribute("availableSchools")
	public List<School> availableSchools() {
		List<School> availableSchools = this.schoolRepo.findAll();
		return availableSchools;
	}

}
