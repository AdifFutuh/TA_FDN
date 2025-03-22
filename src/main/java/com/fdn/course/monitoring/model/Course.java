package com.fdn.course.monitoring.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
@Table(name = "MstCourse")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdCourse")
    private Long id;

    @Column(name = "Nama", nullable = false, unique = true)
    private String nama;

    @Column(name = "Deskripsi", columnDefinition = "TEXT")
    private String deskripsi;


    @Column(name = "CreatedDate",nullable = false,updatable = false)
    @CreationTimestamp
    private LocalDateTime createdDate;


    @Column(name = "ModifiedDate",insertable = false)
    @UpdateTimestamp
    private LocalDateTime modifiedDate;

    @Column(name = "CreatedBy", nullable = false, updatable = false)
    private Long createdBy = 1L;

    @Column(name = "ModifiedBy", insertable = false)
    private Long modifiedBy = 1L;
}

