package com.positivarium.api.repository;

import com.positivarium.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    
    User findByClaimToken(String token);
}
