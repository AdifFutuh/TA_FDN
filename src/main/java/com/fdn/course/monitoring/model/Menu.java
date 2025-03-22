package com.fdn.course.monitoring.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "MstMenu")
public class Menu {
    public Menu(String nama, String path, LocalDateTime createdDate) {
        this.nama = nama;
        this.path = path;
        this.createdDate = createdDate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdMenu")
    private Long id;

    @Column(name = "NamaMenu", nullable = false, length = 20, unique = true)
    private String nama;

    @Column(name = "Path", nullable = false, length = 20)
    private String path;

    @Column(name = "CreatedBy", nullable = false, updatable = false)
    private Long createdBy = 1L;

    @Column(name = "CreatedDate", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdDate;

    @Column(name = "ModifiedBy", insertable = false)
    private Long modifiedBy = 1L;

    @Column(name = "ModifiedDate", insertable = false)
    @UpdateTimestamp
    private LocalDateTime modifiedDate;


}