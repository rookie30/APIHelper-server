package com.ning.modules.system.service.Impl;

import com.ning.modules.system.domain.Role;
import com.ning.modules.system.repository.RoleRepository;
import com.ning.modules.system.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public List<Role> findByUserId(Long id) {
        return new ArrayList<>(roleRepository.findByUserId(id));
    }
}
