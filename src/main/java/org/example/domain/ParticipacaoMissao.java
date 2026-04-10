package org.example.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.domain.ENUM.PapelMissao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "participacao_missao", schema = "operacoes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"missao_id", "aventureiro_id"})
        })
@AllArgsConstructor
@NoArgsConstructor
public class ParticipacaoMissao {

    @EmbeddedId
    private ParticipacaoMissaoId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("missaoId")
    @JoinColumn(name = "missao_id")
    private Missao missao;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("aventureiroId")
    @JoinColumn(name = "aventureiro_id")
    private Aventureiro aventureiro;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private PapelMissao papel;

    @DecimalMin(value = "0.0", message = "A recompensa em ouro não pode ser negativa")
    @Column(name = "recompensa_ouro", precision = 10, scale = 2)
    private BigDecimal recompensaOuro;

    @Column(name = "destaque", nullable = false)
    private Boolean destaque = false;

    @Column(name = "data_registro", updatable = false)
    private LocalDateTime dataRegistro;

    @PrePersist
    public void prePersist() {
        this.dataRegistro = LocalDateTime.now();
        if (this.recompensaOuro == null) {
            this.recompensaOuro = BigDecimal.ZERO;
        }
        if (this.destaque == null) {
            this.destaque = false;
        }
    }
}