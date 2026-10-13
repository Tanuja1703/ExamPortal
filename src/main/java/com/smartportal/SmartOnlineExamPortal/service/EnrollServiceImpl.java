package com.smartportal.SmartOnlineExamPortal.service;

import com.smartportal.SmartOnlineExamPortal.entity.Course;
import com.smartportal.SmartOnlineExamPortal.entity.Enrollment;
import com.smartportal.SmartOnlineExamPortal.entity.User;
import com.smartportal.SmartOnlineExamPortal.repository.CourseRepository;
import com.smartportal.SmartOnlineExamPortal.repository.EnrollRepository;
import com.smartportal.SmartOnlineExamPortal.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollServiceImpl implements EnrollService {

    @Autowired
    private EnrollRepository enrollRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    /* ----------------- ENROLL USER ----------------- */
    @Override
    public void enrollUser(Long userId, Long courseId) {
        if (!isAlreadyEnrolled(userId, courseId)) {
            User user = userRepository.findById(userId).orElse(null);
            Course course = courseRepository.findById(courseId).orElse(null);

            if (user != null && course != null) {
                Enrollment enrollment = new Enrollment();
                enrollment.setUser(user);
                enrollment.setCourse(course);
                enrollRepository.save(enrollment);
            }
        }
    }

    /* ----------------- CHECK ALREADY ENROLLED ----------------- */
    @Override
    public boolean isAlreadyEnrolled(Long userId, Long courseId) {
        return enrollRepository.existsByUserIdAndCourseId(userId, courseId);
    }

    /* ----------------- GET ENROLLED COURSES ----------------- */
    @Override
    public List<Course> getEnrolledCourses(Long userId) {
        List<Enrollment> enrollments = enrollRepository.findByUserId(userId);
        return enrollments.stream()
                .map(Enrollment::getCourse)
                .collect(Collectors.toList());
    }
}
