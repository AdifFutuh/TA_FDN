package com.fdn.course.monitoring.dto.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RespUserCourseProgressDTO {

    private Long courseId;
    private String courseName;
    private Double persentase;
    private LocalDateTime createdDate;
    private LocalDateTime updateTime;
}
