package org.example.DTO.audit;

import java.time.OffsetDateTime;
import java.util.Set;

public class UsuarioDTO {

    public UsuarioDTO() {}

    public record Create(
            String nome,
            String email,
            String senha,
            Long organizacaoId,
            Set<Long> rolesIds
    ) {}

    public record Response(
            Long id,
            String nome,
            String email,
            String status,
            String nomeOrganizacao,
            Set<String> roles,
            OffsetDateTime createdAt

    ) {}
}