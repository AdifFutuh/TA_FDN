package com.fdn.course.monitoring.model;

import jakarta.persistence.*;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "MstUsers")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdUser")
    private long id;

    @Column(name = "Username", length = 40, nullable = false, unique = true)
    private String username;

    @Column(name = "Password", length = 60, nullable = false, unique = true )
    private String password;

    @Column(name = "Email",length = 64,nullable = false,unique = true)
    private String email;

    @Column(name = "Alamat",nullable = false)
    private String alamat;

    @Column(name = "NoHp",length = 16,nullable = false,unique = true)
    private String noHp;

    @Column(name = "Nama" , length = 50, nullable = false)
    private String nama;

    @Column(name = "TanggalLahir")
    private LocalDate tanggalLahir;

    @Column(name = "ProfilePicture")
    private String pathImage;

    @Column(name = "LinkProfilePicture")
    private String linkImage;

    @Column(name = "IsRegistered")
    private Boolean isRegistered=false;

    @ManyToOne
    @JoinColumn(name = "IdAkses",foreignKey = @ForeignKey(name = "fk-user-to-akses"))
    private Access akses;

    @Column(name = "CreatedBy",nullable = false,updatable = false)
    private Long createdBy=1L;

    @Column(name = "CreatedDate",nullable = false,updatable = false)
    @CreationTimestamp
    private LocalDateTime createdDate;

    @Column(name = "ModifiedBy",insertable = false)
    private Long modifiedBy=1L;

    @Column(name = "ModifiedDate",insertable = false)
    @UpdateTimestamp
    private LocalDateTime modifiedDate;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "IdOtp")
    private Otp otp;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<Menu> listMenu = this.akses.getLtMenu();

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Menu menu : listMenu){
            grantedAuthorities.add(new SimpleGrantedAuthority(menu.getNama()));
        }
        System.out.println("Jumlah menu: " + this.akses.getLtMenu().size());
        return grantedAuthorities;
    }
}
