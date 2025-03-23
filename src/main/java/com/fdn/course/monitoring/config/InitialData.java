package com.fdn.course.monitoring.config;

import com.fdn.course.monitoring.model.Access;
import com.fdn.course.monitoring.model.Menu;
import com.fdn.course.monitoring.model.User;
import com.fdn.course.monitoring.repository.AccessRepository;
import com.fdn.course.monitoring.repository.MenuRepository;
import com.fdn.course.monitoring.repository.UserRepository;
import com.fdn.course.monitoring.security.BcryptImpl;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class InitialData implements CommandLineRunner {
    private final MenuRepository menuRepository;
    private final AccessRepository accessRepository;
    private final UserRepository userRepository;

    public InitialData(MenuRepository menuRepository, AccessRepository accessRepository, UserRepository userRepository) {
        this.menuRepository = menuRepository;
        this.accessRepository = accessRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (menuRepository.count() > 0 ) {
            System.out.println("⚡ Data sudah ada, tidak perlu insert ulang.");
            return;
        }

        List<Menu> menus = List.of(
                new Menu("Dashboard-admin", "/dashboard-admin", LocalDateTime.now()),
                new Menu("Dashboard", "/dashboard", LocalDateTime.now()),
                new Menu("Manajemen Pengguna", "/users", LocalDateTime.now()),
                new Menu("Manajemen Akses", "/access", LocalDateTime.now()),
                new Menu("Daftar Pengguna", "/user-list", LocalDateTime.now()),
                new Menu("Daftar Kursus", "/courses", LocalDateTime.now())
        );
        menuRepository.saveAll(menus);

        if (accessRepository.count() > 0 ) {
            System.out.println("Data sudah ada, tidak perlu insert ulang.");
            return;
        }

        List<Access> accesses = List.of(
                new Access("Super Admin", "Hak akses tertinggi", LocalDateTime.now(), menus),
                new Access("Admin", "Akses manajemen dan tata kelola", LocalDateTime.now(), menus),
                new Access("Peserta", "Hak akses khusus dan umum", LocalDateTime.now(), List.of(menus.get(3)))
        );
        accessRepository.saveAll(accesses);

        System.out.println(" Initial data berhasil ditambahkan ke database!");

        if (!userRepository.existsByEmail("admin@gmail.com")) {
            User superAdmin = new User();
            superAdmin.setNama("Super Admin");
            superAdmin.setEmail("admin@gmail.com");
            superAdmin.setUsername("super234");
            superAdmin.setNoHp("0000000000");
            superAdmin.setAlamat("alamat super admin dimana-mana");
            superAdmin.setIsRegistered(true);
            superAdmin.setPassword(BcryptImpl.hash(superAdmin.getUsername() + "superman"));

            accessRepository.findBynama("Super Admin")
                    .ifPresentOrElse(
                            superAdmin::setAkses,
                            () -> System.out.println(" Akses 'Super Admin' tidak ditemukan, pengguna tidak ditambahkan ke database!")
                    );

            userRepository.save(superAdmin);
            System.out.println(" Pengguna Super Admin berhasil ditambahkan!");
        } else {
            System.out.println(" Pengguna Super Admin sudah ada, tidak perlu insert ulang.");
        }
    }
}

