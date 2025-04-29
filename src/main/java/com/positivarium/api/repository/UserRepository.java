package com.positivarium.api.repository;

import com.positivarium.api.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    
    User findByClaimToken(String token);

    @Query(
            value = "SELECT EXISTS(" +
                    "SELECT 1 FROM follows " +
                    "WHERE user_id = ?1 AND publisher_id = ?2" +
                    ") AS followed",
            nativeQuery = true
    )
    Boolean userFollowsPublisher(Long userId, Long publisherId);

    @Query("SELECT u FROM User u JOIN u.followers f WHERE f.id = :userId")
    Page<User> findUsersFollowedBy(@Param("userId") Long userId, Pageable pageable);
}
