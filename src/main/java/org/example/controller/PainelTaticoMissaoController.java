package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.DTO.MissaoTop15DiasResponseDTO;
import org.example.service.PainelTaticoMissaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/missoes")
@RequiredArgsConstructor
public class PainelTaticoMissaoController {
    private final PainelTaticoMissaoService service;

    @GetMapping("/top15dias")
    public ResponseEntity<List<MissaoTop15DiasResponseDTO>> buscarTop10MissoesUltimos15Dias() {
        return ResponseEntity.ok(service.buscarTop10MissoesUltimos15Dias());
    }
}