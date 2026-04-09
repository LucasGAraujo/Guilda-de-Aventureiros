package org.example.repository;

import org.example.domain.Companheiro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanheiroRepository extends JpaRepository<Companheiro, Long> {
    Optional<Companheiro> findByAventureiroId(Long aventureiroId);
}