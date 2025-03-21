package com.fdn.course.monitoring.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "MstAkses")
public class Access {

    public Access(String nama, String deskripsi, LocalDateTime createdTime, List<Menu> ltMenu) {
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.createdTime = createdTime;
        this.ltMenu = ltMenu;
    }

    @Id
    @Column(name = "IDAkses")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NamaAkses", unique = true, nullable = false, length = 20)
    private String nama;

    @Column(name = "Deskripsi", nullable = false, length = 100)
    private String deskripsi;

    @Column(name = "CreatedBy",nullable = false,updatable = false)
    private Long createdBy=1L;

    @Column(name = "CreatedDate", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdTime;

    @Column(name = "ModifiedBy",insertable = false)
    private Long modifiedBy=1L;

    @Column(name = "ModifiedDate",insertable = false)
    @UpdateTimestamp
    private LocalDateTime modifiedDate;

    @ManyToMany
    @JoinTable(
            name = "MapAksesMenu", uniqueConstraints = @UniqueConstraint(name = "unq-akses-to-menu", columnNames = {"IDAkses, IDMenu"}),
            joinColumns = @JoinColumn(name = "IDAkses", foreignKey = @ForeignKey(name = "fk-to-akses")),
            inverseJoinColumns = @JoinColumn(name = "IDMenu", foreignKey = @ForeignKey(name = "fk-to-menu"))
    )
    private List<Menu> ltMenu;
}