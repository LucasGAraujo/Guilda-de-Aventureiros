package org.example.service;

import org.example.DTO.ParticipacaoMissaoDTO;
import org.example.domain.*;
import org.example.domain.ENUM.PapelMissao;
import org.example.domain.audit.Organizacao;
import org.example.exception.BusinessException;
import org.example.repository.ParticipacaoMissaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParticipacaoMissaoServiceTest {

    @Mock
    private ParticipacaoMissaoRepository participacaoRepository;

    @InjectMocks
    private ParticipacaoMissaoService service;

    private ParticipacaoMissaoDTO.Create dtoCriacao;
    private Missao missaoValida;
    private Aventureiro aventureiroValido;
    private Organizacao orgComum;

    @BeforeEach
    void setUp() {
        orgComum = new Organizacao();
        orgComum.setId(1L);
        missaoValida = new Missao();
        missaoValida.setId(100L);
        missaoValida.setOrganizacao(orgComum);
        aventureiroValido = new Aventureiro();
        aventureiroValido.setId(50L);
        aventureiroValido.setOrganizacao(orgComum);
        aventureiroValido.setAtivo(true);
        dtoCriacao = new ParticipacaoMissaoDTO.Create(
                100L, 50L, PapelMissao.BATEDOR, BigDecimal.valueOf(500), false
        );
    }

    @Test
    @DisplayName("Cenário de Sucesso: Deve salvar a participação")
    void deveSalvarParticipacaoComSucesso() {
        when(participacaoRepository.existsByMissaoIdAndAventureiroId(100L, 50L)).thenReturn(false);
        ParticipacaoMissao participacaoSalva = new ParticipacaoMissao();
        participacaoSalva.setId(new ParticipacaoMissaoId(100L, 50L));
        participacaoSalva.setPapel(PapelMissao.BATEDOR);
        participacaoSalva.setRecompensaOuro(BigDecimal.valueOf(500));
        participacaoSalva.setDestaque(false);
        participacaoSalva.setDataRegistro(LocalDateTime.now());
        when(participacaoRepository.save(any(ParticipacaoMissao.class))).thenReturn(participacaoSalva);
        ParticipacaoMissaoDTO.Response response = service.salvar(dtoCriacao, missaoValida, aventureiroValido);
        assertNotNull(response);
        assertEquals(100L, response.missaoId());
        assertEquals(PapelMissao.BATEDOR, response.papel());
        verify(participacaoRepository, times(1)).save(any(ParticipacaoMissao.class));
    }

    @Test
    @DisplayName("Cenário de Erro 1: Aventureiro já participa desta missão")
    void deveLancarErroQuandoJaParticipa() {
        when(participacaoRepository.existsByMissaoIdAndAventureiroId(100L, 50L)).thenReturn(true);
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            service.salvar(dtoCriacao, missaoValida, aventureiroValido);
        });
        assertEquals("Aventureiro já participa desta missão!", exception.getMessage());
        verify(participacaoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Cenário de Erro 2: Organizações diferentes")
    void deveLancarErroQuandoOrganizacaoDiferente() {
        when(participacaoRepository.existsByMissaoIdAndAventureiroId(anyLong(), anyLong())).thenReturn(false);
        Organizacao orgDiferente = new Organizacao();
        orgDiferente.setId(99L);
        aventureiroValido.setOrganizacao(orgDiferente);
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            service.salvar(dtoCriacao, missaoValida, aventureiroValido);
        });

        assertEquals("Aventureiro não pertence à mesma organização da missão", exception.getMessage());
    }

    @Test
    @DisplayName("Cenário de Erro 3: Aventureiro inativo")
    void deveLancarErroQuandoAventureiroInativo() {
        when(participacaoRepository.existsByMissaoIdAndAventureiroId(anyLong(), anyLong())).thenReturn(false);
        aventureiroValido.setAtivo(false);
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            service.salvar(dtoCriacao, missaoValida, aventureiroValido);
        });
        assertEquals("O Aventureiro não esta ativo", exception.getMessage());
    }
}