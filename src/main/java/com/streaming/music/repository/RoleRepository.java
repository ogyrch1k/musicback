package com.streaming.music.repository;

import com.streaming.music.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String nameOfRole);

}
