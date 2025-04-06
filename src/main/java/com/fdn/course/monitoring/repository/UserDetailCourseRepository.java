package com.fdn.course.monitoring.repository;

import com.fdn.course.monitoring.model.Course;
import com.fdn.course.monitoring.model.DetailCourse;
import com.fdn.course.monitoring.model.User;
import com.fdn.course.monitoring.model.UserDetailCourse;
import com.fdn.course.monitoring.util.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailCourseRepository extends JpaRepository<UserDetailCourse, Long> {
    boolean existsByUserAndDetailCourse(User user, DetailCourse detailCourse);

    long countByUserAndStatusAndDetailCourse_Course(User user, Status status, Course course);
}
