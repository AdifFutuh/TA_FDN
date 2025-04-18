package com.fdn.course.monitoring.dto.response;

import lombok.Data;

@Data
public class RespCourseDTO {

    private long id;

    private String nama;

    private String deskripsi;

    private long jumlahSiswa;
}
