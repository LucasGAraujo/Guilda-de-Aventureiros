package org.example.repository.audit;

import org.example.domain.audit.Role;
import org.example.domain.audit.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuditRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    @DisplayName("Deve carregar o usuário, suas roles e a organização (Parte 1)")
    void deveCarregarUsuarioERoles() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        assertThat(usuarios)
                .as("O banco legado deve ter pelo menos um usuário cadastrado")
                .isNotEmpty();
        Usuario usuario = usuarios.stream()
                .filter(u -> u.getRoles() != null && !u.getRoles().isEmpty())
                .findFirst()
                .orElseThrow(() -> new AssertionError("Nenhum usuário com roles foi encontrado no banco legado!"));
        assertThat(usuario.getOrganizacao())
                .as("O usuário deve possuir uma organização vinculada")
                .isNotNull();
        assertThat(usuario.getRoles())
                .as("O usuário deve possuir roles")
                .isNotEmpty();
    }
    @Test
    @DisplayName("Deve listar roles com suas permissões (Parte 1)")
    void deveCarregarRolesEPermissoes() {
        List<Role> roles = roleRepository.findAll();
        assertThat(roles)
                .as("O banco legado deve ter pelo menos uma Role cadastrada")
                .isNotEmpty();
        Role role = roles.stream()
                .filter(r -> r.getPermissions() != null && !r.getPermissions().isEmpty())
                .findFirst()
                .orElseThrow(() -> new AssertionError("Nenhuma role com permissões foi encontrada no banco legado!"));
        assertThat(role.getPermissions())
                .as("As permissões devem estar acessíveis através da role")
                .isNotEmpty();
    }

    @Test
    @DisplayName("Prova de Conceito Parte 1: Validação Geral do Legado")
    void deveValidarMapeamentoDoSistemaLegado() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        assertThat(usuarios).isNotEmpty();
        Usuario usuario = usuarios.stream()
                .filter(u -> u.getRoles() != null && !u.getRoles().isEmpty())
                .findFirst()
                .orElseThrow(() -> new AssertionError("Nenhum usuário válido encontrado"));
        assertThat(usuario.getOrganizacao().getId())
                .as("O ID da Organização deve ter sido mapeado corretamente")
                .isNotNull();
        Role primeiraRole = usuario.getRoles().iterator().next();
        assertThat(primeiraRole.getId())
                .as("A Role deve ter sido mapeada corretamente")
                .isNotNull();
    }
}