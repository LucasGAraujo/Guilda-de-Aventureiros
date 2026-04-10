package org.example.domain.audit;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Entity
@Table(name = "permissions", schema = "audit")
@AllArgsConstructor
@NoArgsConstructor
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "permissions_id")
    @SequenceGenerator(
            name = "permissions_id",
            sequenceName = "permissions_id_seq",
            schema = "audit",
            allocationSize = 1
    )
    private Long id;

    @Column(nullable = false, length = 80, unique = true)
    private String code;

    @Column(nullable = false, length = 255)
    private String descricao;
}