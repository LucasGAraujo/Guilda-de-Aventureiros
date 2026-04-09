package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.DTO.MissaoTop15DiasResponseDTO;
import org.example.repository.PainelTaticoMissaoRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PainelTaticoMissaoService {
    private final PainelTaticoMissaoRepository repository;
    @Cacheable(value = "rankingMissoes")
    public List<MissaoTop15DiasResponseDTO> buscarTop10MissoesUltimos15Dias() {
        LocalDateTime dataLimite = LocalDateTime.now().minusDays(15);
        List<MissaoTop15DiasResponseDTO> result = repository
                .findTop10ByUltimaAtualizacaoGreaterThanEqualOrderByIndiceProntidaoDesc(dataLimite)
                .stream()
                .map(missao -> new MissaoTop15DiasResponseDTO(
                        missao.getMissaoId(),
                        missao.getTitulo(),
                        missao.getStatus(),
                        missao.getNivelPerigo(),
                        missao.getOrganizacaoId(),
                        missao.getTotalParticipantes(),
                        missao.getNivelMedioEquipe(),
                        missao.getTotalRecompensa(),
                        missao.getTotalMvps(),
                        missao.getParticipantesComCompanheiro(),
                        missao.getUltimaAtualizacao(),
                        missao.getIndiceProntidao()
                ))
                .toList();
        return result;
    }
}