package com.fdn.course.monitoring.controller;

import com.fdn.course.monitoring.dto.validation.ValCourseDTO;
import com.fdn.course.monitoring.service.CourseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("Course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping
    @PreAuthorize("hasAuthority('Dashboard-admin')")
    public ResponseEntity<Object> addCourse(
            @Valid @RequestBody ValCourseDTO courseDTO,
            HttpServletRequest request
    ){
        return courseService.save(courseService.convertDtoToEntity(courseDTO), request);
    }

}
