package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.dto.RoleDto;
import ita.softserve.course_evaluation.dto.RoleDtoMapper;
import ita.softserve.course_evaluation.entity.ERole;
import ita.softserve.course_evaluation.entity.Role;
import ita.softserve.course_evaluation.repository.RoleRepository;
import ita.softserve.course_evaluation.service.RoleService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role create(RoleDto dto) {
       String name = dto.getRoleName();
        if (findByName(name).isPresent()) {
            throw new RuntimeException("Role with name <" + name + "> already exist in database");
        }
        return roleRepository.save(RoleDtoMapper.fromDto(dto));
    }

    @Override
    public RoleDto readById(long id) {
        return RoleDtoMapper.toDto(roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role with id " + id + " not found.")));
    }

    @Override
    public RoleDto update(RoleDto dto) {
        readById(dto.getId());
        return RoleDtoMapper.toDto(roleRepository.save(RoleDtoMapper.fromDto(dto)));
    }

    @Override
    public void delete(long id) {
        roleRepository.delete(RoleDtoMapper.fromDto(readById(id)));
    }

    @Override
    public List<RoleDto> getAll() {
        List<RoleDto> roles = RoleDtoMapper.toDto(roleRepository.findAll());
        return roles.isEmpty() ? new ArrayList<>() : roles;
    }

    @Override
    public Optional<Role> findByName(String name) {
        return roleRepository.findByRoleName(ERole.valueOf(name));
    }
}
