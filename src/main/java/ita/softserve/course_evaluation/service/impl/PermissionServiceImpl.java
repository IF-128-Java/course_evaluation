package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.dto.PermissionDto;
import ita.softserve.course_evaluation.dto.PermissionDtoMapper;
import ita.softserve.course_evaluation.entity.Permission;
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
    public Permission create(PermissionDto dto) {
        String name = dto.getPermissionName();
        if (findByName(name).isPresent()) {
            throw new RuntimeException("Permission with name <" + name + "> already exist in database");
        }
        return permissionRepository.save(PermissionDtoMapper.fromDto(dto));
    }

    @Override
    public PermissionDto readById(long id) {
        return PermissionDtoMapper.toDto(permissionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Permission with id " + id + " not found.")));
    }

    @Override
    public PermissionDto update(PermissionDto dto) {
        readById(dto.getId());
        return PermissionDtoMapper
                .toDto(permissionRepository.save(PermissionDtoMapper.fromDto(dto)));
    }

    @Override
    public void delete(long id) {
        permissionRepository.delete(PermissionDtoMapper.fromDto(readById(id)));
    }

    @Override
    public List<PermissionDto> getAll() {
        permissionRepository.findAll().forEach(Permission::getPermissionName);
        List<PermissionDto> permissions = PermissionDtoMapper.toDto(permissionRepository.findAll());
        return permissions.isEmpty() ? new ArrayList<>() : permissions;
    }

    @Override
    public List<PermissionDto> getByRoleId(long id) {
        List<PermissionDto> permissions = PermissionDtoMapper.toDto(permissionRepository.findPermissionsByRole_Id(id));
        return permissions.isEmpty() ? new ArrayList<>() : permissions;
    }

    @Override
    public Optional<Permission> findByName(String name) {
        return permissionRepository.findByPermissionName(name);
    }

}
