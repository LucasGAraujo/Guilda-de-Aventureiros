package org.example.service;
import jakarta.transaction.Transactional;
import org.example.DTO.ParticipacaoMissaoDTO;
import org.example.domain.Aventureiro;
import org.example.domain.Missao;
import org.example.domain.ParticipacaoMissao;
import org.example.domain.ParticipacaoMissaoId;
import org.example.exception.BusinessException;
import org.example.repository.AventureiroRepository;
import org.example.repository.ParticipacaoMissaoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Transactional
public class ParticipacaoMissaoService {

    private final ParticipacaoMissaoRepository participacaoRepository;


    public ParticipacaoMissaoService(ParticipacaoMissaoRepository participacaoRepository) {
        this.participacaoRepository = participacaoRepository;
    }

    public ParticipacaoMissaoDTO.Response salvar(
            ParticipacaoMissaoDTO.Create dto, Missao missao, Aventureiro aventureiro) {
        if (participacaoRepository.existsByMissaoIdAndAventureiroId(dto.missaoId(), dto.aventureiroId())) {
            throw new BusinessException(HttpStatus.NOT_FOUND,"Aventureiro já participa desta missão!");
        }
        if (!missao.getOrganizacao().getId().equals(aventureiro.getOrganizacao().getId())) {
            throw new BusinessException(
                    HttpStatus.BAD_REQUEST,
                    "Aventureiro não pertence à mesma organização da missão"
            );
        }

        ParticipacaoMissao participacao = new ParticipacaoMissao();
        participacao.setId(new ParticipacaoMissaoId(dto.missaoId(), dto.aventureiroId()));
        participacao.setMissao(missao);
        participacao.setAventureiro(aventureiro);
        participacao.setPapel(dto.papel());
        participacao.setRecompensaOuro(dto.recompensa());
        participacao.setDestaque(dto.destaque());

        ParticipacaoMissao salva = participacaoRepository.save(participacao);
        return ConverterparaDTO(salva);
    }

    public List<ParticipacaoMissaoDTO.Response> listarPorMissao(Long missaoId) {
        return participacaoRepository.findByIdMissaoId(missaoId).stream()
                .map(this::ConverterparaDTO)
                .toList();
    }



    public void removerParticipacao(Long missaoId, Long aventureiroId) {
        ParticipacaoMissaoId id = new ParticipacaoMissaoId(missaoId, aventureiroId);
        participacaoRepository.deleteById(id);
    }
    private ParticipacaoMissaoDTO.Response ConverterparaDTO(ParticipacaoMissao p) {
        return new ParticipacaoMissaoDTO.Response(
                p.getId().getMissaoId(),
                p.getId().getAventureiroId(),
                p.getPapel(),
                p.getRecompensaOuro(),
                p.getDestaque(),
                p.getDataRegistro()
        );
    }
}