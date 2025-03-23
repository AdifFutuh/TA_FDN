package com.fdn.course.monitoring.repository;

import com.fdn.course.monitoring.model.Access;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccessRepository extends JpaRepository<Access, Long> {
    Optional<Access> findBynama(String nama);
}
