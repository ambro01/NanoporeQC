package com.nanoporeqc.user.controller;

import com.nanoporeqc.user.dto.UserDto;
import com.nanoporeqc.user.service.ApplicationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final ApplicationUserService applicationUserService;

    @Autowired
    public UserController(final ApplicationUserService applicationUserService) {
        this.applicationUserService = applicationUserService;
    }

    @PostMapping("/sign-up")
    public void signUp(@RequestBody UserDto userDto) {
        applicationUserService.save(userDto);
    }
}