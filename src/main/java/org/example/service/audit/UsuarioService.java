package org.example.service.audit;

import lombok.RequiredArgsConstructor;
import org.example.domain.audit.Organizacao;
import org.example.domain.audit.Role;
import org.example.domain.audit.Usuario;
import org.example.DTO.audit.UsuarioDTO;
import org.example.exception.BusinessException;
import org.example.repository.audit.OrganizacaoRepository;
import org.example.repository.audit.RoleRepository;
import org.example.repository.audit.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor

public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final OrganizacaoRepository organizacaoRepository;
    private final RoleRepository roleRepository;


    public List<Usuario> buscarTodasOrgPorId(Long organizacaoId) {
        return usuarioRepository.findAllWithRolesByOrganizacaoId(organizacaoId);
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findByIdWithRoles(id);
    }
    public Page<Usuario> buscarPorNome(String nome, Pageable pageable) {
        return usuarioRepository.findByNomeContainingIgnoreCase(nome, pageable);
    }
    public Usuario salvar(UsuarioDTO.Create dto) {
        Organizacao org = organizacaoRepository.findById(dto.organizacaoId())
                .orElseThrow(() -> new BusinessException( "Organização não encontrada"));

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

    public void deletar(Long id) {
        if(!usuarioRepository.existsById(id)) {
            throw new BusinessException( "Usuário não encontrado");
        }
        usuarioRepository.deleteById(id);
    }
}