package com.smartportal.SmartOnlineExamPortal.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.smartportal.SmartOnlineExamPortal.entity.Course;
import com.smartportal.SmartOnlineExamPortal.repository.CourseRepository;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    private final Path UPLOAD_DIR = Paths.get("src/main/resources/static/uploads");

    // ======================= SAVE COURSE =======================
    @Override
    public Course saveCourse(Course course, MultipartFile file) {
        try {
            if (file != null && !file.isEmpty()) {
                Files.createDirectories(UPLOAD_DIR);
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path filePath = UPLOAD_DIR.resolve(fileName);
                Files.write(filePath, file.getBytes());
                course.setThumb("/uploads/" + fileName); // relative path for web
            }
            return courseRepository.save(course);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ======================= GET COURSE BY ID =======================
    @Override
    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    // ======================= GET ALL COURSES =======================
    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    // ======================= DELETE COURSE =======================
    @Override
    public void deleteCourse(Long id) {
        Optional<Course> courseOpt = courseRepository.findById(id);
        if (courseOpt.isPresent()) {
            Course course = courseOpt.get();

            // Delete thumbnail file if exists
            if (course.getThumb() != null && !course.getThumb().isEmpty()) {
                try {
                    Path thumbPath = Paths.get("." + course.getThumb());
                    Files.deleteIfExists(thumbPath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            courseRepository.deleteById(id);
        }
    }

    // ======================= UPDATE COURSE =======================
    @Override
    public Course updateCourse(Long id, Course updatedCourse, MultipartFile file, boolean keepOldImage) {
        Optional<Course> courseOpt = courseRepository.findById(id);
        if (courseOpt.isPresent()) {
            Course existingCourse = courseOpt.get();

            existingCourse.setTitle(updatedCourse.getTitle());
            existingCourse.setCategory(updatedCourse.getCategory());
            existingCourse.setDuration(updatedCourse.getDuration());
            existingCourse.setLevel(updatedCourse.getLevel());
            existingCourse.setPrice(updatedCourse.getPrice());
            existingCourse.setDescription(updatedCourse.getDescription());

            try {
                if (!keepOldImage && file != null && !file.isEmpty()) {
                    // Delete old thumbnail
                    if (existingCourse.getThumb() != null && !existingCourse.getThumb().isEmpty()) {
                        Path oldThumb = Paths.get("." + existingCourse.getThumb());
                        Files.deleteIfExists(oldThumb);
                    }

                    // Save new thumbnail
                    Files.createDirectories(UPLOAD_DIR);
                    String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                    Path filePath = UPLOAD_DIR.resolve(fileName);
                    Files.write(filePath, file.getBytes());
                    existingCourse.setThumb("/uploads/" + fileName);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return courseRepository.save(existingCourse);
        }
        return null;
    }
}
