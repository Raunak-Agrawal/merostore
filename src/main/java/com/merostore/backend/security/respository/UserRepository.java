package com.merostore.backend.security.respository;

import com.merostore.backend.security.domain.Role.Role;
import com.merostore.backend.security.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByMobileNumber(String mobileNumber);

    User findByMobileNumberAndRole(String mobileNumber, Role role);
}
