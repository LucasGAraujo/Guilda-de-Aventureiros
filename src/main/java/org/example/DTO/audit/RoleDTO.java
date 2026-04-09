package org.example.DTO.audit;

import java.util.Set;

public class RoleDTO {

    private RoleDTO() {}

    public record Create(
            String nome,
            String descricao,
            Long organizacaoId
    ) {}

    public record Response(
            Long id,
            String nome,
            String descricao,
            Long organizacaoId,
            String nomeOrganizacao
    ) {}
}