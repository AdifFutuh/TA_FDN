package com.fdn.course.monitoring.controller;

import com.fdn.course.monitoring.dto.validation.ValLoginDTO;
import com.fdn.course.monitoring.dto.validation.ValRegistrationDTO;
import com.fdn.course.monitoring.dto.validation.ValVerifyRegisDTO;
import com.fdn.course.monitoring.service.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @PostMapping("/verify-regis")
    public ResponseEntity<Object> verifyRegis(@Valid @RequestBody ValVerifyRegisDTO verifyRegisDTO,
                                              HttpServletRequest request){
        return userDetailsService.verifyRegis(userDetailsService.convertDtoToEntity(verifyRegisDTO),request);
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<Object> resendOtp(@RequestBody Map<String, String> requestBody,
                                            HttpServletRequest request){
        String email = requestBody.get("email");
       return userDetailsService.resendOtp(email, request);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody ValLoginDTO valLoginDTO,
                                              HttpServletRequest request){
        return userDetailsService.login(userDetailsService.convertDtoToEntity(valLoginDTO),request);
    }

}
