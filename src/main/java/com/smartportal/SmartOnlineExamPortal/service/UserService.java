package com.smartportal.SmartOnlineExamPortal.service;

import java.util.List;

import com.smartportal.SmartOnlineExamPortal.entity.User;

public interface UserService {

    // ===== SAVE / REGISTER =====
    User saveUser(User user);

    // ===== FIND BY EMAIL (LOGIN / PROFILE) =====
    User findByEmail(String email);

    // ===== FIND BY ID =====
    User findById(Long id);

    // ===== GET ALL USERS (ADMIN) =====
    List<User> getAllUsers();

    // ===== UPDATE USER =====
    User updateUser(User user);

    // ===== UPDATE USER PHOTO =====
    User updateUserPhoto(Long id, String photoName);

    // ===== DELETE USER =====
    void deleteUser(Long userId);

    User login(String email, String password);
}
