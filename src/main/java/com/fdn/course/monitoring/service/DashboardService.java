package com.fdn.course.monitoring.service;

import com.fdn.course.monitoring.dto.response.RespUserCourseProgressDTO;
import com.fdn.course.monitoring.dto.response.RespUserProfile;
import com.fdn.course.monitoring.handler.GlobalResponse;
import com.fdn.course.monitoring.model.User;
import com.fdn.course.monitoring.model.UserCourseProgress;
import com.fdn.course.monitoring.repository.DetailCourseRepository;
import com.fdn.course.monitoring.repository.UserCourseProgressRepository;
import com.fdn.course.monitoring.repository.UserDetailCourseRepository;
import com.fdn.course.monitoring.repository.UserRepository;
import com.fdn.course.monitoring.util.TransformPagination;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DetailCourseRepository detailCourseRepository;

    @Autowired
    private UserDetailCourseRepository mapUserDetailCourseRepository;

    @Autowired
    private TransformPagination transformPagination;

    @Autowired
    private UserCourseProgressRepository userCourseProgressRepository;

    public ResponseEntity<Object> getUserProfile(long userId, HttpServletRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        List<UserCourseProgress> progressList = userCourseProgressRepository.findByUser(user);
            List<RespUserCourseProgressDTO> progressDTOs = new ArrayList<>();
            for (UserCourseProgress progress : progressList) {
                RespUserCourseProgressDTO dto = new RespUserCourseProgressDTO();
                dto.setCourseId(progress.getCourse().getId());
                dto.setCourseName(progress.getCourse().getNama());
                dto.setPersentase(progress.getPersentase());
                dto.setCreatedDate(progress.getCreatedDate());
                dto.setUpdateTime(progress.getUpdateTime());

                progressDTOs.add(dto);
            }

        RespUserProfile dto = new RespUserProfile();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setAlamat(user.getAlamat());
        dto.setNoHp(user.getNoHp());
        dto.setNama(user.getNama());
        dto.setTanggalLahir(user.getTanggalLahir());
        dto.setCourseProgressList(progressDTOs);

        return GlobalResponse.dataDitemukan(dto, request);
    }

}