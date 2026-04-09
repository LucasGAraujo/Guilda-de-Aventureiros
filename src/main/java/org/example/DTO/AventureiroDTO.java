package org.example.DTO;

import org.example.domain.ENUM.ClasseAventureiro;
import java.time.LocalDateTime;

public class AventureiroDTO {

    private AventureiroDTO() {}

    public record Create(
            String nome,
            ClasseAventureiro classe,
            Integer nivel,
            Long organizacaoId,
            Long usuarioCadastroId
    ) {}

    public record Response(
            Long id,
            String nome,
            ClasseAventureiro classe,
            Integer nivel,
            Boolean ativo,
            String nomeOrganizacao,
            LocalDateTime dataCriacao
    ) {}

    public record Perfil(
            Long id,
            String nome,
            ClasseAventureiro classe,
            Integer nivel,
            String nomeOrganizacao,
            String nomeCompanheiro
    ) {}
}