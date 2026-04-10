package org.example.service;

import org.example.DTO.MissaoTop15DiasResponseDTO;
import org.example.domain.PainelTaticoMissao;
import org.example.repository.PainelTaticoMissaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PainelTaticoMissaoServiceTest {

    @Mock
    private PainelTaticoMissaoRepository repository;

    @InjectMocks
    private PainelTaticoMissaoService service;

    @Test
    public void buscarTop10MissoesUltimos15Dias() {
        PainelTaticoMissao missao = new PainelTaticoMissao();
        missao.setTitulo("Invasão Orc");
        missao.setIndiceProntidao(BigDecimal.valueOf(90.0));
        missao.setUltimaAtualizacao(LocalDateTime.now());
        when(repository.findTop10ByUltimaAtualizacaoBetweenOrderByIndiceProntidaoDesc(any(), any()))
                .thenReturn(List.of(missao));
        List<MissaoTop15DiasResponseDTO> result = service.buscarTop10MissoesUltimos15Dias();
        assertFalse(result.isEmpty());
        assertEquals("Invasão Orc", result.get(0).titulo());
        assertEquals(BigDecimal.valueOf(90.0), result.get(0).indiceProntidao());
    }

    @Test
    public void buscarTop10MissoesFuturas() {
        PainelTaticoMissao missao = new PainelTaticoMissao();
        missao.setTitulo("Defesa do Castelo");
        missao.setIndiceProntidao(BigDecimal.ZERO);
        when(repository.findTop10ByUltimaAtualizacaoGreaterThanEqualOrderByIndiceProntidaoDesc(any()))
                .thenReturn(List.of(missao));
        List<MissaoTop15DiasResponseDTO> result = service.buscarTop10MissoesFuturas();
        assertFalse(result.isEmpty());
        assertEquals("Defesa do Castelo", result.get(0).titulo());
    }

    @Test
    public void retornarVazioQuandoNaoHouverMissoes() {
        when(repository.findTop10ByUltimaAtualizacaoBetweenOrderByIndiceProntidaoDesc(any(), any()))
                .thenReturn(List.of());
        List<MissaoTop15DiasResponseDTO> result = service.buscarTop10MissoesUltimos15Dias();
        assertTrue(result.isEmpty());
    }
}