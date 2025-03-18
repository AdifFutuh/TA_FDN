package com.fdn.course.monitoring.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "otp")
public class Otp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdOTP")
    private Long idOtp;

    @OneToOne
    @JoinColumn(name = "IdUser", nullable = false)
    private User user;

    @Column(name = "OTP",length = 60)
    private String otp;

    @JoinColumn(name = "ExpiredTime", nullable = false)
    private LocalDateTime expiredTime;

}
