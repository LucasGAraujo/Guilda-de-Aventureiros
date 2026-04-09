package org.example.repository;

import org.example.domain.Aventureiro;
import org.example.domain.Companheiro;
import org.example.domain.ENUM.ClasseAventureiro;
import org.example.domain.ENUM.ClasseEspecie;
import org.example.domain.audit.Organizacao;
import org.example.domain.audit.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AventureiroRepositoryTest {

    @Autowired
    private AventureiroRepository aventureiroRepository;

    @Autowired
    private org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager entityManager;

    private Organizacao organizacaoBase;
    private Usuario usuarioBase;

    @BeforeEach
    void setUp() {
        organizacaoBase = entityManager.find(Organizacao.class, 1L);
        usuarioBase = entityManager.find(Usuario.class, 1L);
    }

    @Test
    @DisplayName("Deve buscar aventureiros respeitando os filtros e a paginação")
    void deveBuscarAventureirosComFiltros() {
        Aventureiro a = criarAventureiro("Legolas", ClasseAventureiro.ARQUEIRO, 15, true);
        aventureiroRepository.save(a);

        Page<Aventureiro> resultado = aventureiroRepository.buscarAventureirosComFiltros(
                true, ClasseAventureiro.ARQUEIRO, 10, PageRequest.of(0, 10)
        );

        assertThat(resultado.getContent()).hasSize(1);
        assertThat(resultado.getContent().get(0).getNome()).isEqualTo("Legolas");
    }

    @Test
    @DisplayName("Deve buscar aventureiro por nome com correspondência parcial")
    void deveBuscarPorNomeParcial() {
        Aventureiro a = criarAventureiro("Aragorn", ClasseAventureiro.GUERREIRO, 20, true);
        aventureiroRepository.save(a);

        Page<Aventureiro> resultado = aventureiroRepository.findByNomeContainingIgnoreCase(
                "ragor", PageRequest.of(0, 10)
        );

        assertThat(resultado.getContent()).isNotEmpty();
        assertThat(resultado.getContent().get(0).getNome()).isEqualTo("Aragorn");
    }

    @Test
    @DisplayName("Deve trazer o perfil completo incluindo companheiro usando LEFT JOIN FETCH")
    void deveBuscarPerfilCompleto() {
        Aventureiro a = criarAventureiro("Jon Snow", ClasseAventureiro.GUERREIRO, 10, true);

        Companheiro c = new Companheiro();
        c.setNome("Fantasma");
        c.setEspecie(ClasseEspecie.LOBO);
        c.setLealdade(100);
        c.setAventureiro(a);
        a.setCompanheiro(c);

        Aventureiro salvo = aventureiroRepository.save(a);
        entityManager.flush();
        entityManager.clear();

        Optional<Aventureiro> resultado = aventureiroRepository.buscarPerfilCompleto(salvo.getId());

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getCompanheiro()).isNotNull();
        assertThat(resultado.get().getCompanheiro().getNome()).isEqualTo("Fantasma");
    }

    private Aventureiro criarAventureiro(String nome, ClasseAventureiro classe, Integer nivel, Boolean ativo) {
        Aventureiro a = new Aventureiro();
        a.setNome(nome);
        a.setClasse(classe);
        a.setNivel(nivel);
        a.setAtivo(ativo);
        a.setOrganizacao(organizacaoBase);
        a.setUsuarioCadastro(usuarioBase);
        return a;
    }
}