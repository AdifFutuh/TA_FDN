package com.fdn.course.monitoring.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "DtlCourse")
public class DetailCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdDetailCourse")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "IdCourse", nullable = false)
    private Course course;

    @Column(name = "Judul", nullable = false)
    private String judul;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "Urutan")
    private int urutan;

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
