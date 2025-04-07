package com.fdn.course.monitoring.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuMappingDTO {

    private String nama;
    private String path;
    @JsonIgnore
    private String namaGroupMenu;
}