package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.DTO.ParticipacaoMissaoDTO;
import org.example.domain.Aventureiro;
import org.example.domain.ENUM.StatusMissao;
import org.example.domain.Missao;
import org.example.exception.BusinessException;
import org.example.repository.AventureiroRepository;
import org.example.repository.MissaoRepository;
import org.example.service.ParticipacaoMissaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/participacoes")
@RequiredArgsConstructor

public class ParticipacaoMissaoController {

    private final ParticipacaoMissaoService participacaoService;
    private final MissaoRepository missaoRepository;
    private final AventureiroRepository aventureiroRepository;


    @PostMapping
    public ResponseEntity<ParticipacaoMissaoDTO.Response> criar(@RequestBody ParticipacaoMissaoDTO.Create dto) {
        Missao missao = missaoRepository.findById(dto.missaoId())
                .orElseThrow(() -> new BusinessException( "Missão não encontrada"));
        Aventureiro aventureiro = aventureiroRepository.findById(dto.aventureiroId())
                .orElseThrow(() -> new BusinessException( "Aventureiro não encontrado"));
        if (missao.getStatus() == StatusMissao.CANCELADA ||
                missao.getStatus() == StatusMissao.CONCLUIDA) {
            throw new BusinessException(
                    "Não é possível adicionar participantes em missões canceladas ou concluídas"
            );
        }
        ParticipacaoMissaoDTO.Response response = participacaoService.salvar(dto, missao, aventureiro);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }



    @GetMapping("/missao/{missaoId}")
    public ResponseEntity<List<ParticipacaoMissaoDTO.Response>> listarPorMissao(@PathVariable Long missaoId) {
        List<ParticipacaoMissaoDTO.Response> participacoes = participacaoService.listarPorMissao(missaoId);
        return ResponseEntity.ok(participacoes);
    }


    @DeleteMapping
    public ResponseEntity<Void> deletar(
            @RequestParam Long missaoId,
            @RequestParam Long aventureiroId
    ) {
        participacaoService.deletar(missaoId, aventureiroId);
        return ResponseEntity.noContent().build();
    }
}