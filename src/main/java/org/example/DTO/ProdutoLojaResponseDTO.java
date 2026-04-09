package org.example.DTO;

public record ProdutoLojaResponseDTO(
        String id,
        String nome,
        String categoria,
        String raridade,
        Double preco,
        String descricao
) {}