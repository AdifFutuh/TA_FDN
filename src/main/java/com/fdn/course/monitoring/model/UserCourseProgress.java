package com.fdn.course.monitoring.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "MapUserCourseProgress")
public class UserCourseProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "IdUser", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "IdCourse", nullable = false)
    private Course course;

    @Column(name = "persentase", nullable = false)
    private Double persentase = 0.0;

    @Column(name = "CreatedDate",nullable = false,updatable = false)
    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "UpdateDate",nullable = false)
    private LocalDateTime updateTime;

    @Column(name = "ModifiedBy",insertable = false)
    private Long modifiedBy=1L;
}