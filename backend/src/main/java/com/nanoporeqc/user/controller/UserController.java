package com.nanoporeqc.user.controller;

import com.nanoporeqc.user.dto.ChangePasswordUserDto;
import com.nanoporeqc.user.dto.UserDto;
import com.nanoporeqc.exceptions.UserNotFoundException;
import com.nanoporeqc.user.service.ApplicationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/create")
    public ResponseEntity createUser(@RequestBody final UserDto userDto) {
        applicationUserService.save(userDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/change-password")
    public ResponseEntity changePassword(@RequestBody final ChangePasswordUserDto changePasswordUserDto) {
        applicationUserService.changePassword(changePasswordUserDto);
        return new ResponseEntity(HttpStatus.OK);
    }
}