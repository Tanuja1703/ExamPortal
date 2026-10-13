package com.smartportal.SmartOnlineExamPortal.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smartportal.SmartOnlineExamPortal.entity.Course;
import com.smartportal.SmartOnlineExamPortal.entity.User;
import com.smartportal.SmartOnlineExamPortal.service.CourseService;
import com.smartportal.SmartOnlineExamPortal.repository.UserRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserRepository userRepository;  // Add this to fetch users

    private final Path UPLOAD_DIR = Paths.get("src/main/resources/static/uploads");

    // ================= REDIRECT /admin TO /admin/dashboard =================
    @GetMapping("")
    public String redirectToDashboard() {
        return "redirect:/admin/dashboard";
    }

    // ================= DASHBOARD =================
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        // Courses
        List<Course> courses = courseService.getAllCourses();
        model.addAttribute("courses", courses);

        // Users
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);

        return "AdminPanel/admin";  // Thymeleaf template
    }

    // ================= ADD COURSE PAGE =================
    @GetMapping("/courses/add")
    public String showAddCourseForm(Model model) {
        model.addAttribute("course", new Course());
        return "AdminPanel/add-course";
    }

    // ================= SAVE COURSE =================
    @PostMapping("/courses/save")
    public String saveCourse(@ModelAttribute("course") Course course,
                             @RequestParam("file") MultipartFile file) {
        try {
            courseService.saveCourse(course, file);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/admin/courses/add?error=true";
        }
        return "redirect:/admin/dashboard?success=true";
    }

    // ================= EDIT COURSE PAGE =================
    @GetMapping("/courses/edit/{id}")
    public String showEditCourseForm(@PathVariable Long id, Model model) {
        Optional<Course> courseOpt = courseService.getCourseById(id);
        if (courseOpt.isPresent()) {
            model.addAttribute("course", courseOpt.get());
            return "AdminPanel/edit-course";
        } else {
            return "redirect:/admin/dashboard?error=notfound";
        }
    }

    // ================= UPDATE COURSE =================
    @PostMapping("/courses/update/{id}")
    public String updateCourse(@PathVariable Long id,
                               @ModelAttribute("course") Course course,
                               @RequestParam(value = "file", required = false) MultipartFile file,
                               @RequestParam(value = "keepOldImage", required = false) boolean keepOldImage) {
        try {
            courseService.updateCourse(id, course, file, keepOldImage);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/admin/courses/edit/" + id + "?error=true";
        }
        return "redirect:/admin/dashboard?success=updated";
    }

    // ================= DELETE COURSE =================
    @GetMapping("/courses/delete/{id}")
    public String deleteCourse(@PathVariable Long id) {
        try {
            courseService.deleteCourse(id);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/admin/dashboard?error=delete";
        }
        return "redirect:/admin/dashboard?success=deleted";
    }
}
