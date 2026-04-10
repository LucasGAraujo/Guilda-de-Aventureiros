package org.example.service;

import org.example.DTO.CompanheiroDTO;
import org.example.domain.Aventureiro;
import org.example.domain.Companheiro;
import org.example.domain.ENUM.ClasseEspecie;
import org.example.exception.BusinessException;
import org.example.repository.AventureiroRepository;
import org.example.repository.CompanheiroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CompanheiroServiceTest {

    @Mock
    private AventureiroRepository aventureiroRepository;
    @Mock
    private CompanheiroRepository companheiroRepository;
    @InjectMocks
    private CompanheiroService service;

    private Aventureiro aventureiroValido;
    private CompanheiroDTO.Request dtoCriacao;

    @BeforeEach
    void setUp() {
        aventureiroValido = new Aventureiro();
        aventureiroValido.setId(10L);
        aventureiroValido.setNome("Aragorn");
        dtoCriacao = new CompanheiroDTO.Request("Lobo Gigante", ClasseEspecie.LOBO, 100);
    }

    @Test
    @DisplayName("Deve salvar e ASSOCIAR o companheiro ao aventureiro com sucesso")
    void deveSalvarEAssociarCompanheiro() {
        when(aventureiroRepository.findById(10L)).thenReturn(Optional.of(aventureiroValido));
        when(companheiroRepository.findByAventureiroId(10L)).thenReturn(Optional.empty());
        Companheiro companheiroSalvo = new Companheiro();
        companheiroSalvo.setId(1L);
        companheiroSalvo.setNome("Lobo Gigante");
        companheiroSalvo.setAventureiro(aventureiroValido);
        when(companheiroRepository.save(any(Companheiro.class))).thenReturn(companheiroSalvo);
        Companheiro resultado = service.salvar(10L, dtoCriacao);
        assertNotNull(resultado);
        assertEquals("Lobo Gigante", resultado.getNome());
        assertNotNull(resultado.getAventureiro(), "O aventureiro não pode ser nulo no companheiro");
        assertEquals(10L, resultado.getAventureiro().getId(), "O ID do aventureiro associado deve ser 10");
        assertEquals("Aragorn", resultado.getAventureiro().getNome());
        verify(companheiroRepository, times(1)).save(any(Companheiro.class));
    }

    @Test
    @DisplayName("Deve lançar erro quando o Aventureiro não for encontrado")
    void deveLancarErroAventureiroNaoEncontrado() {
        when(aventureiroRepository.findById(99L)).thenReturn(Optional.empty());
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            service.salvar(99L, dtoCriacao);
        });
        assertEquals("Aventureiro não encontrado", exception.getMessage());
        verify(companheiroRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar erro quando Aventureiro já possui um companheiro")
    void deveLancarErroAventureiroJaTemCompanheiro() {
        when(aventureiroRepository.findById(10L)).thenReturn(Optional.of(aventureiroValido));
        Companheiro existente = new Companheiro();
        existente.setId(5L);
        when(companheiroRepository.findByAventureiroId(10L)).thenReturn(Optional.of(existente));
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            service.salvar(10L, dtoCriacao);
        });
        assertEquals("Aventureiro já possui um companheiro", exception.getMessage());
        verify(companheiroRepository, never()).save(any());
    }
}