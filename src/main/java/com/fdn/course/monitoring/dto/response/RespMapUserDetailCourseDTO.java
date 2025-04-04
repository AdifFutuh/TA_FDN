package com.fdn.course.monitoring.dto.response;

import com.fdn.course.monitoring.util.Status;
import lombok.Data;

@Data
public class RespMapUserDetailCourseDTO {

    private long id;

    private String username;

    private String namaCourse;

    private String judulDetailCourse;

    private String summary;

    private String status;
}