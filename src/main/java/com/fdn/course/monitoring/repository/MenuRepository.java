package com.fdn.course.monitoring.repository;

import com.fdn.course.monitoring.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
