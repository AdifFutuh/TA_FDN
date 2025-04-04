//package com.fdn.course.monitoring.service;
//
//import com.fdn.course.monitoring.model.User;
//import com.fdn.course.monitoring.repository.DetailCourseRepository;
//import com.fdn.course.monitoring.repository.MapUserDetailCourseRepository;
//import com.fdn.course.monitoring.repository.UserRepository;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class DashboardService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private DetailCourseRepository detailCourseRepository;
//
//    @Autowired
//    private MapUserDetailCourseRepository mapUserDetailCourseRepository;
//
//    public ResponseEntity<Object> showUserAndDetailCourse(Pageable pageable, HttpServletRequest request){
//        Page<User> page = null;
//        List<User> listUser = null;
//
//        page = userRepository.findAll(pageable);
//        listUser = page.getContent();
//    }
//}