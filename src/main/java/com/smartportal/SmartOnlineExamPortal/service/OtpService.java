package com.smartportal.SmartOnlineExamPortal.service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class OtpService {

    @Autowired
    private JavaMailSender mailSender;

    // email -> otp
    private final Map<String, String> otpStorage = new ConcurrentHashMap<>();

    // ================= GENERATE + SEND =================
    public void generateAndSendOtp(String email) {

        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        otpStorage.put(email, otp);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("EduExam - OTP Verification");
            message.setText("Your OTP is: " + otp);

            mailSender.send(message);
            System.out.println("OTP sent to " + email + " : " + otp);
        } catch (Exception e) {
            System.out.println("OTP email error: " + e.getMessage());
        }
    }

    // ================= VALIDATE =================
    public boolean validateOtp(String email, String otp) {

        if (!otpStorage.containsKey(email)) return false;

        boolean valid = otp.equals(otpStorage.get(email));

        if (valid) {
            otpStorage.remove(email); // one-time use
        }

        return valid;
    }
}
