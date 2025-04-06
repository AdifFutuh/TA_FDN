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
import java.util.Objects;
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
        if (groupMenuRepository.count() == 0) {
            List<GroupMenu> groupMenus = List.of(
                    new GroupMenu("Admin", "untuk keperluan manajemen akun user"),
                    new GroupMenu("Kursus", "untuk menu-menu terkait kursus"),
                    new GroupMenu("Report", "untuk menu-menu dashboard dan print laporan"),
                    new GroupMenu("Pengguna", "untuk menu-menu pengguna")
            );
            groupMenuRepository.saveAll(groupMenus);
            System.out.println("✅ GroupMenu berhasil ditambahkan.");
        } else {
            System.out.println("⚡ GroupMenu sudah ada, tidak perlu insert ulang.");
        }

        if (menuRepository.count() == 0) {
            List<GroupMenu> groupMenus = groupMenuRepository.findAll();

            GroupMenu adminGroup = groupMenus.stream()
                    .filter(g -> g.getNama().equals("Admin"))
                    .findFirst().orElseThrow(() -> new RuntimeException("GroupMenu Admin tidak ditemukan"));
            GroupMenu reportGroup = groupMenus.stream()
                    .filter(g -> g.getNama().equals("Report"))
                    .findFirst().orElseThrow(() -> new RuntimeException("GroupMenu Report tidak ditemukan"));
            GroupMenu penggunaGroup = groupMenus.stream()
                    .filter(g -> g.getNama().equals("Pengguna"))
                    .findFirst().orElseThrow(() -> new RuntimeException("GroupMenu Pengguna tidak ditemukan"));
            GroupMenu kursusGroup = groupMenus.stream()
                    .filter(g -> g.getNama().equals("Kursus"))
                    .findFirst().orElseThrow(() -> new RuntimeException("GroupMenu Kursus tidak ditemukan"));

            List<Menu> menus = List.of(
                    new Menu("Dashboard Admin", adminGroup, "/dashboard-admin", LocalDateTime.now()),
                    new Menu("menu", adminGroup, "/menu", LocalDateTime.now()),
                    new Menu("Dashboard", reportGroup, "/dashboard", LocalDateTime.now()),
                    new Menu("Manajemen Pengguna", adminGroup, "/users", LocalDateTime.now()),
                    new Menu("Manajemen Akses", adminGroup, "/access", LocalDateTime.now()),
                    new Menu("Daftar Pengguna", penggunaGroup, "/user-list", LocalDateTime.now()),
                    new Menu("Daftar Kursus", kursusGroup, "/courses", LocalDateTime.now())
            );
            menuRepository.saveAll(menus);
            System.out.println("✅ Menu berhasil ditambahkan.");
        } else {
            System.out.println("⚡ Menu sudah ada, tidak perlu insert ulang.");
        }

        if (accessRepository.count() == 0) {
            List<Menu> allMenus = menuRepository.findAll();

            Access superAdminAccess = new Access("Super Admin", "Hak akses tertinggi", LocalDateTime.now(), allMenus);
            Access adminAccess = new Access("Admin", "Akses manajemen dan tata kelola", LocalDateTime.now(), allMenus);

            Access pesertaAccess = new Access("Peserta", "Hak akses khusus dan umum", LocalDateTime.now(),
                    List.of(
                            allMenus.stream().filter(m -> m.getNama().equals("Dashboard")).findFirst()
                                    .orElseThrow(() -> new RuntimeException("Menu Dashboard tidak ditemukan")),
                            allMenus.stream().filter(m -> m.getNama().equals("Manajemen Pengguna")).findFirst()
                                    .orElseThrow(() -> new RuntimeException("Menu Manajemen Pengguna tidak ditemukan")),
                            allMenus.stream().filter(m -> m.getNama().equals("Daftar Pengguna")).findFirst()
                                    .orElseThrow(() -> new RuntimeException("Menu Daftar Pengguna tidak ditemukan")),
                            allMenus.stream().filter(m -> m.getNama().equals("Daftar Kursus")).findFirst()
                                    .orElseThrow(() -> new RuntimeException("Menu Daftar Kursus tidak ditemukan"))
                    )
            );

            accessRepository.saveAll(List.of(superAdminAccess, adminAccess, pesertaAccess));
            System.out.println("✅ Access berhasil ditambahkan.");
        } else {
            System.out.println("⚡ Access sudah ada, tidak perlu insert ulang.");
        }

        if (!userRepository.existsByEmail("admin@gmail.com")) {
            User superAdmin = new User();
            superAdmin.setNama("Super Admin");
            superAdmin.setEmail("admin@gmail.com");
            superAdmin.setUsername("super234");
            superAdmin.setNoHp("0000000000");
            superAdmin.setAlamat("alamat super admin dimana-mana");
            superAdmin.setIsRegistered(true);
            superAdmin.setPassword(BcryptImpl.hash(superAdmin.getUsername() + "superman"));

            Access akses = accessRepository.findBynama("Super Admin")
                    .orElseThrow(() -> new RuntimeException("Akses Super Admin tidak ditemukan"));
            superAdmin.setAkses(akses);

            userRepository.save(superAdmin);
            System.out.println("✅ Pengguna Super Admin berhasil ditambahkan!");
        } else {
            System.out.println("⚡ Pengguna Super Admin sudah ada, tidak perlu insert ulang.");
        }
    }

}

