package org.example.service.audit;

import org.example.domain.audit.Organizacao;
import org.example.domain.audit.Role;
import org.example.DTO.audit.RoleDTO;
import org.example.exception.BusinessException;
import org.example.repository.audit.OrganizacaoRepository;
import org.example.repository.audit.RoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;
    private final OrganizacaoRepository organizacaoRepository;

    public RoleService(RoleRepository roleRepository, OrganizacaoRepository organizacaoRepository) {
        this.roleRepository = roleRepository;
        this.organizacaoRepository = organizacaoRepository;
    }

    public List<Role> findAllByOrganizacaoId(Long organizacaoId) {
        return roleRepository.findAllWithPermissionsByOrganizacaoId(organizacaoId);
    }

    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }

    public Role save(RoleDTO.Create dto) {
        Organizacao org = organizacaoRepository.findById(dto.organizacaoId())
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Organização não encontrada"));

        Role role = new Role();
        role.setNome(dto.nome());
        role.setDescricao(dto.descricao());
        role.setOrganizacao(org);

        return roleRepository.save(role);
    }

    public void deleteById(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new BusinessException(HttpStatus.NOT_FOUND, "Role não encontrada");
        }
        roleRepository.deleteById(id);
    }
}