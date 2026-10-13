package com.smartportal.SmartOnlineExamPortal.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.smartportal.SmartOnlineExamPortal.entity.User;
import com.smartportal.SmartOnlineExamPortal.service.OtpService;
import com.smartportal.SmartOnlineExamPortal.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private OtpService otpService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // ================= HOME =================
    @GetMapping({"/", "/home"})
    public String home() {
        return "home";
    }

    // ================= STATIC PAGES =================
    @GetMapping("/about") public String about() { return "about"; }
    @GetMapping("/practice") public String practice() { return "practice"; }
    @GetMapping("/program") public String program() { return "program"; }
    @GetMapping("/computer") public String computer() { return "computer"; }
    @GetMapping("/ai") public String ai() { return "ai"; }
    @GetMapping("/growth") public String growth() { return "growth"; }
    @GetMapping("/web") public String web() { return "web"; }
    @GetMapping("/tips") public String tips() { return "tips"; }

    // ================= REGISTER =================
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, RedirectAttributes ra) {

        if (userService.findByEmail(user.getEmail()) != null) {
            ra.addFlashAttribute("error", "Email already exists!");
            return "redirect:/register";
        }

        user.setRole("STUDENT");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveUser(user);

        ra.addFlashAttribute("success", "Registered successfully!");
        return "redirect:/login";
    }

    // ================= LOGIN =================
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }


  @PostMapping("/login")
public String login(@RequestParam String email,
                    @RequestParam String password,
                    HttpSession session) {
    User user = userService.login(email, password);
    if (user != null) {
        session.setAttribute("user", user);
        return "redirect:/courses";
    } else {
        return "redirect:/login?error=true";
    }
}



    // ================= AFTER LOGIN =================
    @GetMapping("/redirect-after-login")
    public String redirectAfterLogin(Authentication auth) {

        if (auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/admin/dashboard";
        }
        return "redirect:/profile";
    }

    // ================= PROFILE =================
    @GetMapping("/profile")
    public String profile(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal().equals("anonymousUser")) {
            return "redirect:/login";
        }

        User user = userService.findByEmail(auth.getName());
        model.addAttribute("user", user);
        return "profile";
    }

    // ================= EDIT PROFILE =================
    @GetMapping("/edit-profile")
    public String editProfile(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal().equals("anonymousUser")) {
            return "redirect:/login";
        }

        User user = userService.findByEmail(auth.getName());
        model.addAttribute("user", user);
        return "edit-profile";
    }

    @PostMapping("/edit-profile")
    public String updateProfile(
            @RequestParam Long id,
            @RequestParam String name,
            @RequestParam(required = false) Integer age,
            @RequestParam(required = false) String qualification,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String about,
            @RequestParam(required = false) MultipartFile photoFile,
            RedirectAttributes ra) {

        User user = userService.findById(id);

        if (user == null) {
            ra.addFlashAttribute("error", "User not found!");
            return "redirect:/profile";
        }

        user.setName(name);
        user.setAge(age);
        user.setQualification(qualification);
        user.setCity(city);
        user.setCountry(country);
        user.setAbout(about);

        try {
            if (photoFile != null && !photoFile.isEmpty()) {

                String uploadDir = "src/main/resources/static/uploads/";
                new File(uploadDir).mkdirs();

                String fileName = System.currentTimeMillis() + "_" + photoFile.getOriginalFilename();
                Path path = Paths.get(uploadDir + fileName);
                Files.write(path, photoFile.getBytes());

                user.setPhoto(fileName);
            }
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Photo upload failed!");
        }

        userService.updateUser(user);
        ra.addFlashAttribute("success", "Profile updated!");
        return "redirect:/profile";
    }

    // ================= FORGOT PASSWORD =================
    @GetMapping("/forgot")
    public String forgotPage() {
        return "forgot";
    }

    @PostMapping("/forgot")
    public String sendOtp(@RequestParam String email, RedirectAttributes ra) {

        User user = userService.findByEmail(email);
        if (user == null) {
            ra.addFlashAttribute("error", "Email not registered!");
            return "redirect:/forgot";
        }

        otpService.generateAndSendOtp(email); // ✅ FIXED
        ra.addFlashAttribute("success", "OTP sent to your email!");
        return "redirect:/verify-otp?email=" + email;
    }

    // ================= VERIFY OTP =================
    @GetMapping("/verify-otp")
    public String verifyOtpPage(@RequestParam String email, Model model) {
        model.addAttribute("email", email);
        return "verify-otp";
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(
            @RequestParam String email,
            @RequestParam String otp,
            RedirectAttributes ra) {

        if (otpService.validateOtp(email, otp)) {
            return "redirect:/reset-password?email=" + email;
        }

        ra.addFlashAttribute("error", "Invalid OTP!");
        return "redirect:/verify-otp?email=" + email;
    }

    // ================= RESET PASSWORD =================
    @GetMapping("/reset-password")
    public String resetPasswordPage(@RequestParam String email, Model model) {
        model.addAttribute("email", email);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            RedirectAttributes ra) {

        if (!password.equals(confirmPassword)) {
            ra.addFlashAttribute("error", "Passwords do not match!");
            return "redirect:/reset-password?email=" + email;
        }

        User user = userService.findByEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        userService.saveUser(user);

        ra.addFlashAttribute("success", "Password reset successful!");
        return "redirect:/login";
    }
}
