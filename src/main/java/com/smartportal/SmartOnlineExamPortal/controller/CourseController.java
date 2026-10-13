package com.smartportal.SmartOnlineExamPortal.controller;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.ArrayList; // जर Java 8 मध्ये List बनवायची असेल तर


import com.smartportal.SmartOnlineExamPortal.entity.Course;
import com.smartportal.SmartOnlineExamPortal.entity.User;
import com.smartportal.SmartOnlineExamPortal.service.CourseService;
import com.smartportal.SmartOnlineExamPortal.service.EnrollService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CourseController {

    private final CourseService courseService;
    private final EnrollService enrollService;

    public CourseController(CourseService courseService, EnrollService enrollService) {
        this.courseService = courseService;
        this.enrollService = enrollService;
    }

    // ================== USER – VIEW ALL COURSES ==================
    @GetMapping("/courses")
    public String userCourses(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());
        return "courses";
    }

    // ================== USER – COURSE DETAILS ==================
    @GetMapping("/course/details/{id}")
public String courseDetails(@PathVariable Long id, Model model) {
    Course course = courseService.getCourseById(id).orElse(null);
    if (course == null) return "redirect:/courses";

    model.addAttribute("course", course);

    // Syllabus list बनवून model मध्ये add करा
   List<String> syllabus = new ArrayList<>();
syllabus.add("Project 1");
syllabus.add("Project 2");
syllabus.add("Quiz 1");
model.addAttribute("syllabus", syllabus);


    model.addAttribute("alreadyEnrolled", false); // later logic
    return "course-details";
}


    // ================== USER – ENROLL COURSE ==================
    // @PostMapping("/course/enroll/{id}")
    // public String enrollCourse(@PathVariable Long id, HttpSession session) {
    //     User user = (User) session.getAttribute("user");
    //     if (user == null) return "redirect:/login";

    //     if (!enrollService.isAlreadyEnrolled(user.getId(), id)) {
    //         enrollService.enrollUser(user.getId(), id);
    //     }
    //     return "redirect:/my-courses";
    // }
    @PostMapping("/course/enroll/{id}")
public String enrollCourse(@PathVariable Long id, HttpSession session, Model model) {
    User user = (User) session.getAttribute("user");
    if (user == null) return "redirect:/login";

    if (!enrollService.isAlreadyEnrolled(user.getId(), id)) {
        enrollService.enrollUser(user.getId(), id);
        model.addAttribute("msg", "Enroll Done! 🎉");
    } else {
        model.addAttribute("msg", "You are already enrolled!");
    }

    Course course = courseService.getCourseById(id).orElse(null);
    if (course == null) return "redirect:/courses";

    model.addAttribute("course", course);

    List<String> syllabus = new ArrayList<>();
    syllabus.add("Project 1");
    syllabus.add("Project 2");
    syllabus.add("Quiz 1");
    model.addAttribute("syllabus", syllabus);

    model.addAttribute("alreadyEnrolled", true); // button disabled after enroll
    return "course-details";
}


    // ================== USER – MY COURSES ==================
    @GetMapping("/my-courses")
    public String myCourses(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        model.addAttribute("list", enrollService.getEnrolledCourses(user.getId()));
        return "my-courses";
    }

    // ================== ADMIN – VIEW ALL COURSES ==================
    @GetMapping("/admin/courses")
    public String adminCourses(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());
        return "admin/courses";
    }

    // ================== ADMIN – ADD COURSE FORM ==================
    @GetMapping("/admin/course/add")
    public String addCourseForm(Model model) {
        model.addAttribute("course", new Course());
        return "admin/add-course";
    }

    // ================== ADMIN – SAVE COURSE ==================
    @PostMapping("/admin/course/save")
    public String saveCourse(@ModelAttribute Course course,
                             @RequestParam("thumbFile") MultipartFile file) {
        courseService.saveCourse(course, file);
        return "redirect:/admin/courses";
    }

    // ================== ADMIN – EDIT COURSE FORM ==================
    @GetMapping("/admin/course/edit/{id}")
    public String editCourseForm(@PathVariable Long id, Model model) {
        Course course = courseService.getCourseById(id).orElse(null);
        model.addAttribute("course", course);
        return "admin/edit-course";
    }

    // ================== ADMIN – UPDATE COURSE ==================
    @PostMapping("/admin/course/update/{id}")
    public String updateCourse(@PathVariable Long id,
                               @ModelAttribute Course course,
                               @RequestParam(value = "thumbFile", required = false) MultipartFile file,
                               @RequestParam(value = "keepOldImage", required = false) boolean keepOldImage) {
        courseService.updateCourse(id, course, file, keepOldImage);
        return "redirect:/admin/courses";
    }

    // ================== ADMIN – DELETE COURSE ==================
    @GetMapping("/admin/course/delete/{id}")
    public String deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return "redirect:/admin/courses";
    }
}
