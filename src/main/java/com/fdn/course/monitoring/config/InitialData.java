package com.fdn.course.monitoring.config;

import com.fdn.course.monitoring.model.Access;
import com.fdn.course.monitoring.model.GroupMenu;
import com.fdn.course.monitoring.model.Menu;
import com.fdn.course.monitoring.model.User;
import com.fdn.course.monitoring.repository.AccessRepository;
import com.fdn.course.monitoring.repository.GroupMenuRepository;
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
    private final GroupMenuRepository groupMenuRepository;
    private final AccessRepository accessRepository;
    private final UserRepository userRepository;

    public InitialData(GroupMenuRepository groupMenuRepository, MenuRepository menuRepository, AccessRepository accessRepository, UserRepository userRepository) {
        this.menuRepository = menuRepository;
        this.accessRepository = accessRepository;
        this.userRepository = userRepository;
        this.groupMenuRepository = groupMenuRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (groupMenuRepository.count() > 0) {
            System.out.println("⚡ Data sudah ada, tidak perlu insert ulang.");
            return;
        }

        List<GroupMenu> groupMenus = List.of(
                new GroupMenu("Admin", "untuk keperluan manajemen akun user"),
                new GroupMenu("Kursus", "untuk menu-menu terkait kursus"),
                new GroupMenu("Report", "untuk menu-menu dashboard dan print laporan"),
                new GroupMenu("Pengguna", "untuk menu-menu pengguna")
        );
        groupMenuRepository.saveAll(groupMenus);

        if (menuRepository.count() > 0 ) {
            System.out.println("⚡ Data sudah ada, tidak perlu insert ulang.");
            return;
        }

        List<Menu> menus = List.of(
                new Menu("Dashboard Admin", groupMenus.getFirst(), "/dashboard-admin", LocalDateTime.now()),
                new Menu("Dashboard", groupMenus.get(3),"/dashboard",  LocalDateTime.now()),
                new Menu("Manajemen Pengguna", groupMenus.getFirst(), "/users", LocalDateTime.now()),
                new Menu("Manajemen Akses",  groupMenus.getFirst(),  "/access", LocalDateTime.now()),
                new Menu("Daftar Pengguna",groupMenus.get(3), "/user-list", LocalDateTime.now()),
                new Menu("Daftar Kursus",groupMenus.get(3), "/courses", LocalDateTime.now())
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

