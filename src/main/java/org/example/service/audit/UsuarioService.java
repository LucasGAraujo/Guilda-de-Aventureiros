package org.example.service.audit;

import org.example.domain.audit.Organizacao;
import org.example.domain.audit.Role;
import org.example.domain.audit.Usuario;
import org.example.DTO.audit.UsuarioDTO;
import org.example.exception.BusinessException;
import org.example.repository.audit.OrganizacaoRepository;
import org.example.repository.audit.RoleRepository;
import org.example.repository.audit.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final OrganizacaoRepository organizacaoRepository;
    private final RoleRepository roleRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, OrganizacaoRepository organizacaoRepository, RoleRepository roleRepository) {
        this.usuarioRepository = usuarioRepository;
        this.organizacaoRepository = organizacaoRepository;
        this.roleRepository = roleRepository;
    }

    public List<Usuario> findAllByOrganizacaoId(Long organizacaoId) {
        return usuarioRepository.findAllWithRolesByOrganizacaoId(organizacaoId);
    }

    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findByIdWithRoles(id);
    }

    public Usuario salvar(UsuarioDTO.Create dto) {
        Organizacao org = organizacaoRepository.findById(dto.organizacaoId())
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Organização não encontrada"));

        Set<Role> roles = new HashSet<>();
        if (dto.rolesIds() != null && !dto.rolesIds().isEmpty()) {
            roles.addAll(roleRepository.findAllById(dto.rolesIds()));
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setSenhaHash(dto.senha());
        usuario.setOrganizacao(org);
        usuario.setRoles(roles);

        return usuarioRepository.save(usuario);
    }

    public void deleteById(Long id) {
        if(!usuarioRepository.existsById(id)) {
            throw new BusinessException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
        }
        usuarioRepository.deleteById(id);
    }
}