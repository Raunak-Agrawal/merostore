package com.merostore.backend.security.service;


import com.merostore.backend.security.domain.Role.Role;
import com.merostore.backend.security.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User save(User user);

    User findUserByMobileNumberAndRole(String mobileNumber, Role role);

    User changeUserPassword(User user, String newPassword);

}
