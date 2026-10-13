package com.smartportal.SmartOnlineExamPortal.service;

import java.util.List;

import com.smartportal.SmartOnlineExamPortal.entity.Course;

public interface EnrollService {

    /**
     * Enroll a user in a course
     * @param userId - ID of the user
     * @param courseId - ID of the course
     */
    void enrollUser(Long userId, Long courseId);

    /**
     * Check if a user is already enrolled in a course
     * @param userId - ID of the user
     * @param courseId - ID of the course
     * @return true if already enrolled, false otherwise
     */
    boolean isAlreadyEnrolled(Long userId, Long courseId);

    /**
     * Get all courses a user is enrolled in
     * @param userId - ID of the user
     * @return List of enrolled courses
     */
    List<Course> getEnrolledCourses(Long userId);
}
