package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.entity.ERole;
import ita.softserve.course_evaluation.entity.Role;
import ita.softserve.course_evaluation.exception.NullEntityReferenceException;
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
    public Role create(Role role) {
        if (role == null) {
            throw new NullEntityReferenceException("Role cannot be 'null'");
        }
       ERole name = role.getRoleName();
        if (findByName(name).isPresent()) {
            throw new RuntimeException("Role with name <" + name.name() + "> already exist in database");
        }
        return roleRepository.save(role);
    }

    @Override
    public Role readById(long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role with id " + id + " not found."));
    }

    @Override
    public Role update(Role role) {
        if (role == null) {
            throw new NullEntityReferenceException("Role cannot be 'null'");
        }
        readById(role.getId());
        return roleRepository.save(role);
    }

    @Override
    public void delete(long id) {
        roleRepository.delete(readById(id));
    }

    @Override
    public List<Role> getAll() {
        List<Role> roles = roleRepository.findAll();
        return roles.isEmpty() ? new ArrayList<>() : roles;
    }

    @Override
    public Optional<Role> findByName(ERole name) {
        System.out.println(name);
        System.out.println(roleRepository.findByRoleName(name));
        return roleRepository.findByRoleName(name);
    }
}
