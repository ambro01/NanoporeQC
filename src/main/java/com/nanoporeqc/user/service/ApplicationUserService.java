package com.nanoporeqc.user.service;

import com.nanoporeqc.user.domain.ApplicationUser;
import com.nanoporeqc.user.repository.ApplicationUserRepository;
import com.nanoporeqc.user.dto.UserDto;
import com.nanoporeqc.user.exceptions.UserNotFoundException;
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

    public void save(final UserDto userDto) {
        ApplicationUser user = ApplicationUser.builder()
                .username(userDto.getUsername())
                .password(bCryptPasswordEncoder.encode(userDto.getPassword()))
                .build();

        applicationUserRepository.save(user);
    }

    public ApplicationUser getCurrentUser() throws UserNotFoundException {
        return applicationUserRepository.findByUsername(getCurrentUserName());
    }

    private String getCurrentUserName() throws UserNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return authentication.getName();
        } else {
            throw new UserNotFoundException();
        }
    }

}
