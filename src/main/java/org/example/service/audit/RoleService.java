package org.example.service.audit;

import lombok.RequiredArgsConstructor;
import org.example.domain.audit.Organizacao;
import org.example.domain.audit.Role;
import org.example.DTO.audit.RoleDTO;
import org.example.exception.BusinessException;
import org.example.repository.audit.OrganizacaoRepository;
import org.example.repository.audit.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor

public class RoleService {

    private final RoleRepository roleRepository;
    private final OrganizacaoRepository organizacaoRepository;

    public List<Role> buscarTodasOrgPorId(Long organizacaoId) {
        return roleRepository.findAllWithPermissionsByOrganizacaoId(organizacaoId);
    }

    public Optional<Role> buscarPorId(Long id) {
        return roleRepository.findById(id);
    }


    public void deletar(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new BusinessException( "Role não encontrada");
        }
        roleRepository.deleteById(id);
    }
}