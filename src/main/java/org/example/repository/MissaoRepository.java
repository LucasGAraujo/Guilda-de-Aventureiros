package org.example.repository;

import org.example.domain.ENUM.NivelPerigo;
import org.example.domain.ENUM.StatusMissao;
import org.example.domain.Missao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface MissaoRepository extends JpaRepository<Missao, Long> {

    @Query("""
        SELECT m
        FROM Missao m
        LEFT JOIN FETCH m.organizacao
        WHERE (:status IS NULL OR m.status = :status)
          AND (:nivelPerigo IS NULL OR m.nivelPerigo = :nivelPerigo)
          AND (CAST(:dataInicio AS timestamp) IS NULL OR m.dataCriacao >= :dataInicio)
          AND (CAST(:dataFim AS timestamp) IS NULL OR m.dataCriacao <= :dataFim)
""")
    Page<Missao> listarMissoes(
            @Param("status") StatusMissao status,
            @Param("nivelPerigo") NivelPerigo nivelPerigo,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim,
            Pageable pageable
    );

    @Query("""
            SELECT DISTINCT m
            FROM Missao m
            LEFT JOIN FETCH m.participacoes p
            LEFT JOIN FETCH p.aventureiro
            LEFT JOIN FETCH m.organizacao
            WHERE m.id = :id
        """)
    Optional<Missao> buscarMissaoComParticipantes(@Param("id") Long id);

}