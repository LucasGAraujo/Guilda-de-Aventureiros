package org.example.domain.audit;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "organizacoes", schema = "audit"
)
@AllArgsConstructor
@NoArgsConstructor
public class Organizacao {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "organizacoes_id")
    @SequenceGenerator(
            name = "organizacoes_id",
            sequenceName = "organizacoes_id_seq",
            schema = "audit",
            allocationSize = 1
    )
    private Long id;

    @Column(nullable = false, length = 120, unique = true)
    private String nome;

    @Column(nullable = false)
    private boolean ativo = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
    @PrePersist
    public void prePersist() {
        this.createdAt = OffsetDateTime.now();
    }
}