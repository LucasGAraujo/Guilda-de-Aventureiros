package org.example.DTO;

import org.example.domain.ENUM.PapelMissao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ParticipacaoMissaoDTO {

    private ParticipacaoMissaoDTO() {}

    public record Create(
            Long missaoId,
            Long aventureiroId,
            PapelMissao papel,
            BigDecimal recompensa,
            Boolean destaque
    ) {}

    public record Update(
            PapelMissao papel,
            BigDecimal recompensa,
            Boolean destaque
    ) {}

    public record Response(
            Long missaoId,
            Long aventureiroId,
            PapelMissao papel,
            BigDecimal recompensa,
            Boolean destaque,
            LocalDateTime dataRegistro
    ) {}
}