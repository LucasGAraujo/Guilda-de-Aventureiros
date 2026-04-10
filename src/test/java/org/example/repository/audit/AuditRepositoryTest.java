package org.example.repository.audit;

import org.example.domain.audit.Organizacao;
import org.example.domain.audit.Usuario;
import org.example.domain.audit.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuditRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private OrganizacaoRepository organizacaoRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    @DisplayName("Deve buscar usuários por organização com roles")
    void deveBuscarUsuariosPorOrganizacao() {
        List<Usuario> usuarios =
                usuarioRepository.findAllWithRolesByOrganizacaoId(1L);
        assertThat(usuarios).isNotEmpty();
        Usuario usuario = usuarios.get(0);
        assertThat(usuario.getOrganizacao()).isNotNull();
        assertThat(usuario.getRoles()).isNotEmpty();
    }

    @Test
    @DisplayName("Deve buscar roles com permissões por organização")
    void deveBuscarRolesComPermissoes() {
        List<Role> roles =
                roleRepository.findAllWithPermissionsByOrganizacaoId(1L);
        assertThat(roles).isNotEmpty();
        Role role = roles.get(0);
        assertThat(role.getPermissions()).isNotEmpty();
    }


    @Test
    void deveBuscarOrganizacaoPorId() {
        Organizacao org = organizacaoRepository.findById(1L).orElse(null);
        assertThat(org).isNotNull();
        assertThat(org.getId()).isEqualTo(1L);
    }


    @Test
    void deveBuscarUsuarioPorNome() {
        Organizacao org = new Organizacao();
        org.setNome("Org Teste");
        org = organizacaoRepository.save(org);
        Usuario u = new Usuario();
        u.setNome("lucas");
        u.setEmail("lucas@test.com");
        u.setOrganizacao(org);
        u.setSenhaHash("123");
        usuarioRepository.save(u);
        List<Usuario> result =
                usuarioRepository.findByNomeContainingIgnoreCase("lucas", Pageable.unpaged())
                        .getContent();
        assertThat(result).isNotEmpty();
    }

    @Test
    void deveBuscarUsuarioComRolesPorId() {
        Usuario usuario =
                usuarioRepository.findByIdWithRoles(1L).orElse(null);

        assertThat(usuario).isNotNull();
        assertThat(usuario.getRoles()).isNotEmpty();
    }
}