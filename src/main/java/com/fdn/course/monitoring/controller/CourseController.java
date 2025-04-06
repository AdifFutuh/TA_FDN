package com.fdn.course.monitoring.controller;

import com.fdn.course.monitoring.dto.reference.RefDetailCourseDTO;
import com.fdn.course.monitoring.dto.reference.RefUserDTO;
import com.fdn.course.monitoring.dto.validation.ValCourseDTO;
import com.fdn.course.monitoring.dto.validation.ValDetailCourseDTO;
import com.fdn.course.monitoring.dto.validation.ValMapUserDetailCourseDTO;
import com.fdn.course.monitoring.service.CourseService;
import com.fdn.course.monitoring.service.DetailCourseService;
import com.fdn.course.monitoring.service.MapUserDetailCourseService;
import com.fdn.course.monitoring.service.UserCourseProdressService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private DetailCourseService detailCourseService;

    @Autowired
    private MapUserDetailCourseService mapUserDetailCourseService;

    @PostMapping
    @PreAuthorize("hasAuthority('Dashboard-admin')")
    public ResponseEntity<Object> addCourse(
            @Valid @RequestBody ValCourseDTO courseDTO,
            HttpServletRequest request
    ){
        return courseService.save(courseService.convertDtoToEntity(courseDTO), request);
    }

    @PostMapping("/detail")
    @PreAuthorize("hasAuthority('Dashboard-admin')")
    public ResponseEntity<Object> addDetailCourse(
            @Valid @RequestBody ValDetailCourseDTO detailCourseDTO,
            HttpServletRequest request
    ){
        return detailCourseService.save(detailCourseService.convertDtoToEntity(detailCourseDTO), request);
    }

    @PostMapping("/{idUser}/{idDetailCourse}")
    @PreAuthorize("hasAuthority('Dashboard')")
    public ResponseEntity<Object> addProgress(
            @PathVariable(value = "idUser") long idUser,
            @PathVariable(value = "idDetailCourse") long idDetailCourse,
            @RequestBody ValMapUserDetailCourseDTO mapUserDetailCourseDTO,
            HttpServletRequest request
    ){
        RefUserDTO refUserDTO = new RefUserDTO(idUser);
        RefDetailCourseDTO detailCourseDTO = new RefDetailCourseDTO(idDetailCourse);
        mapUserDetailCourseDTO.setUser(refUserDTO);
        mapUserDetailCourseDTO.setDetailCourse(detailCourseDTO);
        return mapUserDetailCourseService.save(mapUserDetailCourseService.convertDtoToEntity(mapUserDetailCourseDTO), request);
    }

    @GetMapping("/all-summary")
    @PreAuthorize("hasAuthority('Dashboard Admin')")
    public ResponseEntity<Object> findAllMapUserDetailCourseInfo(HttpServletRequest request){
        Pageable pageable = PageRequest.of(0,20, Sort.by("id"));
        return mapUserDetailCourseService.findAll(pageable,request);
    }
}
