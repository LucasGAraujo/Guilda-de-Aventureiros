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

        LocalDateTime datainicio = LocalDateTime.now().minusDays(15);
        LocalDateTime datafim = LocalDateTime.now();

        return repository
                .findTop10ByUltimaAtualizacaoBetweenOrderByIndiceProntidaoDesc(datainicio, datafim)
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
    }

    @Cacheable(value = "rankingMissoesFuturo")
    public List<MissaoTop15DiasResponseDTO> buscarTop10MissoesFuturas() {

        LocalDateTime datainicio = LocalDateTime.now().minusDays(15);

        return repository
                .findTop10ByUltimaAtualizacaoGreaterThanEqualOrderByIndiceProntidaoDesc(datainicio)
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
    }
}