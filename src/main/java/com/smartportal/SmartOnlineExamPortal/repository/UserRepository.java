package com.smartportal.SmartOnlineExamPortal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smartportal.SmartOnlineExamPortal.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Email वरून user शोधण्यासाठी custom method
    User findByEmail(String email);
    
}
