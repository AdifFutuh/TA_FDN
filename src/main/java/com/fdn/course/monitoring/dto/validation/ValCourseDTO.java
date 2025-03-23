package com.fdn.course.monitoring.dto.validation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ValCourseDTO {

    @NotBlank(message = "Nama tidak boleh kosong")
    @Pattern(
            regexp = "^[A-Za-z0-9\\s\\-_,.]{3,100}$",
            message = "Nama harus terdiri dari 3-100 karakter dan hanya boleh mengandung huruf, angka, spasi, serta tanda baca dasar (-_,.)"
    )
    private String nama;

    @Pattern(
            regexp = "^[A-Za-z0-9\\s.,'\"!?;:()\\-_/]{0,500}$",
            message = "Deskripsi maksimal 500 karakter dan hanya boleh mengandung huruf, angka, spasi, serta tanda baca dasar"
    )
    private String deskripsi;
}
