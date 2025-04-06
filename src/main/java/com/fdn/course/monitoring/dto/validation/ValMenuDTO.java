package com.fdn.course.monitoring.dto.validation;

import com.fdn.course.monitoring.dto.reference.RefGroupMenuDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValMenuDTO {
    private String nama;

    private String path;

    private RefGroupMenuDTO groupMenu;
}
