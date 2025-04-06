package com.fdn.course.monitoring.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "MstGroupMenu")
public class GroupMenu {

    public GroupMenu(String nama, String deskripsi) {
        this.nama = nama;
        this.deskripsi = deskripsi;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDGroup")
    private Long id;


    @Column(name = "Nama", nullable = false, length = 50, unique = true)
    private String nama;

    @Column(name = "Deskripsi", nullable = false, length = 100, unique = true)
    private String deskripsi;

    @Column(name = "CreatedBy", nullable = false, updatable = false)
    private Long createdBy = 1L;

    @Column(name = "CreatedDate", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdDate;

    @Column(name = "ModifiedBy", insertable = false)
    private Long modifiedBy = -1L;

    @Column(name = "ModifiedDate", insertable = false)
    @UpdateTimestamp
    private LocalDateTime modifiedDate;
}