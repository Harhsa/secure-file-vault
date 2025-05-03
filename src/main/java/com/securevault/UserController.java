package com.securevault;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // 🔐 Register Endpoint
    @PostMapping("/register")
    public RedirectView registerUser(@ModelAttribute User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return new RedirectView("/index.html?error=username_taken");
        }

        String hashedPassword = PasswordUtil.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);
        return new RedirectView("/dashboard.html");
    }

    // 🔐 Login Endpoint
    @PostMapping("/login")
    public RedirectView loginUser(@ModelAttribute User user) {
        User existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser != null) {
            String hashedInput = PasswordUtil.hashPassword(user.getPassword());
            if (existingUser.getPassword().equals(hashedInput)) {
                return new RedirectView("/dashboard.html");
            }
        }

        return new RedirectView("/index.html?error=invalid");
    }
}