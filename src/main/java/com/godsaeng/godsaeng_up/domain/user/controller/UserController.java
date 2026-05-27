package com.godsaeng.godsaeng_up.domain.user.controller;

import com.godsaeng.godsaeng_up.domain.user.dto.RegisterRequest;
import com.godsaeng.godsaeng_up.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    @GetMapping("/register")
    public String register(Model model) {
        if (!model.containsAttribute("registerRequest")) {
            model.addAttribute("registerRequest", new RegisterRequest("", "", "", ""));
        }
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegisterRequest registerRequest,
                           BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            addFieldErrors(bindingResult, model);
            return "auth/register";
        }
        try {
            userService.signUp(registerRequest);
            return "redirect:/auth/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("registerRequest", registerRequest);
            model.addAttribute("formError", e.getMessage());
            return "auth/register";
        }
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    private void addFieldErrors(BindingResult bindingResult, Model model) {
        model.addAttribute("registerRequest", bindingResult.getTarget());
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            model.addAttribute(fieldError.getField() + "Error", fieldError.getDefaultMessage());
        }
    }
}
