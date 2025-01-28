package com.merostore.backend.security.service.impl;

import com.merostore.backend.security.domain.Role.Role;
import com.merostore.backend.security.domain.User;
import com.merostore.backend.security.respository.UserRepository;
import com.merostore.backend.security.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private HttpServletRequest request;

    @Override
    public User save(User user) {
        user.setPin(passwordEncoder.encode(user.getPin()));
        userRepository.save(user);
        return user;
    }

    @Override
    public User findUserByMobileNumberAndRole(String mobileNumber, Role role) {
        return userRepository.findByMobileNumberAndRole(mobileNumber, role);
    }

    @Override
    public User changeUserPassword(User user, String newPassword) {
        user.setPin(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String mobileNumber) throws UsernameNotFoundException {
        log.debug("Load by username called");
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.
                        currentRequestAttributes()).
                        getRequest();


        User user = null;
        if (request.getServletPath().equals("/buyer/login")) {
            user = findUserByMobileNumberAndRole(mobileNumber, Role.ROLE_BUYER);

        } else if (request.getServletPath().equals("/seller/login")) {
            user = findUserByMobileNumberAndRole(mobileNumber, Role.ROLE_SELLER);
        }

        if (user == null) {
            throw new UsernameNotFoundException("Invalid mobile number or pin.");
        }
        return new org.springframework.security.core.userdetails.User(user.getMobileNumber(),
                user.getPin(),
                mapRoleToAuthority(Collections.singleton(user.getRole())));
    }

    private Collection<? extends GrantedAuthority> mapRoleToAuthority(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getCode()))
                .collect(Collectors.toList());
    }

}
