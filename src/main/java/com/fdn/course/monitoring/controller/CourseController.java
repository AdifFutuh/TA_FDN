package com.fdn.course.monitoring.controller;

import com.fdn.course.monitoring.dto.reference.RefDetailCourseDTO;
import com.fdn.course.monitoring.dto.reference.RefUserDTO;
import com.fdn.course.monitoring.dto.validation.ValCourseDTO;
import com.fdn.course.monitoring.dto.validation.ValDetailCourseDTO;
import com.fdn.course.monitoring.dto.validation.ValMapUserDetailCourseDTO;
import com.fdn.course.monitoring.model.User;
import com.fdn.course.monitoring.repository.UserRepository;
import com.fdn.course.monitoring.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("course")
public class CourseController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private DetailCourseService detailCourseService;

    @Autowired
    private UserDetailCourseService userDetailCourseService;

    @Autowired
    private UserCourseProgressService userCourseProgressService;


    @GetMapping("/all-course-list")
    @PreAuthorize("hasAuthority('Daftar Kursus')")
    public ResponseEntity<Object>findAllCourseAsAdmin(HttpServletRequest request){
        Pageable pageable = PageRequest.of(0,20, Sort.by("id"));
        return courseService.findAll(pageable,request);
    }

    @GetMapping("/course-list/{page}")
    @PreAuthorize("hasAuthority('Daftar Kursus')")
    public ResponseEntity<Object>findAllCourse(
            HttpServletRequest request,
            @PathVariable(value = "page") Integer page){
        Pageable pageable = PageRequest.of(page,2, Sort.by("id"));
        return courseService.findAll(pageable,request);
    }

    @GetMapping("/dtl/{id}")
    @PreAuthorize("hasAuthority('Dashboard Admin')")
    public ResponseEntity<Object> findCourseById(
            @PathVariable(value = "id") Long id, HttpServletRequest request){
        return courseService.findById(id,request);
    }

    @GetMapping("/{sort}/{sortBy}/{page}")
    @PreAuthorize("hasAuthority('Dashboard Admin')")
    public ResponseEntity<Object> findByParamsAsAdmin(
            @PathVariable(value = "sort") String sort,
            @PathVariable(value = "sortBy") String sortBy,
            @PathVariable(value = "page") Integer page,
            @RequestParam(value = "size") Integer size,
            @RequestParam(value = "column") String column,
            @RequestParam(value = "value") String value,
            HttpServletRequest request
    ) {
        Pageable pageable = null;
        if (sortBy.equals("nama")) {
            sortBy = "nama";
        } else {
            sortBy = "id";
        }
        if (sort.equals("asc")) {
            pageable = PageRequest.of(page, size, Sort.by(sortBy));
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        }

        return courseService.findByParam(pageable,column,value,request);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('Dashboard Admin')")
    public ResponseEntity<Object> addCourse(
            @Valid @RequestBody ValCourseDTO courseDTO,
            HttpServletRequest request
    ){
        return courseService.save(courseService.convertDtoToEntity(courseDTO), request);
    }

    @DeleteMapping("/d/{id}")
    @PreAuthorize("hasAuthority('Dashboard Admin')")
    public ResponseEntity<Object> delete(
            @PathVariable(value = "id") Long id, HttpServletRequest request){
        return courseService.delete(id,request);
    }

    @PostMapping("/add-detail")
    @PreAuthorize("hasAuthority('Dashboard Admin')")
    public ResponseEntity<Object> addDetailCourse(
            @Valid @RequestBody ValDetailCourseDTO detailCourseDTO,
            HttpServletRequest request
    ){
        return detailCourseService.save(detailCourseService.convertDtoToEntity(detailCourseDTO), request);
    }


    //get data detail course by nama course
    @GetMapping("/detail-course/{page}")
    @PreAuthorize("hasAuthority('Daftar Kursus')")
    public ResponseEntity<Object> findDetailCourseByCourse(
            @PathVariable(value = "page") Integer page,
            @RequestParam(value = "size") Integer size,
            @RequestParam(value = "column") String column,
            @RequestParam(value = "course") String value,
            HttpServletRequest request
    ) {
        Pageable pageable = null;

        pageable = PageRequest.of(page,size, Sort.by("urutan"));


        return detailCourseService.findByParam(pageable,column,value,request);
    }

    @GetMapping("/my-course/{idUser}")
    @PreAuthorize("hasAuthority('Daftar Kursus')")
    public ResponseEntity<Object> findEnrollmentCourse(
            @PathVariable(value = "idUser") Long idUser,
            HttpServletRequest request
    ){
        
        Optional<User> user = userRepository.findById(idUser);
        User userNext = user.get();

        return userCourseProgressService.findCourseByUser(userNext,request);
    }

    @GetMapping("/detail-course/dtl/{id}")
    @PreAuthorize("hasAuthority('Dashboard Admin')")
    public ResponseEntity<Object> findDetailCourseById(
            @PathVariable(value = "id") Long id, HttpServletRequest request){
        return detailCourseService.findById(id,request);
    }

    @PostMapping("/{idUser}/{idDetailCourse}")
    @PreAuthorize("hasAuthority('Kursus Saya')")
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
        return userDetailCourseService.save(userDetailCourseService.convertDtoToEntity(mapUserDetailCourseDTO), request);
    }

    @GetMapping("/all-summary")
    @PreAuthorize("hasAuthority('Dashboard Admin')")
    public ResponseEntity<Object> findAllMapUserDetailCourseInfo(HttpServletRequest request){
        Pageable pageable = PageRequest.of(0,20, Sort.by("id"));
        return userDetailCourseService.findAll(pageable,request);
    }
}
