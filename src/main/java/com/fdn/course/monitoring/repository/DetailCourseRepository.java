package com.fdn.course.monitoring.repository;

import com.fdn.course.monitoring.model.Course;
import com.fdn.course.monitoring.model.DetailCourse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetailCourseRepository extends JpaRepository<DetailCourse, Long> {

    boolean existsByUrutan(int urutan);

    long countByCourse(Course course);

    List<DetailCourse> findByCourse(Course course);
    Page<DetailCourse> findByCourse_Nama(String namaCourse, Pageable pageable);
}