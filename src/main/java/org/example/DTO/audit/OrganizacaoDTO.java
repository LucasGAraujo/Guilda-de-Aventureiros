package org.example.DTO.audit;

import java.time.OffsetDateTime;

public class OrganizacaoDTO {

    private OrganizacaoDTO() {}

    public record Create(
            String nome
    ) {}

    public record Update(
            String nome,
            Boolean ativo
    ) {}

    public record Response(
            Long id,
            String nome,
            boolean ativo,
            OffsetDateTime createdAt
    ) {}
}