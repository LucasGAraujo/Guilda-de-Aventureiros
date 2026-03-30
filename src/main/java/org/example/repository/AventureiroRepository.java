package org.example.repository;

import org.example.domain.Aventureiro;
import org.example.domain.ENUM.ClasseAventureiro;
import org.example.DTO.RankingAventureiroDTO; // Importe seu DTO aqui
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface AventureiroRepository extends JpaRepository<Aventureiro, Long> {

    @Query("SELECT a FROM Aventureiro a WHERE " +
            "(:status IS NULL OR a.ativo = :status) AND " +
            "(:classe IS NULL OR a.classe = :classe) AND " +
            "(:nivelMinimo IS NULL OR a.nivel >= :nivelMinimo)")
    Page<Aventureiro> buscarAventureirosComFiltros(
            @Param("status") Boolean status,
            @Param("classe") ClasseAventureiro classe,
            @Param("nivelMinimo") Integer nivelMinimo,
            Pageable pageable);

    Page<Aventureiro> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    @Query("SELECT a FROM Aventureiro a LEFT JOIN FETCH a.companheiro WHERE a.id = :id")
    Optional<Aventureiro> buscarPerfilCompleto(@Param("id") Long id);

    @Query(value = "SELECT new org.example.DTO.RankingAventureiroDTO(" +
            "a.nome, " +
            "COUNT(p), " +
            "SUM(COALESCE(p.recompensaOuro, 0.0)), " +
            "SUM(CASE WHEN p.destaque = true THEN 1L ELSE 0L END)) " +
            "FROM Aventureiro a " +
            "LEFT JOIN a.participacoes p " +
            "WHERE a.organizacao.id = :orgId " +
            "AND (p IS NULL OR CAST(:dataInicio AS timestamp) IS NULL OR p.dataRegistro >= :dataInicio) " +
            "AND (p IS NULL OR CAST(:dataFim AS timestamp) IS NULL OR p.dataRegistro <= :dataFim) " +
            "GROUP BY a.id, a.nome " +
            "ORDER BY COUNT(p) DESC",
            countQuery = "SELECT COUNT(a) FROM Aventureiro a WHERE a.organizacao.id = :orgId")
    Page<RankingAventureiroDTO> buscarRanking(
            @Param("orgId") Long orgId,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim,
            Pageable pageable
    );
}