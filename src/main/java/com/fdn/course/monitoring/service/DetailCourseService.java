package com.fdn.course.monitoring.service;

import com.fdn.course.monitoring.core.IService;
import com.fdn.course.monitoring.dto.response.RespCourseDTO;
import com.fdn.course.monitoring.dto.response.RespDetailCourseDTO;
import com.fdn.course.monitoring.dto.validation.ValDetailCourseDTO;
import com.fdn.course.monitoring.handler.GlobalResponse;
import com.fdn.course.monitoring.model.Course;
import com.fdn.course.monitoring.model.DetailCourse;
import com.fdn.course.monitoring.repository.CourseRepository;
import com.fdn.course.monitoring.repository.DetailCourseRepository;
import com.fdn.course.monitoring.util.TransformPagination;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DetailCourseService implements IService<DetailCourse> {

    @Autowired
    private DetailCourseRepository detailCourseRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private TransformPagination transformPagination;

    @Override
    public ResponseEntity<Object> save(DetailCourse detailCourse, HttpServletRequest request) {

        if (!courseRepository.existsById(detailCourse.getCourse().getId())) {
            return GlobalResponse.dataTidakDitemukan("", request);
        }

        if (detailCourseRepository.existsByCourseAndUrutan(detailCourse.getCourse(), detailCourse.getUrutan())){
            return GlobalResponse.dataSudahTerdaftar("",request);
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
        try {
            Optional<DetailCourse> detailCourseOptional= detailCourseRepository.findById(id);
            if (detailCourseOptional.isEmpty()) {
                return GlobalResponse.dataTidakDitemukan("", request);
            }
            return GlobalResponse.dataDitemukan(detailCourseOptional.get(), request);
        } catch (Exception e) {
            //Logging error//
            return GlobalResponse.terjadiKesalahan("", request);
        }
    }

    @Override
    public ResponseEntity<Object> findByParam(Pageable pageable, String columnName, String value, HttpServletRequest request) {
        Page<DetailCourse> page = null;
        List<DetailCourse> list = null;

        if (columnName.equals("course")) {
            page = detailCourseRepository.findByCourse_Nama(value,pageable);
        } else {
            page = detailCourseRepository.findAll(pageable);
        }
        list = page.getContent();

        List<RespDetailCourseDTO> responseList = convertToRespDetailCourseDTO(list);

        return GlobalResponse.dataDitemukan(transformPagination.transformPagination(value, responseList, page, null, null), request);

    }

    public DetailCourse convertDtoToEntity(ValDetailCourseDTO detailCourseDTO) {
        DetailCourse detailCourse = modelMapper.map(detailCourseDTO, DetailCourse.class);
        Course course = courseRepository.getReferenceById(detailCourseDTO.getCourse().getId());
        detailCourse.setCourse(course);
        return detailCourse;
    }

    public List<RespDetailCourseDTO> convertToRespDetailCourseDTO(List<DetailCourse> detailCourses) {
        List<RespDetailCourseDTO> responseListDTO = new ArrayList<>();
        for (DetailCourse detailCourse : detailCourses) {
            RespDetailCourseDTO respDetailCourseDTO = new RespDetailCourseDTO();
            respDetailCourseDTO.setId(detailCourse.getId());
            respDetailCourseDTO.setJudul(detailCourse.getJudul());
            respDetailCourseDTO.setContent(detailCourse.getContent());
            respDetailCourseDTO.setUrutan(detailCourse.getUrutan());
            respDetailCourseDTO.setCourseId(detailCourse.getCourse().getId());

            responseListDTO.add(respDetailCourseDTO);
        }
        return responseListDTO;
    }
}
