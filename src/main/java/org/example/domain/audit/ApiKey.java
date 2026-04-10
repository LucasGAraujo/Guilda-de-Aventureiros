package org.example.domain.audit;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "api_keys", schema = "audit")
@AllArgsConstructor
@NoArgsConstructor
public class ApiKey {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "api_keys_id")
    @SequenceGenerator(
            name = "api_keys_id",
            sequenceName = "api_keys_id_seq",
            schema = "audit",
            allocationSize = 1
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizacao_id", nullable = false)
    private Organizacao organizacao;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(name = "key_hash", nullable = false, length = 255)
    private String keyHash;

    private boolean ativo = true;
}
