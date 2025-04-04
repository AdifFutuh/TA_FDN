package com.fdn.course.monitoring.service;

import com.fdn.course.monitoring.core.IService;
import com.fdn.course.monitoring.dto.validation.ValDetailCourseDTO;
import com.fdn.course.monitoring.handler.GlobalResponse;
import com.fdn.course.monitoring.model.Course;
import com.fdn.course.monitoring.model.DetailCourse;
import com.fdn.course.monitoring.repository.CourseRepository;
import com.fdn.course.monitoring.repository.DetailCourseRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DetailCourseService implements IService<DetailCourse> {

    @Autowired
    private DetailCourseRepository detailCourseRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ResponseEntity<Object> save(DetailCourse detailCourse, HttpServletRequest request) {

        if (!courseRepository.existsById(detailCourse.getCourse().getId())){
            return GlobalResponse.dataTidakDitemukan("", request);
        }

        detailCourseRepository.save(detailCourse);
        return GlobalResponse.dataBerhasilDisimpan(request);
    }

    @Override
    public ResponseEntity<Object> update(Long id, DetailCourse detailCourse, HttpServletRequest request) {
        return null;
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

    public DetailCourse convertDtoToEntity(ValDetailCourseDTO detailCourseDTO){
        DetailCourse detailCourse = modelMapper.map(detailCourseDTO, DetailCourse.class);
        Course course = courseRepository.getReferenceById(detailCourseDTO.getCourse().getId());
        detailCourse.setCourse(course);
        return detailCourse;
    }
}
