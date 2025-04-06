package com.fdn.course.monitoring.dto.validation;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValGroupMenuDTO {

    @Pattern(regexp = "^[\\w\\s]{5,50}$",message = "Alfanumerik dengan spasi min 5 maks 50 karakter")
    private String nama;

    @Pattern(regexp = "^[\\w\\s]{5,100}$",message = "Alfanumerik dengan spasi min 5 maks 100 karakter")
    private String deskripsi;
    }