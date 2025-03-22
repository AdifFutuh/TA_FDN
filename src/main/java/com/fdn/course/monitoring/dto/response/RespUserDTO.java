package com.fdn.course.monitoring.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RespUserDTO {

    private long id;

    private String username;

    private String email;

    private String alamat;

    private String noHp;

    private String nama;

    private LocalDate tanggalLahir;

    private Integer umur;

    private String pathImage;

    private String linkImage;
}
