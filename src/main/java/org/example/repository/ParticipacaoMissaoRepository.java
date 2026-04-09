package org.example.repository;

import org.example.domain.ParticipacaoMissao;
import org.example.domain.ParticipacaoMissaoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipacaoMissaoRepository extends JpaRepository<ParticipacaoMissao, ParticipacaoMissaoId> {
    boolean existsByMissaoIdAndAventureiroId(Long missaoId, Long aventureiroId);
    List<ParticipacaoMissao> findByIdMissaoId(Long missaoId);
}