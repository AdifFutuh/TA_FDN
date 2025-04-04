package com.fdn.course.monitoring.service;

import com.fdn.course.monitoring.core.IService;
import com.fdn.course.monitoring.dto.validation.ValMapUserDetailCourseDTO;
import com.fdn.course.monitoring.handler.GlobalResponse;
import com.fdn.course.monitoring.model.DetailCourse;
import com.fdn.course.monitoring.model.MapUserDetailCourse;
import com.fdn.course.monitoring.model.User;
import com.fdn.course.monitoring.model.UserCourseProgress;
import com.fdn.course.monitoring.repository.DetailCourseRepository;
import com.fdn.course.monitoring.repository.MapUserDetailCourseRepository;
import com.fdn.course.monitoring.repository.UserCourseProgressRepository;
import com.fdn.course.monitoring.repository.UserRepository;
import com.fdn.course.monitoring.security.JwtUtility;
import com.fdn.course.monitoring.util.Status;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class MapUserDetailCourseService implements IService<MapUserDetailCourse> {
    @Autowired
    private MapUserDetailCourseRepository mapUserDetailCourseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DetailCourseRepository detailCourseRepository;

    @Autowired
    private UserCourseProgressRepository userCourseProgressRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ResponseEntity<Object> save(MapUserDetailCourse mapUserDetailCourse, HttpServletRequest request) {
        if (mapUserDetailCourseRepository.existsByUserAndDetailCourse(
                mapUserDetailCourse.getUser(), mapUserDetailCourse.getDetailCourse())
        ){
            return GlobalResponse.dataGagalDisimpan("",request);
        }
        mapUserDetailCourse.setStatus(Status.PENDING);
        mapUserDetailCourseRepository.save(mapUserDetailCourse);
        return GlobalResponse.dataBerhasilDisimpan(request);
    }

    @Override
    public ResponseEntity<Object> update(Long id, MapUserDetailCourse mapUserDetailCourseService, HttpServletRequest request) {
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

    public ResponseEntity<Object> approveSummary(long id, HttpServletRequest request){
        Map<String, Object> mapToken = JwtUtility.extractToken(request);
        Optional<MapUserDetailCourse> mapUserDetailCourseOptional = mapUserDetailCourseRepository.findById(id);

        if (mapUserDetailCourseOptional.isEmpty()){
            return GlobalResponse.dataTidakDitemukan("", request);
        }

        MapUserDetailCourse mapUserDetailCourse = mapUserDetailCourseOptional.get();

        mapUserDetailCourse.setStatus(Status.APPROVED);
        mapUserDetailCourse.setApprovedAt(new Date());
        mapUserDetailCourse.setApprovedBy(Long.valueOf(mapToken.get("userId").toString()));
        mapUserDetailCourseRepository.save(mapUserDetailCourse);

        Optional<UserCourseProgress> userCourseProgressOptional = userCourseProgressRepository.findByUserAndCourse(
                mapUserDetailCourse.getUser(), mapUserDetailCourse.getDetailCourse().getCourse()
        );

        if (userCourseProgressOptional.isPresent()) {
            UserCourseProgress userCourseProgress = userCourseProgressOptional.get();

            // Hitung total detail course dalam course ini
            long totalDetailCourse = detailCourseRepository.countByCourse(userCourseProgress.getCourse());

            // Hitung berapa yang sudah APPROVED
            long completedDetailCourse = mapUserDetailCourseRepository.countByUserAndStatusAndDetailCourse_Course(
                    userCourseProgress.getUser(),
                    Status.APPROVED,
                    userCourseProgress.getCourse()
            );

            double progress = (totalDetailCourse > 0) ? ((double) completedDetailCourse / totalDetailCourse) * 100 : 0.0;
            userCourseProgress.setPersentase(progress);

            // Set user yang memodifikasi
            userCourseProgress.setModifiedBy(Long.valueOf(mapToken.get("userId").toString()));
            userCourseProgressRepository.save(userCourseProgress);
        }

        return GlobalResponse.dataBerhasilDiubah(request);
    }
    public MapUserDetailCourse convertDtoToEntity(ValMapUserDetailCourseDTO mapUserDetailCourseDTO){
        MapUserDetailCourse mapUserDetailCourse = new MapUserDetailCourse();
        User user = userRepository.getReferenceById(mapUserDetailCourseDTO.getUser().getId());
        DetailCourse detailCourse = detailCourseRepository.getReferenceById(mapUserDetailCourseDTO.getDetailCourse().getId());

        mapUserDetailCourse.setUser(user);
        mapUserDetailCourse.setDetailCourse(detailCourse);
        mapUserDetailCourse.setSummary(mapUserDetailCourseDTO.getSummary());

        return mapUserDetailCourse;
    }
}
