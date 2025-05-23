package com.fdn.course.monitoring.dto.validation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fdn.course.monitoring.dto.reference.RefAccessDTO;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class ValUserDTO {

    @Pattern(regexp = "^([a-z0-9]{8,16})$",
            message = "Format Huruf kecil ,numeric saja min 8 max 16 karakter, contoh : jaenudin123")
    private String username;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@_#\\-$]).{9,16}$",
            message = "Format minimal 1 angka, 1 huruf kecil, 1 huruf besar, 1 spesial karakter (_ \"Underscore\", - \"Hyphen\", # \"Hash\", $ \"Dollar\", atau @ \"At\"), total 9–16 karakter"
    )
    private String password;


    @Pattern(regexp = "^(?=.{1,256})(?=.{1,64}@.{1,255}$)(?:(?![.])[a-zA-Z0-9._%+-]+(?:(?<!\\\\)[.][a-zA-Z0-9-]+)*?)@[a-zA-Z0-9.-]+(?:\\.[a-zA-Z]{2,50})+$",
            message = "Format tidak valid contoh : user_name123@sub.domain.com")
    private String email;

    @Pattern(regexp = "^[\\w\\s\\.\\,]{20,255}$",
            message = "Format Alamat Tidak Valid min 20 maks 255, contoh : Jln. Kenari 2B jakbar 11480")
    private String alamat;

    @Pattern(regexp = "^(62|\\+62|0)8[0-9]{9,13}$",
            message = "Format No HP Tidak Valid , min 9 max 13 setelah angka 8, contoh : (0/62/+62)81111111")
    @JsonProperty("no-hp")
    private String noHp;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonProperty("tanggal-lahir")
    private LocalDate tanggalLahir;

    @Pattern(regexp = "^[a-zA-Z\\s]{4,25}$", message = "Hanya Alfabet dan spasi Minimal 4 Maksimal 25")
    private String nama;

    private RefAccessDTO akses;
}
