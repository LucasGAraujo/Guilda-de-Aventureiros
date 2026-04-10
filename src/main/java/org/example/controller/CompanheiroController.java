package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.DTO.CompanheiroDTO;
import org.example.domain.Companheiro;
import org.example.service.CompanheiroService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companheiros")
@RequiredArgsConstructor
public class CompanheiroController {

    private final CompanheiroService companheiroService;


    @PostMapping("/aventureiro/{aventureiroId}")
    public ResponseEntity<CompanheiroDTO.Response> criar(
            @PathVariable Long aventureiroId,
            @RequestBody CompanheiroDTO.Request dto) {

        Companheiro criado = companheiroService.salvar(aventureiroId, dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new CompanheiroDTO.Response(
                        criado.getId(),
                        criado.getNome(),
                        criado.getEspecie(),
                        criado.getLealdade(),
                        criado.getAventureiro().getNome()
                )
        );
    }

    @PutMapping("/{companheiroId}")
    public ResponseEntity<CompanheiroDTO.Response> atualizarCompanheiro(
            @PathVariable Long companheiroId,
            @RequestBody CompanheiroDTO.Request dto) {

        Companheiro atualizado = companheiroService.atualizar(companheiroId, dto);

        return ResponseEntity.ok(
                new CompanheiroDTO.Response(
                        atualizado.getId(),
                        atualizado.getNome(),
                        atualizado.getEspecie(),
                        atualizado.getLealdade(),
                        atualizado.getAventureiro().getNome()
                )
        );
    }

    @DeleteMapping("/{companheiroId}")
    public ResponseEntity<Void> deletar(@PathVariable Long companheiroId) {
        companheiroService.deletar(companheiroId);
        return ResponseEntity.noContent().build();
    }
}