package com.smartportal.SmartOnlineExamPortal;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncodePassword {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "admin123"; // बदलायचं असल्यास इथे बदल
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println(encodedPassword);
    }
}
