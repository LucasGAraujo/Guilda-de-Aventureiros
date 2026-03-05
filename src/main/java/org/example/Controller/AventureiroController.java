package org.example.Controller;

import jakarta.servlet.http.HttpServletResponse;
import org.example.DTO.AventureiroResponseDTO;
import org.example.domain.Aventureiro;
import org.example.domain.ENUM.ClasseAventureiro;
import org.example.exception.RegraNegocioException;
import org.example.service.AventureiroService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aventureiros")
@CrossOrigin(origins = "*")
public class AventureiroController {

    private final AventureiroService service;

    public AventureiroController(AventureiroService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Correção: Retorna 201 Created
    public AventureiroResponseDTO criar(@RequestBody Aventureiro aventureiro) {
        Aventureiro novo = service.criar(aventureiro);
        return AventureiroResponseDTO.from(novo);
    }

    @GetMapping
    public List<AventureiroResponseDTO> listar(
            @RequestParam(required = false) ClasseAventureiro classe,
            @RequestParam(required = false) Boolean ativo,
            @RequestParam(required = false) Integer nivelMin,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletResponse response
    ) {
        if (page < 0) {
            throw new RegraNegocioException("page não pode ser negativo");
        }
        if (size < 1 || size > 50) {
            throw new RegraNegocioException("size deve estar entre 1 e 50");
        }
        List<Aventureiro> filtrados = service.listar(classe, ativo, nivelMin);
        int total = filtrados.size();
        int totalPages = (int) Math.ceil((double) total / size);
        int start = page * size;
        int end = Math.min(start + size, total);
        List<Aventureiro> pagina = (start >= total) ? List.of() : filtrados.subList(start, end);
        response.setHeader("X-Total-Count", String.valueOf(total));
        response.setHeader("X-Page", String.valueOf(page));
        response.setHeader("X-Size", String.valueOf(size));
        response.setHeader("X-Total-Pages", String.valueOf(totalPages));
        return pagina.stream()
                .map(AventureiroResponseDTO::from)
                .toList();
    }

    @GetMapping("/{id}")
    public AventureiroResponseDTO buscarPorId(@PathVariable Long id) {
        return AventureiroResponseDTO.from(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public AventureiroResponseDTO atualizar(
            @PathVariable Long id,
            @RequestBody Aventureiro dadosAtualizados
    ) {
        Aventureiro aventureiro = service.atualizar(id, dadosAtualizados);
        return AventureiroResponseDTO.from(aventureiro);
    }

    @PatchMapping("/{id}/encerrar-vinculo")
    public AventureiroResponseDTO encerrarVinculo(@PathVariable Long id) {
        Aventureiro aventureiro = service.encerrarVinculoGuilda(id);
        return AventureiroResponseDTO.from(aventureiro);
    }

    @PatchMapping("/{id}/recrutar-novamente")
    public AventureiroResponseDTO recrutarNovamente(@PathVariable Long id) {
        Aventureiro aventureiro = service.recrutarNovamente(id);
        return AventureiroResponseDTO.from(aventureiro);
    }
}