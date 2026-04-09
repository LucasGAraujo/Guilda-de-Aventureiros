package org.example.DTO;

import org.example.domain.ENUM.NivelPerigo;
import org.example.domain.ENUM.StatusMissao;
import java.math.BigDecimal;

public record MissaoMetricasDTO(
        String titulo,
        StatusMissao status,
        NivelPerigo nivelPerigo,
        Long quantidadeParticipantes,
        BigDecimal totalRecompensas
) {}