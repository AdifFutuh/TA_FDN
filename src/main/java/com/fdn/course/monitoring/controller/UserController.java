package com.fdn.course.monitoring.controller;

import com.fdn.course.monitoring.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("user-list")
    @PreAuthorize("hasAuthority('Dashboard')")
    public ResponseEntity<Object>findAll(HttpServletRequest request){
        Pageable pageable = PageRequest.of(0,20, Sort.by("id"));
        return userService.findAll(pageable,request);
    }
}
