package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.DTO.AventureiroDTO;
import org.example.domain.Aventureiro;
import org.example.domain.ENUM.ClasseAventureiro;
import org.example.service.AventureiroService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/aventureiros")
@RequiredArgsConstructor
public class AventureiroController {

    private final AventureiroService aventureiroService;

    @GetMapping("/filtros")
    public ResponseEntity<Page<AventureiroDTO.Response>> buscarComFiltros(@RequestParam(required = false) Boolean status, @RequestParam(required = false) ClasseAventureiro classe, @RequestParam(required = false) Integer nivelMinimo,
    @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        Page<AventureiroDTO.Response> page = aventureiroService
                .buscarComFiltros(status, classe, nivelMinimo, pageable)
                .map(a -> new AventureiroDTO.Response(
                        a.getId(),
                        a.getNome(),
                        a.getClasse(),
                        a.getNivel(),
                        a.getAtivo(),
                        a.getOrganizacao() != null ? a.getOrganizacao().getNome() : null,
                        a.getDataCriacao()
                ));
        return ResponseEntity.ok(page);
    }

    @GetMapping("/buscar")
    public ResponseEntity<Page<AventureiroDTO.Response>> buscarPorNome(
            @RequestParam String nome, @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        Page<AventureiroDTO.Response> page = aventureiroService.buscarPorNome(nome, pageable)
                .map(a -> new AventureiroDTO.Response(
                        a.getId(),
                        a.getNome(),
                        a.getClasse(),
                        a.getNivel(),
                        a.getAtivo(),
                        a.getOrganizacao() != null ? a.getOrganizacao().getNome() : null,
                        a.getDataCriacao()
                ));
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}/perfil")
    public ResponseEntity<AventureiroDTO.Perfil> buscarPerfilCompleto(@PathVariable Long id) {
        return aventureiroService.buscarPerfilCompleto(id)
                .map(a -> ResponseEntity.ok(
                        new AventureiroDTO.Perfil(
                                a.getId(),
                                a.getNome(),
                                a.getClasse(),
                                a.getNivel(),
                                a.getOrganizacao() != null ? a.getOrganizacao().getNome() : null,
                                a.getCompanheiro() != null ? a.getCompanheiro().getNome() : "Sem companheiro"
                        )
                ))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AventureiroDTO.Response> criar(@RequestBody AventureiroDTO.Create dto) {
        Aventureiro criado = aventureiroService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new AventureiroDTO.Response(
                        criado.getId(),
                        criado.getNome(),
                        criado.getClasse(),
                        criado.getNivel(),
                        criado.getAtivo(),
                        criado.getOrganizacao() != null ? criado.getOrganizacao().getNome() : null,
                        criado.getDataCriacao()
                )
        );
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        aventureiroService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}