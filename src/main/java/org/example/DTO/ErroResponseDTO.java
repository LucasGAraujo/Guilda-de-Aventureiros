package org.example.DTO;

import java.util.List;

public record ErroResponseDTO(String mensagem, List<String> detalhes) {}