package com.fdn.course.monitoring.repository;

import com.fdn.course.monitoring.model.Course;
import com.fdn.course.monitoring.model.User;
import com.fdn.course.monitoring.model.UserCourseProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCourseProgressRepository extends JpaRepository<UserCourseProgress, Long> {
    Optional<UserCourseProgress> findByUserAndCourse(User user, Course course);
}