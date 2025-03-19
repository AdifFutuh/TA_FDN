package com.fdn.course.monitoring.controller;

import com.fdn.course.monitoring.dto.validation.ValRegistrationDTO;
import com.fdn.course.monitoring.service.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthController {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping("/regis")
    public ResponseEntity<Object> regis(@Valid @RequestBody ValRegistrationDTO regisDTO,
                                        HttpServletRequest request){
        return userDetailsService.regis(userDetailsService.convertDtoToEntity(regisDTO), request);
    }

    @GetMapping
    public String tes(){
        return "oke";
    }

}
