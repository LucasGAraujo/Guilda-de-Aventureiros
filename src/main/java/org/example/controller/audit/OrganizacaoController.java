package org.example.controller.audit;

import org.example.DTO.audit.OrganizacaoDTO;
import org.example.domain.audit.Organizacao;
import org.example.service.audit.OrganizacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organizacoes")
public class OrganizacaoController {
    private final OrganizacaoService organizacaoService;
    public OrganizacaoController(OrganizacaoService organizacaoService) {
        this.organizacaoService = organizacaoService;
    }
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

    @PatchMapping("/{id}")
    public ResponseEntity<OrganizacaoDTO.Response> atualizar(@PathVariable Long id, @RequestBody OrganizacaoDTO.Update dto) {
        Organizacao atualizada = organizacaoService.atualizar(id, dto);
        ResponseEntity<OrganizacaoDTO.Response> code200 = ResponseEntity.ok(
                new OrganizacaoDTO.Response(
                        atualizada.getId(),
                        atualizada.getNome(),
                        atualizada.isAtivo(),
                        atualizada.getCreatedAt()
                )
        );
        return code200;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        organizacaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}