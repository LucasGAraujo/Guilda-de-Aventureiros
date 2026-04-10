package org.example.controller;

import org.example.DTO.MissaoDTO;
import org.example.domain.ENUM.NivelPerigo;
import org.example.domain.ENUM.StatusMissao;
import org.example.domain.Missao;
import org.example.service.MissaoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/missoes")
public class MissaoController {

    private final MissaoService missaoService;

    public MissaoController(MissaoService missaoService) {
        this.missaoService = missaoService;
    }


    @GetMapping("/filtros")
    public ResponseEntity<Page<MissaoDTO.Response>> listarMissoes(
            @RequestParam(required = false) StatusMissao status,
            @RequestParam(required = false) NivelPerigo nivelPerigo,
            @RequestParam(required = false) LocalDateTime dataInicio,
            @RequestParam(required = false) LocalDateTime dataFim,
            Pageable pageable
    ) {
        return ResponseEntity.ok(missaoService.listarMissoes(status, nivelPerigo, dataInicio, dataFim, pageable));
    }

    @GetMapping("/{id}/participantes")
    public ResponseEntity<MissaoDTO.Detalhe> listarMissaoComParticipantes(@PathVariable Long id) {
        return missaoService.buscarMissaoComParticipantes(id)
                .map(m -> {
                    var participantes = m.getParticipacoes().stream()
                            .map(p -> new MissaoDTO.ParticipanteResumo(
                                    p.getAventureiro().getNome(),
                                    p.getRecompensaOuro(),
                                    p.getPapel(),
                                    p.getDestaque()))
                            .toList();

                    MissaoDTO.Detalhe detalhe = new MissaoDTO.Detalhe(
                            m.getId(),
                            m.getTitulo(),
                            m.getNivelPerigo(),
                            m.getStatus(),
                            m.getDataInicio(),
                            m.getDataFim(),
                            m.getOrganizacao().getNome(),
                            participantes
                    );
                    return ResponseEntity.ok(detalhe);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MissaoDTO.Response> criar(@RequestBody MissaoDTO.Create dto) {
        MissaoDTO.Response salva = missaoService.salvar(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(salva);
    }

    @PatchMapping("/{id}/iniciar")
    public ResponseEntity<String> iniciar(@PathVariable Long id) {
        Missao missao = missaoService.iniciarMissao(id);
        return ResponseEntity.ok("Missão '" + missao.getTitulo() + "' foi iniciada com sucesso");
    }
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<String> cancelar(@PathVariable Long id) {
        Missao missao = missaoService.cancelarMissao(id);
        return ResponseEntity.ok("Missão '" + missao.getTitulo() + "' foi cancelada com sucesso");
    }
    @PatchMapping("/{id}/concluido")
    public ResponseEntity<String> concluido(@PathVariable Long id) {
        Missao missao = missaoService.concluirMissao(id);
        return ResponseEntity.ok("Missão '" + missao.getTitulo() + "' foi concluida com sucesso");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        missaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}