package com.fdn.course.monitoring.controller;

import com.fdn.course.monitoring.service.DashboardService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/peserta/{id}")
    @PreAuthorize("hasAuthority('Daftar Pengguna')")
    public ResponseEntity<Object> dashboardPeserta(
            @PathVariable(value = "id") Long id,
            HttpServletRequest request
    ){
        return dashboardService.getUserProfile(id,request);
    }
}
