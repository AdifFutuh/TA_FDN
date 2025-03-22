package com.fdn.course.monitoring.repository;

import com.fdn.course.monitoring.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Page<User> findByNamaContainsIgnoreCase(String nama, Pageable pageable);
    List<User> findByNamaContainsIgnoreCase(String nama);

    Page<User> findByAlamatContainsIgnoreCase(String nama, Pageable pageable);
    List<User> findByAlamatContainsIgnoreCase(String nama);

}
