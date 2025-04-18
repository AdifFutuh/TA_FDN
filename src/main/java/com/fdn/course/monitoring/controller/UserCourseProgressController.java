package com.fdn.course.monitoring.controller;

import com.fdn.course.monitoring.dto.validation.ValUserCourseProgressDTO;
import com.fdn.course.monitoring.service.UserDetailCourseService;
import com.fdn.course.monitoring.service.UserCourseProgressService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-course")
public class UserCourseProgressController {
    @Autowired
    private UserCourseProgressService userCourseProgressService;

    @Autowired
    private UserDetailCourseService userDetailCourseService;

    @PostMapping
    @PreAuthorize("hasAuthority('Dashboard Admin')")
    public ResponseEntity<Object> addUserCourse(
            @RequestBody ValUserCourseProgressDTO userCourseProgressDTO,
            HttpServletRequest request
    ){
        return userCourseProgressService.save(userCourseProgressService.convertDtoToEntity(userCourseProgressDTO),request);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('Dashboard Admin')")
    public ResponseEntity<Object> approveSummary(
            @PathVariable(value = "id") Long id,
            HttpServletRequest request
    ){
        return userDetailCourseService.approveSummary(id,request);
    }

}
