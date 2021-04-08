package com.ning.modules.system.service;

import com.ning.modules.system.domain.Role;

import java.util.List;

public interface RoleService {

    List<Role> findByUserId(Long id);

    Role findByLevel(Integer level);
}
