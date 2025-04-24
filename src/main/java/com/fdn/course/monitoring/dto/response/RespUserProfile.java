package com.fdn.course.monitoring.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class RespUserProfile {

    private long id;

    private String username;

    private String email;

    private String alamat;

    private String noHp;

    private String nama;

    private LocalDate tanggalLahir;

    private List<RespUserCourseProgressDTO> courseProgressList;

    private List<RespMapUserDetailCourseDTO> userDetailCourseList;

}
