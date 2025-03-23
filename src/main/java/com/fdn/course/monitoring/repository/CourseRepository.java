package com.fdn.course.monitoring.repository;

import com.fdn.course.monitoring.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByNama(String nama);

    boolean existsByNama(String nama);
}
