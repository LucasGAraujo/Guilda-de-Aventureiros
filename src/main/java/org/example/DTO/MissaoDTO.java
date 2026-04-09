package org.example.DTO;

import org.example.domain.ENUM.NivelPerigo;
import org.example.domain.ENUM.StatusMissao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class MissaoDTO {
    private MissaoDTO() {}

    public record Create(
            String titulo,
            NivelPerigo nivelPerigo,
            Long organizacaoId,
            LocalDateTime dataInicio,
            LocalDateTime dataFim
    ) {}

    public record Response(
            Long id,
            String titulo,
            NivelPerigo nivelPerigo,
            StatusMissao status,
            String nomeOrganizacao
    ) {}

    public record Detalhe(
            Long id,
            String titulo,
            NivelPerigo nivelPerigo,
            StatusMissao status,
            List<ParticipanteResumo> participantes
    ) {}

    public record ParticipanteResumo(
            String nomeAventureiro,
            BigDecimal recompensa,
            Boolean destaque
    ) {}
}