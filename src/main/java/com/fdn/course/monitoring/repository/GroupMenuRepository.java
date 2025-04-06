package com.fdn.course.monitoring.repository;

import com.fdn.course.monitoring.model.GroupMenu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupMenuRepository extends JpaRepository<GroupMenu, Long> {

    Page<GroupMenu> findByNamaContainsIgnoreCase(String nama, Pageable pageable);

    List<GroupMenu> findByNamaContainsIgnoreCase(String nama);

    Page<GroupMenu> findByDeskripsiContainsIgnoreCase(String nama, Pageable pageable);
    List<GroupMenu> findByDeskripsiContainsIgnoreCase(String nama);


}
