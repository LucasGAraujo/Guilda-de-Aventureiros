package org.example.repository;

import org.example.domain.ENUM.NivelPerigo;
import org.example.domain.ENUM.StatusMissao;
import org.example.domain.Missao;
import org.example.domain.audit.Organizacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MissaoRepositoryTest {

    @Autowired
    private MissaoRepository missaoRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Organizacao organizacaoBase;

    @BeforeEach
    void setUp() {
        organizacaoBase = entityManager.find(Organizacao.class, 1L);
    }

    @Test
    @DisplayName("Deve listar as missões aplicando os filtros corretamente")
    void deveListarMissoes() {
        Missao m = new Missao();
        m.setTitulo("Limpar o Porão da Taverna");
        m.setStatus(StatusMissao.PLANEJADA);
        m.setNivelPerigo(NivelPerigo.EXTREMO);
        m.setOrganizacao(organizacaoBase);
        m.setDataCriacao(LocalDateTime.now());

        missaoRepository.save(m);

        entityManager.flush();
        entityManager.clear();

        Page<Missao> resultado = missaoRepository.listarMissoes(
                StatusMissao.PLANEJADA,
                NivelPerigo.EXTREMO,
                null,
                null,
                PageRequest.of(0, 10)
        );

        assertThat(resultado.getContent()).isNotEmpty();
        assertThat(resultado.getContent().get(0).getTitulo())
                .isEqualTo("Limpar o Porão da Taverna");
    }

    @Test
    @DisplayName("Deve buscar a missão e seus participantes sem estourar o banco vazio")
    void deveBuscarMissaoComParticipantes() {
        Missao m = new Missao();
        m.setTitulo("Invasão Orc");
        m.setStatus(StatusMissao.EM_ANDAMENTO);
        m.setNivelPerigo(NivelPerigo.EXTREMO);
        m.setOrganizacao(organizacaoBase);
        m.setDataCriacao(LocalDateTime.now());

        Missao salva = missaoRepository.save(m);

        entityManager.flush();
        entityManager.clear();

        Optional<Missao> resultado = missaoRepository.buscarMissaoComParticipantes(salva.getId());

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getParticipacoes()).isEmpty();
    }
}