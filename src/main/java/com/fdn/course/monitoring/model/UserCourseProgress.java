package com.fdn.course.monitoring.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
@Table(name = "UserCourseProgress")
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

    @UpdateTimestamp
    private LocalDateTime updateTime;

}
