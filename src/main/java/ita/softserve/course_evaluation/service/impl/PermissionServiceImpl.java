package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.entity.Permission;
import ita.softserve.course_evaluation.exception.NullEntityReferenceException;
import ita.softserve.course_evaluation.repository.PermissionRepository;
import ita.softserve.course_evaluation.service.PermissionService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public Permission create(Permission permission) {
        if (permission == null) {
            throw new NullEntityReferenceException("Permission cannot be 'null'");
        }
        String name = permission.getPermissionName();
        if (findByName(name).isPresent()) {
            throw new RuntimeException("Permission with name <" + name + "> already exist in database");
        }
        return permissionRepository.save(permission);
    }

    @Override
    public Permission readById(long id) {
        return permissionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Permission with id " + id + " not found."));
    }

    @Override
    public Permission update(Permission permission) {
        if (permission == null) {
            throw new NullEntityReferenceException("Permission cannot be 'null'");
        }
        readById(permission.getId());
        return permissionRepository.save(permission);
    }

    @Override
    public void delete(long id) {
        permissionRepository.delete(readById(id));
    }

    @Override
    public List<Permission> getAll() {
        List<Permission> permissions = permissionRepository.findAll();
        return permissions.isEmpty() ? new ArrayList<>() : permissions;
    }

    @Override
    public List<Permission> getByRoleId(long id) {
        List<Permission> permissions = permissionRepository.findAllByRoleId(id);
        return permissions.isEmpty() ? new ArrayList<>() : permissions;
    }

    @Override
    public Optional<Permission> findByName(String name) {
        return permissionRepository.findByPermissionName(name);
    }
}
