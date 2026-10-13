package com.smartportal.SmartOnlineExamPortal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smartportal.SmartOnlineExamPortal.entity.Enrollment;

@Repository
public interface EnrollRepository extends JpaRepository<Enrollment, Long> {

    // Check if a user is already enrolled in a course
    boolean existsByUserIdAndCourseId(Long userId, Long courseId);

    // Get all enrollments of a specific user
    List<Enrollment> findByUserId(Long userId);

    // Get all enrollments for a specific course
    List<Enrollment> findByCourseId(Long courseId);
}
