package com.fdn.course.monitoring.model;

import com.fdn.course.monitoring.util.Status;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@Entity
@Table(name = "MapUserDetailCourse",uniqueConstraints = @UniqueConstraint(name = "unq-user-to-detailcourse", columnNames = {"IdUser", "IdDetailCourse"}))
public class MapUserDetailCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "IdUser", nullable = false, foreignKey = @ForeignKey(name = "fk-to-user"))
    private User user;

    @ManyToOne
    @JoinColumn(name = "IdDetailCourse", nullable = false, foreignKey = @ForeignKey(name = "fk-to-course"))
    private DetailCourse detailCourse;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Enumerated(EnumType.STRING)
    private Status status;

    @CreationTimestamp
    private Date submittedAt ;

    @Temporal(TemporalType.TIMESTAMP)
    private Date approvedAt;

    @ManyToOne
    @Column(name = "ApprovedBy",insertable = false)
    private Long approvedBy;



}
