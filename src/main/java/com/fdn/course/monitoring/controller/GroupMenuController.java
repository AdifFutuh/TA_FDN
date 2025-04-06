package com.fdn.course.monitoring.controller;

import com.fdn.course.monitoring.dto.validation.ValGroupMenuDTO;
import com.fdn.course.monitoring.dto.validation.ValMenuDTO;
import com.fdn.course.monitoring.service.GroupMenuService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/group-mapping")
public class GroupMenuController {

    @Autowired
    private GroupMenuService groupMenuService;

    @PostMapping
    @PreAuthorize("hasAuthority('Group-Menu')")
    public ResponseEntity<Object> addMenu(
            @Valid @RequestBody ValGroupMenuDTO groupMenuDTO,
            HttpServletRequest request
    ){
        return groupMenuService.save(groupMenuService.convertDtoToEntity(groupMenuDTO), request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('Group-Menu')")
    public ResponseEntity<Object> delete(
            @PathVariable(value = "id") Long id, HttpServletRequest request){
        return groupMenuService.delete(id,request);
    }
}
