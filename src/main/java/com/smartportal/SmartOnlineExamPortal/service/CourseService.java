package com.smartportal.SmartOnlineExamPortal.service;

import java.util.List;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;
import com.smartportal.SmartOnlineExamPortal.entity.Course;

public interface CourseService {
    Course saveCourse(Course course, MultipartFile file);
    Optional<Course> getCourseById(Long id);
    List<Course> getAllCourses();
    void deleteCourse(Long id);
    Course updateCourse(Long id, Course updatedCourse, MultipartFile file, boolean keepOldImage);
}
