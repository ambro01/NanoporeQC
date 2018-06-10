package com.nanoporeqc.user.service;

import com.nanoporeqc.exceptions.UserAlreadyExistException;
import com.nanoporeqc.user.domain.ApplicationUser;
import com.nanoporeqc.user.dto.ChangePasswordUserDto;
import com.nanoporeqc.user.repository.ApplicationUserRepository;
import com.nanoporeqc.user.dto.UserDto;
import com.nanoporeqc.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserService {

    private final ApplicationUserRepository applicationUserRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public ApplicationUserService(final ApplicationUserRepository applicationUserRepository,
                                  final BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.applicationUserRepository = applicationUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void create(final UserDto userDto) {
        final ApplicationUser user = applicationUserRepository.findByUsername(userDto.getUsername());
        if (user != null) {
            throw new UserAlreadyExistException();
        }
        final ApplicationUser newUser = ApplicationUser.builder()
                .username(userDto.getUsername())
                .password(bCryptPasswordEncoder.encode(userDto.getPassword()))
                .build();

        applicationUserRepository.save(newUser);
    }

    public ApplicationUser getCurrentUser() {
        return applicationUserRepository.findByUsername(getCurrentUserName());
    }

    public String getCurrentUserName() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return authentication.getName();
        } else {
            throw new UserNotFoundException();
        }
    }

    public void changePassword(final ChangePasswordUserDto changePasswordUserDto) {
        final ApplicationUser user = applicationUserRepository.findByUsername(changePasswordUserDto.getUsername());
        if (user != null && bCryptPasswordEncoder.matches(changePasswordUserDto.getOldPassword(), user.getPassword())) {
            user.setPassword(bCryptPasswordEncoder.encode(changePasswordUserDto.getNewPassword()));
            applicationUserRepository.save(user);
        }
    }

}
