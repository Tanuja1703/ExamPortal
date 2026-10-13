package com.smartportal.SmartOnlineExamPortal.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.smartportal.SmartOnlineExamPortal.entity.User;
import com.smartportal.SmartOnlineExamPortal.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    // ================= SAVE / REGISTER USER =================
    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // ================= FIND BY EMAIL =================
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // ================= LOGIN =================
    @Override
    public User login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    // ================= GET ALL USERS =================
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ================= UPDATE FULL USER =================
    @Override
    public User updateUser(User updatedUser) {
        User existing = userRepository.findById(updatedUser.getId()).orElse(null);
        if (existing == null) return null;

        existing.setName(updatedUser.getName());
        existing.setEmail(updatedUser.getEmail());
        existing.setAge(updatedUser.getAge());
        existing.setCity(updatedUser.getCity());
        existing.setCountry(updatedUser.getCountry());
        existing.setQualification(updatedUser.getQualification());
        existing.setAbout(updatedUser.getAbout());

        if (updatedUser.getPhoto() != null && !updatedUser.getPhoto().isEmpty()) {
            existing.setPhoto(updatedUser.getPhoto());
        }

        return userRepository.save(existing);
    }

    // ================= UPDATE ONLY PHOTO =================
    @Override
    public User updateUserPhoto(Long id, String photoName) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return null;

        user.setPhoto(photoName);
        return userRepository.save(user);
    }

    // ================= FIND BY ID =================
    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // ================= DELETE USER =================
    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
