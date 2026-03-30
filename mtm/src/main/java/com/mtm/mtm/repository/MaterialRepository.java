package com.mtm.mtm.repository;

import com.mtm.mtm.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<Material, Integer> {
}