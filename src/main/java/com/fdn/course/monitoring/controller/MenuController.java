package com.fdn.course.monitoring.controller;

import com.fdn.course.monitoring.dto.validation.ValMenuDTO;
import com.fdn.course.monitoring.service.MenuService;
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
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @PostMapping
    @PreAuthorize("hasAuthority('menu')")
    public ResponseEntity<Object> addMenu(
            @Valid @RequestBody ValMenuDTO valMenuDTO,
            HttpServletRequest request
            ){
        return menuService.save(menuService.convertDtoToEntity(valMenuDTO), request);
    }
}
