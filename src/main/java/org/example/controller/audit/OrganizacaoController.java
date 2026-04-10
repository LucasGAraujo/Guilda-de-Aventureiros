package org.example.controller.audit;

import lombok.RequiredArgsConstructor;
import org.example.DTO.audit.OrganizacaoDTO;
import org.example.service.audit.OrganizacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organizacoes")
@RequiredArgsConstructor
public class OrganizacaoController {
    private final OrganizacaoService organizacaoService;

    @GetMapping
    public ResponseEntity<List<OrganizacaoDTO.Response>> listar() {
        List<OrganizacaoDTO.Response> lista = organizacaoService.listarOrganizacoes().stream()
                .map(organizacao -> new OrganizacaoDTO.Response(
                        organizacao.getId(),
                        organizacao.getNome(),
                        organizacao.isAtivo(),
                        organizacao.getCreatedAt()
                ))
                .toList();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizacaoDTO.Response> buscar(@PathVariable Long id) {
        return organizacaoService.buscarPorId(id)
                .map(organizacao -> ResponseEntity.ok(
                        new OrganizacaoDTO.Response(
                                organizacao.getId(),
                                organizacao.getNome(),
                                organizacao.isAtivo(),
                                organizacao.getCreatedAt()
                        )
                ))
                .orElse(ResponseEntity.notFound().build());
    }
}