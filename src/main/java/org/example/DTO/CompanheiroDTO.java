package org.example.DTO;

import org.example.domain.ENUM.ClasseEspecie;

public class CompanheiroDTO {

    private CompanheiroDTO() {}
    public record Request(
            String nome,
            ClasseEspecie especie,
            Integer lealdade
    ) {}

    public record Response(
            Long id,
            String nome,
            ClasseEspecie especie,
            Integer lealdade,
            String nomeAventureiro
    ) {}
}