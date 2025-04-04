package com.fdn.course.monitoring.dto.validation;

import com.fdn.course.monitoring.dto.reference.RefDetailCourseDTO;
import com.fdn.course.monitoring.dto.reference.RefUserDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValMapUserDetailCourseDTO {
    private RefUserDTO user;

    private RefDetailCourseDTO detailCourse;

    private String summary;

}
