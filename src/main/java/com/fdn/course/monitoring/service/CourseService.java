package com.fdn.course.monitoring.service;

import com.fdn.course.monitoring.core.IService;
import com.fdn.course.monitoring.dto.validation.ValCourseDTO;
import com.fdn.course.monitoring.dto.validation.ValUserDTO;
import com.fdn.course.monitoring.handler.GlobalResponse;
import com.fdn.course.monitoring.model.Course;
import com.fdn.course.monitoring.model.User;
import com.fdn.course.monitoring.repository.CourseRepository;
import com.fdn.course.monitoring.security.JwtUtility;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class CourseService implements IService<Course> {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ResponseEntity<Object> save(Course course, HttpServletRequest request) {

        if (courseRepository.existsByNama(course.getNama())){
            //sesuaikan response
            return GlobalResponse.dataGagalDisimpan("",request);
        }
        courseRepository.save(course);
        return GlobalResponse.dataBerhasilDisimpan(request);
    }

    @Override
    public ResponseEntity<Object> update(Long id, Course course, HttpServletRequest request) {
        Map<String,Object> mapToken = JwtUtility.extractToken(request);

        if (course == null){
            return GlobalResponse.dataTidakValid("",request);
        }
        try{
            Optional<Course> courseOptional = courseRepository.findById(id);
            if (courseOptional.isEmpty()){
                return GlobalResponse.dataTidakDitemukan("",request);
            }
            Course courseNext = courseOptional.get();
            modelMapper.map(course, courseNext);
            courseNext.setModifiedBy(Long.valueOf(mapToken.get("courseId").toString()));

            // Simpan perubahan
            courseRepository.save(courseNext);

            return GlobalResponse.dataBerhasilDiubah(request);
        } catch (Exception e) {
            //Logging error//
            return GlobalResponse.terjadiKesalahan("",request);
        }
    }

    @Override
    public ResponseEntity<Object> delete(Long id, HttpServletRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<Object> findAll(Pageable pageable, HttpServletRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<Object> findById(Long id, HttpServletRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<Object> findByParam(Pageable pageable, String columnName, String value, HttpServletRequest request) {
        return null;
    }


    public Course convertDtoToEntity(ValCourseDTO courseDTO){
        return modelMapper.map(courseDTO, Course.class);
    }}
