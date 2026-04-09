package org.example.DTO;
import org.example.domain.PainelTaticoMissao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MissaoTop15DiasResponseDTO(
        Long missaoId,
        String titulo,
        String status,
        String nivelPerigo,
        Long organizacaoId,
        Long totalParticipantes,
        BigDecimal nivelMedioEquipe,
        BigDecimal totalRecompensa,
        Long totalMvps,
        Long participantesComCompanheiro,
        LocalDateTime ultimaAtualizacao,
        BigDecimal indiceProntidao
){}