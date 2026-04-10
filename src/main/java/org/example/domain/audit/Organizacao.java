package org.example.domain.audit;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "org_id")
    @SequenceGenerator(
            name = "org_id",
            sequenceName = "org_id_seq",
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