package com.fdn.course.monitoring.dto.validation;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ValLoginDTO implements Serializable {

    private String username;
    private String password;
}
