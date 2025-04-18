package com.fdn.course.monitoring.service;

import com.fdn.course.monitoring.core.IService;
import com.fdn.course.monitoring.dto.response.RespCourseDTO;
import com.fdn.course.monitoring.dto.response.RespUserDTO;
import com.fdn.course.monitoring.dto.validation.ValCourseDTO;
import com.fdn.course.monitoring.dto.validation.ValUserDTO;
import com.fdn.course.monitoring.handler.GlobalResponse;
import com.fdn.course.monitoring.model.Course;
import com.fdn.course.monitoring.model.DetailCourse;
import com.fdn.course.monitoring.model.User;
import com.fdn.course.monitoring.repository.CourseRepository;
import com.fdn.course.monitoring.repository.DetailCourseRepository;
import com.fdn.course.monitoring.repository.UserCourseProgressRepository;
import com.fdn.course.monitoring.security.JwtUtility;
import com.fdn.course.monitoring.util.TransformPagination;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class CourseService implements IService<Course> {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private DetailCourseRepository detailCourseRepository;

    @Autowired
    private UserCourseProgressRepository userCourseProgressRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private TransformPagination transformPagination;


    @Override
    public ResponseEntity<Object> save(Course course, HttpServletRequest request) {

        if (courseRepository.existsByNama(course.getNama())) {
            //sesuaikan response
            return GlobalResponse.dataGagalDisimpan("", request);
        }
        courseRepository.save(course);
        return GlobalResponse.dataBerhasilDisimpan(request);
    }

    @Override
    public ResponseEntity<Object> update(Long id, Course course, HttpServletRequest request) {
        Map<String, Object> mapToken = JwtUtility.extractToken(request);

        if (course == null) {
            return GlobalResponse.dataTidakValid("", request);
        }
        try {
            Optional<Course> courseOptional = courseRepository.findById(id);
            if (courseOptional.isEmpty()) {
                return GlobalResponse.dataTidakDitemukan("", request);
            }
            Course courseNext = courseOptional.get();
            modelMapper.map(course, courseNext);
            courseNext.setModifiedBy(Long.valueOf(mapToken.get("courseId").toString()));

            // Simpan perubahan
            courseRepository.save(courseNext);

            return GlobalResponse.dataBerhasilDiubah(request);
        } catch (Exception e) {
            //Logging error//
            return GlobalResponse.terjadiKesalahan("", request);
        }
    }

    @Override
    public ResponseEntity<Object> delete(Long id, HttpServletRequest request) {
        try {
            Optional<Course> courseOptional = courseRepository.findById(id);
            if (courseOptional.isEmpty()) {
                return GlobalResponse.dataTidakDitemukan("", request);
            }
            Course courseNext = courseOptional.get();
            List<DetailCourse> ltDetailCourse = detailCourseRepository.findByCourse(courseNext);
            detailCourseRepository.deleteAll(ltDetailCourse);
            courseRepository.deleteById(id);
            return GlobalResponse.dataBerhasilDihapus(request);
        } catch (Exception e) {
            //Logging error//
            return GlobalResponse.terjadiKesalahan("", request);
        }
    }

    @Override
    public ResponseEntity<Object> findAll(Pageable pageable, HttpServletRequest request) {
        try {
            Page<Course> page = courseRepository.findAll(pageable);
            List<Course> list = page.getContent();

            List<RespCourseDTO> responseList = new ArrayList<>();
            for (Course course : list) {
                long jumlahSiswa = userCourseProgressRepository.countByCourse(course);

                RespCourseDTO respCourseDTO = convertToRespCourseDTO(course);

                respCourseDTO.setJumlahSiswa(jumlahSiswa);
                responseList.add(respCourseDTO);
            }

            return GlobalResponse.dataDitemukan(
                    transformPagination.transformPagination(responseList, page, null, null),
                    request
            );

        } catch (Exception e) {
            // Log error ke konsol atau sistem logging
            e.printStackTrace();

            // Kamu bisa membuat GlobalResponse.error / gagalDitemukan() sesuai dengan konvensimu
            return GlobalResponse.terjadiKesalahan(
                    "",
                    request
            );
        }
    }

    @Override
    public ResponseEntity<Object> findById(Long id, HttpServletRequest request) {
        Optional<Course> userOptional = Optional.empty();
        try {
            userOptional = courseRepository.findById(id);
            if (userOptional.isEmpty()) {
                return GlobalResponse.dataTidakDitemukan("", request);
            }
            return GlobalResponse.dataDitemukan(userOptional.get(), request);
        } catch (Exception e) {
            //Logging error//
            return GlobalResponse.terjadiKesalahan("", request);
        }
    }

    @Override
    public ResponseEntity<Object> findByParam(Pageable pageable, String columnName, String value, HttpServletRequest request) {
        Page<Course> page = null;
        List<Course> list = null;

        if (columnName.equals("nama")) {
            page = courseRepository.findByNamaContainsIgnoreCase(value, pageable);
        } else {
            page = courseRepository.findAll(pageable);
        }
        list = page.getContent();

        List<RespCourseDTO> responseList = new ArrayList<>();
        for (Course course : list) {
            long jumlahSiswa = userCourseProgressRepository.countByCourse(course);

            RespCourseDTO respCourseDTO = convertToRespCourseDTO(course);

            respCourseDTO.setJumlahSiswa(jumlahSiswa);
            responseList.add(respCourseDTO);
        }
        return GlobalResponse.dataDitemukan(transformPagination.transformPagination(responseList, page, columnName, value), request);
    }

    public Course convertDtoToEntity(ValCourseDTO courseDTO) {
        return modelMapper.map(courseDTO, Course.class);
    }


    public RespCourseDTO convertToRespCourseDTO(Course courses) {


        return modelMapper.map(courses, RespCourseDTO.class);
    }
}