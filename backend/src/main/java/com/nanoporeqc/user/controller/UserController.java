package com.nanoporeqc.user.controller;

import com.nanoporeqc.user.dto.UserDto;
import com.nanoporeqc.user.exceptions.UserNotFoundException;
import com.nanoporeqc.user.service.ApplicationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final ApplicationUserService applicationUserService;

    @Autowired
    public UserController(final ApplicationUserService applicationUserService) {
        this.applicationUserService = applicationUserService;
    }

    @GetMapping("/current-user")
    public String signUp() throws UserNotFoundException {
        return applicationUserService.getCurrentUserName();
    }

    @PostMapping("/sign-up")
    public void signUp(@RequestBody UserDto userDto) {
        applicationUserService.save(userDto);
    }
}