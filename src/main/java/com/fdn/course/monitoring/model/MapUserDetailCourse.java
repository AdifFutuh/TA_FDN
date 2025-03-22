package com.fdn.course.monitoring.model;

import com.fdn.course.monitoring.util.Status;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "MapUserDetailCourse")
public class MapUserDetailCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "IdUser", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "IdDetailCourse", nullable = false)
    private DetailCourse detailCourse;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date submittedAt ;

    @Temporal(TemporalType.TIMESTAMP)
    private Date approvedAt;

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private User approvedBy; ;



}
