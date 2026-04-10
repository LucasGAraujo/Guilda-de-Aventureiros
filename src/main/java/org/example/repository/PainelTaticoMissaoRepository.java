package org.example.repository;

import org.example.domain.PainelTaticoMissao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PainelTaticoMissaoRepository extends JpaRepository<PainelTaticoMissao, Long> {
    List<PainelTaticoMissao> findTop10ByUltimaAtualizacaoGreaterThanEqualOrderByIndiceProntidaoDesc (
            LocalDateTime datainicio
    );
    List<PainelTaticoMissao> findTop10ByUltimaAtualizacaoBetweenOrderByIndiceProntidaoDesc (
            LocalDateTime datainicio,
            LocalDateTime dataFim
    );
}
