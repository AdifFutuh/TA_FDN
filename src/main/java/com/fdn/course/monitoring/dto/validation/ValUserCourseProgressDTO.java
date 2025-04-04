package com.fdn.course.monitoring.dto.validation;

import com.fdn.course.monitoring.dto.reference.RefCourseDTO;
import com.fdn.course.monitoring.dto.reference.RefUserDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValUserCourseProgressDTO {

    private RefUserDTO user;

    private RefCourseDTO course;
}
