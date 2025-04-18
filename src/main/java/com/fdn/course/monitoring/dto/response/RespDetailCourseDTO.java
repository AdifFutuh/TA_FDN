package com.fdn.course.monitoring.dto.response;

import lombok.Data;

@Data
public class RespDetailCourseDTO {
    private Long id;

    private String judul;

    private String content;

    private Integer urutan;

    private Long courseId;
}
