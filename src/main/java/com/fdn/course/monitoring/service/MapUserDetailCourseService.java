package com.fdn.course.monitoring.service;

import com.fdn.course.monitoring.core.IService;
import com.fdn.course.monitoring.dto.response.RespMapUserDetailCourseDTO;
import com.fdn.course.monitoring.dto.response.RespUserDTO;
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
import com.fdn.course.monitoring.util.TransformPagination;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

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

    @Autowired
    private TransformPagination transformPagination;

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
            Page<MapUserDetailCourse> page = null;
            List<MapUserDetailCourse> list = null;

            page = mapUserDetailCourseRepository.findAll(pageable);
            list = page.getContent();

            List<RespMapUserDetailCourseDTO> responseList = convertToRespMapUserDetailCourseDTO(list);

            return GlobalResponse.dataDitemukan(transformPagination.transformPagination(responseList,page,null,null),request);

        }

    @Override
    public ResponseEntity<Object> findById(Long id, HttpServletRequest request) {
        Optional<MapUserDetailCourse> mapUserDetailCourseOptional = Optional.empty();
        try{
            mapUserDetailCourseOptional = mapUserDetailCourseRepository.findById(id);
            if (mapUserDetailCourseOptional.isEmpty()){
                return GlobalResponse.dataTidakDitemukan("",request);
            }
            MapUserDetailCourse mapUserDetailCourse = mapUserDetailCourseOptional.get();

            RespMapUserDetailCourseDTO respDTO = new RespMapUserDetailCourseDTO();
            respDTO.setUsername(mapUserDetailCourse.getUser().getUsername());
            respDTO.setNamaCourse(mapUserDetailCourse.getDetailCourse().getCourse().getNama());
            respDTO.setJudulDetailCourse(mapUserDetailCourse.getDetailCourse().getJudul());
            respDTO.setSummary(mapUserDetailCourse.getSummary());
            respDTO.setStatus(mapUserDetailCourse.getStatus().name());

            return GlobalResponse.dataDitemukan(respDTO, request);
        } catch (Exception e) {
            //Logging error//
            return GlobalResponse.terjadiKesalahan("",request);
        }
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

    public List<RespMapUserDetailCourseDTO> convertToRespMapUserDetailCourseDTO(List<MapUserDetailCourse> mapUserDetailCourses) {
        List<RespMapUserDetailCourseDTO> responseListDTO = new ArrayList<>();
        for (MapUserDetailCourse mapUserDetailCourse : mapUserDetailCourses) {

            RespMapUserDetailCourseDTO respDTO = new RespMapUserDetailCourseDTO();
            respDTO.setId(mapUserDetailCourse.getId());
            respDTO.setUsername(mapUserDetailCourse.getUser().getUsername());
            respDTO.setNamaCourse(mapUserDetailCourse.getDetailCourse().getCourse().getNama());
            respDTO.setJudulDetailCourse(mapUserDetailCourse.getDetailCourse().getJudul());
            respDTO.setSummary(mapUserDetailCourse.getSummary());
            respDTO.setStatus(mapUserDetailCourse.getStatus().name());

            responseListDTO.add(respDTO);
        }
        return responseListDTO;
    }

}
