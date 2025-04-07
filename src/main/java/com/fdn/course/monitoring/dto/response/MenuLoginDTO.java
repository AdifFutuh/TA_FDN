package com.fdn.course.monitoring.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuLoginDTO {
    private String nama;
    private String path;
    private GroupMenuLoginDTO groupMenu;
}