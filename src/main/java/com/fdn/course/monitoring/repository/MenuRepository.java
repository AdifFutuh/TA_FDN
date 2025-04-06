package com.fdn.course.monitoring.repository;

import com.fdn.course.monitoring.model.GroupMenu;
import com.fdn.course.monitoring.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    void deleteByGroupMenu(GroupMenu groupMenu);

}
