package com.course.api.spring_security_course.persistence.repository;

import com.course.api.spring_security_course.persistence.entity.security.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IJwTokenRepository extends JpaRepository<JwtToken, Integer> {
    Optional<JwtToken> findByToken(String jwt);
}
