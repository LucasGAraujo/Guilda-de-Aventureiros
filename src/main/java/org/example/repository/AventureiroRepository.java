package org.example.repository;

import org.example.domain.Aventureiro;
import org.example.domain.ENUM.ClasseAventureiro;
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

    @Query(value = "SELECT a FROM Aventureiro a LEFT JOIN FETCH a.organizacao WHERE " +
            "(:status IS NULL OR a.ativo = :status) AND " +
            "(:classe IS NULL OR a.classe = :classe) AND " +
            "(:nivelMinimo IS NULL OR a.nivel >= :nivelMinimo)",
            countQuery = "SELECT COUNT(a) FROM Aventureiro a WHERE " +
                    "(:status IS NULL OR a.ativo = :status) AND " +
                    "(:classe IS NULL OR a.classe = :classe) AND " +
                    "(:nivelMinimo IS NULL OR a.nivel >= :nivelMinimo)")
    Page<Aventureiro> buscarAventureirosComFiltros(
            @Param("status") Boolean status,
            @Param("classe") ClasseAventureiro classe,
            @Param("nivelMinimo") Integer nivelMinimo,
            Pageable pageable);

    Page<Aventureiro> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    @Query("SELECT a FROM Aventureiro a LEFT JOIN FETCH a.companheiro LEFT JOIN FETCH a.organizacao WHERE a.id = :id")
    Optional<Aventureiro> buscarPerfilCompleto(@Param("id") Long id);

}