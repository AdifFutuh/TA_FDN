package com.fdn.course.monitoring.dto.validation;

import com.fdn.course.monitoring.dto.reference.RefCourseDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValDetailCourseDTO {
    private String judul;
    private String content;
    private Integer urutan;
    private RefCourseDTO course;
}
