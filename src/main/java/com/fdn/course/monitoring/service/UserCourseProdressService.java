package com.fdn.course.monitoring.service;

import com.fdn.course.monitoring.core.IService;
import com.fdn.course.monitoring.dto.validation.ValUserCourseProgressDTO;
import com.fdn.course.monitoring.handler.GlobalResponse;
import com.fdn.course.monitoring.model.UserCourseProgress;
import com.fdn.course.monitoring.repository.*;
import com.fdn.course.monitoring.security.JwtUtility;
import com.fdn.course.monitoring.util.Status;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class UserCourseProdressService implements IService<UserCourseProgress> {

    @Autowired
    private UserCourseProgressRepository userCourseProgressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private DetailCourseRepository detailCourseRepository;

    @Autowired
    private UserDetailCourseRepository userDetailCourseRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ResponseEntity<Object> save(UserCourseProgress userCourseProgress, HttpServletRequest request) {
        Optional<UserCourseProgress> userCourseProgressOptional = userCourseProgressRepository.findByUserAndCourse(
                userCourseProgress.getUser(),
                userCourseProgress.getCourse()
        );

        if (userCourseProgressOptional.isPresent()){
            return GlobalResponse.dataGagalDisimpan("",request);
        }
        userCourseProgressRepository.save(userCourseProgress);
        return GlobalResponse.dataBerhasilDisimpan(request);
    }

    @Override
    public ResponseEntity<Object> update(Long id, UserCourseProgress userCourseProgress, HttpServletRequest request) {
        Map<String, Object> mapToken = JwtUtility.extractToken(request);

        if (userCourseProgress == null) {
            return GlobalResponse.dataTidakValid("", request);
        }
        try {
            Optional<UserCourseProgress> userCourseProgressOptional = userCourseProgressRepository.findById(id);
            if (userCourseProgressOptional.isEmpty()) {
                return GlobalResponse.dataTidakDitemukan("", request);
            }

            UserCourseProgress userCourseProgressNext = userCourseProgressOptional.get();

            // Pastikan User dan Course tidak null
            if (userCourseProgressNext.getUser() == null || userCourseProgressNext.getCourse() == null) {
                return GlobalResponse.dataTidakValid("", request);
            }

            // Menghitung jumlah total detail course dalam course ini
            Long totalDetailCourse = detailCourseRepository.countByCourse(userCourseProgressNext.getCourse());

            // Menghitung jumlah detail course yang sudah diambil oleh user
            Long completedDetailCourse = userDetailCourseRepository.countByUserAndStatusAndDetailCourse_Course(
                    userCourseProgressNext.getUser(),
                    Status.APPROVED,
                    userCourseProgressNext.getCourse()
            );
            // Menghitung progress dalam persentase
            double progress = (totalDetailCourse > 0) ? ((double) completedDetailCourse / totalDetailCourse) * 100 : 0.0;
            userCourseProgressNext.setPersentase(progress);

            // Set user yang memodifikasi
            userCourseProgressNext.setModifiedBy(Long.valueOf(mapToken.get("userId").toString()));

            // Simpan perubahan ke database
            userCourseProgressRepository.save(userCourseProgressNext);

            return GlobalResponse.dataBerhasilDiubah(request);
        } catch (Exception e) {
            //Logging error
            return GlobalResponse.terjadiKesalahan("", request);
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

    public UserCourseProgress convertDtoToEntity(ValUserCourseProgressDTO userCourseProgressDTO){
        return modelMapper.map(userCourseProgressDTO, UserCourseProgress.class);
    }
}
