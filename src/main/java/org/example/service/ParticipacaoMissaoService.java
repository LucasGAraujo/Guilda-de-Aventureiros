package org.example.service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.DTO.ParticipacaoMissaoDTO;
import org.example.domain.Aventureiro;
import org.example.domain.Missao;
import org.example.domain.ParticipacaoMissao;
import org.example.domain.ParticipacaoMissaoId;
import org.example.exception.BusinessException;
import org.example.repository.ParticipacaoMissaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Transactional
@RequiredArgsConstructor

public class ParticipacaoMissaoService {

    private final ParticipacaoMissaoRepository participacaoRepository;

    public ParticipacaoMissaoDTO.Response salvar(
            ParticipacaoMissaoDTO.Create dto, Missao missao, Aventureiro aventureiro) {
        if (participacaoRepository.existsByMissaoIdAndAventureiroId(dto.missaoId(), dto.aventureiroId())) {throw new BusinessException("Aventureiro já participa desta missão!");
        }
        if (!missao.getOrganizacao().getId().equals(aventureiro.getOrganizacao().getId())) {
            throw new BusinessException("Aventureiro não pertence à mesma organização da missão");
        }
        if(!aventureiro.getAtivo()) {
            throw new BusinessException("O Aventureiro não esta ativo");
        }

        ParticipacaoMissao participacao = new ParticipacaoMissao();
        participacao.setId(new ParticipacaoMissaoId(dto.missaoId(), dto.aventureiroId()));
        participacao.setMissao(missao);
        participacao.setAventureiro(aventureiro);
        participacao.setPapel(dto.papel());
        participacao.setRecompensaOuro(dto.recompensa());
        participacao.setDestaque(dto.destaque());

        ParticipacaoMissao salva = participacaoRepository.save(participacao);
        return new ParticipacaoMissaoDTO.Response(
                salva.getId().getMissaoId(),
                salva.getId().getAventureiroId(),
                salva.getPapel(),
                salva.getRecompensaOuro(),
                salva.getDestaque(),
                salva.getDataRegistro()
        );
    }

    public List<ParticipacaoMissaoDTO.Response> listarPorMissao(Long missaoId) {
        return participacaoRepository.findByIdMissaoId(missaoId).stream()
                .map(p -> new ParticipacaoMissaoDTO.Response(
                        p.getId().getMissaoId(),
                        p.getId().getAventureiroId(),
                        p.getPapel(),
                        p.getRecompensaOuro(),
                        p.getDestaque(),
                        p.getDataRegistro()
                ))
                .toList();
    }

    public void deletar(Long missaoId, Long aventureiroId) {
        ParticipacaoMissaoId id = new ParticipacaoMissaoId(missaoId, aventureiroId);
        participacaoRepository.deleteById(id);
    }

}