package org.example.domain;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class ParticipacaoMissaoId implements Serializable {

    @Column(name = "missao_id")
    private Long missaoId;

    @Column(name = "aventureiro_id")
    private Long aventureiroId;

    public ParticipacaoMissaoId() {}

    public ParticipacaoMissaoId(Long missaoId, Long aventureiroId) {
        this.missaoId = missaoId;
        this.aventureiroId = aventureiroId;
    }
}